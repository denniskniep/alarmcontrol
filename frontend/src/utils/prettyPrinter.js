import moment from "moment";

export function prettifyDateTimeLong(dateTime){
  return moment.utc(dateTime).local().format("DD.MM.YYYY HH:mm:ss")
}

export function prettifyDateLong(dateTime){
  return moment.utc(dateTime).local().format("DD.MM.YYYY")
}

export function prettifyDateTimeShort(dateTime){
  return moment.utc(dateTime).local().format("DD.MM.YYYY HH:mm")
}
