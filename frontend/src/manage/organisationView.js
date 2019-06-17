import React, {Component} from 'react';
import {Col, Container, Row} from "react-bootstrap";
import OrganisationEditMutation from "./organisationEditMutation";
import EmployeesEditMutation from "./employeesEditMutation";

class OrganisationView extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (<Container>
      <Row className={"row-header"}>
        <Col md="10">
          <h4>Organisation</h4>
        </Col>
      </Row>

      <Row className={"row-mid"}>
        <Col>
          <OrganisationEditMutation id={this.props.match.params.id}/>
        </Col>
      </Row>

      <Row>
        <Col md="10">
          <h4>Employees</h4>
        </Col>
      </Row>

      <Row className={"row-mid"}>
        <EmployeesEditMutation id={this.props.match.params.id}/>
      </Row>
    </Container>);
  }
}

export default OrganisationView;