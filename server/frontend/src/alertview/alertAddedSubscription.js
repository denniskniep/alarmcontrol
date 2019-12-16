import React, {Component} from 'react';
import {Subscription} from "react-apollo";
import {gql} from "apollo-boost";

const ALERT_ADDED = gql`
  subscription alertAdded {
    alertAdded {
      id
      organisationId
    }
  }
`;

class AlertAddedSubscription extends Component {
  render() {
    return (
        <Subscription fetchPolicy="no-cache"
                      subscription={ALERT_ADDED}
                      onSubscriptionData={o => this.props.onSubscriptionData && this.props.onSubscriptionData(o)}/>

    );
  }
}

export default AlertAddedSubscription