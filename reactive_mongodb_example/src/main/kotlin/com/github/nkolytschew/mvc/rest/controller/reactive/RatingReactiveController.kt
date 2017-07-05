package com.github.nkolytschew.mvc.rest.controller.reactive

import com.github.nkolytschew.data.document.RatingComment
import com.github.nkolytschew.data.document.RatingDocument
import com.github.nkolytschew.data.repository.RatingDocumentRepository
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(value = "*")
@RequestMapping("ratings")
class RatingReactiveController(val repository: RatingDocumentRepository) {

  /**
   * update/insert new rating comment.
   * get RatingDocument by configurationId.
   * if there is no RatingDocument yet, create a new RatingDocument and add the RatingComment.
   * else add RatingComment to existing RatingDocument.
   * save document.
   */
  @PutMapping("{configurationId}")
  fun updateRating(@RequestBody ratingComment: RatingComment, @PathVariable configurationId: String) =
      repository.findAllByConfigurationIdOrderByCreationDateDesc(configurationId)
          .map { rDoc ->
            // add rating comment to first position and return document
            rDoc.ratingCommentList.add(0, ratingComment)
            rDoc
          }
          .defaultIfEmpty(
              // return new document if there is no rating for configId
              RatingDocument(configurationId = configurationId, ratingCommentList = mutableListOf(ratingComment))
          )
          .map { rDoc ->
            // we need to subscribe, or there will be no entry
            repository.save(rDoc).subscribe()
          }

  /**
   * delete rating comment.
   * get RatingDocument by configurationId.
   * delete RatingComment from List.
   * update document.
   */
  @DeleteMapping("{configurationId}/{ratingCommentId}")
  fun deleteRatingComment(@PathVariable configurationId: String, @PathVariable ratingCommentId: String) =
      repository.findAllByConfigurationIdOrderByCreationDateDesc(configurationId)
          .map { rDoc ->
            // find specified RatingComment, remove it from RatingComment list and return document
            rDoc.ratingCommentList.remove(rDoc.ratingCommentList.find { rCom -> rCom.id.equals(ratingCommentId) })
            rDoc
          }
          .map { rDoc -> repository.save(rDoc).subscribe() }

}