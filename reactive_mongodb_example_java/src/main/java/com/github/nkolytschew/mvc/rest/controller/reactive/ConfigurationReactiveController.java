package com.github.nkolytschew.mvc.rest.controller.reactive;

import com.github.nkolytschew.data.document.ConfigurationDocument;
import com.github.nkolytschew.data.repository.ConfigurationDocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("configurations")
public class ConfigurationReactiveController {

    private final ConfigurationDocumentRepository repository;

    @Autowired
    public ConfigurationReactiveController(ConfigurationDocumentRepository repository) {
        this.repository = repository;
    }

    /**
     * get all configuration
     */
    @GetMapping
    public Flux<ConfigurationDocument> getConfigurationList() {
        return repository.findAll();
    }

    /**
     * get configuration with a specific id
     */
    @GetMapping("{id}")
    public Mono<ConfigurationDocument> getConfigurationById(@PathVariable String id) {
        return repository.findById(id);
    }

    /**
     * insert new configuration
     */
    @PostMapping
    public Mono<ResponseEntity<ConfigurationDocument>> saveConfiguration(@RequestBody ConfigurationDocument config) {
        return this.repository.save(config).map(cDoc -> new ResponseEntity<ConfigurationDocument>(cDoc, HttpStatus.CREATED));
    }

    /**
     * update existing configuration
     */
    @PutMapping
    public Mono<ResponseEntity<ConfigurationDocument>> updateConfiguration(@RequestBody ConfigurationDocument config) {
        return this.repository.save(config).map(cDoc -> new ResponseEntity<ConfigurationDocument>(cDoc, HttpStatus.CREATED));
    }

    /**
     * delete configuration by id
     */
    @DeleteMapping("{id}")
    public Mono<Void> deleteConfigurationById(@PathVariable String id) {
        return repository.deleteById(id);
    }

    /**
     * playground
     */

    /**
     * infinite stream of configuration id and configuration name.
     * print a configuration every second.
     * repeat if end of Flux is reached.
     */
    @GetMapping(value = "infiniteStream/{seconds}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tuple2<Long, String>> getInfiniteStreamOfConfigurationsDelayed(@PathVariable Long seconds) {
        return Flux.interval(Duration.ofSeconds(1))
                .zipWith(repository.findAll().delayElements(Duration.ofSeconds(seconds)).map(t -> "id: " + t.getId() + " :: " + t.getName()))
                .repeat()
                .log();
    }

    /**
     * get all configuration ordered by name descending and delayed with x-seconds
     */
    @GetMapping("/delay/{seconds}")
    public Flux<ConfigurationDocument> getConfigurationListDelayed(@PathVariable Long seconds) {
        return repository.findAllByOrderByNameDesc().delaySubscription(Duration.ofSeconds(seconds));
    }

    /**
     * get all configuration as stream. with a delay of X seconds
     */
    @GetMapping(value = "stream/delayed/{seconds}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ConfigurationDocument> getConfigurationListAsDelayedStream(@PathVariable Long seconds) {
        return repository.findAllByOrderByNameDesc().delayElements(Duration.ofSeconds(seconds)).log();
    }

}
