package com.github.nkolytschew.mvc.rest.controller;

import com.github.nkolytschew.data.document.helper.ConfigurationWithRatings;
import com.github.nkolytschew.data.repository.ConfigurationDocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("configurations/details")
class ConfigurationWithRatingController {

    private final ConfigurationDocumentRepository repository;

    @Autowired
    public ConfigurationWithRatingController(ConfigurationDocumentRepository repository) {
        this.repository = repository;
    }

    // getAggregatedConfigurationWithRatingsById is blocking

    /**
     * get aggregated ConfigurationWithRatingDoc.
     * if result is empty return ConfigurationDoc instead.
     */
    @GetMapping("{id}")
    public ConfigurationWithRatings getConfigurationList(@PathVariable String id) {
        return Optional
                .of(repository.getAggregatedConfigurationWithRatingsById(id).get(0))
                .orElse(new ConfigurationWithRatings(repository.findById(id).block(), new ArrayList<>()));
    }

}