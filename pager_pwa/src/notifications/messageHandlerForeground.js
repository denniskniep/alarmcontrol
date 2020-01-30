import firebaseApp from '../firebaseApp';
import firebase from "firebase";
import {saveMessage, getMessages} from "./messageStore";
import {
  OBJECT_TYPE_ID,
  onMessage,
  onServiceWorkerNotificationClick
} from "./messageHandler";


export const registerForegroundMessageHandler = () => {
  if (!firebase.messaging.isSupported()) {
    console.log("Firebase Messaging is not supported in this Browser!");
    return;
  }

  const messaging = firebaseApp.messaging();

  messaging.onMessage((payload) =>
      onForegroundMessage(payload));

  // Listen for sending Data from ServiceWorker to Client
  navigator.serviceWorker.addEventListener('message', (e) =>
      onServiceWorkerMessageToForeground(e));

  navigator.serviceWorker.addEventListener('notificationclick', (event) =>
      onServiceWorkerNotificationClick(event), false);
};

const onForegroundMessage = (payload) => {
  let message = onMessage("Foreground", payload, {},
      (title, options) => {
        navigator.serviceWorker.getRegistration().then(function (registration) {
          if (registration) {
            registration.showNotification(title, options);
          }
        });
      });
  saveMessage(message);
};

const onServiceWorkerMessageToForeground = (event) => {
  console.log("Received data from ServiceWorker", event)
  if (event && event.data && event.data.objectTypeId === OBJECT_TYPE_ID) {
    // Possible, that Service Worker calls many clients that persist to the same LocalStorage
    let existing = getMessages().filter(m => m.uuid == event.data.message_uuid);
    if(existing.length == 0){
      saveMessage(event.data);
    }
  }
};
