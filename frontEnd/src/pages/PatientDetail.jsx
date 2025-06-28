import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';

function PatientDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { username, password } = useAuth();
  const [patient, setPatient] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    axios.get(`http://localhost:8080/api/patients/${id}`, {
      auth: { username, password }
    })
    .then(res => {
      setPatient(res.data);
    })
    .catch(() => {
      setError("Erreur lors du chargement du patient");
    });
  }, [id, username, password]);

  if (error) {
    return <div style={{ color: 'red', textAlign: 'center', marginTop: 40 }}>{error}</div>;
  }

  if (!patient) {
    return <div style={{ textAlign: 'center', marginTop: 40 }}>Chargement...</div>;
  }

  return (
    <div style={{ maxWidth: 600, margin: 'auto', marginTop: 40 }}>
      <h2>Détails du patient</h2>
      <p><strong>Nom :</strong> {patient.nom}</p>
      <p><strong>Prénom :</strong> {patient.prenom}</p>
      <p><strong>Date de naissance :</strong> {new Date(patient.dateNaissance).toLocaleDateString()}</p>
      <p><strong>Genre :</strong> {patient.genre}</p>
      <p><strong>Adresse postale :</strong> {patient.adressePostale || 'Non renseignée'}</p>
      <p><strong>Téléphone :</strong> {patient.telephone || 'Non renseigné'}</p>

      <button onClick={() => navigate(`/patients/${id}/edit`)} title="Modifier" style={{ marginTop: 20 }}>
        ✏️ Modifier le dossier
      </button>{' '}
      <button onClick={() => navigate('/patients')} style={{ marginTop: 20 }}>
        Retour à la liste
      </button>
    </div>
  );
}

export default PatientDetail;
