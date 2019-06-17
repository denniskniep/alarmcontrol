import React, {Component} from 'react';
import {Col, Container, Row} from "react-bootstrap";
import EditableTable from "../components/editableTable";

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
                               }
                             ]}

                             onNewRow={newRow =>{
                                 this.props.onNewEmployee
                                 && this.props.onNewEmployee(newRow);
                             }}

                             onRowEdited={(oldRow, newRow) =>
                                 this.props.onEmployeeEdited
                                 && this.props.onEmployeeEdited(newRow)}

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