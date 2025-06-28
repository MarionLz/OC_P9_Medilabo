import React, { createContext, useContext, useState } from 'react';

// 1. Crée le contexte
const AuthContext = createContext();

// 2. Fournisseur du contexte (AuthProvider)
export function AuthProvider({ children }) {
  const [username, setUsername] = useState(sessionStorage.getItem("username") || null);
  const [password, setPassword] = useState(sessionStorage.getItem("password") || null);

  const login = (user, pass) => {
    sessionStorage.setItem("username", user);
    sessionStorage.setItem("password", pass);
    setUsername(user);
    setPassword(pass);
  };

  const logout = () => {
    sessionStorage.clear();
    setUsername(null);
    setPassword(null);
  };

  const isAuthenticated = !!username && !!password;

  return (
    <AuthContext.Provider value={{ username, password, login, logout, isAuthenticated }}>
      {children}
    </AuthContext.Provider>
  );
}

// 3. Hook personnalisé pour accéder facilement au contexte
export function useAuth() {
  return useContext(AuthContext);
}
