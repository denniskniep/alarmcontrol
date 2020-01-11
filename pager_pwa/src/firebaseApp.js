import firebase from "firebase";
import {loadFirebaseConfig} from "./config/config";

const firebaseConfig = loadFirebaseConfig();
const firebaseApp = firebaseConfig ? initFirebaseApp(firebaseConfig) : null;

export function initFirebaseApp(config) {
  //By default, when you start Firebase, it looks for a file called firebase-messaging-sw.js and starts that service worker.
  let app = firebase.initializeApp(config);
  console.log("Firebase App initialized!");
  return app;
}

export default firebaseApp;