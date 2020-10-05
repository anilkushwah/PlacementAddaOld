package com.dollop.placementadda.model;

import java.io.Serializable;

public class FilteredHistory implements Serializable {
    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public String getTest_date() {
        return test_date;
    }

    public void setTest_date(String test_date) {
        this.test_date = test_date;
    }

    public String getTest_time() {
        return test_time;
    }

    public void setTest_time(String test_time) {
        this.test_time = test_time;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public FilteredHistory(String topic_name, String test_date, String test_time, String category_image, String subject_name, String category_name, String topic_id, String subject_id, String category_id) {
        this.topic_name = topic_name;
        this.test_date = test_date;
        this.test_time = test_time;
        this.category_image = category_image;
        this.subject_name = subject_name;
        this.category_name = category_name;
        this.topic_id = topic_id;
        this.subject_id = subject_id;
        this.category_id = category_id;
    }

    String topic_name;
    String test_date;
    String test_time;
    String category_image;

    public FilteredHistory() {
    }

    String subject_name;
    String category_name;
    String topic_id;
    String subject_id;
    String category_id;
}