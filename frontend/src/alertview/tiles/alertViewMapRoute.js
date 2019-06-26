import React, {Component} from 'react';
import 'leaflet/dist/leaflet.css';
import {CircleMarker, Map, TileLayer} from "react-leaflet";
import Route from "./map/route";


class AlertViewMapRoute extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
        <div className="alertViewOuterBox h-100">
          <Map className={"alertViewBox"} viewport={{ zoom: 13, center:{lat:0, lng:0}}}>


            <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                attribution="&copy; <a href=&quot;http://osm.org/copyright&quot;>OpenStreetMap</a> contributors"
            />

            <Route route={this.props.alert.route} fitSelectedRoutes={"smart"} />

            <CircleMarker center={{lat: this.props.alert.addressLat, lng: this.props.alert.addressLng}}/>
          </Map>
        </div>
    )
  }
}

export default AlertViewMapRoute