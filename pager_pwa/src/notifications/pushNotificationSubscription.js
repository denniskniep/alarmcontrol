import firebaseApp from '../firebaseApp';
import "core-js/stable";
import "regenerator-runtime/runtime";
import firebase from "firebase";

const TOKEN_KEY = 'localToken';

const saveTokenToDatabase = async (user, token, forceDatabaseRefresh) => {
  let db = firebaseApp.firestore();

  let localToken = localStorage.getItem(TOKEN_KEY);
  if (!forceDatabaseRefresh && localToken == token) {
    console.log(
        "Don't send it to database. Should be already there. \nToken: " + token
        + " is equal to locally stored token: " + localToken + "");
    return;
  }

  let userRef = db.collection('subscriptiontokens').doc(user.uid);

  // Add or Merge the user document
  await userRef.set({
    message_token: token,
    email: user.email
  }, {merge: true});
  console.log("Token saved to Database");

  localStorage.setItem(TOKEN_KEY, token);
  console.log("Token saved to Local Store");
};

export const getAndSaveToken = async (forceDatabaseRefresh) => {
  let messaging = firebaseApp.messaging();
  let currentUser = firebaseApp.auth().currentUser;
  if (!currentUser) {
    console.log(
        "Can not subscribe to notifications without authenticated user");
    return;
  }

  console.log('Start/Refresh Subscription...');
  let token = await messaging.getToken();

  console.log('Subscription created. Token:', token);
  await saveTokenToDatabase(currentUser, token, forceDatabaseRefresh);

  let event = new CustomEvent('onPushMessageTokenChanged', {
    detail: {token: token}
  });
  document.dispatchEvent(event);

  return token;
};

export const initializeMessageSubscription = async () => {
  if (!firebase.messaging.isSupported()) {
    console.log("Firebase Messaging is not supported in this Browser!");
    return;
  }

  const messaging = firebaseApp.messaging();
  await messaging.requestPermission();

  messaging.onTokenRefresh(() => onTokenRefresh());
  firebaseApp.auth().onAuthStateChanged((user) => onAuthStateChanged(user));

  let token = await getAndSaveToken(false);
  return token;
};

const onTokenRefresh = () => {
  getAndSaveToken().then((refreshedToken) => {
    console.log('Refreshed Token:', refreshedToken);
  }).catch((err) => {
    console.log('Unable to retrieve refreshed token ', err);
  });
};

const onAuthStateChanged = (user) => {
  getAndSaveToken().then((refreshedToken) => {
    console.log('Token:', refreshedToken);
  }).catch((err) => {
    console.log('Unable to retrieve token ', err);
  });
};




