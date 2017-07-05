package com.github.nkolytschew.data.repository

import com.github.nkolytschew.data.document.RatingDocument
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.repository.query.Param
import reactor.core.publisher.Flux

/**
 * simple mongo repository.
 *
 * provides the main CRUD operations.
 */
interface RatingDocumentRepository : ReactiveMongoRepository<RatingDocument, String> {
  /**
   * return all ratingsDocs with corresponding configurationId
   */
  fun findAllByConfigurationIdOrderByCreationDateDesc(@Param("configurationId") configurationId: String): Flux<RatingDocument>
}