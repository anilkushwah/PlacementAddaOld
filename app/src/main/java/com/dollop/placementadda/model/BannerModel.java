package com.dollop.placementadda.model;

import com.dollop.placementadda.database.model.QuestionaryModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kasim on 22-01-2018.
 */

public class BannerModel implements Serializable {

    String bannerCreatedDate,isActive,bannerImage,banner_id;

    public String getBannerCreatedDate() {
        return bannerCreatedDate;
    }

    public void setBannerCreatedDate(String bannerCreatedDate) {
        this.bannerCreatedDate = bannerCreatedDate;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }
}
