package com.vumobile.celeb.Utils;

/**
 * Created by toukirul on 12/4/2017.
 */

public class CelebrityClass {

    private String celeb_name;
    private String celeb_image;
    private String celeb_code;
    private String fb_name;
    private String isOnline;
    private String Isfollow;
    private String followerCount;
    private String nextLive;
    private int nextLiveStatus; // if nextLive is not "" the set 1 or 0

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getFb_name() {
        return fb_name;
    }

    public void setFb_name(String fb_name) {
        this.fb_name = fb_name;
    }

    public String getCeleb_name() {
        return celeb_name;
    }

    public void setCeleb_name(String celeb_name) {
        this.celeb_name = celeb_name;
    }

    public String getCeleb_image() {
        return celeb_image;
    }

    public void setCeleb_image(String celeb_image) {
        this.celeb_image = celeb_image;
    }

    public String getCeleb_code() {
        return celeb_code;
    }

    public void setCeleb_code(String celeb_code) {
        this.celeb_code = celeb_code;
    }

    public String getIsfollow() {
        return Isfollow;
    }

    public void setIsfollow(String isfollow) {
        Isfollow = isfollow;
    }

    public String getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(String followerCount) {
        this.followerCount = followerCount;
    }

    public String getNextLive() {
        return nextLive;
    }

    public void setNextLive(String nextLive) {
        this.nextLive = nextLive;
    }

    public int getNextLiveStatus() {
        return nextLiveStatus;
    }

    public void setNextLiveStatus(int nextLiveStatus) {
        this.nextLiveStatus = nextLiveStatus;
    }
}
