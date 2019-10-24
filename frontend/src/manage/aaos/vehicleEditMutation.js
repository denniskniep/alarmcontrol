import React, {Component} from 'react';
import {gql} from "apollo-boost";
import MutationHandler from "../../utils/mutationHandler";
import {Button, Col, Container, Row} from "react-bootstrap";
import EditableTable from "../../components/editableTable";
import TagViewer from "../../components/tagViewer";
import TagEditor from "../../components/DraggableTagEditor";

const DELETE_VEHICLE = gql`
mutation deleteVehicle($organisationId: ID!, $uniqueVehicleId: String!){
  deleteVehicle(
    organisationId: $organisationId,  
    uniqueVehicleId : $uniqueVehicleId)
}
`;

const ADD_VEHICLE = gql`
mutation addVehicle($organisationId: ID!, $name : String!){
  addVehicle(organisationId: $organisationId, name: $name) {   
    uniqueId
  }
}
`;

class VehicleEditMutation extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (<MutationHandler mutation={DELETE_VEHICLE}
                                 onCompleted={() => this.props.onVehiclesChanged
                                     && this.props.onVehiclesChanged()}>
                {deleteVehicle => {
                    return (
                        <MutationHandler mutation={ADD_VEHICLE}
                                         onCompleted={() => this.props.onVehiclesChanged
                                             && this.props.onVehiclesChanged()}>
                            {addContact => {

                                var vehicles= this.props.vehicles ? this.props.vehicles : [];
                                return (
                                    <EditableTable data={vehicles}
                                                   canView={false}
                                                   canEdit={false}
                                                   columns={[
                                                       {
                                                           key: "name",
                                                           name: "Fahrzeugbezeichnung"
                                                       }
                                                   ]}

                                                   onNewRow={newRow => {
                                                       console.log('newRow',newRow);
                                                       addContact({
                                                           variables: {
                                                               organisationId: this.props.organisationId,
                                                               name: newRow.name
                                                           }
                                                       })
                                                   }}

                                                   onRowDeleted={(deletedRow) =>
                                                   {
                                                      console.log('deleted',deletedRow);
                                                       deleteVehicle({
                                                           variables: {
                                                               organisationId: this.props.organisationId,
                                                               uniqueVehicleId: deletedRow.uniqueId
                                                           }
                                                       })
                                                   }
                                                   }

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

export default VehicleEditMutation;