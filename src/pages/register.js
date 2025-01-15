import '.././App.css';
import api from '../api/axiosConfig.js';
import { useState } from 'react';
import ReCAPTCHA from "react-google-recaptcha";
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom'; // Import useNavigate for navigation

function RegisterPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [responseMessage, setResponseMessage] = useState('');
  const [VerifyValue, SetVerifedValue] = useState(null);
  const { t } = useTranslation();
  const navigate = useNavigate(); // Initialize the useNavigate hook


  const handleRegister = async (e) => {
    e.preventDefault();

    try {
      const response = await api.post('http://localhost:8080/api/v1/auth/Register', {
        headers: {
          'Content-Type': 'application/json'
        },
        username: email,
        password: password,
      });

      setResponseMessage('registerSuccess');
      setTimeout(() => navigate('/'), 1000);
    } catch (error) {
      console.error('Error during Register:', error);
      setResponseMessage('registerFailed');
    }
  };

  return (
    <div className="App">
      <h1>{t('register.title')}</h1>
      <form onSubmit={handleRegister}>
        <div>
          <label htmlFor="email">{t('register.emailLabel')}</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="password">{t('register.passwordLabel')}</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <div align="center">
          <ReCAPTCHA
            sitekey="6LfIy64qAAAAAFiaiLzzlCVAJgj2zawU1JXXr_X1"
            onChange={(val) => SetVerifedValue(val)}
          />
        </div>
        <button disabled={!VerifyValue} type="submit">{t('register.registerButton')}</button>
      </form>
      {responseMessage && <p>{t(`register.${responseMessage}`)}</p>}
    </div>
  );
}

export default RegisterPage;