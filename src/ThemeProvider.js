import React, { useState, useEffect } from 'react';
import "react-toggle/style.css";
import Toggle from 'react-toggle';
import "./App.css";

const Finaltoggle = () => {
  const [isDarkTheme, setIsDarkTheme] = useState(false);

  useEffect(() => {
    document.body.className = isDarkTheme ? 'dark_theme' : 'light_theme';
  }, [isDarkTheme]);

  return (
    <div className="App">
      {/* Use an arrow function to update the state */}
      <Toggle 
        onChange={() => setIsDarkTheme(!isDarkTheme)} 
      />
    </div>
  );
};

export default Finaltoggle;
