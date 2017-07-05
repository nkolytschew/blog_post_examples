package com.github.nkolytschew.mvc.rest.controller.reactive;

import com.github.nkolytschew.data.document.ConfigurationDocument;
import com.github.nkolytschew.data.repository.ConfigurationDocumentRepository;
import com.github.nkolytschew.data.repository.RatingDocumentRepository;
import com.github.nkolytschew.helper.DocBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ConfigurationReactiveControllerTest {

    private static final String CONFIG_DOC_AS_JSON_STRING =
            "{ " +
                    "\"id\": \"i-am-a-random-id\"," +
                    "\"name\": \"configuration test name\"," +
                    "\"author\": \"john doe\"," +
                    "\"description\": \"random description for test case\"," +
                    "\"thumbImage\": \"/9j/4AAQSkZJRgABAQAAAQAB...\"," +
                    "\"configItem\": \"same as above wohooo...\"" +
                    "}";

    private WebTestClient webTestClient;

    @Autowired
    private ConfigurationDocumentRepository configurationRepository;
    @Autowired
    private RatingDocumentRepository ratingRepository;

    @Before
    public void setUp() throws Exception {
        // make sure db is empty before each test
        configurationRepository.deleteAll().subscribe();
        ratingRepository.deleteAll().subscribe();

        // create WebTestClient
        this.webTestClient = WebTestClient.bindToController(new ConfigurationReactiveController(configurationRepository)).build();

        // create one entry
        configurationRepository.save(DocBuilder.getSimpleConfigurationDoc()).subscribe();
        ratingRepository.save(DocBuilder.getSimpleRatingDoc()).subscribe();
    }

    @After
    public void tearDown() throws Exception {
        configurationRepository.deleteAll().subscribe();
        ratingRepository.deleteAll().subscribe();
    }

    /**
     * test [ConfigurationReactiveController.getConfigurationList] method.
     * call the uri with [WebTestClient] and test if HTTP-Status and responded JSON contain the expected values.
     */
    @Test
    public void testGetConfigurationList() throws Exception {
        final ConfigurationDocument expected = DocBuilder.getSimpleConfigurationDoc();
        webTestClient.get().uri("/configurations")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(expected.getId())
                .jsonPath("$[0].description").isEqualTo(expected.getDescription())
                .jsonPath("$[0].author").isEqualTo(expected.getAuthor())
                .jsonPath("$[0].name").isEqualTo(expected.getName())
                // only one entry; element at array pos 2 should not exist
                .jsonPath("$[1].name").doesNotExist();
    }

    /**
     * test [ConfigurationReactiveController.getConfigurationList] method.
     * same as [testGetConfigurationList] but blocking.
     */
    @Test
    public void testGetConfigurationListBlocking() throws Exception {
        final ConfigurationDocument cDoc = webTestClient.get().uri("/configurations")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .returnResult(ConfigurationDocument.class)
                .getResponseBody().toIterable().iterator().next();
        assertThat(cDoc.getAuthor(), is("nik ko"));
    }

    /**
     * test [ConfigurationReactiveController.getConfigurationById] method.
     * call the uri with [WebTestClient] and test if HTTP-Status and responded JSON contain the expected values.
     */
    @Test
    public void testGetConfigurationById() throws Exception {
        webTestClient.get().uri("/configurations/simple-test-config-id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo("simple-test-config-id");
    }

    /**
     * test [ConfigurationReactiveController.getConfigurationById] method.
     * same as [testGetConfigurationById] but with Id that doesn't exist.
     */
    @Test
    public void testGetConfigurationByIdWhichDoesNotExist() throws Exception {
        webTestClient.get().uri("/configurations/shazaaam")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").doesNotExist();
    }

    @Test
    public void testGetConfigurationFromWrongUrl() throws Exception {
        webTestClient.get().uri("/configuratio/xyz")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    /**
     * test [ConfigurationReactiveController.saveConfiguration] method.
     * call the uri with [WebTestClient] and test if HTTP-Status and responded JSON contain the expected values.
     */
    @Test
    public void testSaveConfiguration() throws Exception {
        this.webTestClient.post().uri("/configurations")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(this.CONFIG_DOC_AS_JSON_STRING)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("id").isEqualTo("i-am-a-random-id")
                .jsonPath("description").isEqualTo("random description for test case")
                .jsonPath("author").isEqualTo("john doe")
                .jsonPath("name").isEqualTo("configuration test name");
    }

    /**
     * test [ConfigurationReactiveController.saveConfiguration] method.
     * same as [testSaveConfiguration] but with an URL that doesn't exist.
     */
    @Test
    public void testSaveConfigurationWrongUrl() throws Exception {
        this.webTestClient.post().uri("/configurati")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(CONFIG_DOC_AS_JSON_STRING)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }

    @Test
    public void testUpdateConfiguration() throws Exception {
        this.webTestClient.put().uri("/configurations")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(CONFIG_DOC_AS_JSON_STRING)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("id").isEqualTo("i-am-a-random-id")
                .jsonPath("description").isEqualTo("random description for test case")
                .jsonPath("author").isEqualTo("john doe")
                .jsonPath("name").isEqualTo("configuration test name");
    }

    @Test
    public void testDeleteConfigurationById() throws Exception {
        this.webTestClient.delete().uri("/configurations/simple-test-config-id")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }

}