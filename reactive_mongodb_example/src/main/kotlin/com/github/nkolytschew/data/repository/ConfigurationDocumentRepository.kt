package com.github.nkolytschew.data.repository

import com.github.nkolytschew.data.document.ConfigurationDocument
import com.github.nkolytschew.data.repository.custom.ConfigurationDocumentRepositoryCustom
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

/**
 * simple mongo repository.
 *
 * provides the main CRUD operations.
 */
interface ConfigurationDocumentRepository : ReactiveMongoRepository<ConfigurationDocument, String>, ConfigurationDocumentRepositoryCustom {
  /**
   * find all configurations by name
   */
  fun findAllByName(@Param("name") name: String): Flux<ConfigurationDocument>

  /**
   * return all configuration ordered by name descending
   */
  fun findAllByOrderByNameDesc(): Flux<ConfigurationDocument>
}