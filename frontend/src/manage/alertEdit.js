import React, {Component} from 'react';
import {Col, Container, Row} from "react-bootstrap";
import PagedEditableTable from "../components/pagedEditableTable";

class AlertEdit extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
        <Container>
          <Row>
            <Col>
              <PagedEditableTable canCreate={false}
                                  canDelete={true}
                                  canEdit={false}
                                  data={this.props.alerts}
                                  totalPages={this.props.totalPages}
                                  currentPage={this.props.currentPage}

                                  columns={[
                                    {
                                      key: "id",
                                      name: "#",
                                      readOnly: true
                                    },
                                    {
                                      key: "title",
                                      name: "Titel",
                                      readOnly: true
                                    }
                                  ]}

                                  onRowDeleted={(deletedRow) =>
                                      this.props.onAlertDeleted
                                      && this.props.onAlertDeleted(deletedRow)}

                                  onPageRequested={(pageRequest) =>
                                      this.props.onPageRequested
                                      && this.props.onPageRequested(pageRequest)}

              />
            </Col>
          </Row>
        </Container>)
  }
}

export default AlertEdit;