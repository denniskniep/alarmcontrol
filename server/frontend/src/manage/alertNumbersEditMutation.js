import React, {Component} from 'react';
import {gql} from "apollo-boost";
import AlertNumbersEdit from "./alertNumbersEdit";
import QueryHandler from "../utils/queryHandler";
import MutationHandler from "../utils/mutationHandler";

const ALERT_NUMBERS_BY_ORGANISATION_ID = gql`
  query organisationById($id: ID!) {
    organisationById(organisationId: $id) {
      id
      alertNumbers {
        id
        number
        description
        shortDescription
      }
    }
  }
`;

const NEW_ALERT_NUMBER = gql`
    mutation newAlertNumber($organisationId: ID!, $number: String!, $description: String, $shortDescription: String!) {
     newAlertNumber(organisationId: $organisationId, number: $number, description: $description, shortDescription: $shortDescription) {
      id
    }
  }
`;

const EDIT_ALERT_NUMBER = gql`
    mutation editAlertNumber($id: ID!,  $number: String!, $description: String, $shortDescription: String!) {
     editAlertNumber(id: $id, number: $number, description: $description, shortDescription: $shortDescription) {
      id
    }
  }
`;

const DELETE_ALERT_NUMBER = gql`
    mutation deleteAlertNumber($id: ID!) {
     deleteAlertNumber(id: $id) 
  }
`;

class AlertNumbersEditMutation extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
        <QueryHandler fetchPolicy="no-cache"
                      query={ALERT_NUMBERS_BY_ORGANISATION_ID}
                      variables={{id: this.props.id}}>
          {({data, refetch}) => {

            if (data && !data.organisationById) {
              return <React.Fragment></React.Fragment>;
            }

            return (
                <MutationHandler  mutation={NEW_ALERT_NUMBER}
                                  onCompleted={() =>refetch()}>
                  { createNewAlertNumber => (

                      <MutationHandler mutation={EDIT_ALERT_NUMBER}
                                       onCompleted={() => refetch()}>
                        { editAlertNumber => (

                            <MutationHandler mutation={DELETE_ALERT_NUMBER}
                                             onCompleted={() => refetch()}>
                              { deleteAlertNumber => (

                                  <AlertNumbersEdit
                                      alertNumbers={data.organisationById.alertNumbers}

                                      onNewAlertNumber={newAlertNumber => {
                                        createNewAlertNumber({
                                          variables: {
                                            organisationId: this.props.id,
                                            number: newAlertNumber.number,
                                            shortDescription: newAlertNumber.shortDescription,
                                            description: newAlertNumber.description
                                          }
                                        });
                                      }}

                                      onAlertNumberEdited={editedAlertNumber => {
                                        editAlertNumber({
                                          variables: {
                                            id: editedAlertNumber.id,
                                            number: editedAlertNumber.number,
                                            shortDescription: editedAlertNumber.shortDescription,
                                            description: editedAlertNumber.description
                                          }
                                        });
                                      }}

                                      onAlertNumberDeleted={deletedAlertNumber => {
                                        deleteAlertNumber({
                                          variables: {
                                            id: deletedAlertNumber.id
                                          }
                                        });
                                      }}
                                  />
                              )}
                            </MutationHandler>
                        )}
                      </MutationHandler>
                  )}
                </MutationHandler>
            );
          }}
        </QueryHandler>);
  }
}

export default AlertNumbersEditMutation;