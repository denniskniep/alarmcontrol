import moment from "moment";

export const OBJECT_TYPE_ID = "firebasePushMessage";

export const onServiceWorkerNotificationClick = (event) => {
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

export const onMessage = (source, payload, additionalOptions, showNotification) => {
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
