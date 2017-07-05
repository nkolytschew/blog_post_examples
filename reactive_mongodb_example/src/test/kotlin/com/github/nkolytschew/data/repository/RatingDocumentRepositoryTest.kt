package com.github.nkolytschew.data.repository;

import com.github.nkolytschew.helper.DocBuilder
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class RatingDocumentRepositoryTest {


  @Autowired
  lateinit var repository: RatingDocumentRepository

  @Before
  fun setUp() {
    // make sure db is empty before each test
    repository.deleteAll().block()

    // create one entry
    repository.save(DocBuilder.getSimpleRatingDoc()).block()
  }

  @After
  fun cleanUp() {
    this.repository.deleteAll().block()
  }

  @Test
  fun testFindAllByName() {
    val expected = DocBuilder.getSimpleRatingDoc()
    // blocking; we should only have 1 entry
    val actual = repository.findAllByConfigurationIdOrderByCreationDateDesc("simple-test-config-id").toIterable().first()

    assertThat(actual.ratingCommentList.size, `is`(expected.ratingCommentList.size))
    // reference is not the same
    assertThat(actual.ratingCommentList[2], `not`(expected.ratingCommentList[2]))
    //values should be same
    assertThat(actual.ratingCommentList[2].author, `is`(expected.ratingCommentList[2].author))
    assertThat(actual.ratingCommentList[2].email, `is`(expected.ratingCommentList[2].email))
    assertThat(actual.ratingCommentList[2].rating, `is`(expected.ratingCommentList[2].rating))
  }

}
