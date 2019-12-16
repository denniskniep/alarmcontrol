import firebaseApp from '../firebaseApp';
import firebase from "firebase";
import {saveMessage, getMessages} from "./messageStore";
import moment from "moment";

const OBJECT_TYPE_ID = "firebasePushMessage";

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

export const registerServiceWorkerMessageHandler = (self) => {
  if (!firebase.messaging.isSupported()) {
    console.log("Firebase Messaging is not supported in this Browser!");
    return;
  }

  const messaging = firebaseApp.messaging();
  messaging.setBackgroundMessageHandler((payload) =>
      onServiceWorkerMessage(self, payload));

  self.addEventListener('notificationclick', (event) =>
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

const onServiceWorkerMessage = (self, payload) => {
  let message = onMessage("ServiceWorker", payload, {},
      (title, options) => self.registration.showNotification(title, options));

  // Send Data from ServiceWorker to all Client! So it can be added to localstorage (not possible from service worker)
  self.clients.matchAll().then(all => all.forEach(client => {
    console.log("Sent data from ServiceWorker to Foreground ", message)
    client.postMessage(message);
  }));
};

const onServiceWorkerNotificationClick = (event) => {
  console.log("onServiceWorkerNotificationClick", event);
  if (!event.action || event.action == 'open_message') {
    event.notification.close();

    // This checks if the current is already open and
    // focuses if it is
    event.waitUntil(self.clients.matchAll({
      type: "window"
    }).then(function (clientList) {
      for (let i = 0; i < clientList.length; i++) {
        let client = clientList[i];
        if ('focus' in client) {
          client.focus().then(function (c) {
            // do something with your WindowClient once it has been focused
            c.navigate(
                "/#/messages?uuid=" + event.notification.data.message_uuid);
          });
          return;
        }
      }

      if (self.clients.openWindow) {
        return self.clients.openWindow(
            "/#/messages?uuid=" + event.notification.data.message_uuid);
      }
    }));
  }
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

const onMessage = (source, payload, additionalOptions, showNotification) => {
  // message received
  console.log('onMessage (received in ' + source + '):', payload);

  let alarmVibration = [
    500, 100, 500, 100, 500, 100,
    2000, 100, 2000, 100, 2000, 100,
    500, 100, 500, 100, 500, 100
  ];

  let infoVibration = [200, 100, 200];
  let message = payload.notification;
  message = message ? message : payload.data;

  if (message) {

    let vibrate = infoVibration;
    if (message.type == "ALERT") {
      vibrate = alarmVibration;
    }

    message.uuid = newUuid();
    let notificationTitle = message.title;
    let notificationOptions = {
      body: message.body,
      badge: "icon-96x96.png",
      icon: "icon-96x96.png",
      tag:  message.uuid,
      data: {message_uuid: message.uuid},
      actions: [{action: "open_message", title: "Read"}],
      requireInteraction: true,
      vibrate: vibrate,
      ...additionalOptions
    };

    showNotification(notificationTitle, notificationOptions);
    message.transport = payload.notification ? 'notification' : 'data';
    message.receivedAt = moment().toISOString(true);
    message.source = source;
    message.objectTypeId = OBJECT_TYPE_ID;
    return message;
  }
};

const newUuid = () => {
  return newUuidPart() + "_" + newUuidPart() + "_" + newUuidPart();
};

const newUuidPart = () => {
  return Math.random().toString(36).substr(2, 9);
};
