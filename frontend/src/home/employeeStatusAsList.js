import {Col, Row, Table} from "react-bootstrap";
import React, {Component} from 'react';
import PrettyPrinter from "../utils/prettyPrinter";
import EmployeeStatusDot from "./employeeStatusDot";

class EmployeeStatusAsList extends Component {

  render() {
    return (
        <React.Fragment>
          <Row>
            <Col className={"noPadding"}>
              <Table responsive>
                <tbody>
                {
                  this.props.employees.map(
                      (e, index) => {
                        return (
                            <tr key={e.id}>
                              <td className={"dot-td"}>
                                <p className={"dot-td-container"}>
                                  <EmployeeStatusDot employee={e} />
                                </p>
                              </td>
                              <td>
                                <p className={"employee-status-name"}>
                                  {e.firstname} {e.lastname}
                                </p>
                                <p className={"employee-status-date"}>
                                  {e.status
                                  && new PrettyPrinter().prettifyDateTimeShort(
                                      e.status.dateTime)}
                                </p>
                              </td>
                            </tr>
                        )
                      })
                }
                </tbody>
              </Table>
            </Col>
          </Row>
        </React.Fragment>);
  }
}

export default EmployeeStatusAsList