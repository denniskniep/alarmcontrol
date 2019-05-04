import React from "react";
import { Query } from "react-apollo";
import { gql } from "apollo-boost";

const ALERT_BY_ID = gql`
    query alertById($id: ID) {
      alertById(id: $id) {
        id
        keyword
        dateTime
      }
    }
`;

function AlertView({ match }) {
  return (
    <Query query={ALERT_BY_ID} variables={{id :match.params.id}}>
      {({ loading, error, data }) => {
        if (loading) return <p>Loading...</p>;
        if (error) return <p>Error: ${error.message}</p>;
        if (!data.alertById) return <p>NO DATA</p>;

        return (<ul>
          <li>{data.alertById.id}</li>
          <li>{data.alertById.keyword}</li>
          <li>{data.alertById.dateTime}</li>
        </ul>);
      }}
    </Query>
  );
}

export default AlertView;
