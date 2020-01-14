import polyline from "polyline";
import L from "leaflet";
import 'lrm-graphhopper';

class GraphhopperRouter {

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

export default GraphhopperRouter;