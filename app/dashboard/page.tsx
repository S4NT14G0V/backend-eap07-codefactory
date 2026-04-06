import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";

const stats = [
  {
    title: "Comerciantes activos",
    value: "—",
    description: "Disponible en próximo sprint",
  },
  {
    title: "Transacciones hoy",
    value: "—",
    description: "Disponible en próximo sprint",
  },
  {
    title: "Monto total procesado",
    value: "—",
    description: "Disponible en próximo sprint",
  },
  {
    title: "Credenciales API activas",
    value: "—",
    description: "Disponible en próximo sprint",
  },
];

export default function DashboardPage() {
  return (
    <div className="space-y-8">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-zinc-900">Panel de control</h1>
        <p className="text-sm text-zinc-500 mt-1">
          Bienvenido a AppStripe — resumen general del sistema.
        </p>
      </div>

      {/* Stats grid */}
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
        {stats.map((s) => (
          <Card key={s.title}>
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium text-zinc-500">
                {s.title}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-3xl font-bold text-zinc-900">{s.value}</p>
              <p className="text-xs text-zinc-400 mt-1">{s.description}</p>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Sprint status */}
      <Card>
        <CardHeader>
          <CardTitle className="text-base">Estado del proyecto — Sprint 1</CardTitle>
        </CardHeader>
        <CardContent>
          <ul className="space-y-2 text-sm">
            {[
              { label: "Autenticación 2FA (POST /2fa/verify)", done: true },
              { label: "Registro de comerciantes", done: false },
              { label: "Generación de credenciales API", done: false },
              { label: "Creación de transacciones", done: false },
              { label: "Gestión de estado de transacciones", done: false },
            ].map((item) => (
              <li key={item.label} className="flex items-center gap-2">
                <span
                  className={`inline-block h-2 w-2 rounded-full ${
                    item.done ? "bg-green-500" : "bg-zinc-300"
                  }`}
                />
                <span className={item.done ? "text-zinc-800" : "text-zinc-400"}>
                  {item.label}
                </span>
                {item.done && (
                  <span className="ml-auto text-xs font-medium text-green-600">
                    Disponible
                  </span>
                )}
              </li>
            ))}
          </ul>
        </CardContent>
      </Card>
    </div>
  );
}
