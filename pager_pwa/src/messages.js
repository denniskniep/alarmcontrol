import React, {Component} from 'react';
import Grid from "@material-ui/core/Grid";
import MessagesContainer from "./notifications/messagesContainer";
import {MessagesContext} from "./notifications/messagesContext";
import CardHeader from "@material-ui/core/CardHeader";
import Card from "@material-ui/core/Card";
import Avatar from "@material-ui/core/Avatar";
import IconButton from "@material-ui/core/IconButton";
import CardMedia from "@material-ui/core/CardMedia";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import MoreVertIcon from '@material-ui/icons/MoreVert';
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import moment from "moment";
import CardActions from "@material-ui/core/CardActions";
import Collapse from "@material-ui/core/Collapse";
import Message from "./message";

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

  deleteItem(message) {
    return false;
  }

  renderDate(message){
    let sentAt = moment(message.sentAt);
    let receivedAt = moment(message.receivedAt);
    let formattedSentAt = sentAt.format('DD.MM.YYYY HH:mm:ss');
    let delay = moment.duration(receivedAt.diff(sentAt));
    return formattedSentAt + " (Received within a delay of " + delay.humanize() +")";
  }

  render() {
    return (
        <React.Fragment>
          <MessagesContainer>
            <MessagesContext.Consumer>
              {messagesContext => {
                return (
                    <React.Fragment>
                      <h1>Messages</h1>
                      <Grid container
                            spacing={5}
                            direction="column">
                        {messagesContext.messages.map(
                            (message, messageIndex) => {
                              return (
                                  <Message message={message} key={moment(message.sentAt).unix()}/>
                              );
                            })}
                      </Grid>
                    </React.Fragment>
                )
              }}
            </MessagesContext.Consumer>
          </MessagesContainer>
        </React.Fragment>
    )
  }
}

export default Messages