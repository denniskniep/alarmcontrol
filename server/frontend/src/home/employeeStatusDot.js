import React, {Component} from 'react';
import EmployeeStates from "./employeeStates";

class EmployeeStatusDot extends Component {

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
        <span
            className={"dot " + this.props.className + " "
            + this.mapClassForFeedback(
                this.props.employee &&
                this.props.employee.status &&
                this.props.employee.status.status)}></span>
    );
  }
}

export default EmployeeStatusDot