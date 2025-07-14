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

  const [notes, setNotes] = useState([]);
  const [newNote, setNewNote] = useState('');
  const [notesError, setNotesError] = useState('');

  const [riskLevel, setRiskLevel] = useState('');
  const [riskLoading, setRiskLoading] = useState(false);
  const [riskError, setRiskError] = useState('');

  // Charger les infos du patient
  useEffect(() => {
    axios.get(`http://localhost:8080/api/patients/${id}`, {
      auth: { username, password }
    })
    .then(res => setPatient(res.data))
    .catch(() => setError("Erreur lors du chargement du patient"));
  }, [id, username, password]);

  // Charger les notes du patient
  useEffect(() => {
    axios.get(`http://localhost:8080/api/notes/patient/${id}`, {
      auth: { username, password }
    })
    .then(res => {
      const receivedNotes = res.data;
      setNotes(receivedNotes);
      if (receivedNotes.length === 0) {
        setNotesError("Aucune note trouvée pour ce patient.");
      }
    })
    .catch(() => {
      setNotesError("Erreur lors de la récupération des notes.");
    });
  }, [id, username, password]);

    // Charger le niveau de risque
    const fetchRiskLevel = () => {
      setRiskLoading(true);
      setRiskError('');
      axios.get(`http://localhost:8080/api/diabetes-report/patient/${id}`, {
        auth: { username, password }
      })
      .then(res => {
        // Supposons que la réponse contienne un champ "riskLevel"
        setRiskLevel(res.data || 'NON DEFINI');
        setRiskLoading(false);
      })
      .catch(() => {
        setRiskError("Impossible de récupérer le niveau de risque.");
        setRiskLoading(false);
      });
    };

    // Charger le risque au chargement du patient (ou de l’id)
    useEffect(() => {
      if (id) {
        fetchRiskLevel();
      }
    }, [id, username, password]);

  // Ajouter une nouvelle note
  const handleAddNote = () => {
    if (!newNote.trim()) return;

    const data = {
      patient: patient.lastName,  // ou patient.firstName + ' ' + patient.lastName si tu veux
      notes: newNote
    };

    axios.post(`http://localhost:8080/api/notes/patient/${id}`, data, {
      headers: { 'Content-Type': 'application/json' },
      auth: { username, password }
    })
    .then(() => {
      setNotes(prev => [...prev, newNote]);
      setNewNote('');
    })
    .catch(() => {
      alert("Erreur lors de l'ajout de la note.");
    });
  };

  if (error) {
    return <div style={{ color: 'red', textAlign: 'center', marginTop: 40 }}>{error}</div>;
  }

  if (!patient) {
    return <div style={{ textAlign: 'center', marginTop: 40 }}>Chargement...</div>;
  }

  return (
    <div style={{ maxWidth: 600, margin: 'auto', marginTop: 40 }}>
      <h2>Détails du patient</h2>
      <p><strong>Nom :</strong> {patient.lastName}</p>
      <p><strong>Prénom :</strong> {patient.firstName}</p>
      <p><strong>Date de naissance :</strong> {new Date(patient.dateOfBirth).toLocaleDateString()}</p>
      <p><strong>Genre :</strong> {patient.gender}</p>
      <p><strong>Adresse postale :</strong> {patient.address || 'Non renseignée'}</p>
      <p><strong>Téléphone :</strong> {patient.phoneNumber || 'Non renseigné'}</p>

      <h3 style={{ marginTop: 30 }}>📝 Notes</h3>
      {notesError ? (
        <p style={{ color: 'gray' }}>{notesError}</p>
      ) : (
        <details open style={{ marginBottom: 10 }}>
          <summary>Voir les notes ({notes.length})</summary>
          <ul>
            {notes.map((note, index) => (
              <li key={index} style={{ marginBottom: 5 }}>{note}</li>
            ))}
          </ul>
        </details>
      )}

      <div style={{ marginTop: 20 }}>
        <textarea
          rows={3}
          value={newNote}
          onChange={(e) => setNewNote(e.target.value)}
          placeholder="Ajouter une nouvelle note..."
          style={{ width: '100%', padding: 8 }}
        />
        <button onClick={handleAddNote} style={{ marginTop: 10 }}>
          ➕ Ajouter la note
        </button>
      </div>


      <div style={{ display: 'flex', alignItems: 'center', marginTop: 20, gap: 10 }}>
        <div style={{
            border: '1px solid #ccc',
            padding: 10,
            borderRadius: 4,
            minWidth: 150,
            textAlign: 'center',
            backgroundColor: riskLevel === 'IN_DANGER' ? '#f8d7da' : '#d1e7dd',
            color: riskLevel === 'IN_DANGER' ? '#842029' : '#0f5132',
            fontWeight: 'bold',
        }}>
            {riskLoading ? 'Chargement...' : `Risque: ${riskLevel}`}
            {riskError && <div style={{ color: 'red', fontSize: 12 }}>{riskError}</div>}
        </div>
        <button onClick={fetchRiskLevel} disabled={riskLoading}>
            🔄 Recalculer le risque
        </button>
      </div>

      <div style={{ marginTop: 30 }}>
        <button onClick={() => navigate(`/patients/${id}/edit`)}>
          ✏️ Modifier le dossier
        </button>{' '}
        <button onClick={() => navigate('/patients')}>
          Retour à la liste
        </button>
      </div>
    </div>
  );
}

export default PatientDetail;
