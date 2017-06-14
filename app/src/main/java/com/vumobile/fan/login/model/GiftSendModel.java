package com.vumobile.fan.login.model;

/**
 * Created by IT-10 on 6/14/2017.
 */

public class GiftSendModel {

    String id, graphicsCode, fan, celebrity, sendTime;

    public GiftSendModel(String id, String graphicsCode, String fan, String celebrity, String sendTime) {
        this.id = id;
        this.graphicsCode = graphicsCode;
        this.fan = fan;
        this.celebrity = celebrity;
        this.sendTime = sendTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGraphicsCode() {
        return graphicsCode;
    }

    public void setGraphicsCode(String graphicsCode) {
        this.graphicsCode = graphicsCode;
    }

    public String getFan() {
        return fan;
    }

    public void setFan(String fan) {
        this.fan = fan;
    }

    public String getCelebrity() {
        return celebrity;
    }

    public void setCelebrity(String celebrity) {
        this.celebrity = celebrity;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "GiftSendModel: Id: " + this.id + " Graphics Code: " + this.graphicsCode +
                " Fan: " + this.fan + " Celebrity: " + this.celebrity + " sendTime: " + this.sendTime;
    }
}
