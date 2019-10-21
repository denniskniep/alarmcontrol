import React, {Component} from 'react';
import {gql} from "apollo-boost";
import MutationHandler from "../../../utils/mutationHandler";
import EditableTable from "../../../components/editableTable";

const DELETE_NOTIFICATION_CONTACT = gql`
mutation deleteNotificationContact($organisationId: ID!, $uniqueContactId: String!){
  deleteNotificationContact(
    organisationId: $organisationId,  
    uniqueContactId : $uniqueContactId)
}
`;

const ADD_NOTIFICATION_CONTACT = gql`
mutation addNotificationFirebaseMessageContact($organisationId: ID!, $name : String!, $token: String!){
  addNotificationFirebaseMessageContact(organisationId: $organisationId, name: $name, token: $token) {   
    uniqueId
  }
}
`;

class FirebaseMessageContactsEditMutation extends Component {

  render() {
    return (
        <MutationHandler mutation={DELETE_NOTIFICATION_CONTACT}
                         onCompleted={() => this.props.onContactsChanged
                             && this.props.onContactsChanged()}>
          {deleteContact => {
            return (
                <MutationHandler mutation={ADD_NOTIFICATION_CONTACT}
                                 onCompleted={() => this.props.onContactsChanged
                                     && this.props.onContactsChanged()}>
                  {addContact => {

                    var contacts= this.props.contacts ? this.props.contacts : [];
                    return (
                        <EditableTable data={contacts}
                                       canView={false}
                                       canEdit={false}
                                       columns={[
                                         {
                                           key: "name",
                                           name: "Name"
                                         },
                                         {
                                           key: "token",
                                           name: "Token"
                                         }
                                       ]}

                                       onNewRow={newRow => {
                                         addContact({
                                           variables: {
                                             organisationId: this.props.organisationId,
                                             name: newRow.name,
                                             token: newRow.token
                                           }
                                         });
                                       }}

                                       onRowDeleted={(deletedRow) => {
                                         deleteContact({
                                           variables: {
                                             organisationId: this.props.organisationId,
                                             uniqueContactId: deletedRow.uniqueId
                                           }
                                         });
                                       }}

                        />
                    )
                  }}
                </MutationHandler>
            )
          }}
        </MutationHandler>
    );
  }
}

export default FirebaseMessageContactsEditMutation;