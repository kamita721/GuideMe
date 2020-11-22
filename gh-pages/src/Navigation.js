import React from "react";
import {LinkContainer} from 'react-router-bootstrap'
import Nav from 'react-bootstrap/Nav'
import Navbar from 'react-bootstrap/Navbar'
import NavDropdown from "react-bootstrap/NavDropdown";

export default function Navigation() {
  return (
    <Navbar bg="dark" expand="lg" fixed="top" variant="dark">
      <Navbar.Brand>GuideMe Documentation</Navbar.Brand>
      <Navbar.Toggle aria-controls="basic-navbar-nav"/>
      <Navbar.Collapse id="top-navbar-nav">
        <Nav className="mr-auto">
          <LinkContainer exact to="/">
            <Nav.Link>Home</Nav.Link>
          </LinkContainer>
          <NavDropdown id="nav-dropdown-docs" title="Documentation">
            <LinkContainer exact to="/docs/xml">
              <NavDropdown.Item>XML Tease Files</NavDropdown.Item>
            </LinkContainer>
            <LinkContainer exact to="/docs/js">
              <NavDropdown.Item>JavaScript Functionality</NavDropdown.Item>
            </LinkContainer>
          </NavDropdown>
        </Nav>
      </Navbar.Collapse>
    </Navbar>
  );
}
