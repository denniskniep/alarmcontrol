import React, {Component} from 'react';
import {Form} from "react-bootstrap";

class BooleanViewer extends Component {

  render() {
    return (
        <Form.Check checked={this.props.value}
                         readOnly={true}

        />
        )
  }
}

export default BooleanViewer;