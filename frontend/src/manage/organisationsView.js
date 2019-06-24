import React, {Component} from 'react';
import {Button, Col, Container, Row} from "react-bootstrap";
import OrganisationsEditMutation from "./organisationsEditMutation";

class OrganisationsView extends Component {
  render() {
    return (
        <Container>
          <Row className={"row-header"}>
            <Col>
              <h2>Organisations</h2>
            </Col>
          </Row>

          <Row>
            <Col>
              <OrganisationsEditMutation/>
            </Col>
          </Row>
        </Container>)
  }
}

export default OrganisationsView;