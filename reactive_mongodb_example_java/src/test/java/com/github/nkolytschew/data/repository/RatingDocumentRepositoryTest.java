package com.github.nkolytschew.data.repository;

import com.github.nkolytschew.data.document.RatingDocument;
import com.github.nkolytschew.helper.DocBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RatingDocumentRepositoryTest {

    @Autowired
    private RatingDocumentRepository repository;

    @Before
    public void setUp(){
        // make sure db is empty before each test
        repository.deleteAll().block();

        // create one entry
        repository.save(DocBuilder.getSimpleRatingDoc()).block();
    }

    @After
    public void cleanUp(){
        repository.deleteAll().block();
    }

    @Test
    public void findAllByConfigurationIdOrderByCreationDateDesc() throws Exception {
        final RatingDocument expected = DocBuilder.getSimpleRatingDoc();
        // blocking; we have only 1 entry
        final RatingDocument actual = repository.findAllByConfigurationIdOrderByCreationDateDesc("simple-test-config-id").toIterable().iterator().next();


        assertThat(actual.getRatingCommentList().size(), is(expected.getRatingCommentList().size()));
        // reference is not the same
        assertThat(actual.getRatingCommentList().get(2), not(expected.getRatingCommentList().get(2)));
        //values should be same
        assertThat(actual.getRatingCommentList().get(2).getAuthor(), is(expected.getRatingCommentList().get(2).getAuthor()));
        assertThat(actual.getRatingCommentList().get(2).getEmail(), is(expected.getRatingCommentList().get(2).getEmail()));
        assertThat(actual.getRatingCommentList().get(2).getRating(), is(expected.getRatingCommentList().get(2).getRating()));
    }

}