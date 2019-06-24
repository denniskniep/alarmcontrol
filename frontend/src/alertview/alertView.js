import {Query} from "react-apollo";
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
    }
  }
`;

class AlertView extends Component {
  render() {
    return (

        <React.Fragment>
          <AlertViewSwitcher />
          <Query query={ALERT_BY_ID}
                 variables={{id: this.props.match.params.id}}>
            {({loading, error, data}) => {
              if (loading) {
                return <p>Loading...</p>;
              }
              if (error) {
                return <p>Error: ${error.message}</p>;
              }
              if (!data.alertById) {
                return <p>NO DATA</p>;
              }

              return (<AlertViewLayout alert={data.alertById}/>);
            }}
          </Query>
        </React.Fragment>);
  }
}

export default AlertView;