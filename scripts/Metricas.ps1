<#
.SYNOPSIS
    Obtiene mÃ©tricas de SonarCloud usando un token y project key desde un archivo .env
.DESCRIPTION
    Lee SONAR_TOKEN y PROJECT_KEY desde archivo .env, luego consulta las APIs de SonarCloud.
    Muestra mÃ©tricas, issues y top 10 archivos. Al final, espera que se presione Enter.
#>

# Establecer la codificaciÃ³n para leer .env correctamente (UTF-8 sin BOM)
$envFile = Join-Path $PSScriptRoot ".env"

if (-not (Test-Path $envFile)) {
    Write-Error "No se encontrÃ³ el archivo .env en $PSScriptRoot"
    Read-Host "Presione Enter para salir"
    exit 1
}

Write-Host "Cargando variables desde $envFile ..." -ForegroundColor Cyan

# Leer cada lÃ­nea no vacÃ­a y asignar variables de entorno
Get-Content $envFile | ForEach-Object {
    if ($_ -match '^\s*([^#][^=]+)=(.*)\s*$') {
        $key = $matches[1].Trim()
        $value = $matches[2].Trim()
        # Eliminar comillas simples o dobles si existen alrededor del valor
        if ($value -match '^"(.+)"$' -or $value -match "^'(.+)'$") {
            $value = $matches[1]
        }
        Set-Item -Path "env:$key" -Value $value
        Write-Host "  - $key = *** (oculto)" -ForegroundColor Gray
    }
}

# Verificar que las variables necesarias existan
$token = $env:SONAR_TOKEN
$project = $env:PROJECT_KEY

if (-not $token) {
    Write-Error "SONAR_TOKEN no estÃ¡ definido en el archivo .env"
    Read-Host "Presione Enter para salir"
    exit 1
}
if (-not $project) {
    Write-Error "PROJECT_KEY no estÃ¡ definido en el archivo .env"
    Read-Host "Presione Enter para salir"
    exit 1
}

Write-Host "`nConsultando mÃ©tricas para el proyecto: $project" -ForegroundColor Green

# Preparar autenticaciÃ³n Basic Auth
$pair = "$token`:"
$auth = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes($pair))
$headers = @{ Authorization = "Basic $auth" }

# 1. MÃ©tricas principales del componente
$urlMetrics = "https://sonarcloud.io/api/measures/component?component=$project&metricKeys=coverage,complexity,sqale_index,sqale_debt_ratio,ncloc,code_smells,bugs,vulnerabilities"

try {
    $result = Invoke-RestMethod -Uri $urlMetrics -Headers $headers -ErrorAction Stop
} catch {
    Write-Error "Error al consultar mÃ©tricas: $_"
    Read-Host "Presione Enter para salir"
    exit 1
}

$coverage = ($result.component.measures | Where-Object { $_.metric -eq 'coverage' }).value
$complexity = ($result.component.measures | Where-Object { $_.metric -eq 'complexity' }).value
$sqale_index_minutes = ($result.component.measures | Where-Object { $_.metric -eq 'sqale_index' }).value

# Convertir sqale_index (minutos) a dÃ­as (jornada de 8 horas)
$debt_days = [math]::Round(($sqale_index_minutes -as [double]) / 60 / 8, 2)

Write-Host "`n--- Resumen de mÃ©tricas generales ---" -ForegroundColor Yellow
Write-Host "coverage=$coverage, complexity=$complexity, sqale_index_min=$sqale_index_minutes, debt_days=$debt_days"

# 2. Total de issues no resueltos
$urlIssues = "https://sonarcloud.io/api/issues/search?componentKeys=$project&resolved=false&ps=1"

try {
    $issuesResult = Invoke-RestMethod -Uri $urlIssues -Headers $headers -ErrorAction Stop
    $totalIssues = $issuesResult.total
    Write-Host "`nTotal de issues no resueltos: $totalIssues" -ForegroundColor Yellow
} catch {
    Write-Error "Error al consultar issues: $_"
}

# --- Vulnerabilidades críticas ---
$urlCriticalCount = "https://sonarcloud.io/api/issues/search?componentKeys=$project&resolved=false&severities=CRITICAL&types=VULNERABILITY&ps=1"
try {
    $critCountRes = Invoke-RestMethod -Uri $urlCriticalCount -Headers $headers -ErrorAction Stop
    $criticalVulns = [int]$critCountRes.total
    Write-Host "`nVulnerabilidades CRITICAS: $criticalVulns" -ForegroundColor Yellow

    if ($criticalVulns -gt 0) {
        $urlCriticalList = "https://sonarcloud.io/api/issues/search?componentKeys=$project&resolved=false&severities=CRITICAL&types=VULNERABILITY&ps=100"
        $critListRes = Invoke-RestMethod -Uri $urlCriticalList -Headers $headers -ErrorAction Stop

        $critListRes.issues | ForEach-Object {
            [PSCustomObject]@{
                File     = (($_.component -split ':')[-1])
                Line     = $_.line
                Rule     = $_.rule
                Severity = $_.severity
                Message  = $_.message
            }
        } | Select-Object -First 10 | Format-Table -AutoSize
    }
} catch {
    Write-Error "Error al obtener vulnerabilidades criticas: $_"
}

# 3. Top 10 archivos con mayor complejidad
$urlTree = "https://sonarcloud.io/api/measures/component_tree?component=$project&metricKeys=complexity,cognitive_complexity,coverage&ps=500&qualifiers=FIL"

try {
    $treeResult = Invoke-RestMethod -Uri $urlTree -Headers $headers -ErrorAction Stop
} catch {
    Write-Error "Error al consultar el Ã¡rbol de componentes: $_"
    Read-Host "Presione Enter para salir"
    exit 1
}

Write-Host "`n--- Top 10 archivos con mayor complejidad ---" -ForegroundColor Yellow

$fileList = $treeResult.components | ForEach-Object {
    [PSCustomObject]@{
        Path       = $_.path
        Complexity = ($_.measures | Where-Object { $_.metric -eq 'complexity' }).value
        Cognitive  = ($_.measures | Where-Object { $_.metric -eq 'cognitive_complexity' }).value
        Coverage   = ($_.measures | Where-Object { $_.metric -eq 'coverage' }).value
    }
} | Where-Object { $_.Complexity -ne $null } | Sort-Object { [int]$_.Complexity } -Descending | Select-Object -First 10

$fileList | Format-Table -AutoSize

Write-Host "`nScript finalizado." -ForegroundColor Green

# ðŸ”´ PAUSA: evita que la ventana se cierre inmediatamente
Write-Host "`nPresione Enter para salir..." -ForegroundColor Cyan
Read-Host