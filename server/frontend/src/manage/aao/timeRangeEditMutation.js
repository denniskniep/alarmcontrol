import React, {Component} from 'react';
import {gql} from "apollo-boost";
import MutationHandler from "../../utils/mutationHandler";
import EditableTable from "../../components/editableTable";
import TagViewer from "../../components/tagViewer";
import TagEditor from "../../components/tagEditor";
import ComboboxEditor from "../../components/comboboxEditor";
import TimeEditor from "../../components/timeEditor";
import TextFromReferenceViewer from "../../components/textFromReferenceViewer";

const DELETE_TIMERANGE = gql`
mutation deleteTimeRange($organisationId: ID!, $uniqueTimeRangeId: String!){
  deleteTimeRange(
    organisationId: $organisationId,  
    uniqueTimeRangeId : $uniqueTimeRangeId)
}
`;

const ADD_TIMERANGE = gql`
mutation addTimeRange($organisationId: ID!, $name : String!, $fromTimeHour : Int!, $fromTimeMinute : Int!, $toTimeHour : Int!, $toTimeMinute : Int!, $daysOfWeek : [DayOfWeek]!, $feiertagBehaviour : FeiertagBehaviour!){
  addTimeRange(organisationId: $organisationId, name: $name, fromTimeHour: $fromTimeHour, fromTimeMinute: $fromTimeMinute, toTimeHour: $toTimeHour, toTimeMinute: $toTimeMinute, daysOfWeek: $daysOfWeek, feiertagBehaviour: $feiertagBehaviour) {   
    uniqueId
  }
}
`;

const WEEK_DAYS = [{
  id: "MONDAY",
  name: "Montag",
  shortName: "Mo"
  },
  {
    id: "TUESDAY",
    name: "Dienstag",
    shortName: "Di"
  },
  {
    id: "WEDNESDAY",
    name: "Mittwoch",
    shortName: "Mi"
  },
  {
    id: "THURSDAY",
    name: "Donnerstag",
    shortName: "Do"
  },
  {
    id: "FRIDAY",
    name: "Freitag",
    shortName: "Fr"
  },
  {
    id: "SATURDAY",
    name: "Samstag",
    shortName: "Sa"
  },
  {
    id: "SUNDAY",
    name: "Sonntag",
    shortName: "So"
  }];

const FEIERTAG_BEHAVIOURS = [{
  id: "IGNORE",
  name: "Feiertag ignorieren"
  },
  {
    id: "MATCH",
    name: "oder Feiertag"
  },
  {
    id: "NO_MATCH",
    name: "und kein Feiertag"
  }];

class TimeRangeEditMutation extends Component {

  constructor(props) {
    super(props);
  }

  convert(timeRange){
    return {
      ...timeRange,
      from : this.formatTime(timeRange.fromTimeHour, timeRange.fromTimeMinute),
      to : this.formatTime(timeRange.toTimeHour, timeRange.toTimeMinute),
      days: timeRange.daysOfWeek.map(day => WEEK_DAYS.find(wd => day == wd.id))
    };
  }


  formatTime(hour, minute){
    let hourFormatted = hour + "";
    if(hourFormatted.length === 1){
      hourFormatted = hourFormatted.padStart(2, "0");
    }

    let minuteFormatted = minute + "";
    if(minuteFormatted.length === 1){
      minuteFormatted = minuteFormatted.padStart(2, "0");
    }

    return hourFormatted + ":" + minuteFormatted;
  }

  parseTime(time) {
    let splitted = time.split(":");
    if (splitted.length == 2) {
      let hour = splitted[0] * 1;
      let minute = splitted[1] * 1;
      if (splitted[0].length == 2 && splitted[1].length == 2
          && Number.isInteger(hour) && Number.isInteger(minute)) {
        return {
          hour,
          minute
        }
      }
    }
    return null;
  }

  parseFromTime(time) {
    let parsedTime = this.parseTime(time);
    if (parsedTime == null ||
        parsedTime.hour > 23 || parsedTime.minute > 59 ||
        parsedTime.hour < 0 || parsedTime.minute < 0) {
      return null;
    }
    return parsedTime;
  }

  parseToTime(time) {
    let parsedTime = this.parseTime(time);
    if (parsedTime == null) {
      return null;
    }

    if (parsedTime.hour == 24 && parsedTime.minute == 0) {
      return parsedTime;
    }

    if (parsedTime.hour > 23 || parsedTime.minute > 59 ||
        parsedTime.hour < 0 || parsedTime.minute
        < 0) {
      return null;
    }
    return parsedTime;
  }

  buildVariables(row){
    let fromTime = this.parseFromTime(row.from);
    let toTime = this.parseToTime(row.to);

    return {
      organisationId: this.props.organisationId,
      name: row.name,
      fromTimeHour: fromTime.hour,
      fromTimeMinute: fromTime.minute,
      toTimeHour: toTime.hour,
      toTimeMinute: toTime.minute,
      daysOfWeek: row.days.map(d => d.id),
      feiertagBehaviour: row.feiertagBehaviour
    };
  }

  render() {
    return (<MutationHandler mutation={DELETE_TIMERANGE}
                             onCompleted={() => this.props.onTimeRangeChanged
                                 && this.props.onTimeRangeChanged()}>
          {deleteTimeRange => {
            return (
                <MutationHandler mutation={ADD_TIMERANGE}
                                 onCompleted={() => this.props.onTimeRangeChanged
                                     && this.props.onTimeRangeChanged()}>
                  {addTimeRange => {

                    let timeRanges = this.props.timeRanges
                        ? this.props.timeRanges.map(tr => this.convert(tr))
                        : [];

                    console.log(timeRanges)
                    return (
                        <EditableTable data={timeRanges}
                                       canView={false}
                                       canEdit={false}
                                       columns={[
                                         {
                                           key: "name",
                                           name: "Name",
                                           editorProps: {
                                             className: "name-field-aao-time-rule"
                                           }
                                         },
                                         {
                                           key: "from",
                                           name: "Von",
                                           editor: TimeEditor,
                                           defaultValue: "00:00",
                                           valueValidator: (value) => this.parseFromTime(
                                               value) != null,
                                           viewerProps: {
                                             className: "time-field-aao"
                                           },
                                           editorProps: {
                                             className: "time-field-aao"
                                           }
                                         },
                                         {
                                           key: "to",
                                           name: "Bis",
                                           editor: TimeEditor,
                                           defaultValue: "24:00",
                                           valueValidator: (value) => this.parseToTime(
                                               value) != null,
                                           viewerProps: {
                                             className: "time-field-aao"
                                           },
                                           editorProps: {
                                             className: "time-field-aao"
                                           }
                                         },
                                         {
                                           key: "days",
                                           name: "Tage",
                                           viewer: TagViewer,
                                           editor: TagEditor,
                                           editorProps: {
                                             suggestions: WEEK_DAYS
                                           },
                                           defaultValue: []
                                         },
                                         {
                                           key: "feiertagBehaviour",
                                           name: "",
                                           viewer: TextFromReferenceViewer,
                                           viewerProps: {
                                             className: "feiertag-field-aao",
                                             items: FEIERTAG_BEHAVIOURS
                                           },
                                           editor: ComboboxEditor,
                                           defaultValue: "IGNORE",
                                           editorProps: {
                                             className: "feiertag-field-aao",
                                             items: FEIERTAG_BEHAVIOURS
                                           }
                                         }
                                       ]}

                                       onNewRow={newRow => {
                                         console.log('newRow', newRow);
                                         addTimeRange({
                                           variables: this.buildVariables(newRow)
                                         })
                                       }}

                                       onRowDeleted={(deletedRow) => {
                                         console.log('deleted', deletedRow);
                                         deleteTimeRange({
                                           variables: {
                                             organisationId: this.props.organisationId,
                                             uniqueTimeRangeId: deletedRow.uniqueId
                                           }
                                         })
                                       }}
                        />
                    )
                  }}
                </MutationHandler>
            )
          }}
        </MutationHandler>
    );
  }
}

export default TimeRangeEditMutation;