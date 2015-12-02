package com.example.qzero.Outlet.ObjectClasses;

import java.io.Serializable;

/**
 * Created by Braintech on 11/24/2015.
 */
public class Advertisement implements Serializable {

    String imageAd;
    String imgId;
    String imgUrl;
    boolean isAdminAdd;

    public Advertisement(String imageAd, String imgId, String imgUrl) {
        this.imageAd = imageAd;
        this.imgId = imgId;
        this.imgUrl = imgUrl;
        this.isAdminAdd = isAdminAdd;
    }

    public boolean isAdminAdd() {
        return isAdminAdd;
    }

    public void setIsAdminAdd(boolean isAdminAdd) {
        this.isAdminAdd = isAdminAdd;
    }

    public String getImageAd() {
        return imageAd;
    }

    public void setImageAd(String imageAd) {
        this.imageAd = imageAd;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
