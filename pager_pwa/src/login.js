import React, {Component} from 'react';
//other import than in docs due to https://github.com/parcel-bundler/parcel/issues/3387
import * as firebaseui from "firebaseui/dist/npm";
import firebaseApp from './firebaseApp';
import firebase from "firebase";
import 'firebaseui/dist/firebaseui.css';

const uiConfig = {
  signInSuccessUrl: '/',
  credentialHelper: firebaseui.auth.CredentialHelper.NONE,
  signInOptions: [
    firebase.auth.EmailAuthProvider.PROVIDER_ID
  ],

  // Terms of service url/callback.
  tosUrl:  '/tos',

  // Privacy policy url/callback.
  privacyPolicyUrl: '/policy'
};

// Promise that resolves unless the FirebaseUI instance is currently being deleted.
let firebaseUiDeletion = Promise.resolve();

class Login extends Component {

  constructor(props) {
    super(props);
    this.unregisterAuthObserver = () => {};
  }

  componentDidMount() {
    // Wait in case the firebase UI instance is currently being deleted.
    // This can happen if you unmount/remount the element quickly.
    return firebaseUiDeletion.then(() => {
      // Get or Create a firebaseUI instance.
      this.firebaseUiWidget = firebaseui.auth.AuthUI.getInstance()
          || new firebaseui.auth.AuthUI(firebaseApp.auth());

      if (uiConfig.signInFlow === 'popup') {
        this.firebaseUiWidget.reset();
      }

      // We track the auth state to reset firebaseUi if the user signs out.
      this.userSignedIn = false;
      this.unregisterAuthObserver = firebaseApp.auth().onAuthStateChanged(
          (user) => {
            if (!user && this.userSignedIn) {
              this.firebaseUiWidget.reset();
            }
            this.userSignedIn = !!user;
          });

      //if (this.firebaseUiWidget.isPendingRedirect()) {
        // The start method will wait until the DOM is loaded.
        this.firebaseUiWidget.start('#firebaseui-auth-container', uiConfig);
     // }
    });
  }

  componentWillUnmount() {
    firebaseUiDeletion = firebaseUiDeletion.then(() => {
      this.unregisterAuthObserver();
      return this.firebaseUiWidget.delete();
    });
    return firebaseUiDeletion;
  }

  render() {
    return (
        <div id={"firebaseui-auth-container"}>
        </div>)
  }
}

export default Login