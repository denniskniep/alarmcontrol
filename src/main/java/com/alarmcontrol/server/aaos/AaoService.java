package com.alarmcontrol.server.aaos;

import com.alarmcontrol.server.data.OrganisationConfigurationService;
import com.alarmcontrol.server.notifications.core.Event;
import com.alarmcontrol.server.notifications.core.NotificationBuilder;
import com.alarmcontrol.server.notifications.core.config.NotificationConfig;
import com.alarmcontrol.server.notifications.core.config.NotificationOrganisationConfiguration;
import com.alarmcontrol.server.notifications.core.config.NotificationSubscription;
import com.alarmcontrol.server.notifications.core.config.Contact;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class AaoService implements ApplicationContextAware {

    private Logger logger = LoggerFactory
            .getLogger(AaoService.class);

    private ApplicationContext applicationContext;
    private OrganisationConfigurationService organisationConfigurationService;

    public AaoService(OrganisationConfigurationService organisationConfigurationService) {
        this.organisationConfigurationService = organisationConfigurationService;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


}
