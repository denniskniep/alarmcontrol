import {Query, Subscription} from "react-apollo";
import React, {Component} from 'react';
import {gql} from "apollo-boost";
import AlertViewLayout from "./alertViewLayout";
import AlertViewSwitcher from "./alertViewSwitcher";
import QueryDefaultHandler from "../utils/queryDefaultHandler";

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
          <Query query={ALERT_BY_ID}
                 fetchPolicy="no-cache"
                 variables={{id: this.props.match.params.id}}>
            {({loading, error, data, refetch}) => {
              let result = new QueryDefaultHandler().handleGraphQlQuery(loading,
                  error,
                  data,
                  data.alertById);

              if (result) {
                return result;
              }

              let alertData = data;
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