import './App.css';
import api from './api/axiosConfig';
import { useState } from 'react';

function App() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [responseMessage, setResponseMessage] = useState('');
  const [token, setToken] = useState(null); // State to store the JWT token

  // Function to handle login
  const handleLogin = async (e) => {
    e.preventDefault(); // Prevent page reload on form submission

    try {
      const response = await api.post('http://localhost:8080/api/v1/auth/authenticate', {
        email: email,
        password: password,
      });

      // Extract and store the JWT token
      const bearerToken = response.data.token; // Assuming the response contains a token field
      setToken(bearerToken);

      // Save the token in localStorage for persistence
      localStorage.setItem('bearerToken', bearerToken);

      setResponseMessage('Login successful!');
    } catch (error) {
      console.error('Error during login:', error);
      setResponseMessage(error.response?.data?.message || 'Login failed. Please try again.');
    }
  };

  // Function to retrieve token from localStorage
  const getToken = () => {
    return localStorage.getItem('bearerToken');
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
      {responseMessage && <p>{responseMessage}</p>}
      {token && <p>JWT Token: {token}</p>}
    </div>
  );
}

export default App;
