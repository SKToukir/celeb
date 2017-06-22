package com.vumobile.celeb.model;

import java.util.List;

/**
 * Created by toukirul on 19/6/2017.
 */

public class GiftClass {

    private List<String> giftList;

    public List<String> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<String> giftList) {
        this.giftList = giftList;
    }

    private String msisdn;
    private String imageUrl;
    private String post_urls;
    private String giftOne;
    private String giftTwo;
    private String giftThree;

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
