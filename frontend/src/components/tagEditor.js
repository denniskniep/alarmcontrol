import React, {Component} from 'react';
import ReactTags from 'react-tag-autocomplete'
import '../react-tag.css';

class TagEditor extends Component {

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
    const tags = [].concat(this.props.value, tag);
    if(this.props.onChange){
      this.props.onChange(tags)
    }
  }

  suggestionsFilter(suggestion, query) {
    const existing = this.props.value.map(v => v.id);

    return suggestion &&
        suggestion.name &&
        suggestion.name.startsWith(query) &&
        !existing.includes(suggestion.id);
  }

  render() {
    return (
        <ReactTags
            minQueryLength={1}
            autofocus={false}
            tags={this.props.value}
            suggestions={this.props.suggestions}
            suggestionsFilter={(suggestion, query) => this.suggestionsFilter(suggestion, query)}
            handleDelete={this.handleDelete.bind(this)}
            handleAddition={this.handleAddition.bind(this)} />)
  }
}

export default TagEditor;