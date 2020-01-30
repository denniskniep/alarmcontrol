import moment from "moment";

const MESSAGE_KEY = 'messages';

export const saveMessage = (message) => {
 saveMessageToStore(message);
};

export const removeMessage = (uuid) => {
  let messages = getMessagesFromStore();
  let allToRemove = messages.filter(m => m.uuid === uuid);
  for (const toRemove of allToRemove) {
    let index = messages.indexOf(toRemove);
    if (index > -1) {
      messages.splice(index, 1);
    }
  }

  saveMessages(messages);
};

const saveMessageToStore = (message) => {
  let messages = getMessagesFromStore();
  messages.push(message);
  saveMessages(messages);
};

const saveMessages = (messages) => {
  messages = messages.sort((a, b) => compareDesc(a,b))
  let messagesAsString = JSON.stringify(messages);
  localStorage.setItem(MESSAGE_KEY, messagesAsString);

  let event = new CustomEvent('onMessagesChanged');
  document.dispatchEvent(event);
};

export const getMessages = () => {
  let messages = getMessagesFromStore();
  return messages.filter(m => m.transport === 'data');
};

const getMessagesFromStore = () => {
  let messagesAsString = localStorage.getItem(MESSAGE_KEY);
  if(!messagesAsString){
    return [];
  }

  let messages = JSON.parse(messagesAsString);
  if(!messages){
    return [];
  }
  return messages;
};

const compareDesc = (a, b) => {
  let unixA = moment(a.sentAt).unix();
  let unixB = moment(b.sentAt).unix();

  if (unixA > unixB) {
    return -1;
  }
  if (unixA <unixB) {
    return 1;
  }
  return 0;
};

