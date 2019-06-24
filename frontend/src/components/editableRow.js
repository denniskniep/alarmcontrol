import React, {Component} from 'react';
import {Button, ButtonToolbar} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {chooseEditor, chooseViewer, chooseDefaultValue} from "./defaultsChooser";

class EditableRow extends Component {

  constructor(props) {
    super(props);
    let obj = {...this.props.obj};
    for (let column of this.props.columns) {
      if (!obj.hasOwnProperty(column.key)) {
        obj[column.key] = chooseDefaultValue(column.defaultValue);
      }
    }

    this.state = {
      obj: obj
    }
  }

  handleChange(newValue, column) {
    this.setState((state, props) => {
      state.obj[column.key] = newValue;
      return {obj: state.obj}
    });
  }

  render() {

    return (
        <tr>
          {this.props.columns.map((column, index) => {
            return (
                <React.Fragment key={column.key}>
                  <td>
                    {!column.readOnly &&
                    React.createElement(chooseEditor(column.editor,
                        this.state.obj[column.key]), {
                      value: this.state.obj[column.key],
                      onChange: newValue => this.handleChange(newValue, column),
                      ...column.editorProps
                    })}

                    {
                      column.readOnly &&
                      React.createElement(chooseViewer(column.viewer,
                          this.state.obj[column.key]), {
                        value:  this.state.obj[column.key]
                      })
                    }
                  </td>
                </React.Fragment>
            )
          })}
          <td>
            <ButtonToolbar>
              <Button className={"btn-icon"} variant="success"
                      onClick={e => this.props.onSave && this.props.onSave(
                          this.state.obj)}>
                <FontAwesomeIcon icon={["far", "save"]}/>
              </Button>
              <Button className={"btn-icon btn-icon-space"} variant="danger"
                      onClick={e => this.props.onCancel
                          && this.props.onCancel()}>
                <FontAwesomeIcon icon={["fas", "times"]}/>
              </Button>

            </ButtonToolbar>
          </td>
        </tr>)
  }
}

export default EditableRow;