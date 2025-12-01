// src/context/AuthContext.tsx
import { createContext, useContext, useState, type ReactNode, useEffect } from "react";

/**
 * User type for authentication context.
 */
type User = {
  id: number;
  name: string;
  email: string;
  role: "admin" | "agente";
};

/**
 * Type of the authentication context.
 */
type AuthContextType = {
  /** The currently logged-in user or null if not authenticated */
  user: User | null;
  /** Function to log in a user */
  login: (userData: User) => void;
  /** Function to log out the user */
  logout: () => void;
};

/** Authentication context */
const AuthContext = createContext<AuthContextType | undefined>(undefined);

/**
 * Provider component for authentication context.
 *
 * Manages user state and persists it in localStorage.
 *
 * @param {Object} props
 * @param {ReactNode} props.children - Child components that will have access to the context.
 * @returns {JSX.Element} The AuthContext provider.
 */
export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);

  // Load user from localStorage on app start
  useEffect(() => {
    const storedUser = localStorage.getItem("user");
    if (storedUser) {
      try {
        setUser(JSON.parse(storedUser));
      } catch (e) {
        console.error("Error parsing user from localStorage:", e);
        localStorage.removeItem("user");
      }
    }
  }, []);

  /**
   * Logs in a user and stores it in localStorage.
   *
   * @param {User} userData - User data to be saved.
   */
  const login = (userData: User) => {
    setUser(userData);
    localStorage.setItem("user", JSON.stringify(userData)); 
  };

  /**
   * Logs out the user and removes it from localStorage.
   */
  const logout = () => {
    setUser(null);
    localStorage.removeItem("user"); 
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

/**
 * Hook to access the authentication context.
 *
 * @throws {Error} If used outside of AuthProvider.
 * @returns {AuthContextType} The authentication context.
 */
export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used inside AuthProvider");
  return context;
}
