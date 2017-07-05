package com.github.nkolytschew.data.document;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

@Document(collection = "rating")
public class RatingDocument {

    @Id
    private String id = UUID.randomUUID().toString();

    @Field
    @NotEmpty
    private String configurationId;

    @Field
    @NotNull
    private List<RatingComment> ratingCommentList;

    @Field
    @NotNull
    private Date creationDate = new Date();

    @Field
    private Boolean active = true;

    // jpa
    public RatingDocument() {
    }

    public RatingDocument(String configurationId, List<RatingComment> ratingCommentList) {
        this.configurationId = configurationId;
        this.ratingCommentList = ratingCommentList;
    }

    public RatingDocument(String id, String configurationId, List<RatingComment> ratingCommentList, Date creationDate, Boolean active) {
        this.id = id;
        this.configurationId = configurationId;
        this.ratingCommentList = ratingCommentList;
        this.creationDate = creationDate;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(String configurationId) {
        this.configurationId = configurationId;
    }

    public List<RatingComment> getRatingCommentList() {
        return ratingCommentList;
    }

    public void setRatingCommentList(List<RatingComment> ratingCommentList) {
        this.ratingCommentList = ratingCommentList;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}