import {Col, Row, Table} from "react-bootstrap";
import React, {Component} from 'react';
import EmployeeStates from "./employeeStates";
import PrettyPrinter from "../utils/prettyPrinter";

class EmployeeStatusAsList extends Component {

  mapClassForFeedback(feedback) {
    switch (feedback) {
      case EmployeeStates.getNotAvailable():
        return "dot-not-available"
      case EmployeeStates.getAvailable():
        return "dot-available"
      default:
        return null;
    }
  }

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
                                              <span
                                                  className={"dot dot-td-container "
                                                  + this.mapClassForFeedback(
                                                      e.status
                                                      && e.status.status)}></span>
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