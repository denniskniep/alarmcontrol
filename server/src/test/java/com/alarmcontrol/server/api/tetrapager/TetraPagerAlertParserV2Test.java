package com.alarmcontrol.server.api.tetrapager;


import com.alarmcontrol.server.api.ExternalAlertRequest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TetraPagerAlertParserV2Test {

  @Test
  public void parseText_Case1(){
    ExternalAlertRequest alertRequest = parseText("&32S32*H1*SONSTIGE*HOFGEISMAR STEINWEG 45 HOFGEISMAR");
    assertThat(alertRequest.getAlertNumber()).isEqualTo("9876543-S32");
    assertThat(alertRequest.getAlertReferenceId()).isEqualTo("H1_HOFGEISMAR-STEINWEG-45-HOFGEISMAR");
    assertThat(alertRequest.getAlertCallReferenceId()).isEqualTo("189");
    assertThat(alertRequest.getDescription()).isEqualToIgnoringCase("SONSTIGE");
    assertThat(alertRequest.getKeyword()).isEqualToIgnoringCase("H1");
    assertThat(alertRequest.getAddress()).isEqualToIgnoringCase("HOFGEISMAR STEINWEG 45 HOFGEISMAR");
  }

  @Test
  public void parseText_Case2(){
    ExternalAlertRequest alertRequest = parseText("&01S01*BF2*RAUCHENTWICKLUNG*BURGUFFELN K47  CALDEN");
    assertThat(alertRequest.getAlertNumber()).isEqualTo("9876543-S01");
    assertThat(alertRequest.getAlertReferenceId()).isEqualTo("BF2_BURGUFFELN-K47--CALDEN");
    assertThat(alertRequest.getAlertCallReferenceId()).isEqualTo("189");
    assertThat(alertRequest.getDescription()).isEqualToIgnoringCase("RAUCHENTWICKLUNG");
    assertThat(alertRequest.getKeyword()).isEqualToIgnoringCase("BF2");
    assertThat(alertRequest.getAddress()).isEqualToIgnoringCase("BURGUFFELN K47  CALDEN");
  }

  @Test
  public void parseText_Case3(){
    ExternalAlertRequest alertRequest = parseText("&01S01*BF1**CALDEN B7  CALDEN");
    assertThat(alertRequest.getAlertNumber()).isEqualTo("9876543-S01");
    assertThat(alertRequest.getAlertReferenceId()).isEqualTo("BF1_CALDEN-B7--CALDEN");
    assertThat(alertRequest.getAlertCallReferenceId()).isEqualTo("189");
    assertThat(alertRequest.getDescription()).isEqualToIgnoringCase("");
    assertThat(alertRequest.getKeyword()).isEqualToIgnoringCase("BF1");
    assertThat(alertRequest.getAddress()).isEqualToIgnoringCase("CALDEN B7  CALDEN");
  }

  @Test
  public void parseText_Case4(){
    ExternalAlertRequest alertRequest = parseText("&56$2002*S56*BF1**WESTUFFELN MALSBURGER WEG  CALDEN");
    assertThat(alertRequest.getAlertNumber()).isEqualTo("9876543-S56");
    assertThat(alertRequest.getAlertReferenceId()).isEqualTo("BF1_WESTUFFELN-MALSBURGER-WEG--CALDEN");
    assertThat(alertRequest.getAlertCallReferenceId()).isEqualTo("189");
    assertThat(alertRequest.getDescription()).isEqualToIgnoringCase("");
    assertThat(alertRequest.getKeyword()).isEqualToIgnoringCase("BF1");
    assertThat(alertRequest.getAddress()).isEqualToIgnoringCase("WESTUFFELN MALSBURGER WEG  CALDEN");
  }

  @Test
  public void parseText_Case5(){
    ExternalAlertRequest alertRequest = parseText("&49S49*NOTARZTZUBRINGER");
    assertThat(alertRequest.getAlertNumber()).isEqualTo("9876543-S49");
    assertThat(alertRequest.getAlertReferenceId()).isEqualTo("NOTARZTZUBRINGER_");
    assertThat(alertRequest.getAlertCallReferenceId()).isEqualTo("189");
    assertThat(alertRequest.getDescription()).isEqualToIgnoringCase(null);
    assertThat(alertRequest.getKeyword()).isEqualToIgnoringCase("NOTARZTZUBRINGER");
    assertThat(alertRequest.getAddress()).isEqualToIgnoringCase(null);
  }


  private ExternalAlertRequest parseText(String text) {
    TetraPagerAlertParserV2 tetraPagerAlertParser = new TetraPagerAlertParserV2();

    Map<String,String> params = new HashMap<>();
    params.put(TetraPagerAlertParserV2.GSSI, "9876543");
    params.put(TetraPagerAlertParserV2.ID, "189");
    params.put(TetraPagerAlertParserV2.TEXT, text);

    return tetraPagerAlertParser.parse(params);
  }

  private ExternalAlertRequest parseTextWithWhitespaceReplacementChar(String text, String whitespaceReplacementChar) {
    TetraPagerAlertParserV2 tetraPagerAlertParser = new TetraPagerAlertParserV2();

    Map<String,String> params = new HashMap<>();
    params.put(TetraPagerAlertParserV2.GSSI, "9876543");
    params.put(TetraPagerAlertParserV2.ID, "189");
    params.put(TetraPagerAlertParserV2.TEXT, text);
    params.put(TetraPagerAlertParserV2.WHITESPACE_REPLACEMENT_CHAR, whitespaceReplacementChar);

    return tetraPagerAlertParser.parse(params);
  }

}