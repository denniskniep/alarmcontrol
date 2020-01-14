import React, {Component} from 'react';
import {MessagesContext} from "./messagesContext";
import {getMessages} from "./messageStore"
import moment from "moment";

class MessagesContainer extends Component {

  constructor(props) {
    super(props);
    document.addEventListener('onMessagesChanged', () => this.onMessagesChanged());

    this.state = {
      messages: getMessages()
    }
  }

  onMessagesChanged() {
    this.setState((state, props) => {
      return {
        messages: getMessages()
      }
    });
  }

  render() {
    return (
        <MessagesContext.Provider value={{
          messages: this.state.messages
        }}>
          {this.props.children}
        </MessagesContext.Provider>
    )
  }
}

export default MessagesContainer