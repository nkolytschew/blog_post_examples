package com.github.nkolytschew.data.document

import org.hibernate.validator.constraints.NotEmpty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Document(collection = "rating")
class RatingDocument(
    @Id val id: String? = UUID.randomUUID().toString(),
    // for every configuration, there should only be one rating-document
    @Field @NotEmpty val configurationId: String,
    @Field @NotNull val ratingCommentList: MutableList<RatingComment>,
    @Field var active: Boolean? = true,
    @Field var creationDate: Date? = Date()
)

class RatingComment() {
  @Id var id: String? = UUID.randomUUID().toString()
  @Field @NotEmpty lateinit var email: String
  @Field @NotEmpty lateinit var author: String
  @Field @NotNull var creationDate: Date? = Date()

  @Field @Min(1) @Max(10) @NotEmpty var rating: Int = 0
  @Field @NotEmpty @Size(min = 10) lateinit var description: String
  // set default to true
  @Field var active: Boolean? = true

  // constructors are for convenience
  constructor(email: String,
              author: String,
              rating: Int,
              description: String) : this() {
    this.email = email
    this.author = author
    this.rating = rating
    this.description = description
  }

  constructor(id: String,
              email: String,
              author: String,
              rating: Int,
              description: String) : this(email, author, rating, description) {
    this.id = id
  }


}