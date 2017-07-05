package com.github.nkolytschew.data.document;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.UUID;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RatingComment {

    @Id
    @NotNull
    private String id = UUID.randomUUID().toString();

    @Field
    @NotEmpty
    private String email;

    @Field
    @NotEmpty
    private String author;

    @Field
    @Min(1)
    @Max(10)
    @NotEmpty
    private Integer rating;

    @Field
    @NotEmpty
    @Size(min = 10)
    private String description;

    @Field
    @NotNull
    private Date creationDate = new Date();

    @Field
    private Boolean active = true;

    // jpa
    public RatingComment() {
    }

    public RatingComment(String id, String email, String author, Integer rating, String description, Date creationDate, Boolean active) {
        this.id = id;
        this.email = email;
        this.author = author;
        this.rating = rating;
        this.description = description;
        this.creationDate = creationDate;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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