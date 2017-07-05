package com.github.nkolytschew.data.document.helper

import com.github.nkolytschew.data.document.ConfigurationDocument
import com.github.nkolytschew.data.document.RatingDocument

class ConfigurationWithRatings() {
  lateinit var id: String
  lateinit var name: String
  lateinit var author: String
  lateinit var description: String
  lateinit var thumbImage: String
  lateinit var configItem: String
  lateinit var ratings: MutableList<RatingDocument>

  // constructor is for convenience
  constructor(configDoc: ConfigurationDocument, ratings: MutableList<RatingDocument>) : this() {
    this.id = configDoc.id
    this.name = configDoc.name
    this.author = configDoc.author
    this.description = configDoc.description
    this.thumbImage = configDoc.thumbImage
    this.configItem = configDoc.configItem
    this.ratings = ratings
  }
}