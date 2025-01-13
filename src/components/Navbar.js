import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom'; // Detect route changes
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import Finaltoggle from '.././ThemeProvider.jsx';
import { useTranslation } from 'react-i18next';

import './Navbar.css';

function FinalNavbar() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const { t } = useTranslation();
  const location = useLocation(); // Detect current route

  useEffect(() => {
    // Check if a token exists in localStorage on route change
    const token = localStorage.getItem('bearerToken');
    setIsAuthenticated(!!token); // Update the authentication state
  }, [location]); // Trigger effect on location change

  const handleLogout = () => {
    // Clear the token from localStorage and update authentication state
    localStorage.removeItem('bearerToken');
    setIsAuthenticated(false);
  };

  return (
    <Navbar
      key={location.pathname} // Use location.pathname to reset Navbar content on route change
      id="navbar"
      expand="lg"
      className="navbar-dark"
    >
      <Container>
        <Navbar.Brand href="/">{t('navbar.brand')}</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <NavDropdown title={t('navbar.pages.title')} id="basic-nav-dropdown">
              <Nav.Link href="/">{t('navbar.home')}</Nav.Link>
              {!isAuthenticated && (
                <NavDropdown.Item href="/Login">{t('navbar.pages.login')}</NavDropdown.Item>
              )}
              {!isAuthenticated && (
                <NavDropdown.Item href="/Register">{t('navbar.pages.register')}</NavDropdown.Item>
              )}
              {isAuthenticated && (
                <NavDropdown.Item onClick={handleLogout}>
                  {t('navbar.pages.logout')}
                </NavDropdown.Item>
              )}
              {isAuthenticated && (
                <NavDropdown.Item href="/Main">{t('navbar.pages.main')}</NavDropdown.Item>
              )}
              {isAuthenticated && (
                <NavDropdown.Item href="/Report">{t('navbar.pages.report')}</NavDropdown.Item>
              )}
            </NavDropdown>
          </Nav>
          <Nav className="ml-auto">
            <Finaltoggle />
          </Nav>
          <Nav>
            <p> </p>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default FinalNavbar;
