package com.dollop.placementadda.database.datahelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.dollop.placementadda.database.DataManager;
import com.dollop.placementadda.database.model.UserModel;
import com.dollop.placementadda.sohel.S;

import java.util.ArrayList;

/**
 * Created by CRUD-PC on 10/7/2016.
 */
public class UserDataHelper {
    private static UserDataHelper instance;
    private SQLiteDatabase db;
    private DataManager dm;
    Context cx;

    public UserDataHelper(Context cx) {
        instance = this;
        this.cx = cx;
        dm = new DataManager(cx, DataManager.DATABASE_NAME, null, DataManager.DATABASE_VERSION);
    }

    public static UserDataHelper getInstance() {
        return instance;
    }

    public void open() {
        db = dm.getWritableDatabase();
    }

    public void close() {
      //  db.close();
    }

    public void read() {
        db = dm.getReadableDatabase();
    }

    public void delete(int companyId) {
        open();
        db.delete(UserModel.TABLE_NAME, UserModel.KEY_ID + " = '" + companyId + "'", null);
        close();
    }

    public void deleteAll() {
        open();
        db.delete(UserModel.TABLE_NAME, null, null);
        close();
    }

    private boolean isExist(UserModel userModel) {
        read();
        Cursor cur = db.rawQuery("select * from " + UserModel.TABLE_NAME + " where " + UserModel.KEY_ID + "='" + userModel.getUserId() + "'", null);
        if (cur.moveToFirst()) {
            return true;
        }
        return false;
    }

    public void insertData(UserModel userModel) {
        open();
        ContentValues values = new ContentValues();
        values.put(UserModel.KEY_USER_ID, userModel.getUserId());
        values.put(UserModel.KEY_USERNAME, userModel.getUserName());
        values.put(UserModel.KEY_USEREMAIL, userModel.getUserEmail());
        values.put(UserModel.KEY_USERPHONE, userModel.getUserPhone());
        values.put(UserModel.KEY_USERCOLLEGENAME, userModel.getUserCollegeName());
        values.put(UserModel.KEY_GENDER, userModel.getUserGender());
        values.put(UserModel.KEY_USERDOB, userModel.getUserDob());
        values.put(UserModel.KEY_USERDOJ, userModel.getUserDoj());
        values.put(UserModel.KEY_USERADDRESS, userModel.getUserAddress());
        values.put(UserModel.KEY_USER_IMEI, userModel.getUserImeiNo());
        values.put(UserModel.KEY_USER_City, userModel.getCity());
        values.put(UserModel.KEY_USER_Profile_PIC, userModel.getProfile_pic());
        if (!isExist(userModel)) {
            S.E("insert successfully");
            db.insert(UserModel.TABLE_NAME, null, values);
        } else {
            S.E("update successfully" + userModel.getUserId());
            db.update(UserModel.TABLE_NAME, values, UserModel.KEY_ID + "=" + userModel.getUserId(), null);
        }
        close();
    }

    public ArrayList<UserModel> getList() {
        ArrayList<UserModel> userItem = new ArrayList<UserModel>();
        read();
       // Cursor cursor = db.rawQuery("select * from UserModel", null);

        Cursor cursor = db.rawQuery("select * from "+UserModel.TABLE_NAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            do {
                UserModel taxiModel = new UserModel();
                taxiModel.setUserId(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USER_ID)));
                taxiModel.setUserName(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USERNAME)));
                taxiModel.setUserEmail(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USEREMAIL)));
                taxiModel.setUserPhone(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USERPHONE)));
                taxiModel.setUserCollegeName(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USERCOLLEGENAME)));
                taxiModel.setUserGender(cursor.getString(cursor.getColumnIndex(UserModel.KEY_GENDER)));
                taxiModel.setUserDob(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USERDOB)));
                taxiModel.setUserDoj(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USERDOJ)));
                taxiModel.setUserAddress(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USERADDRESS)));
                taxiModel.setUserImeiNo(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USER_IMEI)));
                taxiModel.setCity(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USER_City)));
                taxiModel.setProfile_pic(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USER_Profile_PIC)));
                userItem.add(taxiModel);
            } while ((cursor.moveToPrevious()));
            cursor.close();
        }
        close();
        return userItem;
    }
}