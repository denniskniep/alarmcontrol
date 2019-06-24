package com.alarmcontrol.server.maps;

public interface GeocodingService {
  GeocodingResult geocode(String query);
}
