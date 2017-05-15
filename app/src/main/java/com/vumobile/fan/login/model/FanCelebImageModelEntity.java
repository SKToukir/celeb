package com.vumobile.fan.login.model;

/**
 * Created by IT-10 on 5/7/2017.
 */

public class FanCelebImageModelEntity {

    String imageName;
    String imageUrl;
    String imageOwnerPhone;

    public String getIsImage() {
        return isImage;
    }

    public void setIsImage(String isImage) {
        this.isImage = isImage;
    }

    String isImage;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageOwnerPhone() {
        return imageOwnerPhone;
    }

    public void setImageOwnerPhone(String imageOwnerPhone) {
        this.imageOwnerPhone = imageOwnerPhone;
    }
}
