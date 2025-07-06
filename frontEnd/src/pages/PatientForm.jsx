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
    firstName: '',
    lastName: '',
    dateOfBirth: '',
    gender: '',
    address: '',
    phoneNumber: ''
  });
  const [errors, setErrors] = useState({});

  // Chargement des données si modification
  useEffect(() => {
    if (id) {
      axios.get(`http://localhost:8080/api/patients/${id}`, {
        auth: { username, password }
      }).then(res => {
        const data = res.data;
        setPatient({
          firstName: data.firstName || '',
          lastName: data.lastName || '',
          dateOfBirth: data.dateOfBirth ? data.dateOfBirth.slice(0,10) : '',
          gender: data.gender || '',
          address: data.address || '',
          phoneNumber: data.phoneNumber || ''
        });
      }).catch(() => {
        alert("Erreur lors du chargement du patient");
      });
    }
  }, [id, username, password]);

  // Validation simple avant submit
  function validate() {
    const newErrors = {};
    if (!patient.lastName.trim()) newErrors.lastName = 'Le nom est obligatoire';
    if (!patient.firstName.trim()) newErrors.firstName = 'Le prénom est obligatoire';
    if (!patient.dateOfBirth) newErrors.dateOfBirth = 'La date de naissance est obligatoire';
    if (!patient.gender) newErrors.gender = 'Le genre est obligatoire';
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
          <input type="text" name="lastName" value={patient.lastName} onChange={handleChange} />
          {errors.lastName && <div style={{ color: 'red' }}>{errors.lastName}</div>}
        </div>
        <div>
          <label>Prénom* :</label><br />
          <input type="text" name="firstName" value={patient.firstName} onChange={handleChange} />
          {errors.firstName && <div style={{ color: 'red' }}>{errors.firstName}</div>}
        </div>
        <div>
          <label>Date de naissance* :</label><br />
          <input type="date" name="dateOfBirth" value={patient.dateOfBirth} onChange={handleChange} />
          {errors.dateOfBirth && <div style={{ color: 'red' }}>{errors.dateOfBirth}</div>}
        </div>
        <div>
          <label>Genre* :</label><br />
          <select name="gender" value={patient.gender} onChange={handleChange}>
            <option value="">-- Choisir --</option>
            <option value="M">Masculin</option>
            <option value="F">Féminin</option>
            <option value="Autre">Autre</option>
          </select>
          {errors.gender && <div style={{ color: 'red' }}>{errors.gender}</div>}
        </div>
        <div>
          <label>Adresse postale :</label><br />
          <input type="text" name="address" value={patient.address} onChange={handleChange} />
        </div>
        <div>
          <label>Téléphone :</label><br />
          <input type="tel" name="phoneNumber" value={patient.phoneNumber} onChange={handleChange} />
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
