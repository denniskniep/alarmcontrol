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

        this.state = { };
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

                            let vehicleSuggestions = this.props.vehicles.map(v => ({id : v.uniqueId, text: v.name, shortName: v.text}));
                            let locationSuggestions = this.props.locations.map(v => ({id : v.uniqueId, text: v.name, shortName: v.text}));

                            var aaoRules= this.props.aaoRules ? this.props.aaoRules : [];

                            aaoRules = aaoRules
                                .map(rule => ({
                                    locations: this.props.locations.filter(l => rule.locations.map(l2 => l2).includes(l.uniqueId)).map(l3 => ({
                                        id: l3.uniqueId,
                                        text: l3.name,
                                        shortName: l3.name
                                })),
                                    vehicles: this.props.vehicles.filter(l => rule.vehicles.map(l2 => l2).includes(l.uniqueId)).map(l3 => ({
                                        id: l3.uniqueId,
                                        text: l3.name,
                                        shortName: l3.name
                                    }))
                                }));
                            return (
                    <EditableTable data={aaoRules}
                                   canView={false}
                                   columns={[
                                       {
                                           key: "keywords",
                                           name: "Alarmstichworte",
                                           viewer: TagViewer,
                                           editor: TagEditor,
                                           editorProps : {
                                               suggestions : [{id: '1', text: 'H1', shortName: 'H1'}, {id:'2', text: 'H1-Y', shortName: 'H1-Y'}]
                                           },
                                           defaultValue: []
                                       },
                                       {
                                           key: "locations",
                                           name: "Orte",
                                           viewer: TagViewer,
                                           editor: TagEditor,
                                           editorProps : {
                                               suggestions : locationSuggestions
                                           },
                                           defaultValue: []
                                       },
                                       {
                                           key: "vehicles",
                                           name: "Fahrzeuge",
                                           viewer: TagViewer,
                                           editor: TagEditor,
                                           editorProps : {
                                               suggestions : vehicleSuggestions
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
                                               locations: newRow.locations.map(location => location.id),
                                               vehicles: newRow.vehicles.map(vehicle => vehicle.id)
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