import React, {Component} from 'react';
import {Col, Container, Row} from "react-bootstrap";
import AlertViewPersons from "./tiles/alertViewPersons";
import AlertViewHeader from "./tiles/alertViewHeader";
import AlertViewMapRoute from "./tiles/alertViewMapRoute";
import AlertViewMapTarget from "./tiles/alertViewMapTarget";

class AlertViewLayout extends Component {
  render() {
    return (
        <Container fluid="true"
                   className={"h-full d-flex flex-column alertView"}>
          <Row className={"flex-fill d-flex justify-content-star"}>
            <Col xs={3}>
              <AlertViewPersons alert={this.props.alert}/>
            </Col>
            <Col>
              <Container fluid="true" className={"d-flex flex-column h-100 "}>
                <Row>
                  <Col>
                    <AlertViewHeader alert={this.props.alert}/>
                  </Col>
                </Row>
                <Row className={"h-100"}>
                  <Col>
                    <AlertViewMapTarget alert={this.props.alert}/>
                  </Col>
                </Row>
              </Container>
            </Col>
          </Row>
        </Container>);
  }
}

export default AlertViewLayout