package com.vumobile.celeb.model;

import org.json.JSONArray;

/**
 * Created by toukirul on 20/6/2017.
 */

public class Gift {

    private JSONArray array;

    public JSONArray getArray() {
        return array;
    }

    public void setArray(JSONArray array) {
        this.array = array;
    }

    private String giftUrl;

    public String getGiftUrl() {
        return giftUrl;
    }

    public void setGiftUrl(String giftUrl) {
        this.giftUrl = giftUrl;
    }
}
