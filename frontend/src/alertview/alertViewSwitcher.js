import React, {Component} from 'react';
import {Route} from 'react-router-dom'
import store from "../utils/store";
import {toast} from "react-toastify";
import AlertAddedSubscription from "./alertAddedSubscription";

class AlertViewSwitcher extends Component {

  handleSubscriptionData(options, history) {
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

      let currentOrganisationId = store.getCurrentOrganisationId();
      if (currentOrganisationId == 0 ||
          currentOrganisationId == alertAdded.organisationId) {
        history.push("/app/alertview/" + alertAdded.id)
      }
    }
  }

  render() {
    return (
        <Route render={({history}) => (
            <AlertAddedSubscription
                onSubscriptionData={o =>
                    this.handleSubscriptionData(o, history)}/>
        )}/>
    );
  }
}

export default AlertViewSwitcher