package com.github.nkolytschew.data.document

import org.hibernate.validator.constraints.NotEmpty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*
import javax.validation.constraints.NotNull

/**
 * poko - plain old kotlin object ;).
 *
 * defines the structure for our document entries.
 */
@Document(collection = "configuration")
class ConfigurationDocument() {
  // make this fields immutable
  @Id var id: String = UUID.randomUUID().toString()
  @Field @Indexed @NotEmpty lateinit var name: String
  @Field @NotEmpty lateinit var author: String

  // mutable fields; can be modified before create/update
  @Field @NotNull var creationDate: Date? = Date()
  @Field @NotEmpty lateinit var description: String

  // use String instead of bson.type Binary; since we read a base64-String anyway
  @Field @NotNull lateinit var thumbImage: String
  @Field @NotNull lateinit var configItem: String

  // set default to true
  @Field @NotNull var active: Boolean? = true


  // constructors are for convenience
  constructor(name: String,
              author: String,
              description: String,
              thumbImage: String,
              configItem: String) : this() {
    this.name = name
    this.author = author
    this.description = description
    this.thumbImage = thumbImage
    this.configItem = configItem
  }

  constructor(id: String,
              name: String,
              author: String,
              description: String,
              thumbImage: String,
              configItem: String) : this(name, author, description, thumbImage, configItem) {
    this.id = id
  }
}