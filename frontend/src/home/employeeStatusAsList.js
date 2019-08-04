import {Col, Row, Table} from "react-bootstrap";
import React, {Component} from 'react';
import PrettyPrinter from "../utils/prettyPrinter";
import EmployeeStatusDot from "./employeeStatusDot";
import EmployeeStates from "./employeeStates";

class EmployeeStatusAsList extends Component {

  firstCharWithDot(value){
    if(value && value.length > 0){
      return value[0] + "."
    }
    return null;
  }

  renderEmployee(e){
    return (
        <tr key={e.id}>
          <td className={"dot-td employee-status-td"}>
            <p className={"dot-td-container"}>
              <EmployeeStatusDot employee={e} />
            </p>
          </td>
          <td className={"employee-status-td"}>
            <p className={"employee-status-name"}>
              {this.firstCharWithDot(e.firstname)} {e.lastname}
            </p>
            <p className={"employee-status-date"}>
              {e.status
              && new PrettyPrinter().prettifyDateTimeShort(
                  e.status.dateTime)}
            </p>
          </td>
        </tr>
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
          <Row>
            <Col className={"noPadding"}>
              <Table responsive>
                <tbody>
                {
                  employees.map((e, index) =>
                      index % 2 == 0 && this.renderEmployee(e))
                }
                </tbody>
              </Table>
            </Col>
            <Col className={"noPadding"}>
              <Table responsive>
                <tbody>
                {
                  employees.map((e, index) =>
                      index % 2 == 1  && this.renderEmployee(e))
                }
                </tbody>
              </Table>
            </Col>
          </Row>
        </React.Fragment>);
  }
}

export default EmployeeStatusAsList