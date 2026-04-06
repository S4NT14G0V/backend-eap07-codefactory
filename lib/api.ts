const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

// ─── Types ────────────────────────────────────────────────────────────────────

export interface CodeRequest {
  username: string;
  code: number;
}

export interface Merchant {
  id: string;
  businessName: string;
  businessId: string;
  email: string;
  status: "INACTIVE" | "VERIFIED" | "SUSPENDED";
}

export interface ApiCredential {
  id: string;
  publicId: string;
  plainSecret?: string;
  active: boolean;
  merchantId: string;
}

export interface Transaction {
  id: string;
  merchantId: string;
  amount: number;
  status: "CREATED" | "PROCESSING" | "APPROVED" | "REJECTED" | "FAILED";
}

// ─── HTTP helper ──────────────────────────────────────────────────────────────

async function request<T>(
  path: string,
  options: RequestInit = {}
): Promise<T> {
  const res = await fetch(`${BASE_URL}${path}`, {
    headers: { "Content-Type": "application/json", ...options.headers },
    ...options,
  });

  if (!res.ok) {
    const text = await res.text().catch(() => res.statusText);
    throw new Error(text || `HTTP ${res.status}`);
  }

  // Some endpoints return plain boolean / empty body
  const contentType = res.headers.get("content-type") ?? "";
  if (contentType.includes("application/json")) {
    return res.json() as Promise<T>;
  }
  return res.text() as unknown as T;
}

// ─── Security ─────────────────────────────────────────────────────────────────

export const authApi = {
  verify2fa: (body: CodeRequest) =>
    request<boolean>("/2fa/verify", {
      method: "POST",
      body: JSON.stringify(body),
    }),
};

// ─── Merchants (ready for future sprint endpoints) ────────────────────────────

export const merchantApi = {
  list: () => request<Merchant[]>("/merchants"),
  getById: (id: string) => request<Merchant>(`/merchants/${id}`),
  create: (data: Omit<Merchant, "id" | "status">) =>
    request<Merchant>("/merchants", { method: "POST", body: JSON.stringify(data) }),
};

// ─── Credentials (ready for future sprint endpoints) ─────────────────────────

export const credentialApi = {
  generate: (merchantId: string) =>
    request<ApiCredential>(`/merchants/${merchantId}/credentials`, {
      method: "POST",
    }),
  list: (merchantId: string) =>
    request<ApiCredential[]>(`/merchants/${merchantId}/credentials`),
};

// ─── Transactions (ready for future sprint endpoints) ─────────────────────────

export const transactionApi = {
  list: () => request<Transaction[]>("/transactions"),
  create: (data: Pick<Transaction, "merchantId" | "amount">) =>
    request<Transaction>("/transactions", {
      method: "POST",
      body: JSON.stringify(data),
    }),
};
