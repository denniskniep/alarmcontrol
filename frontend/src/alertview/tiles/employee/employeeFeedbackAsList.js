import React, {Component} from 'react';
import {Badge, Col, Row, Table} from "react-bootstrap";
import EmployeeFeedbackStates from "./employeeFeedbackStates";

class EmployeeFeedbackAsList extends Component {

  mapClassForFeedback(feedback) {
    switch (feedback) {
      case EmployeeFeedbackStates.getLater():
        return "laterAvailable"
      case EmployeeFeedbackStates.getCommit():
        return "available"
      default:
        return null;
    }
  }

  render() {
    return (
        <Row>
          <Col className={"noPadding"}>
            <Table responsive>
              <tbody>
              {this.props.employeeFeedback.map((ef, index) => {
                return (
                    <tr key={ef.employee.id} className={this.mapClassForFeedback(ef.feedback)}>
                      <td>{ef.employee.firstname} {ef.employee.lastname}</td>
                      <td>
                      {ef.employee.skills.map((skill, index) => {
                        return (

                            <Badge key={ef.employee.id + "-"+ skill.id} className={"badgeSpace"} pill
                                   variant="secondary">
                              {skill.shortName}
                            </Badge>

                        )
                      })}
                      </td>
                    </tr>
                )
              })}
              </tbody>
            </Table>
          </Col>
        </Row>);
  }
}

export default EmployeeFeedbackAsList