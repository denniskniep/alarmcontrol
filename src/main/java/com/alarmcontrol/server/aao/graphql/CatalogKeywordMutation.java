package com.alarmcontrol.server.aao.graphql;

import com.alarmcontrol.server.aao.AaoConfigurationService;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

import java.util.List;

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
