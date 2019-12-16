package com.alarmcontrol.server.aao.graphql;

import com.alarmcontrol.server.aao.AaoConfigurationService;
import com.alarmcontrol.server.aao.config.Keyword;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CatalogKeywordMutation implements GraphQLMutationResolver {

    private AaoConfigurationService aaoConfigurationService;

    public CatalogKeywordMutation(AaoConfigurationService aaoConfigurationService) {
        this.aaoConfigurationService = aaoConfigurationService;
    }

    public List<Keyword> addCatalog(Long organisationId, List<CatalogKeywordInput> keywords) {
      return aaoConfigurationService.addOrReplaceCatalogKeywords(organisationId, keywords);
    }
}
