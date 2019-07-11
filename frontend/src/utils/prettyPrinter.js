import moment from "moment";

class PrettyPrinter {

  prettifyDateTimeLong(dateTime){
    return moment.utc(dateTime).local().format("DD.MM.YYYY HH:mm:ss")
  }

  prettifyDateLong(dateTime){
    return moment.utc(alert.dateTime).local().format("DD.MM.YYYY")
  }

}

export default PrettyPrinter;