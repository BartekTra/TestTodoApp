import { useNavigate } from 'react-router-dom'; // Import useNavigate for navigation
import '../App.css';
import api from '../api/axiosConfig';
import { useState } from 'react';
import ReCAPTCHA from "react-google-recaptcha";
import { useTranslation } from 'react-i18next';

function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [responseMessage, setResponseMessage] = useState('');
  const [VerifyValue, SetVerifedValue] = useState(null);
  const navigate = useNavigate(); // Initialize the useNavigate hook
  const { t, i18n } = useTranslation();

  // Function to handle login
  const handleLogin = async (e) => {
    e.preventDefault(); // Prevent page reload on form submission

    try {
      const response = await api.post('/api/v1/auth/authenticate', {
        headers: {
          'Content-Type': 'application/json', // Required header
        },
        email: email,
        password: password,
      });

      // Extract and store the JWT token
      const bearerToken = response.data.token; // Assuming the response contains a token field
      // Save the token in localStorage for persistence
      localStorage.setItem('bearerToken', bearerToken);

      setResponseMessage('Login successful! Redirecting...');
      // Navigate to the main page
      setTimeout(() => navigate('/'), 1000); // Delay navigation for user feedback
    } catch (error) {
      console.error('Error during login:', error);
      setResponseMessage(error.response?.data?.message || 'Login failed. Please try again.');
    }
  };

  const handleLogout = async () => {
    const token = localStorage.getItem('bearerToken');
    try {
      await api.post('/api/v1/auth/logout', {}, { headers: { Authorization: `Bearer ${token}` } });
      localStorage.removeItem('bearerToken'); // Clear the token
      navigate('/login');
    } catch (err) {
      console.error('Logout failed:', err);
    }
  };


  return (
<div className="App" data-theme="">
<h1>{t('login.title')}</h1>
<form onSubmit={handleLogin}>
  <div>
    <label htmlFor="email">{t('login.emailLabel')}</label>
    <input
      type="email"
      id="email"
      value={email}
      onChange={(e) => setEmail(e.target.value)}
      required
    />
  </div>
  <div>
    <label htmlFor="password">{t('login.passwordLabel')}</label>
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
  <button type="submit">{t('login.loginButton')}</button>
</form>
{responseMessage && <p>{t(responseMessage === 'Login successful! Redirecting...' ? 'login.loginSuccess' : 'login.loginFailed')}</p>}
</div>
  );
}


export default LoginPage;
