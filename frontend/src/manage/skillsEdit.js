import React, {Component} from 'react';
import {Col, Container, Row} from "react-bootstrap";
import EditableTable from "../components/EditableTable";

class SkillsEdit extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
        <Container>
          <Row>
            <Col>
              <EditableTable data={this.props.skills}

                             columns={[
                               {
                                 key: "id",
                                 name: "#",
                                 readOnly: true
                               },
                               {
                                 key: "name",
                                 name: "Name"
                               },
                               {
                                 key: "shortName",
                                 name: "Abkürzung"
                               }
                             ]}

                             onNewRow={newRow =>{
                               this.props.onNewSkill
                               && this.props.onNewSkill(newRow);
                             }}

                             onRowEdited={(oldRow, newRow) =>
                                 this.props.onSkillEdited
                                 && this.props.onSkillEdited(newRow)}

                             onRowDeleted={(deletedRow) =>
                                 this.props.onSkillDeleted
                                 && this.props.onSkillDeleted(deletedRow)}

              />
            </Col>
          </Row>
        </Container>)
  }
}

export default SkillsEdit;