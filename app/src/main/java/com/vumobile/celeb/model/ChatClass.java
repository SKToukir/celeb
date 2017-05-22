package com.vumobile.celeb.model;

/**
 * Created by toukirul on 16/5/2017.
 */

public class ChatClass {

    private String imageUrl;
    private String text;
    private String isCeleb;

    public String getIsCeleb() {
        return isCeleb;
    }

    public void setIsCeleb(String isCeleb) {
        this.isCeleb = isCeleb;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
