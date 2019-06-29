import React, {Component} from 'react';
import {Subscription} from "react-apollo";
import {gql} from "apollo-boost";
import {Route} from 'react-router-dom'
import store from "../utils/store";
import {toast} from "react-toastify";

const ALERT_ADDED = gql`
  subscription alertAdded {
    alertAdded {
      id
      organisationId
    }
  }
`;

class AlertViewSwitcher extends Component {

  handleSubscriptionData(options, history){
    if (options && options.subscriptionData && options.subscriptionData.data && options.subscriptionData.data.alertAdded) {
      let alertAdded = options.subscriptionData.data.alertAdded;
      console.log(alertAdded)
      toast.info("New Alert!", {
        position: "top-right",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true
      });

      let currentOrganisationId = store.getCurrentOrganisationId();
      if(currentOrganisationId == 0 || currentOrganisationId == alertAdded.organisationId){
        console.log("orgid okay", currentOrganisationId)
        history.push("/app/alertview/" + alertAdded.id)
      }
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