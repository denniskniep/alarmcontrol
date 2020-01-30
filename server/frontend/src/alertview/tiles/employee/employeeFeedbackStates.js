const NO_RESPONSE = "NO_RESPONSE";
const CANCEL = "CANCEL";
const COMMIT = "COMMIT";
const LATER = "LATER";

class EmployeeFeedbackStates {

  static getNoResponse() {
    return NO_RESPONSE;
  }

  static getCancel() {
    return CANCEL;
  }

  static getCommit() {
    return COMMIT;
  }

  static getLater() {
    return LATER;
  }
}

export default EmployeeFeedbackStates