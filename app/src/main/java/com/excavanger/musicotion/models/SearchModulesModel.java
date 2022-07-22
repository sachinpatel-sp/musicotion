package com.excavanger.musicotion.models;

import java.util.List;

public class SearchModulesModel {
    private String title;
    private List<SearchModel> searchModelList;

    public SearchModulesModel(String title, List<SearchModel> searchModelList) {
        this.title = title;
        this.searchModelList = searchModelList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SearchModel> getSearchModelList() {
        return searchModelList;
    }

    public void setSearchModelList(List<SearchModel> searchModelList) {
        this.searchModelList = searchModelList;
    }
}
