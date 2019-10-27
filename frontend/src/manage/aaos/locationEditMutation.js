import React, {Component} from 'react';
import {gql} from "apollo-boost";
import MutationHandler from "../../utils/mutationHandler";
import EditableTable from "../../components/editableTable";

const DELETE_VEHICLE = gql`
mutation deleteLocation($organisationId: ID!, $uniqueLocationId: String!){
  deleteLocation(
    organisationId: $organisationId,  
    uniqueLocationId : $uniqueLocationId)
}
`;

const ADD_VEHICLE = gql`
mutation addLocation($organisationId: ID!, $name : String!){
  addLocation(organisationId: $organisationId, name: $name) {   
    uniqueId
  }
}
`;

class LocationEditMutation extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (<MutationHandler mutation={DELETE_VEHICLE}
                                 onCompleted={() => this.props.onLocationsChanged
                                     && this.props.onLocationsChanged()}>
                {deleteLocation => {
                    return (
                        <MutationHandler mutation={ADD_VEHICLE}
                                         onCompleted={() => this.props.onLocationsChanged
                                             && this.props.onLocationsChanged()}>
                            {addLocation => {

                                var locations= this.props.locations ? this.props.locations : [];

                                return (
                                    <EditableTable data={locations}
                                                   canView={false}
                                                   canEdit={false}
                                                   columns={[
                                                       {
                                                           key: "name",
                                                           name: "Ortsbezeichnung"
                                                       }
                                                   ]}

                                                   onNewRow={newRow => {
                                                       console.log('newRow',newRow);
                                                       addLocation({
                                                           variables: {
                                                               organisationId: this.props.organisationId,
                                                               name: newRow.name
                                                           }
                                                       })
                                                   }}

                                                   onRowDeleted={(deletedRow) =>
                                                   {
                                                       console.log('deleted',deletedRow);
                                                       deleteLocation({
                                                           variables: {
                                                               organisationId: this.props.organisationId,
                                                               uniqueLocationId: deletedRow.uniqueId
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

export default LocationEditMutation;