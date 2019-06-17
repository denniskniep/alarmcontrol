import React, {Component} from 'react';
import {
  Button,
  ButtonToolbar,
  Form
} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

class EditableRow extends Component {

  constructor(props) {
    super(props);
    let obj = {...this.props.obj};
    for(let column of this.props.columns){
      if(!obj.hasOwnProperty(column.key)){
        obj[column.key] = "";
      }
    }

    this.state = {
      obj: obj
    }
  }

  handleChange(e, column){
    let newValue = e.target.value;
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
                <td >
                  {!column.readOnly && <Form.Control
                     type="text"
                     value={this.state.obj[column.key]}
                     onChange={e => this.handleChange(e, column)} /> }

                  {column.readOnly && this.state.obj[column.key]}
                </td>
              </React.Fragment>
          )
        })}
        <td>
          <ButtonToolbar>
              <Button className={"btn-icon"} variant="success" onClick={e => this.props.onSave && this.props.onSave(this.state.obj)}>
                <FontAwesomeIcon icon={["far", "save"]}/>
              </Button>
              <Button className={"btn-icon btn-icon-space"} variant="danger" onClick={e => this.props.onCancel && this.props.onCancel()}>
                <FontAwesomeIcon icon={["fas", "times"]}/>
              </Button>

          </ButtonToolbar>
        </td>
      </tr>)
  }
}

export default EditableRow;