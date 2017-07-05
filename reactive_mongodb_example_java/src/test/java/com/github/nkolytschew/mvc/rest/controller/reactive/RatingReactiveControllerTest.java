package com.github.nkolytschew.mvc.rest.controller.reactive;

import com.github.nkolytschew.data.document.RatingDocument;
import com.github.nkolytschew.data.repository.RatingDocumentRepository;
import com.github.nkolytschew.helper.DocBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * test cases similar to [ConfigurationReactiveControllerTest].
 * test only create and update
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RatingReactiveControllerTest {

    private static final String RATING_DOC_AS_JSON_STRING =
            "{ " +
                    "\"email\": \"john_doe@spam_me.de\"," +
                    "\"author\": \"jonny bee\"," +
                    "\"rating\": \"7\"," +
                    "\"description\": \"Lorem ipsum dolor sit amet\" " +
                    "}";

    private WebTestClient webTestClient;

    @Autowired
    private RatingDocumentRepository ratingRepository;

    @Before
    public void setUp() throws Exception {
        // make sure db is empty before each test
        ratingRepository.deleteAll().subscribe();

        // create WebTestClient
        this.webTestClient = WebTestClient.bindToController(new RatingReactiveController(ratingRepository)).build();

        // create one entry
        ratingRepository.save(DocBuilder.getSimpleRatingDoc()).subscribe();
    }

    @After
    public void tearDown() throws Exception {
        ratingRepository.deleteAll().subscribe();
    }

    @Test
    public void updateRating() throws Exception {
        assertThat(ratingRepository.findAll().toIterable().iterator().next().getRatingCommentList().size(), is(6));

        final RatingDocument actual = this.webTestClient.put().uri("/ratings/simple-test-config-id")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(RATING_DOC_AS_JSON_STRING)
                .exchange()
                .expectStatus().isOk()
                .returnResult(RatingDocument.class)
                .getResponseBody().toIterable().iterator().next();

        assertThat(actual.getRatingCommentList().size(), is(7));
        assertThat(actual.getRatingCommentList().get(0).getEmail(), is("john_doe@spam_me.de"));
        assertThat(actual.getRatingCommentList().get(0).getAuthor(), is("jonny bee"));
        assertThat(actual.getRatingCommentList().get(0).getRating(), is(7));
    }

    @Test
    public void deleteRatingComment() throws Exception {
        assertThat(ratingRepository.findAll().toIterable().iterator().next().getRatingCommentList().size(), is(6));
        final RatingDocument deletedDoc1 = this.webTestClient.delete().uri("/ratings/simple-test-config-id/simpleTest-ID")
                .exchange()
                .expectStatus().isOk()
                .returnResult(RatingDocument.class)
                .getResponseBody().toIterable().iterator().next();

        assertThat(deletedDoc1.getRatingCommentList().get(0).getId(), is("1"));
        assertThat(deletedDoc1.getRatingCommentList().get(0).getEmail(), is("muster@spam_me.de"));
        assertThat(deletedDoc1.getRatingCommentList().get(0).getAuthor(), is("muster"));
        assertThat(deletedDoc1.getRatingCommentList().get(0).getRating(), is(8));

        assertThat(ratingRepository.findAll().toIterable().iterator().next().getRatingCommentList().size(), is(5));

        this.webTestClient.delete().uri("/ratings/simple-test-config-id/3")
                .exchange()
                .expectStatus().isOk();
        assertThat(ratingRepository.findAll().toIterable().iterator().next().getRatingCommentList().size(), is(4));

        final RatingDocument deletedDoc2 = this.webTestClient.delete().uri("/ratings/simple-test-config-id/1")
                .exchange()
                .expectStatus().isOk()
                .returnResult(RatingDocument.class)
                .getResponseBody().toIterable().iterator().next();
        assertThat(ratingRepository.findAll().toIterable().iterator().next().getRatingCommentList().size(), is(3));
        assertThat(deletedDoc2.getRatingCommentList().get(0).getId(), is("2"));
        assertThat(deletedDoc2.getRatingCommentList().get(0).getEmail(), is("maxi.muster@spam_me.de"));
        assertThat(deletedDoc2.getRatingCommentList().get(0).getAuthor(), is("maxi muster"));
        assertThat(deletedDoc2.getRatingCommentList().get(0).getRating(), is(9));
    }

}