import 'bootstrap/dist/css/bootstrap.min.css'
import './App.scss';

import 'jquery/dist/jquery.min'
import 'popper.js/dist/popper.min'
import 'bootstrap/dist/js/bootstrap.min'

import React from "react";
import Navigation from "./Navigation";
import Home from "./Home";
import XMLDocumentation from "./documentation/XMLDocumentation";
import {BrowserRouter, Link, Route, Switch} from "react-router-dom";
import {Col, Container, Row} from "react-bootstrap";

function NotFound() {
  return (
    <div className="page-wrap d-flex flex-row align-items-center">
      <Container>
        <Row className="justify-content-center">
          <Col className="text-center">
            <span className="display-1 d-block">404</span>
            <div className="mb-4 lead">The page you are looking for does not exist</div>
            <Link to="/" className="btn btn-outline-secondary">Go Home</Link>
          </Col>
        </Row>
      </Container>
    </div>
  );
}

export default function App() {
  return (
    <BrowserRouter basename={process.env.PUBLIC_URL}>
      <Navigation/>
        <Switch>
          <Route exact path="/" component={Home} />
          <Route exact path="/docs/xml" component={XMLDocumentation} />
          <Route exact path="/docs/js">
            <Container>
              <h1>Coming Soon!</h1>
            </Container>
          </Route>
          <Route path="*" component={NotFound} />
        </Switch>
    </BrowserRouter>
  );
}
