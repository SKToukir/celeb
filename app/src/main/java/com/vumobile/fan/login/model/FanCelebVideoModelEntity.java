package com.vumobile.fan.login.model;

/**
 * Created by IT-10 on 5/7/2017.
 */

public class FanCelebVideoModelEntity {

    String videoName;
    String videoUrl;
    String videoOwnerPhone;

    public String getSetIsImage() {
        return setIsImage;
    }

    public void setSetIsImage(String setIsImage) {
        this.setIsImage = setIsImage;
    }

    String setIsImage;

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoOwnerPhone() {
        return videoOwnerPhone;
    }

    public void setVideoOwnerPhone(String videoOwnerPhone) {
        this.videoOwnerPhone = videoOwnerPhone;
    }

}
