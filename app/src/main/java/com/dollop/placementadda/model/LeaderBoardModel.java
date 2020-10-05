package com.dollop.placementadda.model;

import java.io.Serializable;

/**
 * Created by Kasim on 17-01-2018.
 */

public class LeaderBoardModel implements Serializable{
    private static final long serialVersionUID = 1L;
    private boolean isSelected;

    public LeaderBoardModel() {
    }

    public LeaderBoardModel(boolean isSelected, String userName) {
        this.isSelected = isSelected;
        this.userName = userName;
    }

    public LeaderBoardModel(boolean isSelected, String user_id, String userName) {

        this.isSelected = isSelected;
        this.user_id = user_id;
        this.userName = userName;
    }

    public LeaderBoardModel(boolean isSelected, String user_id, String userName, String userProfilePic) {

        this.isSelected = isSelected;
        this.user_id = user_id;
        this.userName = userName;
        this.userProfilePic = userProfilePic;
    }

    public LeaderBoardModel(boolean isSelected, String user_id, String userName, String userProfilePic, String rankingPoints) {

        this.isSelected = isSelected;
        this.user_id = user_id;
        this.userName = userName;
        this.userProfilePic = userProfilePic;
        this.rankingPoints = rankingPoints;
    }

    String user_id;
    String userName;
    String userProfilePic="";
   String userGender,userCity;

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUser_id() {
        return user_id;
    }

    public String setUser_id(String user_id) {
        this.user_id = user_id;
        return user_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public String getRankingPoints() {
        return rankingPoints;
    }

    public void setRankingPoints(String rankingPoints) {
        this.rankingPoints = rankingPoints;
    }

    String rankingPoints;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean getSelected() {
        return isSelected;
    }
}