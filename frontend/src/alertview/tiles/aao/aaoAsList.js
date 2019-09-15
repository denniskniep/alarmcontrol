import React, {Component} from 'react';
import {Badge, Col, Container, Row, Table} from "react-bootstrap";
import AlertViewBox from "../../alertViewBox";
import EmployeeFeedbackStates from "../employee/employeeFeedbackStates";
import EmployeeStatusDot from "../../../home/employeeStatusDot";

class AaoAsList extends Component {

    render() {
        return (<AlertViewBox>
            <Container fluid="true"
                       className={"d-flex flex-column h-100"}>
                <Row>
                    <Col xs={3}>
                        <h1>Aao</h1>
                    </Col>
                </Row>
                <Row>
                    <Col className={"noPadding"}>
                            {this.props.alert.aao && this.props.alert.aao.map((ef, index) => {
                                return (

                                    <Badge key={index} className={"badgeSpace"}
                                           variant="primary">
                                        <span>{ef}</span>
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