import React, {Component} from 'react';
import {
  Button,
  ButtonToolbar,
  Card,
  Col,
  Container,
  Form,
  Row
} from "react-bootstrap";
import EditableTable from "../../components/editableTable";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import TagEditor from "../../components/tagEditor";

class SubscriberEdit extends Component {

  getTypeDisplay(typename) {
    if (typename == "MailContact") {
      return "E-Mail"
    }

    if (typename == "FirebaseMessageContact") {
      return "Firebase Push"
    }

    return "nA";
  }

  wrapToViewModel(contact){
    return {
      contact: contact,
      id: contact.uniqueId,
      name: contact.name + " (" + this.getTypeDisplay(contact.__typename)
          + ")"
    };
  }

  render() {
    let contactsForSuggestion = this
    .props
    .contacts
    .map(contact => this.wrapToViewModel(contact));

    let receiver = this
    .props
    .receiver
    .map(uniqueId => contactsForSuggestion.find(c => c.id == uniqueId))
    .filter(r => r);

    return (
        <React.Fragment>

          <TagEditor suggestions={contactsForSuggestion}
                     value={receiver}
                     placeholder={"Kontakt eingeben..."}
                     onChange={receiverVms => {
                       let receiver = receiverVms.map(contactViewModel => contactViewModel.id);

                       this.props.onChange &&
                       this.props.onChange(receiver)
                     }}
          />
        </React.Fragment>
    );
  }
}

export default SubscriberEdit;