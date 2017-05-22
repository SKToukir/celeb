package com.vumobile.fan.login.model;

/**
 * Created by IT-10 on 5/18/2017.
 */

public class ChatProfileModel implements java.io.Serializable {

    public String name;
    public String mdisdn;

    public ChatProfileModel() {

    }

    public ChatProfileModel(String name, String mdisdn) {
        this.name = name;
        this.mdisdn = mdisdn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMdisdn() {
        return mdisdn;
    }

    public void setMdisdn(String mdisdn) {
        this.mdisdn = mdisdn;
    }
}
