import React, {Component} from 'react';
import {
  DropdownButton,
  Dropdown
} from "react-bootstrap";
import MutationHandler from "../../utils/mutationHandler";
import AlertCreatedNotification from "./alertCreated/alertCreatedNotification";
import NotificationSubscriptionEditMutation from "./notificationSubscriptionEditMutation";

const TYPES = [
  new AlertCreatedNotification()
]

class NotificationSubscriptionsEditMutation extends Component {

  render() {
    return (<React.Fragment>
          <div className={"float-left"}>
            <h2>Benachrichtigungen</h2>
          </div>
          <div className={"float-right"}>
            <DropdownButton id="dropdown-basic-button"
                            title="Dropdown button"
                            title={"Hinzufügen ..."}>
              {TYPES.map(
                  (type, index) => {

                    let gqlToAddNew = type.getGQLToAddNewNotificationSubscriptionWithDefaults(
                        this.props.organisationId);

                    return (
                        <MutationHandler key={type.getTypeName()}
                                         mutation={gqlToAddNew.gqlQuery}
                                         onCompleted={() => this.props.onSubscriptionChanged
                                             && this.props.onSubscriptionChanged()}>
                          {addNotificationSubscription => {
                            return (
                                <Dropdown.Item
                                    onClick={e => addNotificationSubscription(
                                        {variables: gqlToAddNew.variables})}>
                                  {type.getMenuName()}
                                </Dropdown.Item>
                            )
                          }}</MutationHandler>)
                  }
              )}
            </DropdownButton>
          </div>

          <div style={{clear: "both"}}>
            {this.props.subscriptions.map(
                (subscription, index) => {

                  let foundType = TYPES.find(t => t.getTypeName()
                      == subscription.notificationConfig.__typename);

                  let notificationEditor = React.createElement(
                      foundType.getEditor(), {
                        organisationId: this.props.organisationId
                      });

                  let gqlToSave = foundType.getGQLToSaveNotificationSubscription();

                  return (
                      <div className={"row-header"} key={subscription.uniqueId}>
                        <NotificationSubscriptionEditMutation
                            title={foundType.getTitle()}
                            organisationId={this.props.organisationId}
                            subscription={subscription}
                            contacts={this.props.contacts}
                            onSubscriptionChanged={() => {
                              this.props.onSubscriptionChanged
                              && this.props.onSubscriptionChanged()
                            }}
                            editNotificationSubscriptionGQLQuery={gqlToSave.gqlQuery}>

                          {notificationEditor}

                        </NotificationSubscriptionEditMutation>
                      </div>
                  )
                })}
          </div>
        </React.Fragment>
    )
  }
}

export default NotificationSubscriptionsEditMutation;