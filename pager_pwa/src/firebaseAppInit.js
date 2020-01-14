import firebase from "firebase";

export function initFirebaseApp(config) {
  //By default, when you start Firebase, it looks for a file called firebase-messaging-sw.js and starts that service worker.
  let app = firebase.initializeApp(config);
  console.log("Firebase App initialized!");
  return app;
}