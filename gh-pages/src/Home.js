import React from "react";
import {Col, Container, Jumbotron} from "react-bootstrap";
import Releases from "./Releases";

export default function Home() {
    return (
    <Container>
        <Jumbotron className="pt-5">
            <Col className="mx-auto">
                <h1 className="display-3">Welcome</h1>
                <p className="lead">
                    This site will contain general information for the GuideMe application as well as the syntax
                    of XML files and the overRide JavaScript functionality.
                </p>
                <p className="lead">
                    Click on one of the links up top to get started.
                </p>
            </Col>
        </Jumbotron>
        <Releases />
    </Container>
    )
}