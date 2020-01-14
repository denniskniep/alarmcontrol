import React, {Component} from 'react';
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";
import {getAndSaveToken} from './notifications/pushNotificationSubscription';
import {CurrentUserContext} from "./auth/currentUserContext";
import TextField from "@material-ui/core/TextField";
import InputAdornment from "@material-ui/core/InputAdornment";
import FileCopyIcon from '@material-ui/icons/FileCopy';
import IconButton from "@material-ui/core/IconButton";

class Subscription extends Component {

  constructor(props) {
    super(props);
  }

  subscribe(userContext) {
    getAndSaveToken(true).then((token) => {
          this.copyToClipboard(token);
        }
    );
  }

  copyToClipboard(text) {
    const el = document.createElement('textarea');
    el.value = text;
    document.body.appendChild(el);
    el.select();
    document.execCommand('copy');
    document.body.removeChild(el);
  }

  render() {
    return (
        <Grid container
              spacing={5}
              direction="column"
              justify="center"
              alignItems="center"
        >

          <h1>Subscribtion</h1>

          <CurrentUserContext.Consumer>
            {userContext => {
              return (
                  <React.Fragment>
                    {
                      userContext.user &&
                      <React.Fragment>
                        <Grid item>
                          <TextField
                              id="input-with-icon-textfield"
                              readOnly={true}
                              label="Token"
                              value={userContext.token}
                              InputProps={{
                                endAdornment: (
                                    <InputAdornment position="start">
                                      <IconButton onClick={() => this.copyToClipboard(userContext.token)}>
                                        <FileCopyIcon/>
                                      </IconButton>
                                    </InputAdornment>
                                ),
                              }}
                          />
                        </Grid>
                        <Grid item>
                          <Button variant="contained" color="primary"
                                  onClick={() => this.subscribe(userContext)}>
                            {
                              !userContext.subscribed &&
                              <p>Subscribe</p>
                            }

                            {
                              userContext.subscribed &&
                              <p>Resubscribe</p>
                            }
                          </Button>
                        </Grid>
                      </React.Fragment>
                    }

                  </React.Fragment>
              );
            }}
          </CurrentUserContext.Consumer>
        </Grid>
    )
  }
}

export default Subscription