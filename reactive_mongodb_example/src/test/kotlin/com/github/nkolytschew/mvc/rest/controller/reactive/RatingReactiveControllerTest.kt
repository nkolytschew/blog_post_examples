package com.github.nkolytschew.mvc.rest.controller.reactive

import com.github.nkolytschew.data.repository.RatingDocumentRepository
import com.github.nkolytschew.helper.DocBuilder
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient


/**
 * test cases similar to [ConfigurationReactiveControllerTest].
 * test only create and update
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RatingReactiveControllerTest {

  /**
   * unit test controller methods.
   * autowiring won't work for task: check
   */
  private lateinit var webTestClient: WebTestClient


  @Autowired
  lateinit var ratingRepository: RatingDocumentRepository

  @Before
  fun setUp() {
    // make sure db is empty before each test
    ratingRepository.deleteAll().subscribe()

    // create WebTestClient
    this.webTestClient = WebTestClient.bindToController(RatingReactiveController(ratingRepository)).build()

    // create one entry
    ratingRepository.save(DocBuilder.getSimpleRatingDoc()).subscribe()
  }

  @After
  fun tearDown() {
    ratingRepository.deleteAll().subscribe()
  }

  private fun getRatingCommentDocAsJsonString() =
      "{ " +
          "\"email\": \"john_doe@spam_me.de\"," +
          "\"author\": \"jonny bee\"," +
          "\"rating\": \"7\"," +
          "\"description\": \"Lorem ipsum dolor sit amet\" " +
          "}"

  @Test
  fun updateRating() {
    this.webTestClient.put().uri("/ratings/simple-test-config-id")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .syncBody(this.getRatingCommentDocAsJsonString())
        .exchange()
        .expectStatus().isOk
        .expectBody()
        .jsonPath("$[0]disposed").isBoolean
        .jsonPath("$[0]scanAvailable").isBoolean
  }

  @Test
  fun deleteRatingComment() {
    this.webTestClient.delete().uri("/ratings/simple-test-config-id/simpleTest-ID")
        .exchange()
        .expectStatus().isOk
        .expectBody()
        .jsonPath("$[0]disposed").isBoolean
        .jsonPath("$[0]scanAvailable").isBoolean
  }

}