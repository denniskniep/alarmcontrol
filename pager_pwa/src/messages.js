import React, {Component} from 'react';
import Grid from "@material-ui/core/Grid";
import MessagesContainer from "./notifications/messagesContainer";
import {MessagesContext} from "./notifications/messagesContext";
import Message from "./message";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import IconButton from "@material-ui/core/IconButton";
import MoreVertIcon from "@material-ui/icons/MoreVert";
import {removeMessage} from "./notifications/messageStore";

class Messages extends Component {

  constructor(props) {
    super(props);

    this.state = {
      anchorEl: null
    }
  }

  handleMenuOpen(event) {
    event.persist();
    let currentTarget = event.currentTarget;
    this.setState((state, props) => {
      return {
        anchorEl: currentTarget
      }
    });
  }

  handleMenuClose() {
    this.setState((state, props) => {
      return {
        anchorEl: null
      }
    });
  }

  deleteAllItems(messages) {
    for (const message of messages) {
      removeMessage(message.uuid);
    }
  }

  render() {
    return (
        <React.Fragment>
            <MessagesContext.Consumer>
              {messagesContext => {
                return (
                    <React.Fragment>
                      <div>
                      <h1>Messages
                        <IconButton aria-label="settings"
                                    onClick={(e) => this.handleMenuOpen(
                                        e)}>
                          <MoreVertIcon/>
                        </IconButton>
                      </h1>
                      <Menu
                          id="simple-menu"
                          anchorEl={this.state.anchorEl}
                          keepMounted
                          open={Boolean(this.state.anchorEl)}
                          onClose={() => this.handleMenuClose()}
                      >
                        <MenuItem
                            onClick={() => {
                              this.handleMenuClose();
                              this.deleteAllItems(messagesContext.messages);
                            }}>
                          Delete all Messages
                        </MenuItem>
                      </Menu>
                      </div>
                      <Grid container
                            spacing={5}
                            direction="column">
                        {messagesContext.messages.map(
                            (message, messageIndex) => {
                              return (
                                  <Message message={message} key={messageIndex}/>
                              );
                            })}
                      </Grid>
                    </React.Fragment>
                )
              }}
            </MessagesContext.Consumer>
        </React.Fragment>
    )
  }
}

export default Messages