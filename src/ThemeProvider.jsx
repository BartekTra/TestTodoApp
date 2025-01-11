import React, { createContext, useContext, useState, useEffect } from 'react';
import "react-toggle/style.css";
import Toggle from 'react-toggle';
import "./App.css";

const Finaltoggle = () => {
  const savedTheme = localStorage.getItem('theme');
  const [isDarkTheme, setIsDarkTheme] = useState(savedTheme === 'dark');

  const toggleTheme = () => {
    // Toggle theme and save the preference to localStorage
    const newTheme = !isDarkTheme;
    setIsDarkTheme(newTheme);
    localStorage.setItem('theme', newTheme ? 'dark' : 'light');
  };

  useEffect(() => {
    document.body.className = isDarkTheme ? 'dark_theme' : 'light_theme';
    //document.getElementById(div).className = isDarkTheme ? 'dark_theme' : 'light_theme';
  }, [isDarkTheme]);

  return (
    <div className="App">
      {/* Use an arrow function to update the state */}
      <Toggle 
        checked={isDarkTheme} 
        onChange={toggleTheme} 
      />
    </div>
  );
};

export default Finaltoggle;
