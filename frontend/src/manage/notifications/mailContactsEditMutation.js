import React, {Component} from 'react';
import {gql} from "apollo-boost";
import MutationHandler from "../../utils/mutationHandler";
import EditableTable from "../../components/editableTable";

const DELETE_NOTIFICATION_CONTACT = gql`
mutation deleteNotificationContact($organisationId: ID!, $uniqueContactId: String!){
  deleteNotificationContact(
    organisationId: $organisationId,  
    uniqueContactId : $uniqueContactId)
}
`;

const ADD_NOTIFICATION_CONTACT = gql`
mutation addNotificationMailContact($organisationId: ID!, $name : String!, $mailAddress: String!){
  addNotificationMailContact(organisationId: $organisationId, name: $name, mailAddress: $mailAddress) {   
    uniqueId
  }
}
`;

class MailContactsEditMutation extends Component {

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
                                           key: "mailAddress",
                                           name: "Mail"
                                         }
                                       ]}

                                       onNewRow={newRow => {
                                         addContact({
                                           variables: {
                                             organisationId: this.props.organisationId,
                                             name: newRow.name,
                                             mailAddress: newRow.mailAddress
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

export default MailContactsEditMutation;