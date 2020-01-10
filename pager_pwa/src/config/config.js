
const CONFIG_KEY = 'config';

export function loadConfig(){
  let config = localStorage.getItem(CONFIG_KEY);
  if (!config) {
    return null;
  }
  return JSON.parse(config);
}

export function saveConfig(config){
  localStorage.setItem(CONFIG_KEY, JSON.stringify(config));
}

export function loadFirebaseConfig() {
  let config = loadConfig();
  if(!config || !config.apiKey || !config.projectId || !config.messagingSenderId || !config.appId ){
    return null;
  }

  return {
    apiKey: config.apiKey,
    authDomain: config.projectId + ".firebaseapp.com",
    databaseURL: "https://" + config.projectId  +".firebaseio.com",
    projectId: config.projectId,
    storageBucket: config.projectId + ".appspot.com",
    messagingSenderId: config.messagingSenderId,
    appId: config.appId
  };
}


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

