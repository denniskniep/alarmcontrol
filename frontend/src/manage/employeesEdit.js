import React, {Component} from 'react';
import {Col, Container, Row} from "react-bootstrap";
import EditableTable from "../components/editableTable";
import TagViewer from "../components/tagViewer";
import TagEditor from "../components/tagEditor";

class EmployeesEdit extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
        <Container>
          <Row>
            <Col>
              <EditableTable data={this.props.employees}

                             columns={[
                               {
                                 key: "id",
                                 name: "#",
                                 readOnly: true
                               },
                               {
                                 key: "firstname",
                                 name: "Vorname"
                               },
                               {
                                 key: "lastname",
                                 name: "Nachname"
                               },
                               {
                                 key: "referenceId",
                                 name: "Nummer"
                               },
                               {
                                 key: "skills",
                                 name: "Skills",
                                 viewer: TagViewer,
                                 editor: TagEditor,
                                 editorProps : {
                                   suggestions : this.props.skills
                                 },
                                 defaultValue: []
                               }
                             ]}

                             onNewRow={newRow => {
                               this.props.onNewEmployee
                               && this.props.onNewEmployee(newRow);
                             }}

                             onRowEdited={(oldRow, newRow) =>
                                 this.props.onEmployeeEdited
                                 && this.props.onEmployeeEdited(oldRow, newRow)}

                             onRowDeleted={(deletedRow) =>
                                 this.props.onEmployeeDeleted
                                 && this.props.onEmployeeDeleted(deletedRow)}

              />
            </Col>
          </Row>
        </Container>)
  }
}

export default EmployeesEdit;