import React, {Component} from 'react';
import {Query} from "react-apollo";
import {gql} from "apollo-boost";
import {Badge, Col, Container, Row, Table} from 'react-bootstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import {CircleMarker, LeafletConsumer, Map, TileLayer} from 'react-leaflet'
import L from 'leaflet';
import polyline from 'polyline';
import 'leaflet/dist/leaflet.css';
import 'leaflet-routing-machine';
import 'lrm-graphhopper';
import 'leaflet-control-geocoder';
import 'leaflet-routing-machine/dist/leaflet-routing-machine.css';

const ALERT_BY_ID = gql`
    query alertById($id: ID) {
      alertById(id: $id) {
        id
        keyword
        dateTime
        route
      }
    }
`;

class AlertViewLayout extends Component {
  render() {
    return (
        <Container fluid="true"
                   className={"h-full d-flex flex-column alertView"}>
          <Row className={"flex-fill d-flex justify-content-star"}>
            <Col xs={3}>
              <AlertViewPersons alert={this.props.alert}/>
            </Col>
            <Col>
              <Container fluid="true" className={"d-flex flex-column h-100 "}>
                <Row>
                  <Col>
                    <AlertViewHeader alert={this.props.alert}/>
                  </Col>
                </Row>
                <Row className={"h-100"}>
                  <Col>
                    <AlertViewMap alert={this.props.alert}/>
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
                      <FontAwesomeIcon className={"insideBadge"}
                                       icon={["far", "check-circle"]}/>
                      <Badge variant="light">2</Badge></div>
                  </Badge>
                </h1>
              </Col>
              <Col className={"d-flex align-items-center"}>
                <h4>
                  <Badge variant="success">
                    <div>
                      <span className={"insideBadge"}>FK</span>
                      <Badge variant="light">1</Badge></div>
                  </Badge>
                </h4>
              </Col>
              <Col className={"d-flex align-items-center"}>
                <h4>
                  <Badge variant="success">
                    <div>
                      <span className={"insideBadge"}>C</span>
                      <Badge variant="light">1</Badge></div>
                  </Badge>
                </h4>
              </Col>
              <Col className={"d-flex align-items-center"}>
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
                      <FontAwesomeIcon className={"insideBadge"}
                                       icon={["far", "check-circle"]}/>
                      <Badge variant="light">1</Badge></div>
                  </Badge>
                </h1>
              </Col>
              <Col className={"d-flex align-items-center"}>
                <h4>
                  <Badge variant="info">
                    <div>
                      <span className={"insideBadge"}>FK</span>
                      <Badge variant="light">1</Badge></div>
                  </Badge>
                </h4>
              </Col>
              <Col className={"d-flex align-items-center"}>
                <h4>
                  <Badge variant="info">
                    <div>
                      <span className={"insideBadge"}>C</span>
                      <Badge variant="light">1</Badge></div>
                  </Badge>
                </h4>
              </Col>
              <Col className={"d-flex align-items-center"}>
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
            <Row>
              <Col className={"noPadding"}>
                <Table responsive>
                  <tbody>
                  <tr className={"available"}>
                    <td>Max Mustermann</td>
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
                <Badge className={"badgeSpace"} variant="light">Harald
                  Töpfer</Badge>
                <Badge className={"badgeSpace"} variant="danger">Monika
                  Mauer</Badge>
                <Badge className={"badgeSpace"} variant="danger">Torben
                  Tau</Badge>
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
      pos: {
        lat: 51.406378,
        lng: 9.358980,
        zoom: 13
      },
      target: null
    };
  }

  targetGeocoded(geocodedTarget) {
    console.log("geocoded:", geocodedTarget);
    this.setState({
      target: {
        lat: geocodedTarget.center.lat,
        lng: geocodedTarget.center.lng
      }
    });
  }

  render() {
    return (
        <div className="alertViewOuterBox h-100">
          <Map className={"alertViewBox "}
               center={[this.state.pos.lat, this.state.pos.lng]}
               zoom={this.state.pos.zoom}>
            <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                attribution="&copy; <a href=&quot;http://osm.org/copyright&quot;>OpenStreetMap</a> contributors"
            />

            <Route route={this.props.alert.route}
                   onRouting={(r) => console.log("Routing to:", r)}
                   onRouteFound={(r) => console.log("Route found:", r)}
            />

            {this.state.target &&
            <CircleMarker center={this.state.target}/>
            }
          </Map>
        </div>
    )
  }
}

const ZOOM_FACTOR = {
  Continent: 0,
  Country: 3,
  State: 5,
  Region: 6,
  County: 8,
  City: 10,
  Town: 12,
  Suburb: 14,
  Street: 16,
  Building: 18,
  Max: 21
};

class CacheRouter {

  constructor(response) {
    this.response = response;
  }

  _decodePolyline(geometry) {
    var coords = polyline.decode(geometry, 5),
        latlngs = new Array(coords.length),
        i;
    for (i = 0; i < coords.length; i++) {
      latlngs[i] = new L.LatLng(coords[i][0], coords[i][1]);
    }

    return latlngs;
  }

  _toWaypoints(inputWaypoints, vias) {
    var wps = [],
        i;
    for (i = 0; i < vias.length; i++) {
      wps.push({
        latLng: L.latLng(vias[i]),
        name: inputWaypoints[i].name,
        options: inputWaypoints[i].options
      });
    }
  }

  _convertInstructions(instructions) {
    var signToType = {
          '-7': 'SlightLeft',
          '-3': 'SharpLeft',
          '-2': 'Left',
          '-1': 'SlightLeft',
          0: 'Straight',
          1: 'SlightRight',
          2: 'Right',
          3: 'SharpRight',
          4: 'DestinationReached',
          5: 'WaypointReached',
          6: 'Roundabout',
          7: 'SlightRight'
        },
        result = [],
        type,
        i,
        instr;

    for (i = 0; instructions && i < instructions.length; i++) {
      instr = instructions[i];
      if (i === 0) {
        type = 'Head';
      } else {
        type = signToType[instr.sign];
      }
      result.push({
        type: type,
        modifier: type,
        text: instr.text,
        distance: instr.distance,
        time: instr.time / 1000,
        index: instr.interval[0],
        exit: instr.exit_number
      });
    }

    return result;
  }

  _mapWaypointIndices(waypoints, instructions, coordinates) {
    var wps = [],
        wpIndices = [],
        i,
        idx;

    wpIndices.push(0);
    wps.push(new L.Routing.Waypoint(coordinates[0], ''));

    for (i = 0; instructions && i < instructions.length; i++) {
      if (instructions[i].sign === 5) { // VIA_REACHED
        idx = instructions[i].interval[0];
        wpIndices.push(idx);
        wps.push({
          latLng: coordinates[idx],
          name: ''
        });
      }
    }

    wpIndices.push(coordinates.length - 1);
    wps.push({
      latLng: coordinates[coordinates.length - 1],
      name: ''
    });

    return {
      waypointIndices: wpIndices,
      waypoints: wps
    };
  }

  route(waypoints, callback, context, options) {
    var alts = []
    for (let i = 0; i < this.response.paths.length; i++) {

      let inputWaypoints = []
      let path = this.response.paths[i];
      let coordinates = this._decodePolyline(path.points);
      if (path.points_order) {
        var tempWaypoints = [];
        for (let i = 0; i < path.points_order.length; i++) {
          tempWaypoints.push(inputWaypoints[path.points_order[i]]);
        }
        inputWaypoints = tempWaypoints;
      }
      let mappedWaypoints =
          this._mapWaypointIndices(inputWaypoints, path.instructions,
              coordinates);

      alts.push({
        name: '',
        coordinates: coordinates,
        instructions: this._convertInstructions(path.instructions),
        summary: {
          totalDistance: path.distance,
          totalTime: path.time / 1000,
          totalAscend: path.ascend,
        },
        inputWaypoints: inputWaypoints,
        actualWaypoints: mappedWaypoints.waypoints,
        waypointIndices: mappedWaypoints.waypointIndices
      });
    }

    setTimeout(() => callback.call(context, null, alts), 100)

  }
}

class Route extends Component {
  constructor(props) {
    super(props)
  }

  render() {
    return (
        // Uses react context API
        // https://reactjs.org/docs/context.html
        <LeafletConsumer>
          {
            map => {
              let route = JSON.parse(this.props.route);
              let router = new L.Routing.Control({
                waypoints: [{lat: 0, lng: 0}, {lat: 0, lng: 0}],
                routeWhileDragging: false,
                router: new CacheRouter(route)
              })
              .on('routingstart', (x) => {
                this.props.onRouting && this.props.onRouting(x)
              })
              .on('routesfound', (x) => {
                x && this.props.onRouteFound && this.props.onRouteFound(
                    x.routes[0]);
              })
              .on('routingerror', (x) => {
                x && this.props.onRoutingError && this.props.onRoutingError(x)
              });

              router.addTo(map.map);
            }
          }
        </LeafletConsumer>
    );
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
    return (
        <Query query={ALERT_BY_ID} variables={{id: this.props.match.params.id}}>
          {({loading, error, data}) => {
            if (loading) {
              return <p>Loading...</p>;
            }
            if (error) {
              return <p>Error: ${error.message}</p>;
            }
            if (!data.alertById) {
              return <p>NO DATA</p>;
            }

            return (<AlertViewLayout alert={data.alertById}/>);
          }}
        </Query>);
  }
}

export default AlertView;
