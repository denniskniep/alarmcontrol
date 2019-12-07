import React, {Component} from 'react';
import {Badge, Col, Container, Row} from "react-bootstrap";
import moment from "moment";
import AlertViewBox from "../alertViewBox";
import {
  prettifyDateLong,
  prettifyDateTimeLong
} from "../../utils/prettyPrinter";
import PropTypes from "prop-types";

class AlertViewHeader extends Component {

  constructor(props) {
    super(props);
    this.state = {
      currentUTC: moment.utc()
    };
  }

  componentDidMount() {
    this.interval = setInterval(() =>
        this.setState(() => {
          return {currentUTC: moment.utc()};
        }), 1000);
  }

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  prettifyDistanceAndDuration(alert) {
    if (alert.distance && alert.duration) {
      let distanceInKm = Number(alert.distance / 1000).toFixed(1);
      let durationInMin = Number(alert.duration / 1000 / 60).toFixed(0);
      return distanceInKm + " km (" + durationInMin + " min)"
    }
    return "";
  }

  prettifyDate(alert) {
    return prettifyDateTimeLong(alert.utcDateTime);
  }

  prettifyDuration(alert, currentUTC) {
    let duration = moment.duration(
        currentUTC.diff(moment.utc(this.props.alert.utcDateTime))).locale('de');

    if (duration.asHours() < 24) {

      let hours = duration.hours() + "";
      let minutes = duration.minutes() + "";
      let seconds = duration.seconds() + "";

      return `${hours.padStart(2, '0')}:${minutes.padStart(2,
          '0')}:${seconds.padStart(2, '0')}`;
    }

    return prettifyDateLong(alert.utcDateTime);
  }

  printAddress1Header(alert) {
    if (alert.addressInfo1) {
      return (<h1>{alert.addressInfo1}</h1>);
    }
    return alert.addressRaw;
  }

  printAddress2Header(alert) {
    if (alert.addressInfo2) {
      return alert.addressInfo2;
    }
    return "";
  }

  render() {
    return (
        <AlertViewBox>
          <Container fluid="true">
            <Row className={"align-items-center"}>
              <Col xs={3}>
                <h1 data-testid="keyword">{this.props.alert.keyword}</h1>
              </Col>
              <Col xs={6}>
                <span data-testid="address1Header">{this.printAddress1Header(
                    this.props.alert)}</span>
              </Col>
              <Col xs={3}>
                <h1 data-testid="elapsedTime">{this.prettifyDuration(
                    this.props.alert,
                    this.state.currentUTC)}</h1>
              </Col>
            </Row>
            <Row className={"align-items-center"}>
              <Col xs={3}>
                <span
                    data-testid="description">{this.props.alert.description}</span>
              </Col>
              <Col xs={6}>
                <span data-testid="address2Header">{this.printAddress2Header(
                    this.props.alert)}</span>
              </Col>
              <Col xs={3}>
                <span data-testid="date">{this.prettifyDate(
                    this.props.alert)}</span>
              </Col>
            </Row>
            <Row>
              <Col xs={3}>
             <span>

             </span>
              </Col>
              <Col xs={6}>
                <span data-testid="routeInfo">{this.prettifyDistanceAndDuration(
                    this.props.alert)}</span>
              </Col>
              <Col xs={3}>
                <span data-testid="alertCalls">
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

AlertViewHeader.propTypes = {
  alert: PropTypes.object
};