import { useNavigate } from 'react-router-dom'; // Import useNavigate for navigation
import '../App.css';
import api from '../api/axiosConfig';
import { useState } from 'react';

function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [responseMessage, setResponseMessage] = useState('');
  const navigate = useNavigate(); // Initialize the useNavigate hook

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
      setTimeout(() => navigate('/Main'), 2000); // Delay navigation for user feedback
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
    <div className="App">
      <h1>Login</h1>
      <form onSubmit={handleLogin}>
        <div>
          <label htmlFor="email">Email:</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit">Login</button>
      </form>
      {/* Display the response message */}
      {responseMessage && <p>{responseMessage}</p>}
    </div>
  );
}

export default LoginPage;
