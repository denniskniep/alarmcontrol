import React, {Component} from 'react';
import EmployeeStates from "./employeeStates";

class EmployeeStatusDot extends Component {

  mapClassForStatus(status) {
    switch (status) {
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
            + this.mapClassForStatus(
                this.props.employeeStatus &&
                this.props.employeeStatus.status
            )}></span>
    );
  }
}

export default EmployeeStatusDot
