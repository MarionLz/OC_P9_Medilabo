import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

function PatientList() {
  const { username, password } = useAuth();
  const [patients, setPatients] = useState([]);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  // Charger la liste des patients
  useEffect(() => {
    axios.get('http://localhost:8080/api/patients', {
      auth: { username, password }
    })
    .then(res => {
      setPatients(res.data);
    })
    .catch(() => {
      setError("Impossible de charger les patients");
    });
  }, [username, password]);

  return (
    <div style={{ maxWidth: 800, margin: 'auto', marginTop: 40 }}>
      <h2>Patients</h2>

      <button onClick={() => navigate('/patients/new')} style={{ marginBottom: 20 }}>
        Ajouter un nouveau patient
      </button>

      {error && <p style={{ color: 'red' }}>{error}</p>}

      <table border="1" cellPadding="5" cellSpacing="0" width="100%">
        <thead>
          <tr>
            <th>Nom</th>
            <th>Prénom</th>
            <th>Genre</th>
            <th>Date de naissance</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {patients.map(patient => (
            <tr key={patient.id}>
              <td>{patient.nom}</td>
              <td>{patient.prenom}</td>
              <td>{patient.genre}</td>
              <td>{new Date(patient.dateNaissance).toLocaleDateString()}</td>
              <td>
                <button onClick={() => navigate(`/patients/${patient.id}/edit`)} title="Modifier">✏️</button>{' '}
                <button onClick={() => navigate(`/patients/${patient.id}`)} title="Détails">ℹ️</button>
              </td>
            </tr>
          ))}
          {patients.length === 0 && !error && (
            <tr><td colSpan="5" style={{ textAlign: 'center' }}>Aucun patient</td></tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default PatientList;
