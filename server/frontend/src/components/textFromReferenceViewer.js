import React, {Component} from 'react';

class TextFromReferenceViewer extends Component {

  render() {
    let found = this.props.items.find(i => i.id == this.props.value);
    return (<span className={this.props.className}>{found.name}</span>)
  }
}

export default TextFromReferenceViewer;