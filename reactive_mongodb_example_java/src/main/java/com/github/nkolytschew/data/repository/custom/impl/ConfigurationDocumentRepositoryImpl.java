package com.github.nkolytschew.data.repository.custom.impl;

import com.github.nkolytschew.data.document.helper.ConfigurationWithRatings;
import com.github.nkolytschew.data.repository.custom.ConfigurationDocumentRepositoryCustom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class ConfigurationDocumentRepositoryImpl implements ConfigurationDocumentRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ConfigurationDocumentRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * currently there is no support for reactive-aggregation.
     *
     * @see <a href="https://jira.spring.io/browse/DATAMONGO-1646">DATAMONGO-1646</a>
     */
    @Override
    public List<ConfigurationWithRatings> getAggregatedConfigurationWithRatingsById(String configurationId) {
        // create lookup
        LookupOperation lookupOperation = LookupOperation
                .newLookup().from("rating").localField("_id").foreignField("configurationId").as("ratings");
        Aggregation aggregation = Aggregation
                .newAggregation(match(where("_id").is(configurationId)), lookupOperation);

        List<ConfigurationWithRatings> results = mongoTemplate
                .aggregate(aggregation, "configuration", ConfigurationWithRatings.class)
                .getMappedResults();

        return results;
    }

}