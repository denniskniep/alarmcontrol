import React, { Component } from 'react';
import { Query } from "react-apollo";
import { gql } from "apollo-boost";
import { Container, Row, Col, Table, Badge } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { Map, TileLayer } from 'react-leaflet'

const ALERT_BY_ID = gql`
    query alertById($id: ID) {
      alertById(id: $id) {
        id
        keyword
        dateTime
      }
    }
`;

class AlertViewLayout extends Component {
  render() {
    return (
     <Container fluid="true" className={"h-full d-flex flex-column alertView"}>
      <Row className={"flex-fill d-flex justify-content-star"}>
        <Col xs={3}>
         <AlertViewPersons alert={this.props.alert} />
        </Col>
        <Col>
          <Container fluid="true" className={"d-flex flex-column h-100 "}>
            <Row>
              <Col>
                <AlertViewHeader alert={this.props.alert} />
              </Col>
            </Row>
            <Row className={"h-100"}>
              <Col>
                <AlertViewMap alert={this.props.alert} />
              </Col>
            </Row>
          </Container>
        </Col>
      </Row>
    </Container>);
  }
}

class AlertViewHeader extends Component {
  render() {
    return (
      <AlertViewBox>
        <Container fluid="true">
          <Row>
          <Col xs={3}>
            <h1>F2 Y</h1>
          </Col>
          <Col xs={6}>
            <h1>Musterstraße 25</h1>
          </Col>
          <Col xs={3}>
            <h1>00:02:25</h1>
          </Col>
         </Row>
          <Row>
         <Col xs={3}>
           <span>1 vermisste Person</span>
         </Col>
         <Col xs={6}>
           <span>12345 Berlin</span>
         </Col>
         <Col xs={3}>
           <span>12.01.2019 16:17:03</span>
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
             <span>OT: Spandau</span>
           </Col>
           <Col xs={3}>
             <span></span>
           </Col>
         </Row>
          <Row>
           <Col xs={3}>
             <span></span>
           </Col>
           <Col xs={6}>
             <span>7 km (4 min)</span>
           </Col>
           <Col xs={3}>
             <span></span>
           </Col>
         </Row>
        </Container>
      </AlertViewBox>);
  }
}

class AlertViewPersons extends Component {
  render() {
    return (
      <AlertViewBox>
        <Container fluid="true" className={"d-flex flex-column h-100"}>
          <Row>
            <Col>
              <h1>
                <Badge variant="success">
                  <div>
                  <FontAwesomeIcon className={"insideBadge"} icon={["far","check-circle"]} />
                  <Badge variant="light">2</Badge></div>
                 </Badge>
              </h1>
            </Col>
            <Col className={"d-flex align-items-center"} >
              <h4>
                <Badge variant="success">
                  <div>
                    <span className={"insideBadge"}>FK</span>
                    <Badge variant="light">1</Badge></div>
                </Badge>
              </h4>
            </Col>
            <Col className={"d-flex align-items-center"} >
              <h4>
                <Badge variant="success">
                  <div>
                    <span className={"insideBadge"}>C</span>
                    <Badge variant="light">1</Badge></div>
                </Badge>
              </h4>
            </Col>
            <Col className={"d-flex align-items-center"} >
              <h4>
                <Badge variant="success">
                  <div>
                    <span className={"insideBadge"}>AGT</span>
                    <Badge variant="light">2</Badge></div>
                </Badge>
              </h4>
            </Col>

          </Row>
          <Row>
            <Col>
              <hr/>
            </Col>
          </Row>

          <Row>
            <Col>
              <h1>
                <Badge variant="info">
                  <div>
                    <FontAwesomeIcon className={"insideBadge"} icon={["far","check-circle"]} />
                    <Badge variant="light">1</Badge></div>
                </Badge>
              </h1>
            </Col>
            <Col className={"d-flex align-items-center"} >
              <h4>
                <Badge variant="info">
                  <div>
                    <span className={"insideBadge"}>FK</span>
                    <Badge variant="light">1</Badge></div>
                </Badge>
              </h4>
            </Col>
            <Col className={"d-flex align-items-center"} >
              <h4>
                <Badge variant="info">
                  <div>
                    <span className={"insideBadge"}>C</span>
                    <Badge variant="light">1</Badge></div>
                </Badge>
              </h4>
            </Col>
            <Col className={"d-flex align-items-center"} >
              <h4>
                <Badge variant="info">
                  <div>
                    <span className={"insideBadge"}>AGT</span>
                    <Badge variant="light">2</Badge></div>
                </Badge>
              </h4>
            </Col>

          </Row>
          <Row>
            <Col>
              <br/>
            </Col>
          </Row>
          <Row >
            <Col className={"noPadding"}>
              <Table responsive>
                <tbody>
                <tr className={"available"}>
                  <td >Max Mustermann</td>
                  <td>
                    <Badge className={"badgeSpace"} pill variant="secondary">
                      FK
                    </Badge>
                    <Badge className={"badgeSpace"} pill variant="secondary">
                      C
                    </Badge>
                  </td>
                </tr>
                <tr className={"available"}>
                  <td>Hans Halunke</td>
                  <td>
                    <Badge className={"badgeSpace"} pill variant="secondary">
                      AGT
                    </Badge>
                  </td>
                </tr>
                <tr className={"laterAvailable"}>
                  <td>Lars Laune</td>
                  <td>
                    <Badge className={"badgeSpace"} pill variant="secondary">
                      FK
                    </Badge>
                    <Badge className={"badgeSpace"} pill variant="secondary">
                      C
                    </Badge>
                    <Badge className={"badgeSpace"} pill variant="secondary">
                      AGT
                    </Badge>
                  </td>
                </tr>
                </tbody>
              </Table>
            </Col>
          </Row>
          <Row>
            <Col>
              <Badge className={"badgeSpace"} variant="light">Harald Töpfer</Badge>
              <Badge className={"badgeSpace"} variant="danger">Monika Mauer</Badge>
              <Badge className={"badgeSpace"} variant="danger">Torben Tau</Badge>
            </Col>
          </Row>
        </Container>
      </AlertViewBox>);
  }
}

class AlertViewMap extends Component {

  constructor(props) {
    super(props);
    this.state = {
      lat: 51.406378,
      lng: 9.358980,
      zoom: 13,
    };
  }

  render() {
    const position = [this.state.lat, this.state.lng]
    return (
      <div className="alertViewOuterBox h-100">
        <Map className={"alertViewBox "} center={position} zoom={13}>
          <TileLayer
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
              attribution="&copy; <a href=&quot;http://osm.org/copyright&quot;>OpenStreetMap</a> contributors"
          />
        </Map>
      </div>
    )
  }
}

class AlertViewBox extends Component {
  render() {
    return (<div className="alertViewOuterBox h-100">
      <div className={["alertViewBox", "h-100"].join(' ')}>
        {this.props.children}
      </div>
    </div>);
  }
}


class AlertView extends Component {
  render() {
    return (<Query query={ALERT_BY_ID} variables={{id :this.props.match.params.id}}>
      {({ loading, error, data }) => {
        if (loading) return <p>Loading...</p>;
        if (error) return <p>Error: ${error.message}</p>;
        if (!data.alertById) return <p>NO DATA</p>;

        return (<AlertViewLayout alert={data.alertById} />);
      }}
    </Query>);
  }
}

export default AlertView;
