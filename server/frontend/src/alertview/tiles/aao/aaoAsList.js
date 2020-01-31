import React, {Component} from 'react';
import {Badge, Col, Container, Row, Table} from "react-bootstrap";
import AlertViewBox from "../../alertViewBox";

class AaoAsList extends Component {

    render() {
        return (
            this.props.alert && this.props.alert.aao && this.props.alert.aao.length > 0 &&
            <AlertViewBox>
              <Container fluid="true"
                         className={"d-flex flex-column h-100"}>
                  <Row>
                      <Col>
                              {this.props.alert.aao && this.props.alert.aao.map((ef, index) => {
                                  return (
                                      <Badge key={index} className={"badgeSpace "}
                                             variant="primary">
                                          <h4>
                                            <b className={"aao-fzg"}>{ef}</b>
                                          </h4>
                                      </Badge>
                                  )
                              })}
                      </Col>
                  </Row>
              </Container>
            </AlertViewBox>
        )
    }
}

export default AaoAsList