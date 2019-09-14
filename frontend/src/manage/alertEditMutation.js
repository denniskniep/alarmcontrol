import {Mutation} from "react-apollo";
import React, {Component} from 'react';
import {gql} from "apollo-boost";
import ErrorHandler from "../utils/errorHandler";
import AlertEdit from "./alertEdit";
import {asTitle as asAlertTitle} from "../utils/alert";
import {Route} from 'react-router-dom'
import QueryHandler from "../utils/queryHandler";

const ALERTS_BY_ORGANISATION_ID = gql`
  query alertsByOrganisationId($id: ID, $page: Int!, $size: Int!) {
    alertsByOrganisationId(organisationId: $id, page: $page, size: $size) {
      totalCount
      items {
        id
        keyword
        addressInfo1
        dateTime
        organisation {
          id
          name
        }
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
      pageSize: 25,
      currentPage: 0
    }
  }

  calculateTotalPages(totalCount, pageSize) {
    //round Up
    return Math.ceil(totalCount / pageSize);
  }

  render() {
    return (
        <Route render={({history}) => (
            <QueryHandler  fetchPolicy="no-cache"
                           query={ALERTS_BY_ORGANISATION_ID}
                           variables={{
                             id: this.props.id,
                             page: this.state.currentPage,
                             size: this.state.pageSize
                           }}>
              {({data, refetch}) => {

                if (data && !data.alertsByOrganisationId) {
                  return <React.Fragment></React.Fragment>;
                }

                let alerts = data.alertsByOrganisationId.items.map(a => {
                  a.title = asAlertTitle(a);
                  a.organisationName = a.organisation.name;
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

                              onAlertViewed={alert => {
                                history.push("/app/alertview/" + alert.id)
                              }}
                          />
                      )}
                    </Mutation>
                );
              }}
            </QueryHandler>
        )}/>
    );
  }
}

export default AlertEditMutation;