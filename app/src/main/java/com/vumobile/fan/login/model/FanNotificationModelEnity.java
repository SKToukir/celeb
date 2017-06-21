package com.vumobile.fan.login.model;

/**
 * Created by IT-10 on 5/3/2017.
 */

public class FanNotificationModelEnity {

    String thumbImage;
    String id;
    String name;
    String MSISDN;
    String Celeb_id;
    String gender;
    String IsImage; // 1 is image 2 is video
    String Flags_Notificaton; // 1 is live, 2 is post
    String Image_url; // profile image url
    String post;
    String likeCount;
    String TimeStamp;
    String Post_Urls;
    String notifVideoThumb;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url; // image or video url

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMSISDN() {
        return MSISDN;
    }

    public void setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
    }

    public String getCeleb_id() {
        return Celeb_id;
    }

    public void setCeleb_id(String celeb_id) {
        Celeb_id = celeb_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIsImage() {
        return IsImage;
    }

    public void setIsImage(String isImage) {
        IsImage = isImage;
    }

    public String getFlags_Notificaton() {
        return Flags_Notificaton;
    }

    public void setFlags_Notificaton(String flags_Notificaton) {
        Flags_Notificaton = flags_Notificaton;
    }

    public String getImage_url() {
        return Image_url;
    }

    public void setImage_url(String image_url) {
        Image_url = image_url;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getPost_Urls() {
        return Post_Urls;
    }

    public void setPost_Urls(String post_Urls) {
        Post_Urls = post_Urls;
    }

    public String getNotifVideoThumb() {
        return notifVideoThumb;
    }

    public void setNotifVideoThumb(String notifVideoThumb) {
        this.notifVideoThumb = notifVideoThumb;
    }
    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }
    @Override
    public String toString() {
        return "FanNotif: " + notifVideoThumb;
    }
}
