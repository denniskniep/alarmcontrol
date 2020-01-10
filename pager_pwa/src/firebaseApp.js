import firebase from "firebase";
import {firebaseConfig} from "./config/config";

const firebaseApp = initFirebaseApp();

function initFirebaseApp() {
  //By default, when you start Firebase, it looks for a file called firebase-messaging-sw.js and starts that service worker.
  let app = firebase.initializeApp(firebaseConfig);
  console.log("Firebase App initialized!");
  return app;
}

export default firebaseApp;