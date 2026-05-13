<#
.SYNOPSIS
    Obtiene métricas de SonarCloud y clasifica los issues por tipo (Bug/Vulnerability/Code Smell)
.DESCRIPTION
    Lee SONAR_TOKEN y PROJECT_KEY desde .env, consulta APIs de SonarCloud.
    Muestra métricas generales, total de issues desglosado por tipo y lista los primeros 10 issues.
#>

$envFile = Join-Path $PSScriptRoot ".env"

if (-not (Test-Path $envFile)) {
    Write-Error "No se encontró el archivo .env en $PSScriptRoot"
    Read-Host "Presione Enter para salir"
    exit 1
}

Write-Host "Cargando variables desde $envFile ..." -ForegroundColor Cyan

Get-Content $envFile | ForEach-Object {
    if ($_ -match '^\s*([^#][^=]+)=(.*)\s*$') {
        $key = $matches[1].Trim()
        $value = $matches[2].Trim()
        if ($value -match '^"(.+)"$' -or $value -match "^'(.+)'$") {
            $value = $matches[1]
        }
        Set-Item -Path "env:$key" -Value $value
        Write-Host "  - $key = *** (oculto)" -ForegroundColor Gray
    }
}

$token = $env:SONAR_TOKEN
$project = $env:PROJECT_KEY

if (-not $token) {
    Write-Error "SONAR_TOKEN no está definido en .env"
    Read-Host "Presione Enter para salir"
    exit 1
}
if (-not $project) {
    Write-Error "PROJECT_KEY no está definido en .env"
    Read-Host "Presione Enter para salir"
    exit 1
}

Write-Host "`nConsultando métricas para el proyecto: $project" -ForegroundColor Green

$pair = "$token`:" 
$auth = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes($pair))
$headers = @{ Authorization = "Basic $auth" }

# 1. Métricas principales
$urlMetrics = "https://sonarcloud.io/api/measures/component?component=$project&metricKeys=coverage,complexity,sqale_index,sqale_debt_ratio,ncloc,code_smells,bugs,vulnerabilities"

try {
    $result = Invoke-RestMethod -Uri $urlMetrics -Headers $headers -ErrorAction Stop
} catch {
    Write-Error "Error al consultar métricas: $_"
    Read-Host "Presione Enter para salir"
    exit 1
}

$coverage = ($result.component.measures | Where-Object { $_.metric -eq 'coverage' }).value
$complexity = ($result.component.measures | Where-Object { $_.metric -eq 'complexity' }).value
$sqale_index_minutes = ($result.component.measures | Where-Object { $_.metric -eq 'sqale_index' }).value
$debt_days = [math]::Round(($sqale_index_minutes -as [double]) / 60 / 8, 2)
$code_smells = ($result.component.measures | Where-Object { $_.metric -eq 'code_smells' }).value
$bugs = ($result.component.measures | Where-Object { $_.metric -eq 'bugs' }).value
$vulnerabilities = ($result.component.measures | Where-Object { $_.metric -eq 'vulnerabilities' }).value

Write-Host "`n--- Resumen de métricas generales (SonarCloud) ---" -ForegroundColor Yellow
Write-Host "Coverage       : $coverage%"
Write-Host "Complejidad    : $complexity"
Write-Host "Deuda técnica  : $debt_days días (basado en 8h/día)"
Write-Host "Code Smells    : $code_smells"
Write-Host "Bugs (estimados): $bugs"
Write-Host "Vulnerabilities: $vulnerabilities"

# 2. Obtener TODOS los issues no resueltos (paginado)
Write-Host "`n--- Consultando issues no resueltos desde la API ---" -ForegroundColor Yellow

$allIssues = @()
$page = 1
$pageSize = 100
do {
    $urlIssues = "https://sonarcloud.io/api/issues/search?componentKeys=$project&resolved=false&ps=$pageSize&p=$page"
    try {
        $issuesPage = Invoke-RestMethod -Uri $urlIssues -Headers $headers -ErrorAction Stop
        $allIssues += $issuesPage.issues
        $total = $issuesPage.total
        # LÍNEA CORREGIDA: uso de $($page) en lugar de $page:
        Write-Host "  Página $($page): recuperados $($issuesPage.issues.Count) de $total issues"
        $page++
    } catch {
        Write-Error "Error al consultar issues: $_"
        break
    }
} while ($allIssues.Count -lt $total -and $issuesPage.issues.Count -gt 0)

Write-Host "`nTotal de issues no resueltos (según API) : $total" -ForegroundColor Cyan

# Clasificar por tipo
$bugsList = $allIssues | Where-Object { $_.type -eq 'BUG' }
$vulnsList = $allIssues | Where-Object { $_.type -eq 'VULNERABILITY' }
$smellsList = $allIssues | Where-Object { $_.type -eq 'CODE_SMELL' }
$securityHotspots = $allIssues | Where-Object { $_.type -eq 'SECURITY_HOTSPOT' }

Write-Host "`n--- Desglose por tipo (real) ---" -ForegroundColor Yellow
Write-Host "Bugs            : $($bugsList.Count)"
Write-Host "Vulnerabilities : $($vulnsList.Count)"
Write-Host "Code Smells     : $($smellsList.Count)"
Write-Host "Security Hotspots: $($securityHotspots.Count)" -ForegroundColor Gray

# Mostrar primeros 10 issues
Write-Host "`n--- Primeros 10 issues (ordenados por severidad descendente) ---" -ForegroundColor Yellow
$topIssues = $allIssues | Sort-Object -Property severity -Descending | Select-Object -First 10
$topIssues | ForEach-Object {
    [PSCustomObject]@{
        Tipo       = $_.type
        Severidad  = $_.severity
        Mensaje    = $_.message.Substring(0, [Math]::Min(60, $_.message.Length))
        Archivo    = if ($_.component) { ($_.component -split ':')[-1] } else { "N/A" }
        Línea      = $_.line
    }
} | Format-Table -AutoSize

# Vulnerabilidades críticas
$urlCriticalCount = "https://sonarcloud.io/api/issues/search?componentKeys=$project&resolved=false&severities=CRITICAL&types=VULNERABILITY&ps=1"
try {
    $critCountRes = Invoke-RestMethod -Uri $urlCriticalCount -Headers $headers -ErrorAction Stop
    $criticalVulns = [int]$critCountRes.total
    Write-Host "`nVulnerabilidades CRÍTICAS (no resueltas): $criticalVulns" -ForegroundColor $(if ($criticalVulns -gt 0) { 'Red' } else { 'Green' })
} catch {
    Write-Error "Error al obtener vulnerabilidades críticas: $_"
}

# Top 10 archivos por complejidad
$urlTree = "https://sonarcloud.io/api/measures/component_tree?component=$project&metricKeys=complexity,cognitive_complexity,coverage&ps=500&qualifiers=FIL"
try {
    $treeResult = Invoke-RestMethod -Uri $urlTree -Headers $headers -ErrorAction Stop
} catch {
    Write-Error "Error al consultar árbol de componentes: $_"
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
Write-Host "`nNota: Los 'Bugs estimados' son una métrica agregada por Sonar." -ForegroundColor Cyan
Write-Host "      El desglose por tipo (Bug / Vulnerability / Code Smell) se ha obtenido issue por issue." -ForegroundColor Cyan
Write-Host "`nPresione Enter para salir..." -ForegroundColor Cyan
Read-Host