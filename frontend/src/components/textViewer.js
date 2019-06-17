import React, {Component} from 'react';

class TextViewer extends Component {

  render() {
    return (<span>{this.props.value}</span>)
  }
}

export default TextViewer;