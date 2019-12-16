package com.alarmcontrol.server.aao.ruleengine;

import com.alarmcontrol.server.aao.config.Keyword;
import com.alarmcontrol.server.aao.config.Location;
import com.alarmcontrol.server.aao.config.Vehicle;
import com.alarmcontrol.server.aao.ruleengine.rules.LocationRule;
import java.util.UUID;

public class Constants {

  public static final Location GOTHAMCITY = new Location(newUuid(), "Gothamcity");
  public static final Location METROPOLIS = new Location(newUuid(), "Metropolis");
  public static final Location OWN_LOCATION = new Location(LocationRule.OwnOrganisationUniqueKey, null);
  public static final Location OTHER_LOCATION = new Location(LocationRule.OtherOrganisationsUniqueKey, null);

  public static final Keyword H1 = new Keyword(newUuid(), "H 1");
  public static final Keyword H1Y = new Keyword(newUuid(), "H 1 Y");
  public static final Keyword F1 = new Keyword(newUuid(), "F 1 Y");
  public static final Keyword F2 = new Keyword(newUuid(), "F 2");

  public static final Vehicle ELW = new Vehicle(newUuid(), "ELW");
  public static final Vehicle HLF = new Vehicle(newUuid(), "HLF");
  public static final Vehicle RW = new Vehicle(newUuid(), "RW");
  public static final Vehicle TLF16 = new Vehicle(newUuid(), "TLF16");

  private static String newUuid() {
    return UUID.randomUUID().toString();
  }
}
