import {Query, Subscription} from "react-apollo";
import React, {Component} from 'react';
import {gql} from "apollo-boost";
import AlertViewLayout from "./alertViewLayout";
import QueryHandler from "../utils/queryHandler";

const ALERT_BY_ID = gql`
  query alertById($id: ID!) {
    alertById(id: $id) {
      id
      keyword
      description
      dateTime
      route
      distance
      duration
      organisation {
        id
        name
      }
      addressRaw
      addressInfo1
      addressInfo2
      addressLat,
      addressLng,
      alertCalls{
        id
        dateTime
        alertNumber{
          id
          number
          description
          shortDescription
        }
      },
      aao
    }
  }
`;

const ALERT_CHANGED = gql`
  subscription alertChanged{
    alertChanged {
      id
    }
  }
`;

class AlertView extends Component {

  handleSubscriptionData(options, refetch) {
    if (options && options.subscriptionData && options.subscriptionData.data
        && options.subscriptionData.data.alertChanged) {
      let alertChanged = options.subscriptionData.data.alertChanged;
      refetch();
    }
  }

  render() {
    return (
        <React.Fragment>
          <QueryHandler query={ALERT_BY_ID}
                        fetchPolicy="no-cache"
                        variables={{id: this.props.match.params.id}}>
            {({data, refetch}) => {
              let alertData = data;
              if(!alertData.alertById){
                return (<React.Fragment></React.Fragment>);
              }

              return (
                  <React.Fragment>
                    <Subscription fetchPolicy="no-cache"
                                  subscription={ALERT_CHANGED}
                                  onSubscriptionData={
                                    o => this.handleSubscriptionData(o,
                                        refetch)}/>
                    <AlertViewLayout alert={alertData.alertById}/>
                  </React.Fragment>
              );
            }}
          </QueryHandler>
        </React.Fragment>
    );
  }
}

export default AlertView;