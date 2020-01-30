import {registerServiceWorkerMessageHandler} from './notifications/messageHandlerServiceWorker'
import {convertConfigToFirebaseConfig} from "./config/configConverter";


const configAsString = new URL(self.location).searchParams.get('config');
console.log("Transferred Config to ServiceWorker", configAsString)
const config = JSON.parse(configAsString);
const firebaseConfig = convertConfigToFirebaseConfig(config);
registerServiceWorkerMessageHandler(self, firebaseConfig);