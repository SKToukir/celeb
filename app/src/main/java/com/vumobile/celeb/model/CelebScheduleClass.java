package com.vumobile.celeb.model;

/**
 * Created by toukirul on 15/5/2017.
 */

public class CelebScheduleClass {

    private String name;
    private String imageUrl;
    private String requestType;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String fan_msisdn;
    private String celebrity_msisdn;
    private String start_time;
    private String end_time;

    public String getFan_msisdn() {
        return fan_msisdn;
    }

    public void setFan_msisdn(String fan_msisdn) {
        this.fan_msisdn = fan_msisdn;
    }

    public String getCelebrity_msisdn() {
        return celebrity_msisdn;
    }

    public void setCelebrity_msisdn(String celebrity_msisdn) {
        this.celebrity_msisdn = celebrity_msisdn;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
