import {Query, Subscription} from "react-apollo";
import React, {Component} from 'react';
import {gql} from "apollo-boost";
import AlertViewLayout from "./alertViewLayout";
import AlertViewSwitcher from "./alertViewSwitcher";

const ALERT_BY_ID = gql`
  query alertById($id: ID) {
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
      }
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
          <AlertViewSwitcher/>
          <Query query={ALERT_BY_ID}
                 fetchPolicy="no-cache"
                 variables={{id: this.props.match.params.id}}>
            {({loading, error, data, refetch}) => {
              let alertData = data;
              if (loading) {
                return <p>Loading...</p>;
              }
              if (error) {
                return <p>Error: ${error.message}</p>;
              }
              if (!alertData || !alertData.alertById) {
                return <p>NO DATA</p>;
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
          </Query>
        </React.Fragment>
    );
  }
}

export default AlertView;