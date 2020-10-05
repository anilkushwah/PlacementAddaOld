package com.dollop.placementadda.model;

/**
 * Created by Kasim on 22-01-2018.
 */

public class QuizSubCatModel {
    String title,feeType,duration,questions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public QuizSubCatModel(String title, String feeType, String duration, String questions) {
        this.title = title;
        this.feeType = feeType;
        this.duration = duration;
        this.questions = questions;
    }
}
