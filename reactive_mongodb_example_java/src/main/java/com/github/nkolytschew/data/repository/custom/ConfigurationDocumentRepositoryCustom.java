package com.github.nkolytschew.data.repository.custom;

import com.github.nkolytschew.data.document.helper.ConfigurationWithRatings;

import org.springframework.stereotype.Repository;

import java.util.List;

public interface ConfigurationDocumentRepositoryCustom {

	List<ConfigurationWithRatings> getAggregatedConfigurationWithRatingsById(String configurationId);

}