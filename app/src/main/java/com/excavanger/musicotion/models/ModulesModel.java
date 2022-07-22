package com.excavanger.musicotion.models;

import java.util.List;

public class ModulesModel {
    String title,subTitle,source;
    int type;
    List<MediasModel> medialModelList;

    public ModulesModel(String title, String subTitle, String source, int type, List<MediasModel> medialModelList) {
        this.title = title;
        this.subTitle = subTitle;
        this.source = source;
        this.type = type;
        this.medialModelList = medialModelList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<MediasModel> getMedialModelList() {
        return medialModelList;
    }

    public void setMedialModelList(List<MediasModel> medialModelList) {
        this.medialModelList = medialModelList;
    }
}
