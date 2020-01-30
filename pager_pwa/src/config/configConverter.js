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