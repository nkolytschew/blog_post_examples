package com.github.nkolytschew.mvc.rest.controller.reactive

import com.github.nkolytschew.data.document.ConfigurationDocument
import com.github.nkolytschew.data.repository.ConfigurationDocumentRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.*

@RestController
@CrossOrigin(value = "*")
@RequestMapping("configurations")
class ConfigurationReactiveController(val repository: ConfigurationDocumentRepository) {


  /**
   * get all configuration
   */
  @GetMapping
  fun getConfigurationList() = repository.findAll()

  /**
   * get configuration with a specific id
   */
  @GetMapping("{id}")
  fun getConfigurationById(@PathVariable id: String) = repository.findById(id)

  /**
   * insert new configuration
   */
  @PostMapping
  fun saveConfiguration(@RequestBody config: ConfigurationDocument): Mono<ResponseEntity<ConfigurationDocument>> {
    // set date and active manually; because kotlin won't set them due to no-arg plugin
    if (config.creationDate == null) config.creationDate = Date()
    if (config.active == null) config.active = true

//    return this.repository.save(config)
    return this.repository.save(config)
        .map { cDoc -> ResponseEntity<ConfigurationDocument>(cDoc, HttpStatus.CREATED) }
  }

  /**
   * update existing configuration
   */
  @PutMapping
  fun updateConfiguration(@RequestBody config: ConfigurationDocument): Mono<ResponseEntity<ConfigurationDocument>> {
    // set date and active manually; because kotlin won't set them due to no-arg plugin
    if (config.creationDate == null) config.creationDate = Date()
    if (config.active == null) config.active = true

    return this.repository.save(config)
        .map { cDoc -> ResponseEntity<ConfigurationDocument>(cDoc, HttpStatus.CREATED) }
  }

  /**
   * delete configuration by id
   */
  @DeleteMapping("{id}")
  fun deleteConfigurationById(@PathVariable id: String) = repository.deleteById(id)


  /**
   * playground
   */

  /**
   * infinite stream of configuration id and configuration name.
   * print a configuration every second.
   * repeat if end of Flux is reached.
   */
  @GetMapping(value = "infiniteStream/{seconds}", produces = arrayOf(MediaType.TEXT_EVENT_STREAM_VALUE))
  fun getInfiniteStreamOfConfigurationsDelayed(@PathVariable seconds: Long) = Flux.interval(Duration.ofSeconds(1))
      .zipWith(repository.findAll().delayElements(Duration.ofSeconds(seconds)).map { t: ConfigurationDocument -> "id: " + t.id + " :: " + t.name })
      .repeat()
      .log()


  /**
   * get all configuration ordered by name descending and delayed with x-seconds
   */
  @GetMapping("/delay/{seconds}")
  fun getConfigurationListDelayed(@PathVariable seconds: Long) = repository.findAllByOrderByNameDesc().delaySubscription(Duration.ofSeconds(seconds))


  /**
   * get all configuration as stream. with a delay of X seconds
   */
  @GetMapping(value = "stream/delayed/{seconds}", produces = arrayOf(MediaType.TEXT_EVENT_STREAM_VALUE))
  fun getConfigurationListAsDelayedStream(@PathVariable seconds: Long) = repository.findAllByOrderByNameDesc()
      .delayElements(Duration.ofSeconds(seconds))
      .log()

}