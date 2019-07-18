package com.alarmcontrol.server.maps.mapbox.geocoding;

import static org.assertj.core.api.Assertions.assertThat;

import com.alarmcontrol.server.maps.GeocodingResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;

public class MapboxParserTest {

  @Test
  public void parse_emptyResult() throws IOException {
    String json = loadJson("./emptyResult.json");
    GeocodingResult result = parse(json);
    assertEmptyGeocodingResult(result);
  }

  @Test
  public void parse_emptyFeature() throws IOException {
    String json = loadJson("./emptyFeature.json");
    GeocodingResult result = parse(json);
    assertEmptyGeocodingResult(result);
  }

  @Test
  public void parse_townOnly() throws IOException {
    String json = loadJson("./townOnly.json");
    GeocodingResult result = parse(json);
    assertEmptyGeocodingResult(result);
  }

  @Test
  public void parse_unknownStreet() throws IOException {
    String json = loadJson("./unknownStreet.json");
    GeocodingResult result = parse(json);
    assertEmptyGeocodingResult(result);
  }

  @Test
  public void parse_villageOnly() throws IOException {
    String json = loadJson("./villageOnly.json");
    GeocodingResult result = parse(json);
    assertEmptyGeocodingResult(result);
  }

  @Test
  public void parse_regularStreet() throws IOException {
    String json = loadJson("./regularStreet.json");
    GeocodingResult result = parse(json);

    assertGeocodingResult(
        result,
        "51.38396",
        "9.36701",
        "Hinter Den Gärten 8",
        "Calden-Fürstenwald");
  }

  @Test
  public void parse_regularStreetWithoutOptionalValues() throws IOException {
    String json = loadJson("./regularStreetWithoutOptionalValues.json");
    GeocodingResult result = parse(json);

    assertGeocodingResult(
        result,
        "51.38396",
        "9.36701",
        "Hinter Den Gärten 8",
        "Calden-Fürstenwald");
  }

  @Test
  public void parse_streetWithoutHouseNumber() throws IOException {
    String json = loadJson("./streetWithoutHouseNumber.json");
    GeocodingResult result = parse(json);

    assertGeocodingResult(
        result,
        "51.384615",
        "9.366534",
        "Hinter Den Gärten",
        "Calden-Fürstenwald");
  }

  @Test
  public void parse_streetInMainTown() throws IOException {
    String json = loadJson("./streetInMainTown.json");
    GeocodingResult result = parse(json);

    assertGeocodingResult(
        result,
        "51.408759",
        "9.400976",
        "Wilhelmsthaler Straße 12",
        "Calden");
  }


  private void assertEmptyGeocodingResult(GeocodingResult result) {
    assertGeocodingResult(result, null, null, null, null);
  }

  private void assertGeocodingResult(GeocodingResult result, String lat, String lng, String addrInfo1, String addrInfo2) {
    assertThat(result.getCoordinate().getLat()).isEqualTo(lat);
    assertThat(result.getCoordinate().getLng()).isEqualTo(lng);
    assertThat(result.getAddressInfo1()).isEqualTo(addrInfo1);
    assertThat(result.getAddressInfo2()).isEqualTo(addrInfo2);
  }

  private GeocodingResult parse(String json) {
    return new MapboxParser().parse(json);
  }

  private String loadJson(String path) throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource(path).getFile());
    return Files.readString(file.toPath());
  }
}