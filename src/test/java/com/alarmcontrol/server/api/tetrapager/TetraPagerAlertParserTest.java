package com.alarmcontrol.server.api.tetrapager;


import static org.assertj.core.api.Assertions.assertThat;

import com.alarmcontrol.server.api.ExternalAlertRequest;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TetraPagerAlertParserTest {

  @Test
  public void parseText_alertIdInDescriptionAtTheEnd(){
    ExternalAlertRequest alertRequest = parseText("&54S54*STELLEN SIE EINSATZBEREITSCHAFT HER B123456778*BF2*OBERMUSTERDORF BERLINER STRASSE 15 MUSTERDORF");
    assertThat(alertRequest.getAlertNumber()).isEqualTo("9876543-S54");
    assertThat(alertRequest.getAlertReferenceId()).isEqualTo("B123456778");
    assertThat(alertRequest.getAlertCallReferenceId()).isEqualTo("189");
    assertThat(alertRequest.getDescription()).isEqualToIgnoringCase("STELLEN SIE EINSATZBEREITSCHAFT HER");
    assertThat(alertRequest.getKeyword()).isEqualToIgnoringCase("BF2");
    assertThat(alertRequest.getAddress()).isEqualToIgnoringCase("OBERMUSTERDORF BERLINER STRASSE 15 MUSTERDORF");
  }

  @Test
  public void parseText_alertIdInDescriptionAtStart(){
    ExternalAlertRequest alertRequest = parseText("&54S54*B123456778 STELLEN SIE EINSATZBEREITSCHAFT HER*BF2*OBERMUSTERDORF BERLINER STRASSE 15 MUSTERDORF");
    assertThat(alertRequest.getAlertNumber()).isEqualTo("9876543-S54");
    assertThat(alertRequest.getAlertReferenceId()).isEqualTo("B123456778");
    assertThat(alertRequest.getAlertCallReferenceId()).isEqualTo("189");
    assertThat(alertRequest.getDescription()).isEqualToIgnoringCase("STELLEN SIE EINSATZBEREITSCHAFT HER");
    assertThat(alertRequest.getKeyword()).isEqualToIgnoringCase("BF2");
    assertThat(alertRequest.getAddress()).isEqualToIgnoringCase("OBERMUSTERDORF BERLINER STRASSE 15 MUSTERDORF");
  }

  @Test
  public void parseText2_onlyAlertIdInDescription(){
    ExternalAlertRequest alertRequest = parseText("&06S06*B345432567*H1*OSTHAUSEN KIGASTRASSE 8 OSTHAUSEN");
    assertThat(alertRequest.getAlertNumber()).isEqualTo("9876543-S06");
    assertThat(alertRequest.getAlertReferenceId()).isEqualTo("B345432567");
    assertThat(alertRequest.getAlertCallReferenceId()).isEqualTo("189");
    assertThat(alertRequest.getDescription()).isEqualToIgnoringCase(null);
    assertThat(alertRequest.getKeyword()).isEqualToIgnoringCase("H1");
    assertThat(alertRequest.getAddress()).isEqualToIgnoringCase("OSTHAUSEN KIGASTRASSE 8 OSTHAUSEN");
  }

  @Test
  public void parseText_funktionsprobe(){
    ExternalAlertRequest alertRequest = parseText("&02S02*FUNKTIONSPROBE");
    assertThat(alertRequest.getAlertNumber()).isEqualTo("9876543-S02");
    assertThat(alertRequest.getAlertReferenceId()).isEqualTo("189");
    assertThat(alertRequest.getAlertCallReferenceId()).isEqualTo("189");
    assertThat(alertRequest.getDescription()).isEqualToIgnoringCase(null);
    assertThat(alertRequest.getKeyword()).isEqualToIgnoringCase("FUNKTIONSPROBE");
    assertThat(alertRequest.getAddress()).isEqualToIgnoringCase(null);
  }


  private ExternalAlertRequest parseText(String text) {
    TetraPagerAlertParser tetraPagerAlertParser = new TetraPagerAlertParser();

    Map<String,String> params = new HashMap<>();
    params.put(TetraPagerAlertParser.GSSI, "9876543");
    params.put(TetraPagerAlertParser.ID, "189");
    params.put(TetraPagerAlertParser.TEXT, text);

    return tetraPagerAlertParser.parse(params);
  }

}