package com.mabe.productions.prisiukantraste;

public class TitleItem {

    String title;
    int points;
    int isOrigin;

    public TitleItem(String title, int points, int isOrigin) {
        this.title = title;
        this.points = points;
        this.isOrigin = isOrigin;
    }

    public int getIsOrigin() {
        return isOrigin;
    }

    public void setIsOrigin(int isOrigin) {
        this.isOrigin = isOrigin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
