package com.github.nkolytschew.data.repository;

import com.github.nkolytschew.data.document.RatingDocument;

import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

/**
 * simple mongo repository.
 * <p>
 * provides the main CRUD operations.
 */
public interface RatingDocumentRepository extends ReactiveCrudRepository<RatingDocument, String> {
    /**
     * return all ratingsDocs with corresponding configurationId
     */
    Flux<RatingDocument> findAllByConfigurationIdOrderByCreationDateDesc(@Param("configurationId") String configurationId);
}