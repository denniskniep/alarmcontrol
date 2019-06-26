import React, {Component} from 'react';
import {Col, Container, Row} from "react-bootstrap";
import EditableTable from "../components/editableTable";

class AlertNumbersEdit extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
        <Container>
          <Row>
            <Col>
              <EditableTable data={this.props.alertNumbers}

                             columns={[
                               {
                                 key: "id",
                                 name: "#",
                                 readOnly: true
                               },
                               {
                                 key: "number",
                                 name: "Nummer"
                               },
                               {
                                 key: "shortDescription",
                                 name: "Kurzbeschreibung"
                               },
                               {
                                 key: "description",
                                 name: "Beschreibung"
                               }
                             ]}

                             onNewRow={newRow =>{
                               this.props.onNewAlertNumber
                               && this.props.onNewAlertNumber(newRow);
                             }}

                             onRowEdited={(oldRow, newRow) =>
                                 this.props.onAlertNumberEdited
                                 && this.props.onAlertNumberEdited(newRow)}

                             onRowDeleted={(deletedRow) =>
                                 this.props.onAlertNumberDeleted
                                 && this.props.onAlertNumberDeleted(deletedRow)}

              />
            </Col>
          </Row>
        </Container>)
  }
}

export default AlertNumbersEdit;