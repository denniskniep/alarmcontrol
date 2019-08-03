import React, {Component} from 'react';
import {Route} from 'react-router-dom'
import {toast} from "react-toastify";
import AlertAddedSubscription from "./alertAddedSubscription";
import {CurrentOrganisationContext} from "../currentOrganisationContext";

class AlertViewSwitcher extends Component {

  handleSubscriptionData(options, history, currentOrganisationId) {
    if (options && options.subscriptionData && options.subscriptionData.data
        && options.subscriptionData.data.alertAdded) {
      let alertAdded = options.subscriptionData.data.alertAdded;
      toast.info("New Alert!", {
        position: "top-right",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true
      });

      if (currentOrganisationId == 0 ||
          currentOrganisationId == alertAdded.organisationId) {
        history.push("/app/alertview/" + alertAdded.id)
      }
    }
  }

  render() {
    return (
        <Route render={({history}) => (
            <CurrentOrganisationContext.Consumer>
              {organisationContext => {
                return (
                    <AlertAddedSubscription
                        onSubscriptionData={o =>
                            this.handleSubscriptionData(o, history, organisationContext.organisationId)}/>
                );
              }}
            </CurrentOrganisationContext.Consumer>
        )}/>
    );
  }
}

export default AlertViewSwitcher