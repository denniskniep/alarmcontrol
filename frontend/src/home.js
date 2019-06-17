import React, {Component} from 'react';
import {Col, Container, Row} from "react-bootstrap";
import AlertViewSwitcher from "./alertview/alertViewSwitcher";

class Home extends Component {


  render() {
    return (
        <Container>
          <Row>
            <Col>
                <h2>Home</h2>
                <AlertViewSwitcher/>
            </Col>
          </Row>
        </Container>)
  }
}

export default Home;