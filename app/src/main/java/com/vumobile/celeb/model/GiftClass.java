package com.vumobile.celeb.model;

import org.json.JSONArray;

/**
 * Created by toukirul on 19/6/2017.
 */

public class GiftClass {

    private JSONArray array;
    private String totalGifts;

    public String getTotalGifts() {
        return String.valueOf(Integer.parseInt(totalGifts) - 1);
    }

    public void setTotalGifts(String totalGifts) {
        this.totalGifts = totalGifts;
    }

    private String msisdn;
    private String imageUrl;
    private String post_urls;
    private String giftOne;
    private String giftTwo;
    private String giftThree;

    public JSONArray getArray() {
        return array;
    }

    public void setArray(JSONArray array) {
        this.array = array;
    }

    public String getGiftOne() {
        return giftOne;
    }

    public void setGiftOne(String giftOne) {
        this.giftOne = giftOne;
    }

    public String getGiftTwo() {
        return giftTwo;
    }

    public void setGiftTwo(String giftTwo) {
        this.giftTwo = giftTwo;
    }

    public String getGiftThree() {
        return giftThree;
    }

    public void setGiftThree(String giftThree) {
        this.giftThree = giftThree;
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
