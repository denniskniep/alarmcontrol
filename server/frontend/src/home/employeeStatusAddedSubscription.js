import React, {Component} from 'react';
import {Subscription} from "react-apollo";
import {gql} from "apollo-boost";

const EMPLOYEE_STATUS_ADDED = gql`
  subscription employeeStatusAdded {
    employeeStatusAdded {
      employeeId
    }
  }
`;

class EmployeeStatusAddedSubscription extends Component {
  render() {
    return (
        <Subscription fetchPolicy="no-cache"
                      subscription={EMPLOYEE_STATUS_ADDED}
                      onSubscriptionData={o => this.props.onSubscriptionData && this.props.onSubscriptionData(o)}/>

    );
  }
}

export default EmployeeStatusAddedSubscription