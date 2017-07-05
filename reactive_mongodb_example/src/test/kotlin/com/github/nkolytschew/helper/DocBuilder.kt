package com.github.nkolytschew.helper

import com.github.nkolytschew.data.document.ConfigurationDocument
import com.github.nkolytschew.data.document.RatingComment
import com.github.nkolytschew.data.document.RatingDocument

class DocBuilder {

  // get simple Configuration-Document
  companion object {
    fun getSimpleConfigurationDoc() = ConfigurationDocument("simple-test-config-id",
        "shazaam",
        "nik ko",
        "awesome ",
        "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmN...",
        "/9j/4AAQSkZJRgABAQEAYABgAAD...")

    // get simple Rating-Document for to test custom-repository function
    fun getSimpleRatingDoc() = RatingDocument(
        "simple-test-rating-id",
        "simple-test-config-id", // advantage of using a simple string field: we don't have to provide an existing id
        arrayListOf(
            RatingComment("simpleTest-ID", "maxi@spam_me.de", "maxi", 7, "Lorem ipsum dolor sit amet"),
            RatingComment("muster@spam_me.de", "muster", 8, "consetetur sadipscing elitr"),
            RatingComment("maxi.muster@spam_me.de", "maxi muster", 9, "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat"),
            RatingComment("john@spam_me.de", "john", 7, "sed diam voluptua"),
            RatingComment("doe@spam_me.de", "doe", 9, "At vero eos et accusam et justo duo dolores et ea rebum"),
            RatingComment("john doe@spam_me.de", "john doe", 10, "Stet clita kasd gubergren")
        )

    )

  }


}