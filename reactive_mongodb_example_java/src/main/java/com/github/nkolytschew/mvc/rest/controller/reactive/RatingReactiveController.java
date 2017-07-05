package com.github.nkolytschew.mvc.rest.controller.reactive;

import com.github.nkolytschew.data.document.RatingComment;
import com.github.nkolytschew.data.document.RatingDocument;
import com.github.nkolytschew.data.repository.RatingDocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Optional;

import reactor.core.publisher.Flux;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("ratings")
class RatingReactiveController {

    private final RatingDocumentRepository repository;

    @Autowired
    RatingReactiveController(RatingDocumentRepository repository) {
        this.repository = repository;
    }

    /**
     * update/insert new rating comment.
     * get RatingDocument by configurationId.
     * if there is no RatingDocument yet, create a new RatingDocument and add the RatingComment.
     * else add RatingComment to existing RatingDocument.
     * save document.
     */
    @PutMapping("{configurationId}")
    public Flux<RatingDocument> updateRating(@RequestBody RatingComment ratingComment, @PathVariable String configurationId) {
        return repository.findAllByConfigurationIdOrderByCreationDateDesc(configurationId)
                .map(rDoc -> {
                            // add rating comment to first position and return document
                            rDoc.getRatingCommentList().add(0, ratingComment);
                            return rDoc;
                        }
                )
                .defaultIfEmpty(
                        // return new document if there is no rating for configId
                        new RatingDocument(configurationId, Collections.singletonList(ratingComment))
                )
                .map(ratingDocument -> {
                            // we need to subscribe, or there will be no entry
                            repository.save(ratingDocument).subscribe();
                            return ratingDocument;
                        }
                );
    }

    /**
     * delete rating comment.
     * get RatingDocument by configurationId.
     * delete RatingComment from List.
     * update document.
     */
    @DeleteMapping("{configurationId}/{ratingCommentId}")
    public Flux<RatingDocument> deleteRatingComment(@PathVariable String configurationId, @PathVariable String ratingCommentId) {
        return repository.findAllByConfigurationIdOrderByCreationDateDesc(configurationId)
                .map(rDoc -> {
                    final RatingComment deleteMe = rDoc.getRatingCommentList().stream().filter(rCom -> rCom.getId().equalsIgnoreCase(ratingCommentId)).findFirst().get();
                    rDoc.getRatingCommentList().remove(deleteMe);
                    repository.save(rDoc).subscribe();
                    return rDoc;
                });
    }
}