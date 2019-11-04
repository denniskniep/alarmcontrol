import React, {Component} from 'react';
import {Button, Col, Container, Row} from "react-bootstrap";
import OrganisationEditMutation from "./organisationEditMutation";
import EmployeesEditMutation from "./employeesEditMutation";
import SkillsEditMutation from "./skillsEditMutation";
import AlertNumbersEditMutation from "./alertNumbersEditMutation";
import EditableTable from "../components/editableTable";
import TagViewer from "../components/tagViewer";
import TagEditor from "../components/draggableTagEditor";
import OrganisationsEditMutation from "./organisationsEditMutation";
import AaosEditMutation from "./aaosEditMutation";

class AaoView extends Component {

    constructor(props) {
        super(props);
        this.state = { entries: []};
    }

    render() {
       return ( <Container>
            <Row>
                <Col>
                    <EditableTable data={this.state.entries}
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
                                           key: "districts",
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
                                       this.props.onNewAao
                                       && this.props.onNewAao(newRow);
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
                </Col>
            </Row>
        </Container>);
    }
}

export default AaoView;