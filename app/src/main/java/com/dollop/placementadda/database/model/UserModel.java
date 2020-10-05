package com.dollop.placementadda.database.model;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by CRUD-PC on 10/5/2016.
 */
public class UserModel {
    public static final String TABLE_NAME = "UserModel";
    public static final String KEY_ID = "_id";
    public static final String KEY_USER_ID = "user_id";

    public static final String KEY_USEREMAIL = "userEmail";
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_USERPHONE= "userPhone";
    public static final String KEY_USERCOLLEGENAME= "userCollegeName";
    public static final String KEY_GENDER= "userGender";
    public static final String KEY_USERDOB= "userDOB";
    public static final String KEY_USERDOJ= "userDOJ";
    public static final String KEY_USERADDRESS= "userAddress";
    public static final String KEY_USER_IMEI= "key_user_imei";
    public static final String KEY_USER_City= "city";
    public static final String KEY_USER_Profile_PIC= "Profile_pic";
    public static final String KEY_branch= "branch";

    public static final String KEY_course= "course";



    public static void creteTable(SQLiteDatabase db) {
        String CREATE_CLIENTTABLE = "create table " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_USER_ID + " text, " +
                KEY_USERNAME + " text, " +
                KEY_USEREMAIL+ " text, " +
                KEY_USERPHONE + " text, " +
                KEY_USERCOLLEGENAME + " text, " +
                KEY_GENDER + " text, " +
                KEY_USERDOB + " text, " +
                KEY_USERDOJ + " text, " +
                KEY_USERADDRESS + " text ," +
                KEY_USER_City + " text ," +
                KEY_USER_Profile_PIC + " text ," +
                KEY_branch + " text ," +
                KEY_course + " text ," +
                KEY_USER_IMEI + " text " +
                ")";
        db.execSQL(CREATE_CLIENTTABLE);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }


    String userId;
    String userName;
    String userEmail;
    String userPhone;
    String userCollegeName;
    String userGender;
    String userDob;
    String userDoj;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    String userAddress;
    String userPassword,city,Profile_pic;
    String course,branch;

    public String getProfile_pic() {
        return Profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        Profile_pic = profile_pic;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUserImeiNo() {
        return userImeiNo;
    }

    public void setUserImeiNo(String userImeiNo) {
        this.userImeiNo = userImeiNo;
    }

    String userImeiNo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserCollegeName() {
        return userCollegeName;
    }

    public void setUserCollegeName(String userCollegeName) {
        this.userCollegeName = userCollegeName;

    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserDob() {
        return userDob;
    }

    public void setUserDob(String userDob) {
        this.userDob = userDob;
    }

    public String getUserDoj() {
        return userDoj;
    }

    public void setUserDoj(String userDoj) {
        this.userDoj = userDoj;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
