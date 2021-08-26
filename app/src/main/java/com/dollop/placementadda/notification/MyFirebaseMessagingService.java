package com.dollop.placementadda.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.preference.PreferenceManager;
import android.util.Log;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.JobAlertActivity;
import com.dollop.placementadda.activity.NotificationListActivity;
import com.dollop.placementadda.activity.QuizMainActivity;
import com.dollop.placementadda.activity.QuizQuestionaryActivity;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    String title, message, imageUrl, timestamp, action, userType;
    private NotificationUtils notificationUtils;
    private String notificationTitle = "";
    private String msg = "";
    private String quiz_id = "", quiz_time = "", quiz_topicId = "";
    private String category_name = "", subject_name = "", topic_name = "", point = "", user_name = "", userProfilePic = "", host_user_id = "", defender_user_id = "", tableId = "", player_key = "";
    private String NotificationTitle;
    private String message1;

    /**
     * Showing notification with text only
     */

    @Override
    public void onNewToken(@NonNull String refreshedToken) {


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
            new JSONParser(MyFirebaseMessagingService.this).parseVollyStringRequest(Const.URL.updateFcm, 1, getLoginParam(token), new Helper() {
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


    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From:  fghfdghdfghdfh       " + remoteMessage.getFrom());

        if (remoteMessage == null) return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body:  dfghdfghdf       ----   " + remoteMessage.getNotification().getBody());
            //handleNotification(remoteMessage.getNotification().getBody());
        }
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }
    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            Log.e("Message  ", "" + message);
            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }
    private void sendNotification(String messageBody) {
        try {
            JSONObject jsonObject1 = new JSONObject(messageBody);
            S.E("messageBody" + messageBody);
            notificationTitle = jsonObject1.getString("notificationTitle");
            msg = jsonObject1.getString("msg");
            title = jsonObject1.getString("title");
            JSONObject jsonObject = jsonObject1.getJSONObject("msg");
            msg = "Invite " + jsonObject.getString("user_name") + " On Table";
        } catch (JSONException e) {

        }
        Intent intent = new Intent(this, NotificationListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(notificationTitle)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentText(msg)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                //.setCustomContentView(notificationLayout)
                .setAutoCancel(true)
//                .setCustomBigContentView(notificationLayoutExpanded)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
//
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");
            message = data.getString("message");
            SavedData.saveNotificationRequest("yes");
            SavedData.saveNotificationJson(message);
                        if (data.getString("user_type").equals("twoPlayerTable")) {
                showPrivateTableNotification();
            }
            else if (data.getString("user_type").equals("multiplayerQuix"))
            {
                showMultiplaterTableNotification();
            }
            else if (data.getString("user_type").equals("acceptOnTable")) {
                AcceptPrivateTableNotification();
            } else if (data.getString("user_type").equals("cancelOnTable")) {
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    Intent pushNotification = new Intent(Config.CANCEL_REQUEST);
                    pushNotification.putExtra("message", String.valueOf(message));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                } else {
                    S.E("App is not Open");

                }
            } else if (data.getString("user_type").equals("twoPlayerTableResult")) {
                SavedData.saveNotificationRequestGameStart("true");
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    Intent pushNotification = new Intent(Config.QuizResult);
                    pushNotification.putExtra("message", String.valueOf(message));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                } else {
                    S.E("App is not Open");
                }
            } else if (data.getString("user_type").equals("Notification")) {
                try {
                    JSONObject data1 = json.getJSONObject("data");
                    JSONObject message = data1.getJSONObject("message");
                    JSONObject msg = message.getJSONObject("msg");
                    NotificationTitle = msg.getString("quizName");
                    title = data1.getString("title");
                } catch (JSONException e) {

                }
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    SendAddQuizNotification(NotificationTitle, title);
                } else {
                    SendAddQuizNotification(NotificationTitle, title);
                }
            } else if (data.getString("user_type").equals("job_alert")) {
                try {
                    JSONObject data1 = json.getJSONObject("data");
                    JSONObject message = data1.getJSONObject("message");
                    NotificationTitle = message.getString("msg");
                    title = message.getString("notificationTitle");
                } catch (JSONException e) {

                }
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    GetJob_AlertNotification(NotificationTitle, title);
                } else {
                    GetJob_AlertNotification(NotificationTitle, title);
                }
            } else if (data.getString("user_type").equals("table_expire")) {
                try {
                    if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                        Intent pushNotification = new Intent(Config.Table_Expaire);
                        pushNotification.putExtra("message", String.valueOf(message));
                        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                        notificationUtils.playNotificationSound();
                    } else {
                        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notifManager.cancelAll();
                    }

                } catch (Exception e) {

                }

            } else if (data.getString("user_type").equals("gameStart")) {
                SavedData.saveNotificationRequestGameStart("true");
                try {
                    if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                        Intent pushNotification = new Intent(Config.GAME_START);
                        pushNotification.putExtra("message", String.valueOf(message));
                        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                        notificationUtils.playNotificationSound();
                    } else {
                        S.E("App is not Open");

                    }
                } catch (Exception e) {

                }

            } else if (data.getString("user_type").equals("multiPlayer_notification")) {
                try {
                    if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                        Intent pushNotification = new Intent(Config.QuizRequest);
                        pushNotification.putExtra("message", String.valueOf(message));
                        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                        notificationUtils.playNotificationSound();
                    } else {
                      /*  S.E("App is not Open");
                        send_MultiPlayereNotification(json);*/
                    }
                } catch (Exception e) {

                }
            } else if (data.getString("user_type").equals("multiPlayer_accepted_notification")) {
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    Intent pushNotification = new Intent(Config.QuizRequest);
                    pushNotification.putExtra("message", String.valueOf(message));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                } else {
                    S.E("App is not Open");

                }
            } else if (data.getString("user_type").equals("acceptOnTableMultiplier")) {
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    Intent pushNotification = new Intent(Config.MultiPlayer_defender_Accept);
                    pushNotification.putExtra("message", String.valueOf(message));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                    S.E("checkItsComeHere sending::"+ String.valueOf(message));
                } else {
                    S.E("App is not Open");

                }
            }else if (data.getString("user_type").equals("notification_close")) {
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    Intent pushNotification = new Intent(Config.QuizRequest);
                    pushNotification.putExtra("message", String.valueOf(message));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                } else {
                    S.E("App is not Open");

                }
            } else if (data.getString("user_type").equals("ReadyToPlayQuiz")) {
                            SavedData.saveReadyToPlay("yes");
                            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                                S.E("checkReadyToPlayQuiz On Service");
                                Intent pushNotification = new Intent(Config.readyToPlay);
                                pushNotification.putExtra("message", String.valueOf(message));
                                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                                notificationUtils.playNotificationSound();
                            } else {
                                S.E("App is not Open");

                            }
                        }else if (data.getString("user_type").equals("gameStartByHost")) {
                            SavedData.saveReadyToPlay("yes");
                            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                                S.E("checkReadyToPlayQuiz On Service gameStartByHost");
                                Intent pushNotification = new Intent(Config.gameStartByHost);
                                pushNotification.putExtra("message", String.valueOf(message));
                                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                                notificationUtils.playNotificationSound();
                            } else {
                              S.E("App is not Open");

                            }
                        } else {
                showNotification();
            }

            //  }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void showNotification() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.e("check if", "......");
            // app is not in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            Log.e("Message  ", "" + message);
            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            S.E("newdistributor");
            sendNotification(message);

        }
    }

    private void AcceptPrivateTableNotification() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            SavedData.saveNotificationRequestGameStart("true");
            Intent pushNotification = new Intent(Config.REGISTRATION_COMPLETE);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
            // sendprivateNotification(message);
            S.E("AcceptPrivateTableNotification App is  Open");
        } else {
            S.E("App is not Open");
            /* AcceptprivateNotification(message);*/

        }
    }
   /* private void ReadyToPlay() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            SavedData.saveNotificationRequestGameStart("true");
            Intent pushNotification = new Intent(Config.readyToPlay);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
            // sendprivateNotification(message);
            S.E("REadyToplay App is  Open");
        } else {
            S.E("App is not Open");
            *//* AcceptprivateNotification(message);*//*

        }
    }
*/
    private void showMultiplaterTableNotification(){
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Intent pushNotification = new Intent(Config.QuizRequest);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
            //  sendprivateNotification(message);
            S.E("showPrivateTableNotification App is  Open");
        }
    }

    private void showPrivateTableNotification() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Intent pushNotification = new Intent(Config.QuizRequest);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
            //  sendprivateNotification(message);
            S.E("showPrivateTableNotification App is  Open");
        } else {
            S.E("App is not Open");
            sendprivateNotification(message);

        }
    }


    private void showNotificationMessage(Context context, String title, String action, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, action, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String action, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, action, message, timeStamp, intent, imageUrl);
    }

    private void sendprivateNotification(String messageBody) {
        try {
            JSONObject jsonObject1 = new JSONObject(messageBody);
            S.E("messageBody" + messageBody);
            notificationTitle = jsonObject1.getString("notificationTitle");
            msg = jsonObject1.getString("msg");
            JSONObject jsonObject = jsonObject1.getJSONObject("msg");
            quiz_id = jsonObject.getString("quiz_id");
            quiz_time = jsonObject.getString("quiz_time");
            quiz_topicId = jsonObject.getString("topic_id");
            msg = "Invite " + jsonObject.getString("user_name") + " On Quiz";
            category_name = jsonObject.getString("category_name");
            subject_name = jsonObject.getString("subject_name");
            topic_name = jsonObject.getString("topic_name");
            point = jsonObject.getString("point");
            user_name = jsonObject.getString("user_name");
            userProfilePic = jsonObject.getString("userProfilePic");
            host_user_id = jsonObject.getString("host_user_id");
            defender_user_id = jsonObject.getString("defender_user_id");
            tableId = jsonObject.getString("tableId");
            player_key = jsonObject.getString("player_key");

        } catch (JSONException e) {

        }
        Intent intent = new Intent(this, QuizQuestionaryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("quiz_id", quiz_id);
        bundle.putString("time", quiz_time);
        bundle.putString("topicId", quiz_topicId);
        bundle.putString("ActivityCheck", "FirebaseNotification_Throw");
        bundle.putString("category_name", category_name);
        bundle.putString("subject_name", subject_name);
        bundle.putString("topic_name", topic_name);
        bundle.putString("point", point);
        bundle.putString("user_name", user_name);
        bundle.putString("userProfilePic", userProfilePic);
        bundle.putString("host_user_id", host_user_id);
        bundle.putString("defender_user_id", defender_user_id);
        bundle.putString("tableId", tableId);
        bundle.putString("player_key", player_key);
        bundle.putString("ComingCheck", "PrivateTable");
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        // Get the layouts to use in the custom notification
        //RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);
        // RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification_large);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentText(msg)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                //.setCustomContentView(notificationLayout)
                .setAutoCancel(true)
//                .setCustomBigContentView(notificationLayoutExpanded)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
//                /*.addAction(R.drawable.ic_close_cross, "Cancel", pendingIntent)
//                .addAction(R.drawable.ic_hand_with_thumb_up, "Confirm", pendingIntent);*/
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void SendAddQuizNotification(String message, String title) {
        Intent intent = new Intent(this, QuizMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentText(message)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                //.setCustomContentView(notificationLayout)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void GetJob_AlertNotification(String message, String title) {
        Intent intent = new Intent(this, JobAlertActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentText(message)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                //.setCustomContentView(notificationLayout)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void send_MultiPlayereNotification(JSONObject messageBody) {


        try {
            JSONObject data1 = messageBody.getJSONObject("data");
            JSONObject message = data1.getJSONObject("message");
            JSONObject jsonObject = message.getJSONObject("msg");
            S.E("messageBody" + messageBody);
            notificationTitle = message.getString("notificationTitle");

            quiz_id = jsonObject.getString("quiz_id");
            quiz_time = jsonObject.getString("quiz_time");
            quiz_topicId = jsonObject.getString("topic_id");
            message1 = "Invite " + jsonObject.getString("user_name") + " New Multiplaer Quiz";
            category_name = jsonObject.getString("category_name");
            subject_name = jsonObject.getString("subject_name");
            topic_name = jsonObject.getString("topic_name");
            point = jsonObject.getString("point");
            user_name = jsonObject.getString("user_name");
            userProfilePic = jsonObject.getString("userProfilePic");
            host_user_id = jsonObject.getString("host_user_id");
            // defender_user_id=jsonObject.getString("defender_user_id");
            tableId = jsonObject.getString("tableId");

            // player_key=jsonObject.getString("player_key");

        } catch (JSONException e) {
            S.E("Exception" + e);
        }
        Intent intent = new Intent(this, QuizQuestionaryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("quiz_id", quiz_id);
        bundle.putString("time", quiz_time);
        bundle.putString("topicId", quiz_topicId);
        bundle.putString("ActivityCheck", "FirebaseNotification_Throw");
        bundle.putString("category_name", category_name);
        bundle.putString("subject_name", subject_name);
        bundle.putString("topic_name", topic_name);
        bundle.putString("point", point);
        bundle.putString("user_name", user_name);
        bundle.putString("userProfilePic", userProfilePic);
        bundle.putString("host_user_id", host_user_id);
        bundle.putString("defender_user_id", defender_user_id);
        bundle.putString("tableId", tableId);
        bundle.putString("player_key", player_key);
        bundle.putString("ComingCheck", "Multiplayer");
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        // Get the layouts to use in the custom notification
        //RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);
        // RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification_large);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentText(message1)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                //.setCustomContentView(notificationLayout)
                .setAutoCancel(true)
//                .setCustomBigContentView(notificationLayoutExpanded)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
//                /*.addAction(R.drawable.ic_close_cross, "Cancel", pendingIntent)
//                .addAction(R.drawable.ic_hand_with_thumb_up, "Confirm", pendingIntent);*/
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }

}