package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.NewBatchesAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.NewBatchesModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class NotificationListActivity extends BaseActivity {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    RecyclerView categoryRV;
    private LinearLayoutManager gaggeredGridLayoutManager;
    List<NewBatchesModel> quizCategoryModelList = new ArrayList<>();

    @Override
    protected int getContentResId() {
        return R.layout.activity_notification_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(NotificationListActivity.this);
        setToolbarWithBackButton("Notification");
        notificationCallFromHandler();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
//                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    SavedData.saveNotificationRequest("no");
                    String message = intent.getStringExtra("message");

                    S.E(message);
                    try {
                        SavedData.saveNotificationRequest("no");
                        JSONObject jsonObject = new JSONObject(message);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        categoryRV = (RecyclerView) findViewById(R.id.categoryRV);

        try {
            gaggeredGridLayoutManager = new LinearLayoutManager(this);
            categoryRV.setLayoutManager(gaggeredGridLayoutManager);
            categoryRV.setHasFixedSize(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getNotification();

        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(NotificationListActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }

    private void getNotification() {
        new JSONParser(NotificationListActivity.this).parseVollyStringRequest(Const.URL.getNotification, 1, getParms(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    S.E("check Notification List::" + response);
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        quizCategoryModelList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            NewBatchesModel newBatchesModel = new NewBatchesModel();
                            String[] timeDate = jsonObject1.getString("NotificationCreatedDate").split(" ");
                            String timeNotification = timeDate[1];

                            String dateNotification = "";
                            newBatchesModel.setBatch_id(jsonObject1.getString("notification_id"));
                            newBatchesModel.setBatch_title(jsonObject1.getString("notificationTitle"));
                            newBatchesModel.setBatch_detail(jsonObject1.getString("notificationBody"));
                            dateNotification = dateStr(jsonObject1.getString("NotificationCreatedDate"));
                            newBatchesModel.setBatch_start_date(dateNotification);
                            newBatchesModel.setNewBacthTime(timeNotification);
                            newBatchesModel.setNotificationType(jsonObject1.getString("notificationType"));
                            newBatchesModel.setSubject(jsonObject1.getString("subject"));
                            newBatchesModel.setCategory(jsonObject1.getString("category"));
                            newBatchesModel.setNotificationorBatch("0");
                            newBatchesModel.setTopic(jsonObject1.getString("topic"));
                            S.E("checkDateAK::" + dateNotification);
                            String dateChangeCheck = "";


                            S.E("checkDate In " + dateChangeCheck);
                            S.E("checkDate dateInString In " + dateNotification);
                            if (dateChangeCheck.equals(dateNotification)) {
                                newBatchesModel.setDateChange("SameDate");


                            } else {
                                dateChangeCheck = dateNotification;
                                newBatchesModel.setDateChange(dateNotification);

                            }
                            quizCategoryModelList.add(newBatchesModel);

                        }
                        NewBatchesAdapter quizCategoryAdapter = new NewBatchesAdapter(NotificationListActivity.this, quizCategoryModelList);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(quizCategoryAdapter);
                        categoryRV.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

                    } else {
                        S.T(NotificationListActivity.this, message);
                        quizCategoryModelList.clear();
                        NewBatchesAdapter quizCategoryAdapter = new NewBatchesAdapter(NotificationListActivity.this, quizCategoryModelList);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(quizCategoryAdapter);
                        categoryRV.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String dateStr(String strCurrentDate) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = null;
        try {
            newDate = format.parse(strCurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("dd-MMM-yyyy");
        String date = format.format(newDate);
        return date;
    }

    public String timeStr(String strCurrentDate) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = null;
        try {
            newDate = format.parse(strCurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("hh:mm:ss");
        String timestr = format.format(newDate);
        return timestr;
    }

    private Map<String, String> getParms() {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
        return params;
    }

    private void notificationCallFromHandler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SavedData.getNotificationRequest() != null) {
                    if (SavedData.getNotificationRequest().equals("yes")) {
                        try {
                            SavedData.saveNotificationRequest("no");
                            JSONObject jsonObject = new JSONObject(SavedData.getNotificationJson());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, 2000);
    }

}
