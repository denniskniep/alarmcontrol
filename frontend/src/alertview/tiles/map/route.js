import React, {Component} from 'react';
import GraphhopperRouter from "./graphHopperRouter";
import L from 'leaflet';
import { LeafletConsumer} from 'react-leaflet'
import 'leaflet-routing-machine';
import 'leaflet-control-geocoder';
import 'leaflet-routing-machine/dist/leaflet-routing-machine.css';

class Route extends Component {
  constructor(props) {
    super(props)
    this.router = null;
    this.map = null;
  }

  render() {
    return (
        // Uses react context API (LeafletConsumer provides the map object)
        // https://reactjs.org/docs/context.html
        <LeafletConsumer>
          {
            map => {

              //remove old route
              if (this.router && this.map) {
                this.map.removeControl(this.router);
              }

              let route = JSON.parse(this.props.route);
              this.map = map.map;
              this.router = new L.Routing.Control({
                // The nonsense waypoints are necessary here because of an
                // "is empty check" in the framework.
                // Actually the route object passed to the
                // GraphhopperRouter contains the waypoints
                waypoints: [{lat: 0, lng: 0}, {lat: 0, lng: 0}],
                routeWhileDragging: false,
                router: new GraphhopperRouter(route),
                fitSelectedRoutes: this.props.fitSelectedRoutes
              })
              .on('routingstart', (x) => {
                this.props.onRouting && this.props.onRouting(x)
              })
              .on('routesfound', (x) => {
                x && this.props.onRouteFound && this.props.onRouteFound(x);
              })
              .on('routingerror', (x) => {
                x && this.props.onRoutingError && this.props.onRoutingError(x)
              });
              this.router.addTo(this.map);
              this.router.hide();
            }
          }
        </LeafletConsumer>
    );
  }
}
export default Route;