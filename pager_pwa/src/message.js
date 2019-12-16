import React, {Component} from 'react';
import Grid from "@material-ui/core/Grid";
import CardHeader from "@material-ui/core/CardHeader";
import Card from "@material-ui/core/Card";
import Avatar from "@material-ui/core/Avatar";
import IconButton from "@material-ui/core/IconButton";
import CardMedia from "@material-ui/core/CardMedia";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import MoreVertIcon from '@material-ui/icons/MoreVert';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import moment from "moment";
import CardActions from "@material-ui/core/CardActions";
import Collapse from "@material-ui/core/Collapse";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import {removeMessage} from "./notifications/messageStore";

class Message extends Component {

  constructor(props) {
    super(props);

    this.state = {
      anchorEl: null,
      expanded: false
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

  handleExpandClick() {
    this.setState((state, props) => {
      return {
        expanded: !state.expanded
      }
    });
  }

  deleteItem(message) {
    removeMessage(message.uuid);
  }

  renderSentAt(message) {
    let sentAt = moment(message.sentAt);
    return sentAt.format('DD.MM.YYYY HH:mm:ss');
  }

  renderReceivedAt(message) {
    let receivedAt = moment(message.receivedAt);
    return receivedAt.format('DD.MM.YYYY HH:mm:ss');
  }

  renderDelay(message) {
    let sentAt = moment(message.sentAt);
    let receivedAt = moment(message.receivedAt);
    let duration = moment.duration(receivedAt.diff(sentAt));

    let hours = duration.hours() + "";
    let minutes = duration.minutes() + "";
    let seconds = duration.seconds() + "";
    let milliseconds = duration.milliseconds() + "";

    return `${hours.padStart(2, '0')}:${minutes.padStart(2,
        '0')}:${seconds.padStart(2, '0')}.${milliseconds.padStart(3, '0')}`;
  }

  render() {

    let avatar = <Avatar aria-label="recipe"
                         style={{backgroundColor: "#009fff80"}}>I</Avatar>
    if (this.props.message.type == "ALERT") {
      avatar = <Avatar aria-label="recipe"
                       style={{backgroundColor: "#ff000080"}}>A</Avatar>
    }

    return (
        <Grid item>
          <Card>
            <CardHeader
                avatar={avatar}
                action={
                  <IconButton aria-label="settings"
                              onClick={(e) => this.handleMenuOpen(
                                  e)}>
                    <MoreVertIcon/>
                  </IconButton>
                }
                title={this.props.message.title}
                subheader={
                  <span>{this.renderSentAt(this.props.message)}</span>
                }

            />
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
                    this.deleteItem(this.props.message);
                  }}>
                Delete Message
              </MenuItem>
            </Menu>

            <CardContent>
              <Typography variant="body2"
                          color="textSecondary"
                          component="p">
                {
                  this.props.message.body.split("\n").map(function(line, idx)
                  {
                    return (
                        <span key={idx}>
                          {line}
                          <br/>
                        </span>
                      )
                  })
                }
              </Typography>
            </CardContent>
            <CardActions disableSpacing>
              <IconButton
                  style={{marginLeft: 'auto'}}
                  onClick={() => this.handleExpandClick()}
              >
                <ExpandMoreIcon/>
              </IconButton>
            </CardActions>

            <Collapse in={this.state.expanded} timeout="auto" unmountOnExit>
              <CardContent>
                <Typography paragraph>Details:</Typography>
                <Table aria-label="simple table">
                  <TableBody>
                    <TableRow>
                      <TableCell>Sent At</TableCell>
                      <TableCell>{this.renderSentAt(
                          this.props.message)}</TableCell>
                    </TableRow>

                    <TableRow>
                      <TableCell>Received At</TableCell>
                      <TableCell>{this.renderReceivedAt(
                          this.props.message)}</TableCell>
                    </TableRow>

                    <TableRow>
                      <TableCell>Delay</TableCell>
                      <TableCell>{this.renderDelay(
                          this.props.message)}</TableCell>
                    </TableRow>

                    <TableRow>
                      <TableCell>Type</TableCell>
                      <TableCell>{this.props.message.type}</TableCell>
                    </TableRow>

                    <TableRow>
                      <TableCell>Source</TableCell>
                      <TableCell>{this.props.message.source}</TableCell>
                    </TableRow>

                  </TableBody>
                </Table>

              </CardContent>
            </Collapse>

          </Card>
        </Grid>
    )
  }
}

export default Message