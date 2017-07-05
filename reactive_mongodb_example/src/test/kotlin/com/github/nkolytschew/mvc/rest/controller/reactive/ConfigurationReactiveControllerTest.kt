package com.github.nkolytschew.mvc.rest.controller.reactive


import com.github.nkolytschew.data.document.ConfigurationDocument
import com.github.nkolytschew.data.repository.ConfigurationDocumentRepository
import com.github.nkolytschew.data.repository.RatingDocumentRepository
import com.github.nkolytschew.helper.DocBuilder
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import java.io.IOException


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class ConfigurationReactiveControllerTest {

  private lateinit var webTestClient: WebTestClient

  @Autowired
  lateinit var configurationRepository: ConfigurationDocumentRepository
  @Autowired
  lateinit var ratingRepository: RatingDocumentRepository

  @Before
  fun setUp() {
    // make sure db is empty before each test
    configurationRepository.deleteAll().subscribe()
    ratingRepository.deleteAll().subscribe()

    // create WebTestClient
    this.webTestClient =
        WebTestClient.bindToController(ConfigurationReactiveController(configurationRepository)).build()

    // create one entry
    configurationRepository.save(DocBuilder.getSimpleConfigurationDoc()).subscribe()
    ratingRepository.save(DocBuilder.getSimpleRatingDoc()).subscribe()
  }

  @After
  fun tearDown() {
    configurationRepository.deleteAll().subscribe()
    ratingRepository.deleteAll().subscribe()
  }


  /**
   * test [ConfigurationReactiveController.getConfigurationList] method.
   * call the uri with [WebTestClient] and test if HTTP-Status and responded JSON contain the expected values.
   */
  @Test
  @Throws(IOException::class)
  fun testGetConfigurationList() {
    val expected = DocBuilder.getSimpleConfigurationDoc()
    webTestClient.get().uri("/configurations")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk
        .expectBody()
        .jsonPath("$[0].id").isEqualTo(expected.id)
        .jsonPath("$[0].description").isEqualTo(expected.description)
        .jsonPath("$[0].author").isEqualTo(expected.author)
        .jsonPath("$[0].name").isEqualTo(expected.name)
        // only one entry; element at array pos 2 should not exist
        .jsonPath("$[1].name").doesNotExist()
  }

  /**
   * test [ConfigurationReactiveController.getConfigurationList] method.
   * same as [testGetConfigurationList] but blocking.
   */
  @Test
  @Throws(IOException::class)
  fun testGetConfigurationListBlocking() {
    val cDoc = webTestClient.get().uri("/configurations")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .returnResult<ConfigurationDocument>()
        .responseBody.toIterable().first()
    assertThat(cDoc.author, `is`("nik ko"))
  }

  /**
   * test [ConfigurationReactiveController.getConfigurationById] method.
   * call the uri with [WebTestClient] and test if HTTP-Status and responded JSON contain the expected values.
   */
  @Test
  fun testGetConfigurationById() {
    webTestClient.get().uri("/configurations/simple-test-config-id")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk
        .expectBody()
        .jsonPath("id").isEqualTo("simple-test-config-id")
  }

  /**
   * test [ConfigurationReactiveController.getConfigurationById] method.
   * same as [testGetConfigurationById] but with Id that doesn't exist.
   */
  @Test
  fun testGetConfigurationByIdWhichDoesNotExist() {
    webTestClient.get().uri("/configurations/shazaaam")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk
        .expectBody()
        .jsonPath("id").doesNotExist()
  }

  @Test
  fun testGetConfigurationFromWrongUrl() {
    webTestClient.get().uri("/configuratio/xyz")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound
  }

  private fun getConfigurationDocAsJsonString() =
      "{ " +
          "\"id\": \"i-am-a-random-id\"," +
          "\"name\": \"configuration test name\"," +
          "\"author\": \"john doe\"," +
          "\"description\": \"random description for test case\"," +
          "\"thumbImage\": \"/9j/4AAQSkZJRgABAQAAAQAB...\"," +
          "\"configItem\": \"same as above wohooo...\"" +
          "}"

  /**
   * test [ConfigurationReactiveController.saveConfiguration] method.
   * call the uri with [WebTestClient] and test if HTTP-Status and responded JSON contain the expected values.
   */
  @Test
  fun testSaveConfiguration() {
    this.webTestClient.post().uri("/configurations")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .syncBody(this.getConfigurationDocAsJsonString())
        .exchange()
        .expectStatus().isCreated
        .expectBody()
        .jsonPath("id").isEqualTo("i-am-a-random-id")
        .jsonPath("description").isEqualTo("random description for test case")
        .jsonPath("author").isEqualTo("john doe")
        .jsonPath("name").isEqualTo("configuration test name")
  }

  /**
   * test [ConfigurationReactiveController.saveConfiguration] method.
   * same as [testSaveConfiguration] but with an URL that doesn't exist.
   */
  @Test
  fun testSaveConfigurationWrongUrl() {
    this.webTestClient.post().uri("/configurati")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .syncBody(this.getConfigurationDocAsJsonString())
        .exchange()
        .expectStatus().isNotFound
        .expectBody().isEmpty
  }

  @Test
  fun testUpdateConfiguration() {
    this.webTestClient.put().uri("/configurations")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .syncBody(this.getConfigurationDocAsJsonString())
        .exchange()
        .expectStatus().isCreated
        .expectBody()
        .jsonPath("id").isEqualTo("i-am-a-random-id")
        .jsonPath("description").isEqualTo("random description for test case")
        .jsonPath("author").isEqualTo("john doe")
        .jsonPath("name").isEqualTo("configuration test name")
  }

  @Test
  fun testDeleteConfigurationById() {
    this.webTestClient.delete().uri("/configurations/simple-test-config-id")
        .exchange()
        .expectStatus().isOk
        .expectBody().isEmpty
  }

}