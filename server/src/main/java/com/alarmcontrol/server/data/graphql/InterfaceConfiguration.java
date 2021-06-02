package com.alarmcontrol.server.data.graphql;

import com.alarmcontrol.server.notifications.messaging.firebasepush.FirebaseMessageContact;
import com.alarmcontrol.server.aao.config.AaoRule;
import com.alarmcontrol.server.notifications.messaging.mail.MailContact;
import com.alarmcontrol.server.notifications.messaging.teams.TeamsContact;
import com.alarmcontrol.server.notifications.usecases.alertcreated.AlertCreatedNotificationConfig;
import com.coxautodev.graphql.tools.SchemaParserDictionary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfaceConfiguration {

  @Bean
  public SchemaParserDictionary getSchemaParser() {
    SchemaParserDictionary dictionary = new SchemaParserDictionary();
    dictionary.add(MailContact.class);
    dictionary.add(TeamsContact.class);
    dictionary.add(FirebaseMessageContact.class);
    dictionary.add(AlertCreatedNotificationConfig.class);
    dictionary.add(AaoRule.class);
    return dictionary;
  }
}
