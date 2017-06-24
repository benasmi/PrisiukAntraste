package com.mabe.productions.prisiukantraste;

import android.support.annotation.Nullable;

public class NewsItem {
    private String date;
    private String url;
    private String image_url;
    private String title;
    private String description;
    private TitleItem firstTitle;
    private TitleItem secondTitle;
    private String titleCount;

    private int type;
    private int height;

    public static final int TYPE_POST = 0;
    public static final int TYPE_AD = 1;
    public static final int TYPE_LOADING = 2;
    public static final int TYPE_HEADER = 3;

    public NewsItem(String url, String image_url, String title, String description, int type, @Nullable int height, @Nullable TitleItem firstTitle, @Nullable TitleItem secondTitle, String titleCount, String date) {
        this.url = url;
        this.image_url = image_url;
        this.title = title;
        this.description = description;
        this.type = type;
        this.height = height;
        this.firstTitle = firstTitle;
        this.secondTitle = secondTitle;
        this.titleCount = titleCount;
        this.date = date;
    }

    public NewsItem(int type){
        this.type = type;
    }

    public NewsItem(String date, int type) {
        this.date = date;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitleCount() {
        return titleCount;
    }

    public void setTitleCount(String titleCount) {
        this.titleCount = titleCount;
    }

    public TitleItem getFirstTitle() {
        return firstTitle;
    }

    public void setFirstTitle(TitleItem firstTitle) {
        this.firstTitle = firstTitle;
    }

    public TitleItem getSecondTitle() {
        return secondTitle;
    }

    public void setSecondTitle(TitleItem secondTitle) {
        this.secondTitle = secondTitle;
    }

    public int getImageHeight() {
        return height;
    }

    public void setImageHeight(int height) {
        this.height = height;
    }

    public int getType(){
        return type;
    }
    public void setType(final int type){
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
