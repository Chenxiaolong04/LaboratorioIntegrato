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

export type Immobile = {
  tipo: string;
  nomeProprietario: string;
  dataInserimento: string;
  agenteAssegnato: string | null;
  id: number;
  via: string;
  citta: string;
};

export type AdminDashboardData = {
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
): Promise<AdminDashboardData> {
  return apiFetch<AdminDashboardData>(
    `/admin/dashboard?offset=${offset}&limit=${limit}`,
    {
      method: "GET",
    }
  );
}

export type AgenteDashboardData = {
  statistics: {
    mieiContrattiConclusi: number;
    mieiIncarichiInCorso: number;
    mieiContrattiConclusiMensili: number;
    mieiIncarichiNuoviMensili: number;
    valutazioniConAI: number;
    valutazioniConAIMensili: number;
  };
  immobili: Immobile[];
  nextOffset: number;
  hasMore: boolean;
  pageSize: number;
};

export async function getAgenteDashboard(
  offset: number = 0,
  limit: number = 10
): Promise<AgenteDashboardData> {
  return apiFetch<AgenteDashboardData>(
    `/agent/dashboard?offset=${offset}&limit=${limit}`,
    {
      method: "GET",
    }
  );
}

export type TipoUtente = {
  idTipo: number;
  nome: string;
  role: string;
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
  return apiFetch<Users[]>(`/users`, { method: "GET" });
}

export type RegisterUserRequest = {
  nome: string;
  cognome: string;
  email: string;
  password: string;
  telefono: string;
  tipoUtente: TipoUtente;
};

export async function registerUser(
  nome: string,
  cognome: string,
  email: string,
  password: string,
  telefono: string,
  tipoUtente: TipoUtente
): Promise<RegisterUserRequest[]> {
  return apiFetch<RegisterUserRequest[]>(`/users/register`, {
    method: "POST",
    body: {
      nome,
      cognome,
      email,
      password,
      telefono,
      tipoUtente,
    },
  });
}

export type UpdateUserRequest = {
  nome?: string;
  cognome?: string;
  email?: string;
  telefono?: string;
  via?: string;
  password?: string;
  tipoUtente?: { idTipo?: number; nome?: string; role?: string };
};

export async function updateUser(
  id: number,
  updatedUser: UpdateUserRequest
): Promise<Users> {
  return apiFetch<Users>(`/users/${id}`, {
    method: "PUT",
    body: updatedUser,
  });
}

export type ContrattoChiuso = {
  numeroContratto: string;
  dataInizio: string;
  dataFine: string;
  tipo: string | null;
  nomeProprietario: string | null;
  dataInserimento: string | null;
  agenteAssegnato: string | null;
  via: string | null;
  citta: string | null;
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

export async function getContrattiChiusiByAgente(
  offset: number = 0,
  limit: number = 10,
  agenteId: string
): Promise<ContrattiResponse> {
  return apiFetch<ContrattiResponse>(
    `/agente/contratti/chiusi/${agenteId}?offset=${offset}&limit=${limit}`,
    { method: "GET" }
  );
}

export interface ValutazioneAI {
  id: number;
  prezzoAI: number | null;
  dataValutazione: string;
  descrizione: string | null;

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

export interface AssignIncaricoRequest {
  agenteId: string;
  agenteNome: string;
}

export async function assignIncaricoToMe(
  valutazioneId: number,
  agenteId: string,
  agenteNome: string
): Promise<{ success: boolean; message: string }> {
  return apiFetch<{ success: boolean; message: string }, AssignIncaricoRequest>(
    `/agente/incarico/prendi/${valutazioneId}`,
    {
      method: "POST",
      body: {
        agenteId: agenteId,
        agenteNome: agenteNome,
      },
    }
  );
}

export interface Incarichi {
  id: number;
  prezzoAI: number | null;
  prezzoUmano: number | null;
  dataValutazione: string;
  statoValutazione: string | null;
  descrizione: string | null;

  nomeAgente: string | null;
  emailAgente: string | null;

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

  nomeProprietario: string | null;
  emailProprietario: string | null;
  telefonoProprietario: string | null;

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

export async function getIncarichiByAgente(
  offset: number = 0,
  limit: number = 10,
  agenteId: string
): Promise<incarichiResponse> {
  return apiFetch<incarichiResponse>(
    `/agente/incarichi/${agenteId}?offset=${offset}&limit=${limit}`,
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

export interface AddressSuggestion {
  displayName: string;
  via: string;
  citta: string;
  cap: string;
  civico: string;
  lat: number;
  lon: number;
}

export interface AddressValidationResponse {
  valid: boolean;
  suggestions: AddressSuggestion[];
}

export async function validateAddress(
  via: string,
  citta: string
): Promise<AddressValidationResponse> {
  return apiFetch<AddressValidationResponse, { via: string; citta: string }>(
    "/address/validate",
    {
      method: "POST",
      body: { via, citta },
    }
  );
}

export interface SaveImmobileBody {
  via: string;
  citta: string;
  cap: string;
  tipologia: string;
  metratura: number;
  condizioni: string;
  stanze: number;
  bagni: number;
  riscaldamento: string;
  piano: number;
  ascensore: boolean;
  garage: boolean;
  giardino: boolean;
  balcone: boolean;
  terrazzo: boolean;
  cantina: boolean;
  nome: string;
  cognome: string;
  email: string;
  telefono: string;
}

export interface SaveImmobileResponse {
  success: boolean;
  id: string;
  message: string;
}

export async function saveImmobile(
  data: SaveImmobileBody
): Promise<SaveImmobileResponse> {
  return apiFetch<SaveImmobileResponse, SaveImmobileBody>("/immobili/save", {
    method: "POST",
    body: data,
  });
}
