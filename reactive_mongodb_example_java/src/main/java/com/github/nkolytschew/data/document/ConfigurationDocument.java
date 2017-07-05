package com.github.nkolytschew.data.document;

import org.bson.types.Binary;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.UUID;

import javax.validation.constraints.NotNull;

/**
 * pojo.
 * <p>
 * defines the structure for our document entries.
 */
@Document(collection = "configuration")
public class ConfigurationDocument {
    @Id
    private String id = UUID.randomUUID().toString();

    @Field
    @Indexed
    @NotEmpty
    private String name;

    @Field
    @NotEmpty
    private String description;

    @Field
    @NotEmpty
    private String author;

    @Field
    @NotNull
    private Date creationDate = new Date();

    // use bson.type Binary to avoid ClassConversionErrors while mapping from binary to ByteArray
    // https://jira.spring.io/browse/DATAMONGO-730
    @Field
    private String thumbImage;
    @Field
    @NotNull
    private String configItem;

    @Field
    private Boolean active = true;

    // jpa
    public ConfigurationDocument() {
    }

    public ConfigurationDocument(String id, String name, String description, String author, Date creationDate, String thumbImage, String configItem, Boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.author = author;
        this.creationDate = creationDate;
        this.thumbImage = thumbImage;
        this.configItem = configItem;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public String getConfigItem() {
        return configItem;
    }

    public void setConfigItem(String configItem) {
        this.configItem = configItem;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
