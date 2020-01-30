import {prettifyDateTimeLong} from "./prettyPrinter";

export function asTitle(alert) {
  let keyword = alert.keyword ? alert.keyword : "";
  let address = alert.addressInfo1 ? alert.addressInfo1 : "";
  let date = prettifyDateTimeLong(alert.utcDateTime);
  if (!alert.keyword && !alert.addressInfo1) {
    return date;
  }

  if (keyword && address) {
    address = " - " + address;
  }

  return keyword + address + " (" + date + ")";
}


