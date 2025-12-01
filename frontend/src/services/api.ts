const BASE_URL = "http://localhost:8080/api";

/**
 * Interface defining the optional parameters for an API request.
 * @template TBody - The type of the request body.
 */
interface RequestOptions<TBody = unknown> {
  /** The HTTP method (e.g., "GET", "POST"). Defaults to "GET". */
  method?: string;
  /** Custom request headers. */
  headers?: Record<string, string>;
  /** The body of the request, to be JSON stringified. */
  body?: TBody;
  /** JWT token for Authorization header (Bearer). */
  token?: string;
}

/**
 * Generic function to perform API requests to the backend.
 * Handles setting headers (Content-Type, Authorization) and error checking.
 * @template TResponse - The expected type of the JSON response body.
 * @template TBody - The type of the request body payload.
 * @param {string} endpoint - The API endpoint path (e.g., "/login").
 * @param {RequestOptions<TBody>} [options={}] - Request options including method, body, and token.
 * @returns {Promise<TResponse>} A promise that resolves to the typed JSON data.
 * @throws {Error} Throws an error if the response status is not OK (2xx).
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
    credentials: "include", // Include cookies, if any
  });

  if (!res.ok) {
    const errorText = await res.text();
    throw new Error(errorText || `Errore ${res.status}`);
  }

  return res.json() as Promise<TResponse>;
}

/**
 * Type definition for the response received after a successful or failed login attempt.
 */
export type LoginResponse = {
  username: string;
  roles: string[];
  success: boolean;
  message: string;
};

/**
 * Sends a request to log in a user with the provided credentials.
 * @param {string} email - The user's email address.
 * @param {string} password - The user's password.
 * @returns {Promise<LoginResponse>} A promise resolving to the login response data.
 */
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

/**
 * Type definition for a property (Immobile) listing.
 */
export type Immobile = {
  tipo: string;
  nomeProprietario: string;
  dataInserimento: string;
  agenteAssegnato: string | null;
  id: number;
  via: string;
  citta: string;
};

/**
 * Type definition for the data returned by the Admin Dashboard API.
 */
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

/**
 * Fetches the data for the Admin Dashboard, including statistics and a list of properties (Immobile).
 * @param {number} [offset=0] - The starting offset for pagination.
 * @param {number} [limit=10] - The maximum number of items to return.
 * @returns {Promise<AdminDashboardData>} A promise resolving to the Admin Dashboard data.
 */
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

/**
 * Type definition for the data returned by the Agent Dashboard API.
 */
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

/**
 * Fetches the data for the Agent Dashboard, including personalized statistics and a list of properties (Immobile).
 * @param {number} [offset=0] - The starting offset for pagination.
 * @param {number} [limit=10] - The maximum number of items to return.
 * @returns {Promise<AgenteDashboardData>} A promise resolving to the Agent Dashboard data.
 */
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

/**
 * Type definition for user role information.
 */
export type TipoUtente = {
  idTipo: number;
  nome: string;
  role: string;
};

/**
 * Type definition for a user object.
 */
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

/**
 * Fetches a list of all users.
 * @returns {Promise<Users[]>} A promise resolving to an array of user objects.
 */
export async function getUsers(): Promise<Users[]> {
  return apiFetch<Users[]>(`/users`, { method: "GET" });
}

/**
 * Type definition for the request body when registering a new user.
 */
export type RegisterUserRequest = {
  nome: string;
  cognome: string;
  email: string;
  password: string;
  telefono: string;
  tipoUtente: TipoUtente;
};

/**
 * Registers a new user with the provided details.
 * @param {string} nome - User's first name.
 * @param {string} cognome - User's last name.
 * @param {string} email - User's email.
 * @param {string} password - User's password.
 * @param {string} telefono - User's phone number.
 * @param {TipoUtente} tipoUtente - User's type/role.
 * @returns {Promise<RegisterUserRequest[]>} A promise resolving to the registration response (note: type seems off, might return registered user or success status).
 */
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

/**
 * Type definition for the request body when updating an existing user.
 * All fields are optional.
 */
export type UpdateUserRequest = {
  nome?: string;
  cognome?: string;
  email?: string;
  telefono?: string;
  via?: string;
  password?: string;
  tipoUtente?: { idTipo?: number; nome?: string; role?: string };
};

/**
 * Updates an existing user with the provided ID and fields.
 * @param {number} id - The ID of the user to update.
 * @param {UpdateUserRequest} updatedUser - An object containing the fields to update.
 * @returns {Promise<Users>} A promise resolving to the updated user object.
 */
export async function updateUser(
  id: number,
  updatedUser: UpdateUserRequest
): Promise<Users> {
  return apiFetch<Users>(`/users/${id}`, {
    method: "PUT",
    body: updatedUser,
  });
}

/**
 * Type definition for a closed contract object.
 */
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

/**
 * Type definition for the paginated response containing closed contracts.
 */
export type ContrattiResponse = {
  contratti: ContrattoChiuso[];
  nextOffset: number;
  hasMore: boolean;
  pageSize: number;
};

/**
 * Fetches a paginated list of all closed contracts (Admin access).
 * @param {number} [offset=0] - The starting offset for pagination.
 * @param {number} [limit=10] - The maximum number of contracts to return.
 * @returns {Promise<ContrattiResponse>} A promise resolving to the list of closed contracts.
 */
export async function getContrattiChiusi(
  offset = 0,
  limit = 10
): Promise<ContrattiResponse> {
  return apiFetch<ContrattiResponse>(
    `/admin/contratti/chiusi?offset=${offset}&limit=${limit}`,
    { method: "GET" }
  );
}

/**
 * Fetches a paginated list of closed contracts associated with a specific agent ID.
 * @param {number} [offset=0] - The starting offset for pagination.
 * @param {number} [limit=10] - The maximum number of contracts to return.
 * @param {string} agenteId - The ID of the agent whose contracts to retrieve.
 * @returns {Promise<ContrattiResponse>} A promise resolving to the list of closed contracts for the agent.
 */
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

/**
 * Interface defining an AI evaluation object.
 */
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

/**
 * Interface defining the paginated response containing AI evaluations.
 */
export interface ValutazioniAIResponse {
  valutazioni: ValutazioneAI[];
  nextOffset: number;
  hasMore: boolean;
  pageSize: number;
}

/**
 * Fetches a paginated list of evaluations generated solely by the AI (Admin/Agent access).
 * @param {number} [offset=0] - The starting offset for pagination.
 * @param {number} [limit=10] - The maximum number of evaluations to return.
 * @returns {Promise<ValutazioniAIResponse>} A promise resolving to the list of AI evaluations.
 */
export async function getValutazioniSoloAI(
  offset = 0,
  limit = 10
): Promise<ValutazioniAIResponse> {
  return apiFetch(
    `/admin/valutazioni/solo-ai?offset=${offset}&limit=${limit}`,
    { method: "GET" }
  );
}

/**
 * Deletes a specific AI evaluation by ID (Admin access).
 * @param {number} id - The ID of the AI evaluation to delete.
 * @returns {Promise<any>} A promise resolving to the API response (usually success status).
 */
export async function deleteValutazioneAI(id: number) {
  return apiFetch(`/admin/valutazioni/solo-ai/${id}`, {
    method: "DELETE",
  });
}

/**
 * Interface defining the request body for assigning an assignment to an agent.
 */
export interface AssignIncaricoRequest {
  agenteId: string;
  agenteNome: string;
}

/**
 * Assigns an AI evaluation to a specific agent as a new assignment (Incarico).
 * @param {number} valutazioneId - The ID of the evaluation to assign.
 * @param {string} agenteId - The ID of the agent taking the assignment.
 * @param {string} agenteNome - The name of the agent taking the assignment.
 * @returns {Promise<{ success: boolean; message: string }>} A promise resolving to the success status and message.
 */
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

/**
 * Interface defining an assignment (Incarico) object, which is an evaluation taken by an agent for verification.
 * It includes both AI and potential human pricing.
 */
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

/**
 * Interface defining the paginated response containing assignments (Incarichi).
 */
export interface incarichiResponse {
  valutazioni: Incarichi[];
  nextOffset: number;
  hasMore: boolean;
  pageSize: number;
}

/**
 * Fetches a paginated list of assignments currently in verification (Admin access).
 * @param {number} [offset=0] - The starting offset for pagination.
 * @param {number} [limit=10] - The maximum number of assignments to return.
 * @returns {Promise<incarichiResponse>} A promise resolving to the list of assignments.
 */
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

/**
 * Fetches a paginated list of assignments belonging to a specific agent ID.
 * @param {number} [offset=0] - The starting offset for pagination.
 * @param {number} [limit=10] - The maximum number of assignments to return.
 * @param {string} agenteId - The ID of the agent whose assignments to retrieve.
 * @returns {Promise<incarichiResponse>} A promise resolving to the list of assignments for the agent.
 */
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

/**
 * Deletes a specific assignment (Incarico) by ID (Admin access).
 * @param {number} id - The ID of the assignment to delete.
 * @returns {Promise<{ success: boolean; message: string }>} A promise resolving to the success status and message.
 */
export async function deleteIncarichi(id: number) {
  return apiFetch<{ success: boolean; message: string }>(
    `/admin/valutazioni/in-verifica/${id}`,
    {
      method: "DELETE",
    }
  );
}