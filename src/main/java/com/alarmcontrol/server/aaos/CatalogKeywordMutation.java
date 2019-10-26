package com.alarmcontrol.server.aaos;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CatalogKeywordMutation implements GraphQLMutationResolver {

    private AaoConfigurationService aaoConfigurationService;

    public CatalogKeywordMutation(AaoConfigurationService aaoConfigurationService) {
        this.aaoConfigurationService = aaoConfigurationService;
    }

    public String addCatalog(Long organisationId, List<CatalogKeywordInput> keywords) {
        aaoConfigurationService.addOrReplaceCatalogKeywords(organisationId, keywords);
        return "Dummy";
    }
}
