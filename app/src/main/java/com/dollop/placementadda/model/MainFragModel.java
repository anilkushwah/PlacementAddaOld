package com.dollop.placementadda.model;

/**
 * Created by Kasim on 17-01-2018.
 */

public class MainFragModel {
    int quixImage;
    String nameTV, emailTv, rankTV, totalNumberTV, phoneTV, addressTV;

    public int getQuixImage() {
        return quixImage;
    }

    public void setQuixImage(int quixImage) {
        this.quixImage = quixImage;
    }

    public String getNameTV() {
        return nameTV;
    }

    public void setNameTV(String nameTV) {
        this.nameTV = nameTV;
    }

    public String getEmailTv() {
        return emailTv;
    }

    public void setEmailTv(String emailTv) {
        this.emailTv = emailTv;
    }

    public String getRankTV() {
        return rankTV;
    }

    public void setRankTV(String rankTV) {
        this.rankTV = rankTV;
    }

    public String getTotalNumberTV() {
        return totalNumberTV;
    }

    public void setTotalNumberTV(String totalNumberTV) {
        this.totalNumberTV = totalNumberTV;
    }

    public String getPhoneTV() {
        return phoneTV;
    }

    public void setPhoneTV(String phoneTV) {
        this.phoneTV = phoneTV;
    }

    public String getAddressTV() {
        return addressTV;
    }

    public void setAddressTV(String addressTV) {
        this.addressTV = addressTV;
    }

    public MainFragModel(int quixImage, String nameTV, String emailTv, String rankTV, String totalNumberTV, String phoneTV, String addressTV) {
        this.quixImage = quixImage;

        this.nameTV = nameTV;
        this.emailTv = emailTv;
        this.rankTV = rankTV;
        this.totalNumberTV = totalNumberTV;
        this.phoneTV = phoneTV;
        this.addressTV = addressTV;
    }
}