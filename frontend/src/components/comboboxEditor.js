import React, {Component} from 'react';
import {Dropdown, Form} from "react-bootstrap";
import DropdownButton from "react-bootstrap/DropdownButton";

class ComboboxEditor extends Component {

  constructor(props) {
    super(props);

    this.state = {
      isOpen: false
    }
  }

  handleChange(itemId){
    if(this.props.onChange){
      this.props.onChange(itemId)
      this.setState(() => {
        return {isOpen:false}
      })
    }
  }

  render() {

    let selectedItem = this.props.items.find(i => i.id == this.props.value);
    return (
        <DropdownButton id="dropdown-basic-button"
                        title={selectedItem && selectedItem.name}
                        className={this.props.className}
                        size="sm"
                        show={this.state.isOpen}
                        variant="secondary"
                        onToggle={(isOpen) => this.setState(() => {
                          return {isOpen}
                        })}>
          {
            this.props.items && this.props.items.map((item) => {
              return (
                  <Dropdown.Item key={item.id} onClick={() => this.handleChange(item.id)}>{item.name}</Dropdown.Item>
              )
            })
          }
        </DropdownButton>
    )
  }
}

export default ComboboxEditor;