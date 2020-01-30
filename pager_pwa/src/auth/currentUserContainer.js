import React, {Component} from 'react';
import {CurrentUserContext} from "./currentUserContext";
import firebaseApp from "../firebaseApp";
import firebase from "firebase";

class CurrentUserContainer extends Component {

  constructor(props) {
    super(props);

    firebaseApp.auth().onAuthStateChanged(
        (user) => this.onAuthStateChanged(user));

    if("Notification" in window){
      let requestPermissionPromise = Notification.requestPermission()
      if(requestPermissionPromise){
        requestPermissionPromise.then(
            (permission) => this.onNotificationPermissionChanged(permission));
      }
    }


    document.addEventListener('onPushMessageTokenChanged',
        (e) => this.onTokenChanged(e));

    this.state = {
      user: null,
      pendingUserAuth: true,
      subscribed: false,
      token: "",
      allSupportedAndGranted : this.isAllSupported() && Notification.permission === "granted"
    }
  }

  isAllSupported(){
    return firebase.messaging.isSupported() &&
    'serviceWorker' in navigator &&
    "Notification" in window
  }

  onTokenChanged(e){
    let token = "";
    if(e.detail && e.detail.token){
      token = e.detail.token;
    }
    this.setState((state, props) => {
      return {
        subscribed: !!token,
        token: token
      }
    });
  }

  onNotificationPermissionChanged(permission){
    this.setState((state, props) => {
      return {
        allSupportedAndGranted : this.isAllSupported() && permission === "granted"
      }
    });
  }

  onAuthStateChanged(user) {
    console.log("AuthStateChanged:", user);
    this.setState((state, props) => {
      return {
        user: user,
        pendingUserAuth: false
      }
    });
  }

  render() {
    return (
        <CurrentUserContext.Provider value={{
          user: this.state.user,
          pendingUserAuth: this.state.pendingUserAuth,
          subscribed: this.state.subscribed,
          token: this.state.token,
          allSupportedAndGranted: this.state.allSupportedAndGranted
        }}>
          {this.props.children}
        </CurrentUserContext.Provider>
    )
  }
}

export default CurrentUserContainer