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

export type Immobile = {
  tipo: string;
  nomeProprietario: string;
  dataInserimento: string;
  agenteAssegnato: string | null;
};

export type DashboardData = {
  statistics: {
    contrattiConclusi: number;
    valutazioniInCorso: number;
    valutazioniConAI: number;
    contrattiConclusiMensili: number;
    valutazioniInCorsoMensili: number;
    valutazioniConAIMensili: number;
  };
  immobili: Immobile[];
  nextOffset: number;
  hasMore: boolean;
  pageSize: number;
};

export async function getAdminDashboard(
  offset = 0,
  limit = 10
): Promise<DashboardData> {
  return apiFetch<DashboardData>(
    `/admin/dashboard?offset=${offset}&limit=${limit}`,
    {
      method: "GET",
    }
  );
}

// --- USERS REQUEST --- //
export type TipoUtente = {
  idTipo: number;
  nomeTipo: string;
};

export type Users = {
  idUtente: number;
  nome: string;
  cognome: string;
  email: string;
  telefono: string;
  via: string;
  citta: string;
  cap: string;
  dataRegistrazione: string;
  tipoUtente: TipoUtente;
};

export async function getUsers(): Promise<Users[]> {
  return apiFetch<Users[]>(`/admin/users`, { method: "GET" });
}

// --- REGISTER USER REQUEST --- //
export type RegisterUserRequest = {
  nome: string;
  cognome: string;
  email: string;
  password: string;
  tipoUtente: TipoUtente;
};

export async function registerUser(
  nome: string,
  cognome: string,
  email: string,
  password: string,
  tipoUtente: TipoUtente
): Promise<RegisterUserRequest[]> {
  return apiFetch<RegisterUserRequest[]>(`/admin/user-register`, {
    method: "POST",
    body: {
      nome,
      cognome,
      email,
      password,
      tipoUtente,
    },
  });
}

// --- CONTRATTI CONCLUSI REQUEST --- //
export type ContrattoChiuso = {
  numeroContratto: string;
  dataInizio: string;
  dataFine: string;
  tipo: string | null;
  nomeProprietario: string | null;
  dataInserimento: string | null;
  agenteAssegnato: string | null;
};

export type ContrattiResponse = {
  contratti: ContrattoChiuso[];
  nextOffset: number;
  hasMore: boolean;
  pageSize: number;
};

export async function getContrattiChiusi(
  offset = 0,
  limit = 10
): Promise<ContrattiResponse> {
  return apiFetch<ContrattiResponse>(
    `/admin/contratti/chiusi?offset=${offset}&limit=${limit}`,
    { method: "GET" }
  );
}

// --- VALUTAZIONI AI REQUEST --- //
export interface ValutazioneAI {
  id: number;
  prezzoAI: number | null;
  dataValutazione: string;
  descrizione: string | null;

  // Immobile
  tipo: string | null;
  via: string | null;
  citta: string | null;
  cap: string | null;
  provincia: string | null;
  metratura: number | null;
  condizioni: string | null;
  stanze: number | null;
  bagni: number | null;
  piano: number | null;
  ascensore: boolean | null;
  garage: boolean | null;
  giardino: boolean | null;
  balcone: boolean | null;
  terrazzo: boolean | null;
  cantina: boolean | null;
  riscaldamento: string | null;

  // Proprietario
  nomeProprietario: string | null;
  emailProprietario: string | null;
  telefonoProprietario: string | null;

  dataInserimento: string | null;
}

export interface ValutazioniAIResponse {
  valutazioni: ValutazioneAI[];
  nextOffset: number;
  hasMore: boolean;
  pageSize: number;
}

export async function getValutazioniSoloAI(
  offset = 0,
  limit = 10
): Promise<ValutazioniAIResponse> {
  return apiFetch(
    `/admin/valutazioni/solo-ai?offset=${offset}&limit=${limit}`,
    { method: "GET" }
  );
}

export async function deleteValutazioneAI(id: number) {
  return apiFetch(`/admin/valutazioni/solo-ai/${id}`, {
    method: "DELETE",
  });
}

// --- INCARICHI REQUEST --- //
export interface Incarichi {
  id: number;
  prezzoAI: number | null;
  prezzoUmano: number | null;
  dataValutazione: string;
  statoValutazione: string | null;
  descrizione: string | null;

  // Agente
  nomeAgente: string | null;
  emailAgente: string | null;

  // Immobile
  tipo: string | null;
  via: string | null;
  citta: string | null;
  cap: string | null;
  provincia: string | null;
  metratura: number | null;
  condizioni: string | null;
  stanze: number | null;
  bagni: number | null;
  piano: number | null;
  ascensore: boolean | null;
  garage: boolean | null;
  giardino: boolean | null;
  balcone: boolean | null;
  terrazzo: boolean | null;
  cantina: boolean | null;
  riscaldamento: string | null;

  // Proprietario
  nomeProprietario: string | null;
  emailProprietario: string | null;
  telefonoProprietario: string | null;

  // Immobile extra
  dataInserimento: string | null;
}

export interface incarichiResponse {
  valutazioni: Incarichi[];
  nextOffset: number;
  hasMore: boolean;
  pageSize: number;
}

export async function getIncarichi(
  offset = 0,
  limit = 10
): Promise<incarichiResponse> {
  return apiFetch<incarichiResponse>(
    `/admin/valutazioni/in-verifica?offset=${offset}&limit=${limit}`,
    {
      method: "GET",
    }
  );
}

export async function deleteIncarichi(id: number) {
  return apiFetch<{ success: boolean; message: string }>(
    `/admin/valutazioni/in-verifica/${id}`,
    {
      method: "DELETE",
    }
  );
}
