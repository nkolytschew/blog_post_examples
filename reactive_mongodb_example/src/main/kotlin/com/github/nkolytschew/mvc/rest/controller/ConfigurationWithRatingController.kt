package com.github.nkolytschew.mvc.rest.controller

import com.github.nkolytschew.data.repository.ConfigurationDocumentRepository
import org.springframework.web.bind.annotation.*

@CrossOrigin(value = "*")
@RestController
@RequestMapping("configurations/details")
class ConfigurationWithRatingController(val repository: ConfigurationDocumentRepository) {

  // getAggregatedConfigurationWithRatingsById is blocking
  /**
   * get aggregated ConfigurationWithRatingDoc.
   * if result is empty return ConfigurationDoc instead.
   */
  @GetMapping("{id}")
  fun getConfigurationWithRating(@PathVariable id: String) = repository.getAggregatedConfigurationWithRatingsById(id).getOrElse(0) { repository.findById(id).subscribe() }
}