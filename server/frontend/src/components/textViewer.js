import React, {Component} from 'react';

class TextViewer extends Component {

  render() {
    return (<span className={this.props.className}>{this.props.value}</span>)
  }
}

export default TextViewer;