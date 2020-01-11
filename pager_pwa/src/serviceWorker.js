import {registerServiceWorkerMessageHandler} from './notifications/messageHandler'
import {convertConfigToFirebaseConfig} from "./config/config";


const configAsString = new URL(self.location).searchParams.get('config');
console.log("Transferred Config to ServiceWorker", configAsString)
const config = JSON.parse(configAsString);
const firebaseConfig = convertConfigToFirebaseConfig(config);
registerServiceWorkerMessageHandler(self, firebaseConfig);