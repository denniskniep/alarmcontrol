import React, {Component} from 'react';
import {Form} from "react-bootstrap";

class BooleanEditor extends Component {

  handleChange(e){
    if(this.props.onChange && e.target){
      let changedTo = e.target.checked;
      this.props.onChange(changedTo)
    }
  }

  render() {
    return (
        <Form.Check checked={this.props.value}
                    onChange={e => this.handleChange(e)}

        />
    )
  }
}

export default BooleanEditor;