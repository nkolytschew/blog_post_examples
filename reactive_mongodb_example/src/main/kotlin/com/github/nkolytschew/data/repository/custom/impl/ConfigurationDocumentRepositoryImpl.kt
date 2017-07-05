package com.github.nkolytschew.data.repository.custom.impl

import com.github.nkolytschew.data.document.helper.ConfigurationWithRatings
import com.github.nkolytschew.data.repository.custom.ConfigurationDocumentRepositoryCustom
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.match
import org.springframework.data.mongodb.core.aggregation.LookupOperation
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.stereotype.Repository

class ConfigurationDocumentRepositoryImpl(val template: MongoTemplate) : ConfigurationDocumentRepositoryCustom {

  /**
   * currently there is no support for reactive-aggregation.
   * @see https://jira.spring.io/browse/DATAMONGO-1646
   */
  override fun getAggregatedConfigurationWithRatingsById(configurationId: String): List<ConfigurationWithRatings> {
    // create lookup
    val lookupOperation = LookupOperation
        .newLookup().from("rating").localField("_id").foreignField("configurationId").`as`("ratings")
    val aggregation = Aggregation
        .newAggregation(match(where("_id").`is`(configurationId)), lookupOperation)

    val results = template
        .aggregate(aggregation, "configuration", ConfigurationWithRatings::class.javaObjectType)
        .mappedResults

    return results
  }
}