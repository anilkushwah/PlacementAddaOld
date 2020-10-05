package com.dollop.placementadda.notification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Meenakshi on 6/27/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        S.E("refresh TOken"+refreshedToken);
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
        if (UserDataHelper.getInstance().getList().size() > 0) {
            updatefcm(token);
        }
    }

    private void storeRegIdInPref(String token) {
        SavedData.saveFcmTokenID(token);
        S.E("saved token"+token);

        //editor.commit();
    }

    public void updatefcm(final String token) {
        try {
            new JSONParser(MyFirebaseInstanceIDService.this).parseVollyStringRequest(Const.URL.updateFcm, 1, getLoginParam(token), new Helper() {
            @Override
            public void backResponse(String response) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String value = preferences.getString("reg", "arpit");
                S.E("get all Login detail=======" + response);
                S.E("get all Login Params=======" + getLoginParam(token));
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //   int status = Integer.parseInt(jsonObject.getString("status"));
                    Log.e("befor if condition ", "...");
                    if (jsonObject.getString("status").equals("200")) {
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSONException =", e + "");
                }
            }
        });
    }
    catch (Exception e){

        }
    }

    public Map<String, String> getLoginParam(String token) {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
        params.put("fcm_id", token);
        S.E("token"+token);

        return params;
    }

}