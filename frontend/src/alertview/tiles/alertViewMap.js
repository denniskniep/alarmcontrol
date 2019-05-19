import React, {Component} from 'react';
import 'leaflet/dist/leaflet.css';
import {CircleMarker, Map, TileLayer} from "react-leaflet";
import Route from "./map/route";


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

  render() {
    return (
        <div className="alertViewOuterBox h-100">
          <Map className={"alertViewBox"}
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

export default AlertViewMap