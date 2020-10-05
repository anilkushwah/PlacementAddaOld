package com.dollop.placementadda.model;

import java.io.Serializable;

public class JobAlertModel implements Serializable {
    String id, job_title, job_type, experience, job_location, description, education, contact_detail, isActive;

    String company_logo;
    String company_name;
    String skill_required;
    String created_date;
    String image;
    String viewed;

    public String getViewed() {
        return viewed;
    }

    public void setViewed(String viewed) {
        this.viewed = "0";
    }


    public String getCompany_logo() {
        return company_logo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getSkill_required() {
        return skill_required;
    }

    public void setSkill_required(String skill_required) {
        this.skill_required = skill_required;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getJob_location() {
        return job_location;
    }

    public void setJob_location(String job_location) {
        this.job_location = job_location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getContact_detail() {
        return contact_detail;
    }

    public void setContact_detail(String contact_detail) {
        this.contact_detail = contact_detail;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
