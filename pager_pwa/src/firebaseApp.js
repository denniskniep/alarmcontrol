import {loadFirebaseConfig} from "./config/config";
import {initFirebaseApp} from "./firebaseAppInit";

const firebaseConfig = loadFirebaseConfig();
const firebaseApp = firebaseConfig ? initFirebaseApp(firebaseConfig) : null;

export default firebaseApp;