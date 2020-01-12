import firebase from "firebase";
import {initFirebaseApp} from "../firebaseAppInit";
import {onMessage, onServiceWorkerNotificationClick} from "./messageHandler";

export const registerServiceWorkerMessageHandler = (self, config) => {
  if (!firebase.messaging.isSupported()) {
    console.log("Firebase Messaging is not supported in this Browser!");
    return;
  }
  let firebaseApp = initFirebaseApp(config);
  const messaging = firebaseApp.messaging();
  messaging.setBackgroundMessageHandler((payload) =>
      onServiceWorkerMessage(self, payload));

  self.addEventListener('notificationclick', (event) =>
      onServiceWorkerNotificationClick(event), false);

  console.log("Successfully registered ServiceWorkerMessageHandler!");
};

const onServiceWorkerMessage = (self, payload) => {
  let message = onMessage("ServiceWorker", payload, {},
      (title, options) => self.registration.showNotification(title, options));

  // Send Data from ServiceWorker to all Client! So it can be added to localstorage (not possible from service worker)
  self.clients.matchAll().then(all => all.forEach(client => {
    console.log("Sent data from ServiceWorker to Foreground ", message)
    client.postMessage(message);
  }));
};

