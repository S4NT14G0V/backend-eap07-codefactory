import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import type { ApiCredential } from "@/lib/api";

const mockCredentials: ApiCredential[] = [
  {
    id: "cred-001",
    publicId: "pk_live_4f8a91b3c2e7d605",
    merchantId: "1",
    active: true,
  },
  {
    id: "cred-002",
    publicId: "pk_live_7c3d2a5e1f908b46",
    merchantId: "1",
    active: false,
  },
  {
    id: "cred-003",
    publicId: "pk_live_9e1b6f4c3d2a8075",
    merchantId: "2",
    active: true,
  },
];

export default function CredentialsPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-zinc-900">Credenciales API</h1>
        <p className="text-sm text-zinc-500 mt-1">
          Llaves de acceso generadas para los comerciantes verificados.
          <span className="ml-2 text-amber-500 font-medium">
            [Datos de muestra — endpoint en desarrollo]
          </span>
        </p>
      </div>

      {/* Info card */}
      <Card className="border-blue-100 bg-blue-50">
        <CardHeader className="pb-2">
          <CardTitle className="text-sm text-blue-800">
            ¿Cómo funciona?
          </CardTitle>
        </CardHeader>
        <CardContent>
          <CardDescription className="text-blue-700 text-sm">
            Cada comerciante en estado <strong>VERIFIED</strong> puede generar
            hasta <strong>3 credenciales activas</strong>. El secreto{" "}
            <code className="bg-blue-100 px-1 rounded text-xs">plainSecret</code>{" "}
            se muestra <strong>una sola vez</strong> al momento de la creación
            — almacénalo de forma segura.
          </CardDescription>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-base">Credenciales registradas</CardTitle>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Public ID</TableHead>
                <TableHead>Comerciante</TableHead>
                <TableHead>Estado</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {mockCredentials.map((c) => (
                <TableRow key={c.id}>
                  <TableCell className="font-mono text-xs">{c.publicId}</TableCell>
                  <TableCell>{c.merchantId}</TableCell>
                  <TableCell>
                    <Badge variant={c.active ? "default" : "secondary"}>
                      {c.active ? "Activa" : "Inactiva"}
                    </Badge>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  );
}
