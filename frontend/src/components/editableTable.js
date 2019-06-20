import React, {Component} from 'react';
import {Button, ButtonGroup, ButtonToolbar, Table} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import EditableRow from "./editableRow";
import {chooseDefaultValue, chooseViewer} from "./defaultsChooser";

class EditableTable extends Component {

  constructor(props) {
    super(props);

    this.state = {
      newObj: {},
      newObjKey: 1, //Forces recreate if key changes
      rowsInEditMode: []
    }
  }

  startEditMode(dataIndex, obj) {
    //is startEditModeHandler defined and should or should not start the edit Mode?
    if (this.props.onStartEditMode && !this.props.onStartEditMode(obj)) {
      return;
    }

    this.setState((state, props) => {
      state.rowsInEditMode.push(dataIndex);
      return {rowsInEditMode: state.rowsInEditMode}
    });
  }

  stopEditMode(dataIndex) {
    this.setState((state, props) => {
      let index = state.rowsInEditMode.indexOf(dataIndex);
      if (index > -1) {
        state.rowsInEditMode.splice(index, 1);
      }
      return {rowsInEditMode: state.rowsInEditMode}
    });
  }

  resetNewObjRow() {
    this.setState((state, props) => ({
      newObj: {},
      newObjKey: ++state.newObjKey
    }))
  }

  render() {
    return (
        <Table>
          <thead>
          <tr>

            {this.props.columns.map((column, index) => {
              return (
                  <td key={column.key}>{column.name}</td>
              )
            })}

            <td>
            </td>

          </tr>
          </thead>
          <tbody>
          {this.props.data.map((obj, dataIndex) => {
            let objKey = !obj.id ? dataIndex : obj.id;
            return (
                <React.Fragment key={objKey}>
                  {this.state.rowsInEditMode.includes(dataIndex) &&
                  <EditableRow obj={obj}
                               columns={this.props.columns}
                               onSave={newObj => {
                                 this.stopEditMode(dataIndex);
                                 this.props.onRowEdited &&
                                 this.props.onRowEdited(obj, newObj)
                               }}
                               onCancel={() => this.stopEditMode(dataIndex)}
                  />
                  }

                  {!this.state.rowsInEditMode.includes(dataIndex) && <tr>
                    {this.props.columns.map((column, columnIndex) => {
                      return (
                          <React.Fragment key={objKey + "-" + column.key}>
                            <td>{
                              React.createElement(
                                  chooseViewer(column.viewer, obj[column.key]),
                                  {
                                    value: obj.hasOwnProperty(column.key) ? obj[column.key] : chooseDefaultValue(column.defaultValue)
                                  })
                            }
                            </td>
                          </React.Fragment>
                      )
                    })}
                    <td>
                      <ButtonToolbar>
                        <ButtonGroup className="mr-2">
                          <Button className={"btn-icon"}
                                  variant="outline-secondary"
                                  onClick={e => this.startEditMode(dataIndex,
                                      obj)}>
                            <FontAwesomeIcon icon={["far", "edit"]}/>
                          </Button>
                          <Button className={"btn-icon"}
                                  variant="outline-secondary"
                                  onClick={e => this.props.onRowDeleted
                                      && this.props.onRowDeleted(obj)}>
                            <FontAwesomeIcon icon={["far", "trash-alt"]}/>
                          </Button>
                        </ButtonGroup>
                      </ButtonToolbar>
                    </td>
                  </tr>
                  }
                </React.Fragment>
            )
          })}

          <EditableRow key={this.state.newObjKey}
                       obj={this.state.newObj}
                       columns={this.props.columns}
                       onSave={obj => {
                         this.resetNewObjRow();
                         this.props.onNewRow && this.props.onNewRow(obj)
                       }}
                       onCancel={() => this.resetNewObjRow()}/>

          </tbody>
        </Table>)
  }
}

export default EditableTable;