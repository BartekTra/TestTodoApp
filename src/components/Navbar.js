import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';

import Finaltoggle from '.././ThemeProvider.js';



function FinalNavbar() {
  return (
    <Navbar expand="lg" className="bg-body-tertiary">
      <Container>
        <Navbar.Brand href="/">To Do List</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link href="/">Home</Nav.Link>
            <NavDropdown title="Pages" id="basic-nav-dropdown">
              <NavDropdown.Item href="/Login">Login</NavDropdown.Item>
              <NavDropdown.Item href="/Register">Register</NavDropdown.Item>
              <NavDropdown.Item href="/Main">Main</NavDropdown.Item>
            </NavDropdown>
          </Nav>
          <Nav className="ml-auto">
          <Finaltoggle></Finaltoggle>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default FinalNavbar;