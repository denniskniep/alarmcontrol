package com.alarmcontrol.server.aao.ruleengine.rules;

import com.alarmcontrol.server.aao.config.Keyword;
import com.alarmcontrol.server.aao.ruleengine.AlertContext;
import com.alarmcontrol.server.aao.ruleengine.ReferenceContext;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class KeywordRule implements Rule {

  private List<Keyword> keywords;

  public KeywordRule(List<Keyword> keywords) {
    this.keywords = keywords;
  }

  @Override
  public boolean match(ReferenceContext referenceContext, AlertContext alertContext) {
    if (keywords.size() == 0) {
      return true;
    }

    return keywords
        .stream()
        .anyMatch(k -> isSameKeyword(alertContext.getKeyword(), k.getKeyword()));
  }

  private boolean isSameKeyword(String keywordA, String keywordB) {
    String keywordATrimmed = StringUtils.replace(keywordA, " ", "");
    String keywordBTrimmed = StringUtils.replace(keywordB, " ", "");
    return StringUtils.equalsIgnoreCase(keywordATrimmed, keywordBTrimmed);
  }
}
