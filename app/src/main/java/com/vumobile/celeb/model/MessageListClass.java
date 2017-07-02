package com.vumobile.celeb.model;

/**
 * Created by toukirul on 16/5/2017.
 */

public class MessageListClass {

    private String imageUrl;
    private String name;
    private String room_number;
    private String RequestType;
    private String celebrityMessageCount;

    public String getFanMessageCount() {
        return fanMessageCount;
    }

    public void setFanMessageCount(String fanMessageCount) {
        this.fanMessageCount = fanMessageCount;
    }

    public String getCelebrityMessageCount() {
        return celebrityMessageCount;
    }

    public void setCelebrityMessageCount(String celebrityMessageCount) {
        this.celebrityMessageCount = celebrityMessageCount;
    }

    private String fanMessageCount;

    public String getRequestType() {
        return RequestType;
    }

    public void setRequestType(String requestType) {
        RequestType = requestType;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
