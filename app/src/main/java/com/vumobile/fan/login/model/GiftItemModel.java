package com.vumobile.fan.login.model;

/**
 * Created by IT-10 on 5/31/2017.
 */

public class GiftItemModel {

    String GraphicsCode, ContentTitle, ContentType, PhysicalFileName, PreviewURL, ChargeType, Like, ProductID, PreviewUrlTransperrent;

    public GiftItemModel(String graphicsCode, String contentTitle, String contentType, String physicalFileName, String previewURL, String chargeType, String like, String productID, String previewUrlTransperrent) {
        this.GraphicsCode = graphicsCode;
        this.ContentTitle = contentTitle;
        this.ContentType = contentType;
        this.PhysicalFileName = physicalFileName;
        this.PreviewURL = previewURL;
        this.ChargeType = chargeType;
        this.Like = like;
        this.ProductID = productID;
        this.PreviewUrlTransperrent = previewUrlTransperrent;
    }

    public GiftItemModel(String contentTitle, String graphicsCode, String previewURL, String ChargeType) {
        this.ContentTitle = contentTitle;
        this.GraphicsCode = graphicsCode;
        this.PreviewURL = previewURL;
        this.ChargeType = ChargeType;
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

    public String getLike() {
        return Like;
    }

    public void setLike(String like) {
        Like = like;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getPreviewUrlTransperrent() {
        return PreviewUrlTransperrent;
    }

    public void setPreviewUrlTransperrent(String previewUrlTransperrent) {
        PreviewUrlTransperrent = previewUrlTransperrent;
    }

    @Override
    public String toString() {
        return "" + GraphicsCode + ContentTitle + PreviewURL + ChargeType;
    }
}
