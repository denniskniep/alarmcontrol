import React from 'react';
import ReactDOM from 'react-dom';
import App from './app';
import {registerForegroundMessageHandler} from './notifications/messageHandlerForeground'
import {startRegisterServiceWorker} from './serviceWorkerRegistrator'
import './styles.css';
import {initializeMessageSubscription} from "./notifications/pushNotificationSubscription";
import {saveConfig, loadConfig} from "./config/config";

const configAsString = new URL(location).searchParams.get('config');
if(configAsString){
  console.log("Set config via URL Parameter", configAsString)
  let config = JSON.parse(configAsString);
  saveConfig(config);
  const urlWithoutConfig = new URL(location);
  urlWithoutConfig.search = "";
  console.log("Reload App without config params", urlWithoutConfig.toString())
  window.location.href = urlWithoutConfig.toString()

}else{
  console.log("No configs in URL Parameter found");

  let config = loadConfig();
  if(config){
    startRegisterServiceWorker(config);
    registerForegroundMessageHandler();
    initializeMessageSubscription();
  }

  ReactDOM.render(
      <App/>,
      document.getElementById('app')
  );
}