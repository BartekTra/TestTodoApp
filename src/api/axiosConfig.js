import axios from 'axios';

// Create an Axios instance
const api = axios.create({
  baseURL: 'http://localhost:8080', // Replace with your backend base URL
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add a request interceptor to include the token in the Authorization header
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('bearerToken'); // Retrieve token from localStorage
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default api;
