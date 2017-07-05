package com.github.nkolytschew.data.repository.custom

import com.github.nkolytschew.data.document.helper.ConfigurationWithRatings
import reactor.core.publisher.Flux

interface ConfigurationDocumentRepositoryCustom {

  fun getAggregatedConfigurationWithRatingsById(configurationId: String): List<ConfigurationWithRatings>

}