package com.github.nkolytschew.data.repository;

import com.github.nkolytschew.data.document.ConfigurationDocument;
import com.github.nkolytschew.data.document.helper.ConfigurationWithRatings;
import com.github.nkolytschew.helper.DocBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.NumberFormat;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigurationDocumentRepositoryTest {

    @Autowired
    private ConfigurationDocumentRepository repository;
    @Autowired
    private RatingDocumentRepository ratingRepository;

    @Before
    public void setUp() {
        // make sure db is empty before each test
        repository.deleteAll().block();
        ratingRepository.deleteAll().block();

        // create one entry
        repository.save(DocBuilder.getSimpleConfigurationDoc()).block();
        ratingRepository.save(DocBuilder.getSimpleRatingDoc()).block();
    }

    @After
    public void tearDown() {
        repository.deleteAll().block();
        ratingRepository.deleteAll().block();
    }

    @Test
    public void findAllByName() throws Exception {

        final ConfigurationDocument expected = DocBuilder.getSimpleConfigurationDoc();
        // blocking; we should only have 1 entry
        final ConfigurationDocument actual = repository.findAllByName("shazaam").toIterable().iterator().next();

        //ref is not the same
        assertThat(actual, not(expected));
        // values should be the same
        assertThat(actual.getAuthor(), is(expected.getAuthor()));
        assertThat(actual.getDescription(), is(expected.getDescription()));
    }

    /**
     * no changes necessary, because [getAggregatedConfigurationWithRatingsById] is blocking.
     */
    @Test
    public void findAllByOrderByNameDesc() throws Exception {
        final ConfigurationDocument cDoc = DocBuilder.getSimpleConfigurationDoc();
        final ConfigurationWithRatings expected = new ConfigurationWithRatings(cDoc, Collections.singletonList(DocBuilder.getSimpleRatingDoc()));

        // there is only 1 entry
        final ConfigurationWithRatings actual = repository.getAggregatedConfigurationWithRatingsById("simple-test-config-id").get(0);

        assertThat(actual.getRatings().size(), is(1));
        assertThat(actual.getRatings().get(0).getRatingCommentList().size(), is(6));

        // ref not the same
        assertThat(actual, not(expected));
        // values should be same
        assertThat(actual.getDescription(), is(expected.getDescription()));
        assertThat(actual.getAuthor(), is(expected.getAuthor()));
        assertThat(actual.getRatings().get(0).getRatingCommentList().get(2).getEmail(), is(expected.getRatings().get(0).getRatingCommentList().get(2).getEmail()));
        assertThat(actual.getRatings().get(0).getRatingCommentList().get(3).getRating(), is(expected.getRatings().get(0).getRatingCommentList().get(3).getRating()));
        assertThat(actual.getRatings().get(0).getRatingCommentList().get(4).getDescription(), is(expected.getRatings().get(0).getRatingCommentList().get(4).getDescription()));
        assertThat(actual.getRatings().get(0).getRatingCommentList().get(4).getDescription(), not(expected.getRatings().get(0).getRatingCommentList().get(1).getDescription()));

        final int[] amountOfRatings = new int[] { 0 };
        int avgSumRating = actual.getRatings()
                .stream().mapToInt(ratingDocument -> {
                    amountOfRatings[0] += ratingDocument.getRatingCommentList().size();
                    return ratingDocument.getRatingCommentList().stream().mapToInt(ratingComment -> ratingComment.getRating()).sum();
                })
                .sum() / amountOfRatings[0];
        assertThat(avgSumRating, is(8));

        double avgRating = actual.getRatings()
                .stream()
                .mapToDouble(ratingDocument -> ratingDocument.getRatingCommentList().stream().mapToInt(ratingComment -> ratingComment.getRating()).average().getAsDouble())
                .sum();

        final NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);

        assertThat(nf.format(avgRating), is("8,33"));

    }

}