package com.dollop.placementadda.model;

/**
 * Created by user on 2/20/2018.
 */

public class CollageNameModel {
    String college_id,collegeName,isActive,isRequest,isDelete,CollegeCreatedDate;

    public String getCollege_id() {
        return college_id;
    }

    public void setCollege_id(String college_id) {
        this.college_id = college_id;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsRequest() {
        return isRequest;
    }

    public void setIsRequest(String isRequest) {
        this.isRequest = isRequest;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getCollegeCreatedDate() {
        return CollegeCreatedDate;
    }

    public void setCollegeCreatedDate(String collegeCreatedDate) {
        CollegeCreatedDate = collegeCreatedDate;
    }
}
