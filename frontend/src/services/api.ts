const BASE_URL = "http://localhost:8080/api";

interface RequestOptions<TBody = unknown> {
  method?: string;
  headers?: Record<string, string>;
  body?: TBody;
  token?: string;
}

/**
 * Funzione generica per effettuare richieste API.
 * @param endpoint - es: "/login"
 * @param options - metodo, body, token, ecc.
 * @returns dati JSON tipizzati
 */
export async function apiFetch<TResponse, TBody = unknown>(
  endpoint: string,
  options: RequestOptions<TBody> = {}
): Promise<TResponse> {
  const { method = "GET", body, token } = options;

  const res = await fetch(`${BASE_URL}${endpoint}`, {
    method,
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: body ? JSON.stringify(body) : undefined,
    credentials: "include",
  });

  if (!res.ok) {
    const errorText = await res.text();
    throw new Error(errorText || `Errore ${res.status}`);
  }

  return res.json() as Promise<TResponse>;
}

// --- LOGIN REQUEST --- //
export type LoginResponse = {
  username: string;
  roles: string[];
  success: boolean;
  message: string;
};


export async function loginUser(
  email: string,
  password: string
): Promise<LoginResponse> {
  return apiFetch<LoginResponse, { email: string; password: string }>(
    "/auth/login",
    {
      method: "POST",
      body: { email, password },
    }
  );
}


// --- DASHBOARD ADMIN REQUEST --- //

export type DashboardData = {
  statistics: {
    contrattiConclusi: number;
    valutazioniInCorso: number;
    valutazioniConAI: number;
    contrattiConclusiMensili: number;
    valutazioniInCorsoMensili: number;
    valutazioniConAIMensili: number;
  };
  ultimi10Immobili: {
    tipo: string;
    nomeProprietario: string;
    dataInserimento: string;
    agenteAssegnato: string | null;
  }[];
};

export async function getAdminDashboard(): Promise<DashboardData> {
  return apiFetch<DashboardData>("/admin/dashboard", {
    method: "GET",
  });
}
