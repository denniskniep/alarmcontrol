package com.alarmcontrol.server.rules;

import com.alarmcontrol.server.aaos.Aao;
import com.alarmcontrol.server.aaos.CatalogKeywordInput;
import com.alarmcontrol.server.aaos.Location;
import com.alarmcontrol.server.notifications.core.config.AaoOrganisationConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface Rule {
  boolean match(AlertContext alertContext);
}

