import './Sidebar.scss'

import React, {Component} from "react";
import {XmlData} from "./data/XMLData"
import {Col, Container, Jumbotron, Row, Table} from "react-bootstrap";
import SyntaxHighlighter from "react-syntax-highlighter";
import {atomOneLight} from "react-syntax-highlighter/dist/esm/styles/hljs"
import {DeprecatedBadge, MinVersionBadge, RemovedBadge, RequiredBadge} from "./Badges";
import {VscCircleFilled, VscCircleOutline} from "react-icons/all";

function AttributesTable(props) {
  if (!props.attributes || props.attributes.length === 0) {
    return null
  }
  return (
    <>
      <h2 className="h3">Supported Attributes</h2>
      <Table size="sm" variant="flex">
        <thead>
        <tr>
          <th style={{"minWidth": "150px"}}>Attribute</th>
          <th style={{"minWidth": "100px"}}>Required</th>
          <th style={{"minWidth": "120px"}}>Examples</th>
          <th style={{"width": "100%"}}>Description</th>
        </tr>
        </thead>
        <tbody>
        {props.attributes.map((attr, idx) => {
          return (
            <tr key={attr.name + idx.toString()}>
              <td className="text-monospace">
                {attr.name}
                <MinVersionBadge version={attr.minVersion}/>
                <DeprecatedBadge deprecated={attr.deprecated}/>
                <RemovedBadge version={attr.removed}/>
              </td>
              <td>
                <RequiredBadge required={attr.required}/>
              </td>
              <td className="text-monospace">{attr.format}</td>
              <td>{attr.description}</td>
            </tr>
          );
        })}
        </tbody>
      </Table>
    </>);
}

function RequirementsTable(props) {
  return (
    <>
      <h2 className="h3 mt-3">Requirements</h2>
      <Row>
        <Col xs="12" sm="8" md="8" lg="7" xl="5">
          <Table bordered size="sm">
            <thead>
              <tr>
                <th>Parent</th>
                <th>Min</th>
                <th>Max</th>
                <th>Usage</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{props.node.parents}</td>
                <td>{props.node.min}</td>
                <td>{props.node.max}</td>
                <td><RequiredBadge required={props.node.required}/></td>
              </tr>
            </tbody>
          </Table>
        </Col>
      </Row>
    </>)
}

function SidebarNavLinks() {
  return XmlData.sort((a, b) => a.name.localeCompare(b.name)).map(node => (
    <li key={node.name} className="nav-item">

      <a className="nav-link" href={"#" + node.name}>
        <span className="sidebar-icon">{node.required ? <VscCircleFilled/> : <VscCircleOutline/>}</span>{node.name}</a>
    </li>
  ));
}

export default class XMLDocumentation extends Component {
  componentDidMount() {
    let location = window.location.href;
    if (location.includes('#')) {
      let id = location.split('#')[1];
      let elem = document.getElementById(id);
      if (elem) {
        elem.scrollIntoView();
      }
    }
  }

  render() {
    return (
      <>
        <Container fluid>
          <Row>
            <nav className="col-md-3 col-lg-2 d-none d-md-block bg-light sidebar text-dark">
              <div className="sidebar-sticky">
                <h6
                  className="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
                  <span>Available XML Tags</span>
                </h6>
                <ul className="nav flex-column">
                  <SidebarNavLinks/>
                </ul>
              </div>
            </nav>
            <main className="col-md-9 ml-sm-auto col-lg-10 px-4">
              <Jumbotron className="pt-5">
                <Col className="mx-auto">
                  <h1 className="display-3">XML Syntax Documentation</h1>
                  <p className="lead">
                    Documentation of the XML Tease files.
                  </p>
                  <p className="lead">
                    Throughout this page, you'll see several badges alongside XML tags and attributes you should be
                    familiar
                    with.
                  </p>
                  <ul className="lead">
                    <li>Requirements will be indicated by a <RequiredBadge required={true}/> or <RequiredBadge required={false}/>  badge.
                      Required nodes have a filled dot in the left navigation, while optional nodes have an open dot.
                    </li>
                    <li>New elements will have a badge indicating the minimum required GuideMe
                      version <MinVersionBadge version="1.2.3"/></li>
                    <li>Deprecated elements will have a deprecated badge <DeprecatedBadge deprecated/></li>
                    <li>Elements that have been removed will have a badge indicating the version they were
                      removed in <RemovedBadge version="1.2.3"/></li>
                  </ul>
                  <p className="lead">
                    Examples may demonstrate features not present in all versions of GuideMe. Please check for badges
                    in the title and supported attributes to ensure your XML will work with your intended software version(s).
                  </p>
                </Col>
              </Jumbotron>
              {XmlData.sort((a, b) => a.name.localeCompare(b.name)).map(node => {
                return (
                  <div key={node.name}>
                    <div className="border-bottom d-block mt-5">
                      <h1 className="d-inline" id={node.name}>{node.name}</h1>
                      <MinVersionBadge version={node.minVersion} className="badge badge-info ml-2 mb-1 align-text-top" />
                      <DeprecatedBadge deprecated={node.deprecated} className="badge badge-warning ml-2 mb-1 align-text-top" />
                      <RemovedBadge version={node.removed} className="badge badge-danger ml-2 mb-1 align-text-top" />
                    </div>
                    {node.description}
                    <RequirementsTable node={node}/>
                    <AttributesTable attributes={node.attributes}/>
                    <h2 className="h3">Usage Examples</h2>
                    {node.usage.map((usage, idx) => {
                      return(
                        <SyntaxHighlighter key={idx} language="xml" style={atomOneLight}>
                          {usage}
                        </SyntaxHighlighter>
                      );
                    })}
                  </div>
                );
              })}
            </main>
          </Row>
        </Container>
      </>);
  };
}