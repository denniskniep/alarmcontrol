package com.alarmcontrol.server.notifications.messaging.firebasepush;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FirebaseClientConfigurationResolver implements GraphQLQueryResolver {

  @Value("${notifications.firebase.projectId:}")
  private String projectId;

  @Value("${notifications.firebase.publicApiKey:}")
  private String publicApiKey;

  @Value("${notifications.firebase.messagingSenderId:}")
  private String messagingSenderId;

  @Value("${notifications.firebase.appId:}")
  private String appId;

  @Value("${notifications.pagerUrl:}")
  private String pagerUrl;

  public FirebaseClientConfiguration firebaseClientConfiguration() {
    if(StringUtils.isAnyBlank(projectId, publicApiKey, messagingSenderId, messagingSenderId, pagerUrl)){
      return null;
    }

    return new FirebaseClientConfiguration(publicApiKey, projectId, messagingSenderId, appId, pagerUrl);
  }
}

