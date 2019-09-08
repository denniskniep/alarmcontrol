import PrettyPrinter from "./prettyPrinter";

class Alert {
  constructor(alert) {
    this.alert = alert;
  }

  asTitle(alert) {
    let keyword = alert.keyword ? alert.keyword : "";
    let address = alert.addressInfo1 ? alert.addressInfo1 : "";
    let date = new PrettyPrinter().prettifyDateTimeLong(alert.dateTime);
    if (!alert.keyword && !alert.addressInfo1) {
      return date;
    }

    if (keyword && address) {
      address = " - " + address;
    }

    return keyword + address + " (" + date + ")";
  }
}

export default new Alert();