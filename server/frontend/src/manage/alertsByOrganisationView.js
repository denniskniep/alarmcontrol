import React, {Component} from 'react';
import {Col, Container, Row} from "react-bootstrap";
import AlertEditMutation from "./alertEditMutation";

class AlertsByOrganisationView extends Component {
  render() {
    return (
        <Container>
          <Row className={"row-header"}>
            <Col>
              <h2>Alerts</h2>
            </Col>
          </Row>

          <Row>
            <Col>
              <AlertEditMutation id={this.props.match.params.id} />
            </Col>
          </Row>
        </Container>)
  }
}

export default AlertsByOrganisationView;