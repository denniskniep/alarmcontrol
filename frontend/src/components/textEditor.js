import React, {Component} from 'react';
import {Form} from "react-bootstrap";

class TextEditor extends Component {

  render() {
    return (
        <Form.Control
            type="text"
            value={this.props.value}
            onChange={e => this.props.onChange && e.target && this.props.onChange(e.target.value)}/>)
  }
}

export default TextEditor;