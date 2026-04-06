import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import type { Transaction } from "@/lib/api";

const mockTransactions: Transaction[] = [
  {
    id: "tx-001",
    merchantId: "1",
    amount: 150000,
    status: "APPROVED",
  },
  {
    id: "tx-002",
    merchantId: "1",
    amount: 75500,
    status: "PROCESSING",
  },
  {
    id: "tx-003",
    merchantId: "2",
    amount: 320000,
    status: "REJECTED",
  },
  {
    id: "tx-004",
    merchantId: "3",
    amount: 10000,
    status: "CREATED",
  },
  {
    id: "tx-005",
    merchantId: "2",
    amount: 50000,
    status: "FAILED",
  },
];

const statusVariantMap: Record<
  Transaction["status"],
  "default" | "secondary" | "destructive" | "outline"
> = {
  APPROVED: "default",
  PROCESSING: "secondary",
  REJECTED: "destructive",
  FAILED: "destructive",
  CREATED: "outline",
};

const statusLabelMap: Record<Transaction["status"], string> = {
  APPROVED: "Aprobada",
  PROCESSING: "En proceso",
  REJECTED: "Rechazada",
  FAILED: "Fallida",
  CREATED: "Creada",
};

function formatCOP(amount: number) {
  return new Intl.NumberFormat("es-CO", {
    style: "currency",
    currency: "COP",
    minimumFractionDigits: 0,
  }).format(amount);
}

export default function TransactionsPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-zinc-900">Transacciones</h1>
        <p className="text-sm text-zinc-500 mt-1">
          Historial de transacciones procesadas por la plataforma.
          <span className="ml-2 text-amber-500 font-medium">
            [Datos de muestra — endpoint en desarrollo]
          </span>
        </p>
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-base">Listado de transacciones</CardTitle>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>ID</TableHead>
                <TableHead>Comerciante</TableHead>
                <TableHead className="text-right">Monto</TableHead>
                <TableHead>Estado</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {mockTransactions.map((t) => (
                <TableRow key={t.id}>
                  <TableCell className="font-mono text-xs">{t.id}</TableCell>
                  <TableCell>{t.merchantId}</TableCell>
                  <TableCell className="text-right font-medium">
                    {formatCOP(t.amount)}
                  </TableCell>
                  <TableCell>
                    <Badge variant={statusVariantMap[t.status]}>
                      {statusLabelMap[t.status]}
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
