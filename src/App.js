import { Routes, Route } from 'react-router-dom';
import FinalNavbar from './components/Navbar';
import LoginPage from './pages/login';
import RegisterPage from './pages/register';
import TaskReport from './pages/TaskReport.js';
import Main from './pages/Authorized/main.js';

import "./App.css"


function App() {
  return (
    <div>
      <FinalNavbar /> {/* Navbar appears on every page */}
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/Register" element={<RegisterPage />} />
        <Route path="/" element={<Main />} />
        <Route path="/Report" element={<TaskReport />} />
      </Routes>
    </div>
  );
}

export default App;
