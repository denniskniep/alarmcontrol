import React, {Component} from 'react';
import {Col, Container, Row} from "react-bootstrap";
import EmployeeStatus from "./employeeStatus";

class Home extends Component {


  render() {
    return (
        <Container fluid="true"
                   className={"h-full d-flex flex-column alertView"}>
          <Row className={"flex-fill d-flex justify-content-star"}>
            <Col xs={3}>
              <EmployeeStatus alert={this.props.alert}/>
            </Col>
            <Col>

            </Col>
          </Row>
        </Container>)
  }
}

export default Home;