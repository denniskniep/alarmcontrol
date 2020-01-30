import React, {Component} from 'react';
import {Button, Col, Container, Row} from "react-bootstrap";
import OrganisationEditMutation from "./organisationEditMutation";
import EmployeesEditMutation from "./employeesEditMutation";
import SkillsEditMutation from "./skillsEditMutation";
import AlertNumbersEditMutation from "./alertNumbersEditMutation";

class OrganisationView extends Component {

  constructor(props) {
    super(props);
    this.state = {
      refetchEmployees: 0
    }
  }

  handleSkillsChanged(){
    this.setState((state, props) => {
      return {refetchEmployees: ++state.refetchEmployees}
    });
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
          <h4>AlertNumbers</h4>
        </Col>
      </Row>

      <Row className={"row-mid"}>
        <AlertNumbersEditMutation id={this.props.match.params.id} />
      </Row>

      <Row>
        <Col md="10">
          <h4>Skills</h4>
        </Col>
      </Row>

      <Row className={"row-mid"}>
        <SkillsEditMutation id={this.props.match.params.id} onSkillsChanged={()=> this.handleSkillsChanged()}/>
      </Row>

      <Row>
        <Col md="10">
          <h4>Employees</h4>
        </Col>
      </Row>

      <Row className={"row-mid"}>
        <EmployeesEditMutation id={this.props.match.params.id} refetch={this.state.refetchEmployees} />
      </Row>

      <Row>
        <Col md="10">
          <h4>Alerts</h4>
        </Col>
      </Row>

      <Row className={"row-mid"}>
        <Col md="10">
          <Button variant="primary" href={"./"+ this.props.match.params.id +"/alerts"} >
            View
          </Button>
        </Col>
      </Row>

      <Row>
        <Col md="10">
          <h4>Notifications</h4>
        </Col>
      </Row>

      <Row className={"row-mid"}>
        <Col md="10">
          <Button variant="primary" href={"./"+ this.props.match.params.id +"/notifications"} >
            View
          </Button>
        </Col>
      </Row>

      <Row>
        <Col md="10">
          <h4>Alarm- und Ausrückordnung</h4>
        </Col>
      </Row>

      <Row className={"row-mid"}>
        <Col md="10">
          <Button variant="primary" href={"./"+ this.props.match.params.id +"/aao"} >
            View
          </Button>
        </Col>
      </Row>

      <Row className={"row-mid"}>
        <Col md="10">
        </Col>
      </Row>
    </Container>);
  }
}

export default OrganisationView;