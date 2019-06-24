package com.alarmcontrol.server.maps;

import java.util.List;

public interface RoutingService {
  RoutingResult route(List<Coordinate> points);
}
