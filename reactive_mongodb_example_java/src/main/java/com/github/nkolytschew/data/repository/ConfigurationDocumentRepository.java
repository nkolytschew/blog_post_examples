package com.github.nkolytschew.data.repository;

import com.github.nkolytschew.data.document.ConfigurationDocument;
import com.github.nkolytschew.data.repository.custom.ConfigurationDocumentRepositoryCustom;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;

import reactor.core.publisher.Flux;

/**
 * simple mongo repository.
 * <p>
 * provides the main CRUD operations.
 */
public interface ConfigurationDocumentRepository extends ReactiveMongoRepository<ConfigurationDocument, String>, ConfigurationDocumentRepositoryCustom {
    /**
     * find all configurations by name
     */
    Flux<ConfigurationDocument> findAllByName(@Param("name") final String name);

    /**
     * return all configuration ordered by name descending
     */
    Flux<ConfigurationDocument> findAllByOrderByNameDesc();
}
