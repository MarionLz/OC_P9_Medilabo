import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';

function PatientForm() {
  const { id } = useParams(); // id = undefined si nouveau patient
  const navigate = useNavigate();
  const { username, password } = useAuth();

  // État du formulaire
  const [patient, setPatient] = useState({
    nom: '',
    prenom: '',
    dateNaissance: '',
    genre: '',
    adressePostale: '',
    telephone: ''
  });
  const [errors, setErrors] = useState({});

  // Chargement des données si modification
  useEffect(() => {
    if (id) {
      axios.get(`http://localhost:8080/api/patients/${id}`, {
        auth: { username, password }
      }).then(res => {
        // format date ISO en yyyy-MM-dd pour input date
        const data = res.data;
        setPatient({
          nom: data.nom || '',
          prenom: data.prenom || '',
          dateNaissance: data.dateNaissance ? data.dateNaissance.slice(0,10) : '',
          genre: data.genre || '',
          adressePostale: data.adressePostale || '',
          telephone: data.telephone || ''
        });
      }).catch(() => {
        alert("Erreur lors du chargement du patient");
      });
    }
  }, [id, username, password]);

  // Validation simple avant submit
  function validate() {
    const newErrors = {};
    if (!patient.nom.trim()) newErrors.nom = 'Le nom est obligatoire';
    if (!patient.prenom.trim()) newErrors.prenom = 'Le prénom est obligatoire';
    if (!patient.dateNaissance) newErrors.dateNaissance = 'La date de naissance est obligatoire';
    if (!patient.genre) newErrors.genre = 'Le genre est obligatoire';
    return newErrors;
  }

  function handleChange(e) {
    const { name, value } = e.target;
    setPatient(prev => ({ ...prev, [name]: value }));
  }

  function handleSubmit(e) {
    e.preventDefault();

    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    if (id) {
      // update patient
      axios.put(`http://localhost:8080/api/patients/${id}`, patient, {
        auth: { username, password }
      })
      .then(() => {
        navigate('/patients');
      })
      .catch(() => {
        alert('Erreur lors de la mise à jour');
      });
    } else {
      // create patient
      axios.post('http://localhost:8080/api/patients', patient, {
        auth: { username, password }
      })
      .then(() => {
        navigate('/patients');
      })
      .catch(() => {
        alert('Erreur lors de la création');
      });
    }
  }

  return (
    <div style={{ maxWidth: 600, margin: 'auto', marginTop: 40 }}>
      <h2>{id ? 'Modifier un patient' : 'Ajouter un nouveau patient'}</h2>
      <form onSubmit={handleSubmit} noValidate>
        <div>
          <label>Nom* :</label><br />
          <input type="text" name="nom" value={patient.nom} onChange={handleChange} />
          {errors.nom && <div style={{ color: 'red' }}>{errors.nom}</div>}
        </div>
        <div>
          <label>Prénom* :</label><br />
          <input type="text" name="prenom" value={patient.prenom} onChange={handleChange} />
          {errors.prenom && <div style={{ color: 'red' }}>{errors.prenom}</div>}
        </div>
        <div>
          <label>Date de naissance* :</label><br />
          <input type="date" name="dateNaissance" value={patient.dateNaissance} onChange={handleChange} />
          {errors.dateNaissance && <div style={{ color: 'red' }}>{errors.dateNaissance}</div>}
        </div>
        <div>
          <label>Genre* :</label><br />
          <select name="genre" value={patient.genre} onChange={handleChange}>
            <option value="">-- Choisir --</option>
            <option value="M">Masculin</option>
            <option value="F">Féminin</option>
            <option value="Autre">Autre</option>
          </select>
          {errors.genre && <div style={{ color: 'red' }}>{errors.genre}</div>}
        </div>
        <div>
          <label>Adresse postale :</label><br />
          <input type="text" name="adressePostale" value={patient.adressePostale} onChange={handleChange} />
        </div>
        <div>
          <label>Téléphone :</label><br />
          <input type="tel" name="telephone" value={patient.telephone} onChange={handleChange} />
        </div>
        <button type="submit" style={{ marginTop: 20 }}>
          {id ? 'Mettre à jour' : 'Ajouter'}
        </button>{' '}
        <button type="button" onClick={() => navigate('/patients')} style={{ marginTop: 20 }}>
          Annuler
        </button>
      </form>
    </div>
  );
}

export default PatientForm;
