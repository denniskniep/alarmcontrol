import React, {Component} from 'react';
import {Badge, Col, Row} from "react-bootstrap";
import EmployeeFeedbackStates from "./employeeFeedbackStates";
import EmployeeStatusDot from "../../../home/employeeStatusDot";

class EmployeeFeedbackAsBadges extends Component {

  mapColorForFeedback(feedback) {
    switch (feedback) {
      case EmployeeFeedbackStates.getNoResponse():
        return "light"
      case EmployeeFeedbackStates.getCancel():
        return "danger"
      default:
        return null;
    }
  }

  render() {
    return (
        <Row>
          <Col>
            {this.props.employeeFeedback.map((ef, index) => {
              return (

                  <Badge key={ef.employee.id} className={"badgeSpace badgeEmployee"}
                         variant={this.mapColorForFeedback(ef.feedback)}>
                    {
                      ef.employee && ef.employee.status && ef.feedback == EmployeeFeedbackStates.getNoResponse() &&
                      <EmployeeStatusDot className={"dot-sm dot-space"} employee={ef.employee} />
                    }
                    <span>
                    {ef.employee.firstname} {ef.employee.lastname}
                    </span>
                  </Badge>

              )
            })}
          </Col>
        </Row>);
  }
}

export default EmployeeFeedbackAsBadges