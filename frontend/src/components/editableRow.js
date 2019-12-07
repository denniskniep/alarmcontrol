import React, {Component} from 'react';
import {Button, ButtonToolbar} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {chooseEditor, chooseViewer, chooseDefaultValue} from "./defaultsChooser";

class EditableRow extends Component {

  constructor(props) {
    super(props);

    this.addButtonIcon = this.props.addButtonIcon ? this.props.addButtonIcon: ["far", "save"]

    let obj = {...this.props.obj};
    let objValid = {};
    for (let column of this.props.columns) {
      if (!obj.hasOwnProperty(column.key)) {
        obj[column.key] = chooseDefaultValue(column.defaultValue);
        objValid[column.key] = this.validate(obj[column.key], column);
      }
    }

    this.state = {
      obj: obj,
      objValid : objValid
    }
  }

  validate(value, column){
    if(column.valueValidator){
      return column.valueValidator(value);
    }
    return true;
  }

  handleChange(newValue, column) {
    let valid = this.validate(newValue, column);
    this.setState((state, props) => {
      state.obj[column.key] = newValue;
      state.objValid[column.key] = valid;
      return {
        obj: state.obj,
        objValid : state.objValid
      }
    });
  }

  render() {
    return (
        <tr>
          {this.props.columns.map((column, index) => {
            return (
                <React.Fragment key={column.key}>
                  <td className={ this.state.objValid[column.key] ? "cell-valid" : "cell-invalid" }>
                    {
                      !column.readOnly &&
                    React.createElement(chooseEditor(column.editor,
                        this.state.obj[column.key]), {
                      value: this.state.obj.hasOwnProperty(column.key) ? this.state.obj[column.key] : chooseDefaultValue(column.defaultValue),
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
                      disabled={!Object.entries(this.state.objValid).every(([k,v]) => v == true)}
                      onClick={e => this.props.onSave && this.props.onSave(
                          this.state.obj)}>
                <FontAwesomeIcon icon={this.addButtonIcon}/>
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