
const CONFIG_KEY = 'config';

export function loadConfig(){
  let config = localStorage.getItem(CONFIG_KEY);
  if (!config) {
    return null;
  }
  return JSON.parse(config);
}

export function saveConfig(config){
  if(config && config.apiKey && config.projectId && config.messagingSenderId && config.appId) {
    let newConfig = {
      apiKey: config.apiKey,
      projectId: config.projectId,
      messagingSenderId: config.messagingSenderId,
      appId: config.appId
    };

    localStorage.setItem(CONFIG_KEY, JSON.stringify(newConfig));
  }
}

export function loadFirebaseConfig() {
  let config = loadConfig();
  return convertConfigToFirebaseConfig(config);
}

export function convertConfigToFirebaseConfig(config) {
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
