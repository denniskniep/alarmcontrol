import {Badge, Col, Row, Table} from "react-bootstrap";
import React, {Component} from 'react';
import EmployeeStatusDot from "./employeeStatusDot";
import EmployeeStates from "./employeeStates";

class EmployeeStatusAsList extends Component {

  mapStatusToIntForOrder(employee) {
    if (!employee.status) {
      return 2;
    }

    if (employee.status.status == EmployeeStates.getNotAvailable()) {
      return 1;
    }

    if (employee.status.status == EmployeeStates.getAvailable()) {
      return 0;
    }
  }

  render() {
    let employees = this.props.employees.sort(
        (a, b) => this.mapStatusToIntForOrder(a) - this.mapStatusToIntForOrder(
            b));

    return (
        <React.Fragment>
          <Row className={" h-100"}>
            <Col className={" h-100"}>
              {
                employees.map((e, index) => {
                  return (
                    <Badge key={e.id} className={"badgeSpace badgeEmployee"}
                           variant={"light"}>
                      {
                        e.status &&
                        <EmployeeStatusDot className={"dot-sm dot-space"}
                                           employee={e}/>
                      }
                      <span>
                        {e.firstname} {e.lastname}
                      </span>
                    </Badge>)
                })
              }
            </Col>
          </Row>
        </React.Fragment>);
  }
}

export default EmployeeStatusAsList