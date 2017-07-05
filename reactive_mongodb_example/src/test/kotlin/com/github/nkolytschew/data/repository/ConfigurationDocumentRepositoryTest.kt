package com.github.nkolytschew.data.repository

import com.github.nkolytschew.data.document.RatingComment
import com.github.nkolytschew.data.document.helper.ConfigurationWithRatings
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
import java.text.NumberFormat

@RunWith(SpringRunner::class)
@SpringBootTest
class ConfigurationDocumentRepositoryTest {

  @Autowired
  lateinit var repository: ConfigurationDocumentRepository
  @Autowired
  lateinit var ratingRepository: RatingDocumentRepository

  @Before
  fun setUp() {
    // make sure db is empty before each test
    repository.deleteAll().block()
    ratingRepository.deleteAll().block()

    // create one entry
    repository.save(DocBuilder.getSimpleConfigurationDoc()).block()
    ratingRepository.save(DocBuilder.getSimpleRatingDoc()).block()
  }

  @After
  fun tearDown() {
    repository.deleteAll().block()
    ratingRepository.deleteAll().block()
  }


  @Test
  fun testFindAllByName() {
    val expected = DocBuilder.getSimpleConfigurationDoc()
    // blocking; we should only have 1 entry
    val actual = repository.findAllByName("shazaam").toIterable().first()

    //ref is not the same
    assertThat(actual, `not`(expected))
    // values should be the same
    assertThat(actual.author, `is`(expected.author))
    assertThat(actual.description, `is`(expected.description))
  }

  /**
   * no changes necessary, because [getAggregatedConfigurationWithRatingsById] is blocking.
   */
  @Test
  fun testGetAggregatedConfigurationWithRatingsById() {

    val cDoc = DocBuilder.getSimpleConfigurationDoc()
    val expected = ConfigurationWithRatings(cDoc, mutableListOf(DocBuilder.getSimpleRatingDoc()))
    // there is only 1 entry
    val actual = repository.getAggregatedConfigurationWithRatingsById("simple-test-config-id").first()

    assertThat(actual.ratings.size, `is`(1))
    assertThat(actual.ratings.first().ratingCommentList.size, `is`(6))

    // ref not the same
    assertThat(actual, `not`(expected))
    // values should be same
    assertThat(actual.description, `is`(expected.description))
    assertThat(actual.author, `is`(expected.author))
    assertThat(actual.ratings.first().ratingCommentList[2].email, `is`(expected.ratings.first().ratingCommentList[2].email))
    assertThat(actual.ratings.first().ratingCommentList[3].rating, `is`(expected.ratings.first().ratingCommentList[3].rating))
    assertThat(actual.ratings.first().ratingCommentList[4].description, `is`(expected.ratings.first().ratingCommentList[4].description))
    assertThat(actual.ratings.first().ratingCommentList[4].description, `not`(expected.ratings.first().ratingCommentList[1].description))

    var amountOfRatings: Int = 0
    val avgSumRating = actual.ratings
        .map { ratingDocument ->
          amountOfRatings += ratingDocument.ratingCommentList.size
          ratingDocument.ratingCommentList
              .map { ratingComment: RatingComment -> ratingComment.rating }
              .sum()
        }.sum() / amountOfRatings

    assertThat(avgSumRating, `is`(8))

    val avgRating = actual.ratings
        .map { ratingDocument ->
          amountOfRatings += ratingDocument.ratingCommentList.size
          ratingDocument.ratingCommentList
              .map { ratingComment: RatingComment -> ratingComment.rating }
              .average()
        }.sum()
    val nf = NumberFormat.getNumberInstance()
    nf.maximumFractionDigits = 2
    assertThat(nf.format(avgRating), `is`("8,33"))

  }
}