import {convertConfigToFirebaseConfig} from "./configConverter";

const CONFIG_KEY = 'config';

export function loadConfig(){
  if(!localStorage){
    console.error("trying to load config , but there is no local storage!")
    return null;
  }

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
  }else{
    console.error("Config invalid", config)
  }
}

export function loadFirebaseConfig() {
  let config = loadConfig();
  return convertConfigToFirebaseConfig(config);
}


