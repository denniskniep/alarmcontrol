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
    const startingWith = suggestion &&
        suggestion.name &&
        suggestion.name.toLowerCase().startsWith(query.toLowerCase());
    const expandAll = query === " ";

    return !existing.includes(suggestion.id) &&
        (startingWith || expandAll);
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