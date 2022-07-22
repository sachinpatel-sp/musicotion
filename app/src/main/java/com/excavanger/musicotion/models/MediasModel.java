package com.excavanger.musicotion.models;

public class MediasModel {
    private String id,title,subtitle,type,perma_url,image,media_url,language;

    public MediasModel(String id, String title, String subtitle, String type, String perma_url, String image, String media_url, String language) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.type = type;
        this.perma_url = perma_url;
        this.image = image;
        this.media_url = media_url;
        this.language = language;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPerma_url() {
        return perma_url;
    }

    public void setPerma_url(String perma_url) {
        this.perma_url = perma_url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
