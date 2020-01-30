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

              let employeeStatus = this.props.employeeStatus
              .find(es => ef.employee.id == es.employee.id);

              return (

                  <Badge key={ef.employee.id} className={"badgeSpace badgeEmployee"}
                         variant={this.mapColorForFeedback(ef.feedback)}>
                    {
                      ef.employee && employeeStatus && ef.feedback == EmployeeFeedbackStates.getNoResponse() &&
                      <EmployeeStatusDot className={"dot-sm dot-space"} employeeStatus={employeeStatus} />
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