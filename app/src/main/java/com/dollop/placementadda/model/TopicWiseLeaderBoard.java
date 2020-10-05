package com.dollop.placementadda.model;

public class TopicWiseLeaderBoard {
    private static final long serialVersionUID = 1L;
    private boolean isSelected;
    String topic_id;
    String topic_name;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public String getRankingPoints() {
        return rankingPoints;
    }

    public void setRankingPoints(String rankingPoints) {
        this.rankingPoints = rankingPoints;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public TopicWiseLeaderBoard(boolean isSelected, String topic_id, String topic_name, String category_image, String rankingPoints, String test_date, String test_time, String user_id, String userName, String userPhone, String userEmail, String userProfilePic) {
        this.isSelected = isSelected;
        this.topic_id = topic_id;
        this.topic_name = topic_name;
        this.category_image = category_image;
        this.rankingPoints = rankingPoints;
        this.test_date = test_date;
        this.test_time = test_time;

        this.user_id = user_id;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userProfilePic = userProfilePic;
    }

    public TopicWiseLeaderBoard() {

    }

    String category_image;
    String rankingPoints;
    String test_date;
    String test_time;
    String user_id;
    String userName;
    String userPhone;
    String userEmail;
    String userProfilePic;
}
