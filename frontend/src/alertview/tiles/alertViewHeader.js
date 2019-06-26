import React, {Component} from 'react';
import {Badge, Col, Container, Row} from "react-bootstrap";
import moment from "moment";
import AlertViewBox from "../alertViewBox";
import PrettyPrinter from "../../utils/prettyPrinter";

class AlertViewHeader extends Component {

  constructor(props) {
    super(props);
    this.state = {
      currentUTC: moment.utc()
    };
  }

  componentDidMount() {
    this.interval = setInterval(() => this.setState({currentUTC: moment.utc()}),
        1000);
  }

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  prettifyDistanceAndDuration(alert) {
    let distanceInKm = Number(alert.distance / 1000).toFixed(1);
    let durationInMin = Number(alert.duration / 1000 / 60).toFixed(0);
    return distanceInKm + " km (" + durationInMin + " min)"
  }

  prettifyDate(alert) {
    return new PrettyPrinter().prettifyDateTimeLong(alert.dateTime);
  }

  prettifyDuration(alert, currentUTC) {
    let duration = moment.duration(
        currentUTC.diff(moment.utc(this.props.alert.dateTime))).locale('de');

    if (duration.asHours() < 72) {

      let hours = duration.hours() + "";
      let minutes = duration.minutes() + "";
      let seconds = duration.seconds() + "";

      return `${hours.padStart(2, '0')}:${minutes.padStart(2,
          '0')}:${seconds.padStart(2, '0')}`;
    }

    return new PrettyPrinter().prettifyDateLong(alert.dateTime);
  }

  render() {
    return (
        <AlertViewBox>
          <Container fluid="true">
            <Row>
              <Col xs={3}>
                <h1>{this.props.alert.keyword}</h1>
              </Col>
              <Col xs={6}>
                <h1>{this.props.alert.addressInfo1}</h1>
              </Col>
              <Col xs={3}>
                <h1>{this.prettifyDuration(this.props.alert,
                    this.state.currentUTC)}</h1>
              </Col>
            </Row>
            <Row>
              <Col xs={3}>
                <span>{this.props.alert.description}</span>
              </Col>
              <Col xs={6}>
                <span>{this.props.alert.addressInfo2}</span>
              </Col>
              <Col xs={3}>

                <span>{this.prettifyDate(this.props.alert)}</span>
              </Col>
            </Row>
            <Row>
              <Col xs={3}>
             <span>
               <Badge className={"badgeSpace"} pill variant="primary">
                  LZ2
               </Badge>
               <Badge className={"badgeSpace"} pill variant="primary">
                  ELW
               </Badge>
             </span>
              </Col>
              <Col xs={6}>
                <span>{this.prettifyDistanceAndDuration(
                    this.props.alert)}</span>
              </Col>
              <Col xs={3}>
                <span>
                  {
                    this.props.alert.alertCalls.map((ac, index) => {
                      return (
                          <Badge key={ac.id} className={"badgeSpace"}
                                 pill variant="info">
                            {ac.alertNumber.shortDescription}
                          </Badge>
                      )
                    })
                  }
                </span>
              </Col>
            </Row>
            <Row>
              <Col xs={3}>
                <span></span>
              </Col>
              <Col xs={6}>
                <span></span>
              </Col>
              <Col xs={3}>
                <span></span>
              </Col>
            </Row>
          </Container>
        </AlertViewBox>);
  }
}

export default AlertViewHeader