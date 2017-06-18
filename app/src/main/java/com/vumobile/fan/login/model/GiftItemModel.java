package com.vumobile.fan.login.model;

/**
 * Created by IT-10 on 5/31/2017.
 */

public class GiftItemModel {

    String _id, GraphicsCode, ContentTitle, ContentType, PhysicalFileName, PreviewURL, ChargeType, ChargePrice, TimeStamp;

    public GiftItemModel(String _id, String graphicsCode, String contentTitle, String contentType, String physicalFileName, String previewURL, String chargeType, String chargePrice, String timeStamp) {
        this._id = _id;
        GraphicsCode = graphicsCode;
        ContentTitle = contentTitle;
        ContentType = contentType;
        PhysicalFileName = physicalFileName;
        PreviewURL = previewURL;
        ChargeType = chargeType;
        ChargePrice = chargePrice;
        TimeStamp = timeStamp;
    }

    public GiftItemModel(String graphicsCode, String contentTitle, String contentType, String physicalFileName, String previewURL, String chargeType, String chargePrice, String timeStamp) {
        GraphicsCode = graphicsCode;
        ContentTitle = contentTitle;
        ContentType = contentType;
        PhysicalFileName = physicalFileName;
        PreviewURL = previewURL;
        ChargeType = chargeType;
        ChargePrice = chargePrice;
        TimeStamp = timeStamp;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGraphicsCode() {
        return GraphicsCode;
    }

    public void setGraphicsCode(String graphicsCode) {
        GraphicsCode = graphicsCode;
    }

    public String getContentTitle() {
        return ContentTitle;
    }

    public void setContentTitle(String contentTitle) {
        ContentTitle = contentTitle;
    }

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public String getPhysicalFileName() {
        return PhysicalFileName;
    }

    public void setPhysicalFileName(String physicalFileName) {
        PhysicalFileName = physicalFileName;
    }

    public String getPreviewURL() {
        return PreviewURL;
    }

    public void setPreviewURL(String previewURL) {
        PreviewURL = previewURL;
    }

    public String getChargeType() {
        return ChargeType;
    }

    public void setChargeType(String chargeType) {
        ChargeType = chargeType;
    }

    public String getChargePrice() {
        return ChargePrice;
    }

    public void setChargePrice(String chargePrice) {
        ChargePrice = chargePrice;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "" + GraphicsCode + ContentTitle + PreviewURL + this.ChargePrice;
    }
}
