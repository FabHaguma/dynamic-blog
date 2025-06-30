import React, { createContext, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('authToken'));
  const navigate = useNavigate();

  const login = (newToken) => {
    localStorage.setItem('authToken', newToken);
    setToken(newToken);
    navigate('/admin/dashboard');
  };

  const logout = () => {
    localStorage.removeItem('authToken');
    setToken(null);
    navigate('/login');
  };

  const value = { token, login, logout };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  return useContext(AuthContext);
};