import React, {Component} from 'react';
import {Route} from 'react-router-dom'
import EditableTable from "../components/editableTable";

class OrganisationsEdit extends Component {
  render() {
    return (
        <Route render={({history}) => (
            <EditableTable data={this.props.organisations}

                           columns={[
                             {
                               key: "id",
                               name: "#",
                               readOnly: true
                             },
                             {
                               key: "name",
                               name: "Name"
                             }
                           ]}

                           onNewRow={newRow => {
                             this.props.onNewOrganisation
                             && this.props.onNewOrganisation(newRow);
                           }}

                           onStartEditMode={(row) => {
                             history.push("/app/manage/organisation/" + row.id)
                             return true;
                           }}

                           onRowDeleted={(deletedRow) =>
                               this.props.onOrganisationDeleted
                               && this.props.onOrganisationDeleted(deletedRow)}

            />)}/>);
  }
}

export default OrganisationsEdit;