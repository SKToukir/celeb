package com.vumobile.celeb.model;

/**
 * Created by toukirul on 7/5/2017.
 */

public class RequestClass {

    private String imageUrl;
    private String fanName;
    private String request;

    public String getRequestToTime() {
        return requestToTime;
    }

    public void setRequestToTime(String requestToTime) {
        this.requestToTime = requestToTime;
    }

    private String requestToTime;
    private String MSISDN;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFanName() {
        return fanName;
    }

    public void setFanName(String fanName) {
        this.fanName = fanName;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }



    public String getMSISDN() {
        return MSISDN;
    }

    public void setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
    }
}
