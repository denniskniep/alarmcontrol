import React, {Component} from 'react';
import AlertViewBox from "./alertViewBox";
import {Badge, Col, Container, Row, Table} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

class AlertViewPersons extends Component {
  render() {
    return (
        <AlertViewBox>
          <Container fluid="true" className={"d-flex flex-column h-100"}>
            <Row>
              <Col>
                <h1>
                  <Badge variant="success">
                    <div>
                      <FontAwesomeIcon className={"insideBadge"}
                                       icon={["far", "check-circle"]}/>
                      <Badge variant="light">2</Badge></div>
                  </Badge>
                </h1>
              </Col>
              <Col className={"d-flex align-items-center"}>
                <h4>
                  <Badge variant="success">
                    <div>
                      <span className={"insideBadge"}>FK</span>
                      <Badge variant="light">1</Badge></div>
                  </Badge>
                </h4>
              </Col>
              <Col className={"d-flex align-items-center"}>
                <h4>
                  <Badge variant="success">
                    <div>
                      <span className={"insideBadge"}>C</span>
                      <Badge variant="light">1</Badge></div>
                  </Badge>
                </h4>
              </Col>
              <Col className={"d-flex align-items-center"}>
                <h4>
                  <Badge variant="success">
                    <div>
                      <span className={"insideBadge"}>AGT</span>
                      <Badge variant="light">2</Badge></div>
                  </Badge>
                </h4>
              </Col>

            </Row>
            <Row>
              <Col>
                <hr/>
              </Col>
            </Row>

            <Row>
              <Col>
                <h1>
                  <Badge variant="info">
                    <div>
                      <FontAwesomeIcon className={"insideBadge"}
                                       icon={["far", "check-circle"]}/>
                      <Badge variant="light">1</Badge></div>
                  </Badge>
                </h1>
              </Col>
              <Col className={"d-flex align-items-center"}>
                <h4>
                  <Badge variant="info">
                    <div>
                      <span className={"insideBadge"}>FK</span>
                      <Badge variant="light">1</Badge></div>
                  </Badge>
                </h4>
              </Col>
              <Col className={"d-flex align-items-center"}>
                <h4>
                  <Badge variant="info">
                    <div>
                      <span className={"insideBadge"}>C</span>
                      <Badge variant="light">1</Badge></div>
                  </Badge>
                </h4>
              </Col>
              <Col className={"d-flex align-items-center"}>
                <h4>
                  <Badge variant="info">
                    <div>
                      <span className={"insideBadge"}>AGT</span>
                      <Badge variant="light">2</Badge></div>
                  </Badge>
                </h4>
              </Col>

            </Row>
            <Row>
              <Col>
                <br/>
              </Col>
            </Row>
            <Row>
              <Col className={"noPadding"}>
                <Table responsive>
                  <tbody>
                  <tr className={"available"}>
                    <td>Max Mustermann</td>
                    <td>
                      <Badge className={"badgeSpace"} pill variant="secondary">
                        FK
                      </Badge>
                      <Badge className={"badgeSpace"} pill variant="secondary">
                        C
                      </Badge>
                    </td>
                  </tr>
                  <tr className={"available"}>
                    <td>Hans Halunke</td>
                    <td>
                      <Badge className={"badgeSpace"} pill variant="secondary">
                        AGT
                      </Badge>
                    </td>
                  </tr>
                  <tr className={"laterAvailable"}>
                    <td>Lars Laune</td>
                    <td>
                      <Badge className={"badgeSpace"} pill variant="secondary">
                        FK
                      </Badge>
                      <Badge className={"badgeSpace"} pill variant="secondary">
                        C
                      </Badge>
                      <Badge className={"badgeSpace"} pill variant="secondary">
                        AGT
                      </Badge>
                    </td>
                  </tr>
                  </tbody>
                </Table>
              </Col>
            </Row>
            <Row>
              <Col>
                <Badge className={"badgeSpace"} variant="light">Harald
                  Töpfer</Badge>
                <Badge className={"badgeSpace"} variant="danger">Monika
                  Mauer</Badge>
                <Badge className={"badgeSpace"} variant="danger">Torben
                  Tau</Badge>
              </Col>
            </Row>
          </Container>
        </AlertViewBox>);
  }
}

export default AlertViewPersons