import React, { Component } from 'react';
import { Query } from "react-apollo";
import { gql } from "apollo-boost";
import { Container, Row, Col, Table, Badge } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { Map, TileLayer, LeafletConsumer, CircleMarker } from 'react-leaflet'
import L from 'leaflet';
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
      pos: {
        lat: 51.406378,
        lng: 9.358980,
        zoom: 13
      },
      target: null
    };
  }

  targetGeocoded(geocodedTarget){
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
        <Map className={"alertViewBox "} center={[this.state.pos.lat, this.state.pos.lng]} zoom={this.state.pos.zoom}>
          <TileLayer
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
              attribution="&copy; <a href=&quot;http://osm.org/copyright&quot;>OpenStreetMap</a> contributors"
          />

          <Routing from={{lat: 51.406339, lng: 9.359186}}
                   target={"Hinter den Gärten 8, 34379 Calden"}
                   onTargetGeocoded={(t) => this.targetGeocoded(t)}
                   onRouting={(r) => console.log("Routing to:", r) }
                   onRouteFound={(r) => console.log("Route found:", r)}/>

          {this.state.target &&
            <CircleMarker center={this.state.target}/>
          }
        </Map>
      </div>
    )
  }
}

const ZOOM_FACTOR = {
  Continent : 0,
  Country : 3,
  State : 5,
  Region: 6,
  County : 8,
  City: 10,
  Town: 12,
  Suburb: 14,
  Street: 16,
  Building: 18,
  Max: 21
};

class Routing extends Component {

  constructor(props) {
    super(props)
    this.state = {
      route: null,
      routeAddedToMap : false
    }
  }

  componentWillMount(){
    this.geocodeAsync(this.props.target)
    .then(geocodedLocations => {
      let geocodedLocation = geocodedLocations[0];
      this.props.onTargetGeocoded && this.props.onTargetGeocoded(geocodedLocation);

      let fromLocation ={
        center: {
          lat: this.props.from.lat,
          lng: this.props.from.lng
        }
      };

      let route = this.calculateRoute([fromLocation, geocodedLocation]);
      this.setState({
        route: route
      });
    })
  }

  geocodeAsync(query) {
    const geocoder = new L.Control.Geocoder.Nominatim();
    return new Promise(function (resolve, reject) {
      if (query.hasOwnProperty("lat") && query.hasOwnProperty("lng")) {
        geocoder.reverse(query, ZOOM_FACTOR.Max, function (a, b) {
          resolve(a);
        });
      }else if(typeof query === 'string' || query instanceof String) {
        geocoder.geocode(query, function (a, b) {
          resolve(a);
        });
      }else{
        reject("the argument query is not a string or has the following properties: lat, lng")
      }
    });
  }

  calculateRoute(geocodedLocations){
    let waypoints = geocodedLocations.map( g => new L.LatLng(g.center.lat, g.center.lng));

    return new L.Routing.Control({
      waypoints: waypoints,
      routeWhileDragging: false,
      router: L.Routing.graphHopper('', {
        serviceUrl: 'http://localhost:8080/map/route',
        timeout: 30 * 1000,
        urlParameters: {}
      })
    })
    .on('routingstart', (x)=>{this.props.onRouting && this.props.onRouting(x)})
    .on('routesfound', (x)=>{ x && this.props.onRouteFound && this.props.onRouteFound(x.routes[0]);})
    .on('routingerror', (x)=>{x && this.props.onRoutingError && this.props.onRoutingError(x)});
  }

  render() {
    return (
        // Uses react context API
        // https://reactjs.org/docs/context.html
        <LeafletConsumer>
          {
            map => {
              if (map && map.map && this.state.route && !this.state.routeAddedToMap) {
                console.log(map);
                console.log(this.state);
                this.state.routeAddedToMap = true;
                this.state.route.addTo(map.map);
              }
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
