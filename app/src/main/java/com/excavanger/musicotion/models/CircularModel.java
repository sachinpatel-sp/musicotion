package com.excavanger.musicotion.models;


public class CircularModel {
    String artistId,name,image,perma_url,language;
    Integer followers;

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public CircularModel(String artistId, String name, String image, String perma_url, Integer followers, String lang) {
        this.artistId = artistId;
        this.name = name;
        this.image = image;
        this.perma_url = perma_url;
        this.followers = followers;
        language = lang;
    }
}
