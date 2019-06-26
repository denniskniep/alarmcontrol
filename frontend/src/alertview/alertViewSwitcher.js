import React, {Component} from 'react';
import {Subscription} from "react-apollo";
import {gql} from "apollo-boost";
import {Route} from 'react-router-dom'

const ALERT_ADDED = gql`
  subscription alertAdded {
    alertAdded {
      id
    }
  }
`;

class AlertViewSwitcher extends Component {

  handleSubscriptionData(options, history){
    if (options && options.subscriptionData && options.subscriptionData.data && options.subscriptionData.data.alertAdded) {
      let alertAdded = options.subscriptionData.data.alertAdded;
      history.push("/app/alertview/" + alertAdded.id)
    }
  }

  render() {
    return (
        <Route render={({history}) => (
            <Subscription fetchPolicy="no-cache"
                          subscription={ALERT_ADDED}
                          onSubscriptionData={o => this.handleSubscriptionData(o, history)}/>
        )}/>
    );
  }
}

export default AlertViewSwitcher