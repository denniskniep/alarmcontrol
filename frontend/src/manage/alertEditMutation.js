import {Mutation, Query} from "react-apollo";
import React, {Component} from 'react';
import {gql} from "apollo-boost";
import ErrorHandler from "../utils/errorHandler";
import QueryDefaultHandler from "../utils/queryDefaultHandler";
import AlertEdit from "./alertEdit";
import Alert from "../utils/alert";

const ALERTS_BY_ORGANISATION_ID = gql`
  query alertsByOrganisationId($id: ID, $page: Int!, $size: Int!) {
    alertsByOrganisationId(organisationId: $id, page: $page, size: $size) {
      totalCount
      items {
        id
        keyword
        addressInfo1
        dateTime
      }
    }
  }
`;

const DELETE_ALERT = gql`
    mutation deleteAlert($id: ID!) {
     deleteAlert(id: $id) 
  }
`;

class AlertEditMutation extends Component {

  constructor(props) {
    super(props);

    this.state = {
      pageSize: 10,
      currentPage: 0
    }
  }

  calculateTotalPages(totalCount, pageSize) {
    //round Up
    return Math.ceil(totalCount / pageSize);
  }

  render() {
    return (
        <Query fetchPolicy="no-cache" query={ALERTS_BY_ORGANISATION_ID}
               variables={{
                 id: this.props.id,
                 page: this.state.currentPage,
                 size: this.state.pageSize
               }}>
          {({loading, error, data, refetch}) => {

            let result = new QueryDefaultHandler().handleGraphQlQuery(loading,
                error,
                data,
                data && data.alertsByOrganisationId);

            if (result) {
              return result;
            }

            let alerts = data.alertsByOrganisationId.items.map(a => {
              a.title = Alert.asTitle(a);
              return a;
            });

            let totalCount = data.alertsByOrganisationId.totalCount;

            let totalPages = this.calculateTotalPages(totalCount,
                this.state.pageSize);
            return (

                <Mutation mutation={DELETE_ALERT}

                          onError={(error) =>
                              new ErrorHandler().handleGraphQlMutationError(
                                  error)}

                          onCompleted={() => refetch()}>
                  {deleteAlert => (

                      <AlertEdit
                          alerts={alerts}
                          totalPages={totalPages}
                          currentPage={this.state.currentPage}

                          onPageRequested={pageRequest => {
                              this.setState((state, props) => {
                                return {currentPage: pageRequest.currentPage}
                              });
                            }
                          }

                          onAlertDeleted={deletedAlert => {
                            deleteAlert({
                              variables: {
                                id: deletedAlert.id
                              }
                            });
                          }}
                      />
                  )}
                </Mutation>
            );
          }}
        </Query>);
  }
}

export default AlertEditMutation;