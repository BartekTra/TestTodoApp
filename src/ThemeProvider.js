import React, { useState } from "react";
import useLocalStorage from "use-local-storage";
import "react-toggle/style.css" 
import Toggle from 'react-toggle'
import "./App.css";

const Finaltoggle = () => {
  const preference = window.matchMedia("(prefers-color-scheme: dark)").matches;
  const [isDark, setIsDark] = useLocalStorage("isDark", preference);

  return (
    <div className="App" data-theme={isDark ? "dark" : "light"}>
      <Toggle isChecked={isDark} handleChange={() => setIsDark(!isDark)} />
      <div className="box">
      </div>
    </div>
  );
};

export default Finaltoggle;