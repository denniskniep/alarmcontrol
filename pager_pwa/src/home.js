import React, {Component} from 'react';
import firebase from "firebase";
import Grid from "@material-ui/core/Grid";
import CheckCircleOutlineIcon from '@material-ui/icons/CheckCircleOutline';
import BlockIcon from '@material-ui/icons/Block';
import {CurrentUserContext} from "./auth/currentUserContext";

class Home extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    let pushMessagingSupported = firebase.messaging.isSupported();
    let serviceWorkerSupported = 'serviceWorker' in navigator;
    let notificationSupported = "Notification" in window;
    let notificationPermissionGranted = notificationSupported && Notification.permission === "granted";
    let allSupportedAndGranted = pushMessagingSupported &&
        serviceWorkerSupported &&
        notificationSupported &&
        notificationPermissionGranted;

    return (
        <React.Fragment>
          <h1>Welcome</h1>
            <CurrentUserContext.Consumer>
              {userContext => {
                return (
                    <React.Fragment>
                      {
                        allSupportedAndGranted && userContext.user  &&
                        <p>The AC Pager WebApp should work as expected. All APIs
                          are
                          supported by your Browser, all permissions are
                          allowed and all other requirements are fulfilled.</p>
                      }

                      {
                        (!allSupportedAndGranted || !userContext.user ) &&
                        <p>Some APIs are not supported by your Browser,
                          some
                          permissions are denied or you do not fulfill other requirements! The AC Pager WebApp can not
                          work
                          properly.</p>
                      }

                      <Grid container spacing={2}>
                        <Grid item xs={12}>

                          {userContext.user &&
                          <CheckCircleOutlineIcon style={{color: "green"}}/>
                          }

                          {!userContext.user &&
                          <BlockIcon style={{color: "red"}}/>
                          }
                          <span className={"supported-feature-icon-text"}>Eingeloggt als User{userContext.user && ": " + userContext.user.email}</span>

                        </Grid>

                        <Grid item xs={12}>

                          {serviceWorkerSupported &&
                          <CheckCircleOutlineIcon style={{color: "green"}}/>
                          }

                          {!serviceWorkerSupported &&
                          <BlockIcon style={{color: "red"}}/>
                          }
                          <span className={"supported-feature-icon-text"}>Service Worker</span>

                        </Grid>

                        <Grid item xs={12}>

                          {pushMessagingSupported &&
                          <CheckCircleOutlineIcon style={{color: "green"}}/>
                          }

                          {!pushMessagingSupported &&
                          <BlockIcon style={{color: "red"}}/>
                          }
                          <span
                              className={"supported-feature-icon-text"}>Push API</span>

                        </Grid>

                        <Grid item xs={12}>

                          {notificationSupported &&
                          <CheckCircleOutlineIcon style={{color: "green"}}/>
                          }

                          {!notificationSupported &&
                          <BlockIcon style={{color: "red"}}/>
                          }
                          <span className={"supported-feature-icon-text"}>Notification API</span>

                        </Grid>


                        <Grid item xs={12}>

                          {notificationPermissionGranted &&
                          <CheckCircleOutlineIcon style={{color: "green"}}/>
                          }

                          {!notificationPermissionGranted &&
                          <BlockIcon style={{color: "red"}}/>
                          }
                          <span className={"supported-feature-icon-text"}>Notifications allowed</span>

                        </Grid>

                        <Grid item xs={12}>

                          {userContext.subscribed &&
                          <CheckCircleOutlineIcon style={{color: "green"}}/>
                          }

                          {!userContext.subscribed &&
                          <BlockIcon style={{color: "red"}}/>
                          }
                          <span className={"supported-feature-icon-text"}>Subscribed to messages</span>

                        </Grid>

                      </Grid>
                    </React.Fragment>
                )
              }}
            </CurrentUserContext.Consumer>
        </React.Fragment>
    )
  }
}

export default Home