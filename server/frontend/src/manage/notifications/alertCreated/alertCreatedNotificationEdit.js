import React, {Component} from 'react';
import {
  Col,
  Row
} from "react-bootstrap";
import EditableTable from "../../../components/editableTable";

class AlertCreatedNotificationEdit extends Component {

  render() {
    this.values = this.props.values;
    this.updateDelaysInSeconds = this.props.values.updateDelaysInSeconds;
    let delayInSeconds = this.updateDelaysInSeconds
    .map(s => {
      return {
        id: s,
        seconds: s
      }
    }).sort(
        function (a, b) {
          return a.seconds - b.seconds;
        });

    return (
        <React.Fragment>
          <Row>
            <Col>
              <span style={{clear: "both"}}>
                Bei einem eingehenden Alarm wird eine Benachrichtigung versendet.
                Nachdem Verstreichen der jeweils unten angegebenen Sekunden wird eine Benachrichtigung mit den bisher eingegangenen
                Rückmeldungen der Einsatzkräfte versendet.
              </span>
            </Col>
          </Row>
          <Row>
            <Col xs={6} className={"left-table "}>
              <EditableTable
                  data={delayInSeconds}
                  editableRowAddButtonIcon={["fas", "plus"]}
                  canView={false}
                  canEdit={false}
                  columns={[
                    {
                      key: "seconds",
                      name: "Sekunden"
                    }
                  ]}

                  onNewRow={newRow => {
                    if (newRow.seconds &&
                        parseInt(newRow.seconds) &&
                        this.updateDelaysInSeconds.filter(
                            s => s.seconds == parseInt(newRow.seconds)).length
                        == 0) {
                      this.updateDelaysInSeconds.push(parseInt(newRow.seconds))
                      let newValues = {
                        ...this.values,
                        updateDelaysInSeconds: this.updateDelaysInSeconds
                      };
                      this.props.onValuesChanged(newValues);
                    }
                  }}

                  onRowDeleted={(deletedRow) => {
                    let index = this.updateDelaysInSeconds.indexOf(
                        deletedRow.seconds);

                    if (index > -1) {
                      this.updateDelaysInSeconds.splice(index, 1);
                      let newValues = {
                        ...this.values,
                        updateDelaysInSeconds: this.updateDelaysInSeconds
                      };
                      this.props.onValuesChanged(newValues);
                    }
                  }}
              />
            </Col>
          </Row>
        </React.Fragment>
    )
  }
}

export default AlertCreatedNotificationEdit;