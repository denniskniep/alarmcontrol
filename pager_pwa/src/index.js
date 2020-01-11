import React from 'react';
import ReactDOM from 'react-dom';
import App from './app';
import {registerForegroundMessageHandler} from './notifications/messageHandler'
import {startRegisterServiceWorker} from './serviceWorkerRegistrator'
import './styles.css';
import {initializeMessageSubscription} from "./notifications/pushNotificationSubscription";
import firebase from "firebase";
import {firebaseConfig, loadConfig} from "./config/config";
import firebaseApp from './firebaseApp';

let config = loadConfig();
if(config){
  startRegisterServiceWorker(config);
  registerForegroundMessageHandler();
  initializeMessageSubscription();
}


ReactDOM.render(
    <App/>,
    document.getElementById('app')
);

//Custom Banner für App Allow Notifications & Zum Startbildschirm hinzufügen

// If the app is closed. The Read click on the notification opens the browser not the PWA

// If you click on the notification directly (not the button) nothing happens

// only one (the last) raised notification exists/showed when executing directly

// User icon oben rechts
// Subscribed State oben rechts!

// Initialer Flow -> Login, dann getToken

// Settings Vibr. Muster / Audio Ton


//ToDo: Forgot Password & Verify Email

//ToDo: What happens on token refreh
// Code of Service Worker is not updated!
// Display version to make sure updates are served (even in the webworker...)
// Try to install on website load

// Customize vibration pattern - (Depending on Notification Type!
// Usr other icon for info messages (blue)

// Exchange subscriber id
// Scan barcode - encrypt - send to public service - 

//add "sound": "<URL String>" to notification

//probably add actions so the user can react

// chrome must be excluded from battery saving options
// first send notification then data

//ToDo: Wenn bereits eingeloggt dann login route redirecten
//ToDo: LoadingIcon oben rechts wenn geladen wird ob authentifiziert oder nicht
// if(userContext.pendingUserAuth) {
//    return (<CircularProgress />);
// }

//HomeScreen mit Erklärungen? -> Progressive Web App -> Mit Token subscriben?
//Noch Subscription storage API aufrufen

// Alle empfangenen nachrichten in APP sammeln!

// curl 'https://uclipboard.com/api/clip.new?path=G48Y&text=helloworld&apikey=3589a1ce8b3f5977'
// curl 'https://uclipboard.com/api/clip.get?path=G48Y&apikey=e90527dad5d1236e'

