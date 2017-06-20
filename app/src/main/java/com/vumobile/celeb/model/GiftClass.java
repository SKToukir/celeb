package com.vumobile.celeb.model;

import org.json.JSONArray;

/**
 * Created by toukirul on 19/6/2017.
 */

public class GiftClass {

    private String msisdn;
    private String imageUrl;
    private String post_urls;
    private JSONArray listOfGift;

    public JSONArray getListOfGift() {
        return listOfGift;
    }

    public void setListOfGift(JSONArray listOfGift) {
        this.listOfGift = listOfGift;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPost_urls() {
        return post_urls;
    }

    public void setPost_urls(String post_urls) {
        this.post_urls = post_urls;
    }

}
