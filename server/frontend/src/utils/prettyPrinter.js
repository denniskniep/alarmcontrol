import moment from "moment";

export function prettifyDateTimeLong(utcDateTime){
  return moment.utc(utcDateTime).local().format("DD.MM.YYYY HH:mm:ss")
}

export function prettifyDateLong(utcDateTime){
  return moment.utc(utcDateTime).local().format("DD.MM.YYYY")
}

export function prettifyDateTimeShort(utcDateTime){
  return moment.utc(utcDateTime).local().format("DD.MM.YYYY HH:mm")
}
