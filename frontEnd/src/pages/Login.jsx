import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function Login() {
  const [usernameInput, setUsernameInput] = useState('');
  const [passwordInput, setPasswordInput] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      // Essai d’accès à une ressource protégée
      await axios.get('http://localhost:8080/api/patients', {
        auth: {
          username: usernameInput,
          password: passwordInput
        }
      });

      // Auth réussie → stocke dans le contexte
      login(usernameInput, passwordInput);
      navigate('/patients');
    } catch (err) {
      setError("Identifiants invalides.");
    }
  };

  return (
    <div style={{ maxWidth: 300, margin: 'auto', marginTop: '100px' }}>
      <h2>Connexion</h2>
      <form onSubmit={handleLogin}>
        <input
          type="text"
          placeholder="Nom d'utilisateur"
          value={usernameInput}
          onChange={(e) => setUsernameInput(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Mot de passe"
          value={passwordInput}
          onChange={(e) => setPasswordInput(e.target.value)}
          required
          style={{ marginTop: 10 }}
        />
        <button type="submit" style={{ marginTop: 20 }}>Se connecter</button>
        {error && <p style={{ color: 'red' }}>{error}</p>}
      </form>
    </div>
  );
}

export default Login;
