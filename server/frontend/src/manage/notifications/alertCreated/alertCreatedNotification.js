import AlertCreatedNotificationEdit from "./alertCreatedNotificationEdit";
import {gql} from "apollo-boost";

const ADD_NOTIFICATION_SUBSCRIPTION = gql`
mutation addNotificationSubscriptionForAlertCreated($organisationId: ID!, $updateDelaysInSeconds: [Int]!, $subscriberContactUniqueIds : [String]!){
  addNotificationSubscriptionForAlertCreated(organisationId: $organisationId, updateDelaysInSeconds: $updateDelaysInSeconds, subscriberContactUniqueIds: $subscriberContactUniqueIds) {   
    uniqueId
  }
}
`;

const EDIT_NOTIFICATION_SUBSCRIPTION = gql`
mutation editNotificationSubscriptionForAlertCreated($organisationId: ID!, $uniqueSubscriptionId: String!, $updateDelaysInSeconds: [Int]!, $subscriberContactUniqueIds : [String]!){
  editNotificationSubscriptionForAlertCreated(organisationId: $organisationId, uniqueSubscriptionId: $uniqueSubscriptionId, updateDelaysInSeconds: $updateDelaysInSeconds, subscriberContactUniqueIds: $subscriberContactUniqueIds) {   
    uniqueId
  }
}
`;

class AlertCreatedNotification  {

  getTypeName() {
    return "AlertCreatedNotificationConfig";
  }

  getTitle() {
    return "Alarmbenachrichtigung";
  }

  getMenuName() {
    return "Benachrichtigung bei eingehenden Alarm";
  }

  getEditor(){
    return AlertCreatedNotificationEdit;
  }

  getGQLToAddNewNotificationSubscriptionWithDefaults(organisationId) {
    return {
      gqlQuery: ADD_NOTIFICATION_SUBSCRIPTION,
      variables: {
        organisationId: organisationId,
        updateDelaysInSeconds: [10, 30, 60, 120, 240],
        subscriberContactUniqueIds:[]
      }
    }
  }

  getGQLToSaveNotificationSubscription() {
    return {
      gqlQuery: EDIT_NOTIFICATION_SUBSCRIPTION
    }
  }
}

export default AlertCreatedNotification;