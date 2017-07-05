package com.github.nkolytschew.data.document.helper;

import com.github.nkolytschew.data.document.ConfigurationDocument;
import com.github.nkolytschew.data.document.RatingComment;
import com.github.nkolytschew.data.document.RatingDocument;

import java.util.List;

public class ConfigurationWithRatings {

    private String id;
    private String name;
    private String author;
    private String description;
    private String thumbImage;
    private String configItem;
    private List<RatingDocument> ratings;

    public ConfigurationWithRatings() {
    }

    public ConfigurationWithRatings(ConfigurationDocument configurationDocument,
            List<RatingDocument> ratings) {
        this.id = configurationDocument.getId();
        this.name = configurationDocument.getName();
        this.author = configurationDocument.getAuthor();
        this.description = configurationDocument.getDescription();
        this.thumbImage = configurationDocument.getThumbImage();
        this.configItem = configurationDocument.getConfigItem();
        this.ratings = ratings;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<RatingDocument> getRatings() {
        return ratings;
    }

    public void setRatings(List<RatingDocument> ratings) {
        this.ratings = ratings;
    }
}