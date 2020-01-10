
export const firebaseConfigParams = {
  apiKey: "AIzaSyC3CjgV1slexBAYh9C47eUS8-VVRBPY6z4",
  projectId: "alarmcontrol-ffmeimbressen",
  messagingSenderId: "280424824514",
  appId: "1:280424824514:web:71f9c6481da1bd2697855b"
};

export const firebaseConfig = {
  apiKey: firebaseConfigParams.apiKey,
  authDomain: firebaseConfigParams.projectId + ".firebaseapp.com",
  databaseURL: "https://" + firebaseConfigParams.projectId  +".firebaseio.com",
  projectId: firebaseConfigParams.projectId,
  storageBucket: firebaseConfigParams.projectId + ".appspot.com",
  messagingSenderId: firebaseConfigParams.messagingSenderId,
  appId: firebaseConfigParams.appId
};

//let getUrl = window.location;
//let myUrl = getUrl.protocol + "//" + getUrl.host + ":"  + getUrl.port;

export const config = {
  baseUrl: ""
};