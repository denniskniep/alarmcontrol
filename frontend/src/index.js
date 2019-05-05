import React from 'react';
import ReactDOM from 'react-dom';
import 'bootstrap/dist/css/bootstrap.css';
import './styles.css';
import 'leaflet/dist/leaflet.css';
import App from './app';

// Import fontawesome icons
import { library } from '@fortawesome/fontawesome-svg-core'
import { fab } from '@fortawesome/free-brands-svg-icons'
import { faCheckCircle as fasCheckCircle}  from '@fortawesome/free-solid-svg-icons'
import { faCheckCircle as farCheckCircle } from '@fortawesome/free-regular-svg-icons'
library.add(fab, fasCheckCircle, farCheckCircle)

ReactDOM.render(
    <App/>,
    document.getElementById('app')
);