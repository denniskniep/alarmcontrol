import React, {Component} from 'react';
import {gql} from "apollo-boost";
import MutationHandler from "../../utils/mutationHandler";
import {Button, Col, Container, Row} from "react-bootstrap";
import EditableTable from "../../components/editableTable";
import TagViewer from "../../components/tagViewer";
import TagEditor from "../../components/DraggableTagEditor";

const DELETE_NOTIFICATION_CONTACT = gql`
mutation deleteNotificationContact($organisationId: ID!, $uniqueContactId: String!){
  deleteNotificationContact(
    organisationId: $organisationId,  
    uniqueContactId : $uniqueContactId)
}
`;
//Long organisationId, ArrayList<String> keywords, ArrayList<String> locations, ArrayList<String> vehicles
const ADD_NOTIFICATION_CONTACT = gql`
mutation addAao($organisationId: ID!, $keywords : [String]!, $locations: [String]!, $vehicles: [String]!){
  addAao(organisationId: $organisationId, keywords: $keywords, locations: $locations, vehicles: $vehicles ) {   
    uniqueId
  }
}
`;

class AaoEditMutation extends Component {

    constructor(props) {
        super(props);
        this.state = { entries: []};
    }

    render() {
        return (<MutationHandler mutation={DELETE_NOTIFICATION_CONTACT}
                                    onCompleted={() => this.props.onAaoRulesChanged
                                        && this.props.onAaoRulesChanged()}>
            {deleteContact => {
                return (
                    <MutationHandler mutation={ADD_NOTIFICATION_CONTACT}
                                     onCompleted={() => this.props.onAaoRulesChanged
                                         && this.props.onAaoRulesChanged()}>
                        {addContact => {

                            var contacts= this.props.contacts ? this.props.contacts : [];
                            return (
                    <EditableTable data={contacts}
                                   canView={false}
                                   columns={[
                                       {
                                           key: "id",
                                           name: "#",
                                           readOnly: true
                                       },
                                       {
                                           key: "keywords",
                                           name: "Alarmstichworte",
                                           viewer: TagViewer,
                                           editor: TagEditor,
                                           editorProps : {
                                               suggestions : [{id: '1', text: 'H1'}, {id:'2', text: 'H1-Y'}]
                                           },
                                           defaultValue: []
                                       },
                                       {
                                           key: "locations",
                                           name: "Orte",
                                           viewer: TagViewer,
                                           editor: TagEditor,
                                           editorProps : {
                                               suggestions : [{id: '1', text : 'Carlsdorf'}, {id:'2', text : 'Kelze'}]
                                           },
                                           defaultValue: []
                                       },
                                       {
                                           key: "vehicles",
                                           name: "Fahrzeuge",
                                           viewer: TagViewer,
                                           editor: TagEditor,
                                           editorProps : {
                                               suggestions : [{id: '1', text : 'HLF'}, {id:'2',text: 'ELW'}]
                                           },
                                           defaultValue: []
                                       }
                                   ]}

                                   onNewRow={newRow => {
                                       console.log('newRow',newRow);
                                       addContact({
                                           variables: {
                                               organisationId: this.props.organisationId,
                                               keywords: newRow.keywords,
                                               locations: newRow.locations,
                                               vehicles: newRow.vehicles
                                           }
                                       })
                                   }}

                                   onRowEdited={(oldRow, newRow) =>
                                   {
                                       console.log('onrowedited')
                                       /*this.props.onAaoEdited
                                       && this.props.onAaoEdited(oldRow, newRow)*/
                                   }
                                   }

                                   onRowDeleted={(deletedRow) =>
                                       this.props.onAaoDeleted
                                       && this.props.onAaoDeleted(deletedRow)}

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

export default AaoEditMutation;