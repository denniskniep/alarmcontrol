import React from 'react';
import ReactDOM from 'react-dom';
import 'bootstrap/dist/css/bootstrap.css';
import './styles.css';
import App from './app';

// Import fontawesome icons
import { library } from '@fortawesome/fontawesome-svg-core'
import { fab } from '@fortawesome/free-brands-svg-icons'
import {
  faCheckCircle as fasCheckCircle,
  faTimes as fasTimes
}  from '@fortawesome/free-solid-svg-icons'
import {
  faCheckCircle as farCheckCircle,
  faClock as farClock,
  faEye as farEye,
  faEdit as farEdit,
  faTrashAlt as farTrashAlt,
  faPlusSquare as farPlusSquare,
  faSave as farSave
} from '@fortawesome/free-regular-svg-icons'

library.add(fab, fasCheckCircle, farCheckCircle, farClock, farEdit, farEye, farTrashAlt, farPlusSquare, farSave, fasTimes)

ReactDOM.render(
    <App/>,
    document.getElementById('app')
);