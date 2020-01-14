package com.alarmcontrol.server.aao.ruleengine;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Service
public class FeiertagService {
  @Value("${feiertage.path}")
  private String feiertagePath;

  private ResourceLoader resourceLoader;

  public List<Feiertag> getFeiertage() {
    List<String> mappings = new ArrayList<>();
    try {
      Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
          .getResources(feiertagePath);

      for (Resource resource : resources) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
          String content = FileCopyUtils.copyToString(reader);
          mappings.add(content);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    List<Feiertag> allFeiertage = new ArrayList<>();
    try {
      for (String mapping : mappings) {
        Feiertag[] feiertage = new ObjectMapper().readValue(mapping, Feiertag[].class);
        allFeiertage.addAll(Arrays.asList(feiertage));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return allFeiertage;
  }
}
