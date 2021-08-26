package com.dollop.placementadda.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.notification.NotificationUtils;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CheckPlayerStatusService extends Service {
    Timer t = new Timer();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//                    S.T(getApplicationContext(),"App is open...");
                    getUserOnlineStatus();

                } else {
                    getUserOnlineStatus();
                    stopForeground(true);
                    stopSelf();
                 //   S.T(getApplicationContext(),"App is not open...");


                }
            }
        },0,1000);
    }

    private void getUserOnlineStatus() {

            new JSONParser().parseVollyStringRequestForService(Const.URL.setUserStatusOnLineOffLine, 1, getParams(), new Helper() {
                @Override
                public void backResponse(String response) {
//                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//                    String value = preferences.getString("player_status", "arpit");

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        //   int status = Integer.parseInt(jsonObject.getString("status"));

                        if (jsonObject.getString("status").equals("200")) {
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            });

    }
    private Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
                params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
                params.put("online_status", "1");

            } else {
                params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
                params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
                params.put("online_status", "0");
            }

        return params;
    }
}
