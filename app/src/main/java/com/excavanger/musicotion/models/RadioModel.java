package com.excavanger.musicotion.models;

public class RadioModel {
    private String id,title,image,perma_url;

    public RadioModel(String id, String title, String image, String perma_url) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.perma_url = perma_url;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPerma_url() {
        return perma_url;
    }

    public void setPerma_url(String perma_url) {
        this.perma_url = perma_url;
    }
}
