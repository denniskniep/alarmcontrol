import React, {Component} from 'react';
import {Form} from "react-bootstrap";

class TimeEditor extends Component {

  render() {
    return (
        <Form.Control
            type="text"
            maxLength="5"
            placeholder="00:00"
            className={this.props.className}
            value={this.props.value}
            onChange={e => this.props.onChange && e.target && this.props.onChange(e.target.value)}/>)
  }
}

export default TimeEditor;