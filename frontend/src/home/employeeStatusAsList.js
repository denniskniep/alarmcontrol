import {Col, Row, Table} from "react-bootstrap";
import React, {Component} from 'react';
import PrettyPrinter from "../utils/prettyPrinter";
import EmployeeStatusDot from "./employeeStatusDot";
import EmployeeStates from "./employeeStates";
import Card from "react-bootstrap/Card";

class EmployeeStatusAsList extends Component {

  firstCharWithDot(value){
    if(value && value.length > 0){
      return value[0] + "."
    }
    return null;
  }

  renderEmployee(e){

    let color = null;
    let status = e.status && e.status.status

    switch (status) {
      case EmployeeStates.getNotAvailable():
        color= "#dc35458f";
        break;
      case EmployeeStates.getAvailable():
        color= "#28a74559";
        break;
    }

    return (
        <Card style={{ width: '160px', height: '80px', backgroundColor: color }}>
          <Card.Body>
            <Card.Text>
              <p className={"employee-status-name"}>
                {this.firstCharWithDot(e.firstname)} {e.lastname}
              </p>
              <p className={"employee-status-date"}>
                {e.status
                && new PrettyPrinter().prettifyDateTimeShort(
                    e.status.dateTime)}
              </p>
            </Card.Text>
          </Card.Body>
        </Card>
    )
  }

  mapStatusToIntForOrder(employee){
    if(!employee.status){
      return 2;
    }

    if(employee.status.status == EmployeeStates.getNotAvailable()){
      return 1;
    }

    if(employee.status.status == EmployeeStates.getAvailable()){
      return 0;
    }
  }

  render() {
    let employees = this.props.employees.sort((a, b) => this.mapStatusToIntForOrder(a) - this.mapStatusToIntForOrder(b));

    return (
        <React.Fragment>
          <Row className={"noPadding h-100"}>
            <Col className={"noPadding h-100"}>
              <div className="d-flex flex-wrap flex-column h-100">
                {
                  employees.map((e, index) => this.renderEmployee(e))
                }
              </div>
            </Col>
          </Row>
        </React.Fragment>);
  }
}

export default EmployeeStatusAsList