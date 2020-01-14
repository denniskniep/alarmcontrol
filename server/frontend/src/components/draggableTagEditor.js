import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import { WithContext as ReactTags } from 'react-tag-input';
import '../react-tag-other.css';

class DraggableTagEditor extends Component {

  constructor(props) {
    super(props);
  }

  handleDelete (i) {
    let tags = this.props.value.slice(0);
    tags.splice(i, 1);

    if(this.props.onChange){
      this.props.onChange(tags)
    }
  }

  handleAddition (tag) {
    const existing = this.props.suggestions.map(v => v.id);
    if (!existing.includes(tag.id)) {
      return;
    }
    const tags = [].concat(this.props.value, tag);
    if(this.props.onChange){
      this.props.onChange(tags)
    }
  }

  suggestionsFilter(suggestion, query) {
    const existing = this.props.value.map(v => v.id);
    const startingWith = suggestion &&
        suggestion.name &&
        suggestion.name.toLowerCase().startsWith(query.toLowerCase());
    const expandAll = query === " ";

    return !existing.includes(suggestion.id) &&
        (startingWith || expandAll);
  }

  handleDrag(tag, currPos, newPos) {
    const tags = [...this.props.value];
    const newTags = tags.slice();

    newTags.splice(currPos, 1);
    newTags.splice(newPos, 0, tag);
    if(this.props.onChange){
      this.props.onChange(newTags)
    }
  }


  render() {
    return (<div><ReactTags
        minQueryLength={1}
        autofocus={false}
        tags={this.props.value}
        suggestions={this.props.suggestions}
        handleDelete={this.handleDelete.bind(this)}
        handleAddition={this.handleAddition.bind(this)}
        handleDrag={this.handleDrag.bind(this)}
    /></div>)
  }
}

export default DraggableTagEditor;