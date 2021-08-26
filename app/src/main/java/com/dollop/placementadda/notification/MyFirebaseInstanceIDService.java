package com.dollop.placementadda.notification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Meenakshi on 6/27/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    /*@Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        Utils.E("From: " + remoteMessage.getFrom());

        if (remoteMessage.getNotification() != null) {
            Utils.E("From:notification:: " + remoteMessage.getNotification());
            Utils.E("Notification Body: " + remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getData().size() > 0) {
            Utils.E("Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Utils.E("Exception: " + e.getMessage());
            }

        }

    }

    @Override
    public void onNewToken(@NonNull String token) {
        Utils.E("Refreshed token: service : " + token);
        SavedData.saveFirebaseToken(token);
        if (Utils.IS_LOGIN()) {
            sendRegistrationToServer(token, activity);
        }

    }z
*/


}