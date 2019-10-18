import React, {Component} from 'react';
import {withRouter} from 'react-router-dom'
import EditableTable from "../components/editableTable";

class AaosEdit extends Component {
    constructor(props) {
        super(props);
        this.state = { entries: []};
    }
    render() {
        return (
            <EditableTable data={this.props.organisations}

                           canView={false}

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
                               this.props.onNewAao
                               && this.props.onNewAao(newRow);
                           }}

                           onStartEditMode={(row) => {
                               this.props.history.push("/app/manage/aao/" + row.id)
                               return true;
                           }}

                           onRowEdited={(oldRow, newRow) =>
                               this.props.onAaoEdited
                               && this.props.onAaoEdited(oldRow, newRow)}

                           onRowDeleted={(deletedRow) =>
                               this.props.onOrganisationDeleted
                               && this.props.onOrganisationDeleted(deletedRow)}

            />);
    }
}

export default withRouter(AaosEdit);