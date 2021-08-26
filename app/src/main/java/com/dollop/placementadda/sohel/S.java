package com.dollop.placementadda.sohel;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.QuizMainActivity;
import com.dollop.placementadda.activity.QuizPlayDetailsActivity;
import com.dollop.placementadda.activity.QuizQuestionaryActivity;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class S {
    static Boolean condition = false;
    static String notificationTitle = "";
    static String msg = "";
    static String quiz_id = "";
    static String quiz_time = "";
    static String quiz_topicId = "";
    static String category_name = "";
    static String subject_name = "";
    static String topic_name = "";
    static String point = "";
    static String user_name = "";
    static String userProfilePic = "";
    static String host_user_id = "";
    static String defender_user_id = "";
    static String tableId = "";
    static String player_key = "";
    static String message = "";
    static Handler handler;
    static Runnable myRunnable;
    static Dialog dialog;

    public static void I(Context cx, Class<?> startActivity, Bundle data) {
        Intent i = new Intent(cx, startActivity);
        if (data != null)
            i.putExtras(data);
        cx.startActivity(i);
    }

    public static void I_finish(Context cx, Class<?> startActivity, Bundle data) {
        Intent i = new Intent(cx, startActivity);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (data != null)
            i.putExtras(data);
        cx.startActivity(i);
    }

    public static void I_clear(Context cx, Class<?> startActivity, Bundle data) {
        Intent i = new Intent(cx, startActivity);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (data != null)
            i.putExtras(data);
        cx.startActivity(i);
    }

    public static void E(String msg) {
        if (true)
            Log.e("Log.E By Sohel", ":::"+msg);
    }

    private static void askForPermission(Activity context, String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);
            }
        } else {
            /*   Toast.makeText(context, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
             */
        }
    }


    /* public static void dialogForAvailableStatus(Context cx) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(cx);
        dialog.setCancelable(false);
        dialog.setTitle("Not Available");
        dialog.setMessage(R.string.city_available_status);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        final AlertDialog alert = dialog.create();
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.show();
    }
*/
    public static Dialog initProgressDialog(Context c) {

        Dialog dialog = new Dialog(c);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    public static void T(Context c, String msg) {
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }

    public static Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
        return params;
    }

    /*public static void Snack(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        textView.setTextSize(20);
        snackbar.show();
    }*/

    public static void share(Context c, String subject, String shareBody) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        c.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static void shareFile(Context c, File file, String subject, String shareBody) {
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        if (file.exists()) {
            intentShareFile.setType("application/image");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file));
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, subject);
            intentShareFile.putExtra(Intent.EXTRA_TEXT, shareBody);
            c.startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }
    }

    public static BroadcastReceiver LocalBroadcastReciver(final Context c) {
        BroadcastReceiver TableRequest = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Get extra data included in the Intent
                // handler.removeCallbacksAndMessages(null);

                S.E("checkPrivate and Multiplayer ::" + intent.getStringExtra("message"));


                try {
                    JSONObject jsonObject1 = new JSONObject(intent.getStringExtra("message"));
                    notificationTitle = jsonObject1.getString("notificationTitle");
                    msg = jsonObject1.getString("msg");
                     dialog = new Dialog(c);
                    if (msg.equals("Close Notification")) {
                        stophandler();
                        S.I_clear(c,QuizMainActivity.class,null);

                    } else {
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
//                    defender_user_id=jsonObject.getString("defender_user_id");
                        tableId = jsonObject.getString("tableId");

                        message = jsonObject.getString("message");


                        if (message.equals("New Multiplaer Notification")) {
                            dialog.dismiss();
                        } else {
                            defender_user_id = jsonObject.getString("defender_user_id");
                            player_key = jsonObject.getString("player_key");
                        }


                        dialog.setContentView(R.layout.player_table_notification);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setCanceledOnTouchOutside(false);
                        CircleImageView comment_user_circullerimage;
                        ImageView cancel_img, full_image;
                        Button continue_btn, cancel_btn;
                        TextView CommentUserName_tv, categery_id, subject_id, topic_id, points_tv;
                        /* GifView gifView1 = (GifView) dialog.findViewById(R.id.gif1);*/
                        comment_user_circullerimage = (CircleImageView) dialog.findViewById(R.id.comment_user_circullerimage);
                        CommentUserName_tv = (TextView) dialog.findViewById(R.id.CommentUserName_tv);
                        categery_id = (TextView) dialog.findViewById(R.id.categery_id);
                        subject_id = (TextView) dialog.findViewById(R.id.subject_id);
                        topic_id = (TextView) dialog.findViewById(R.id.topic_id);
                        points_tv = (TextView) dialog.findViewById(R.id.points_tv);
                        continue_btn = (Button) dialog.findViewById(R.id.continue_btn);
                        cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
                        points_tv.setText(point + " Coins to play this Quiz");
                        CommentUserName_tv.setText(user_name);
                        categery_id.setText(category_name);
                        subject_id.setText(subject_name);
                        topic_id.setText(topic_name);

               /* host_user_id = bundle.getString("host_user_id");
                defender_id = bundle.getString("defender_user_id");
                player_key = bundle.getString("player_key");

                quiz_id = getIntent().getStringExtra("quiz_id");
                quiz_time = bundle.getString("time");
                quiz_topicId = bundle.getString("topicId");
                tableId = getIntent().getStringExtra("tableId");*/
                        final String finalHost_user_id = host_user_id;
                        final String finalpoint = point;
                        if (!userProfilePic.equals("") && !userProfilePic.equals("null")) {
                            Picasso.with(c).
                                    load(Const.URL.IMAGE_URL + userProfilePic)
                                    .into(comment_user_circullerimage);

                        }

                        continue_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                stophandler();
                                dialog.dismiss();
                                if (message.equals("NewRequestForTwoPlayer")) {

                                    AcceptonTable(c, finalHost_user_id, finalpoint, player_key);


                                } else {
                                    Accept_Multiplayer(c, finalHost_user_id, finalpoint, player_key);
                                }

                            }

                        });
                        cancel_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                cancelRequestToPlayers(c, finalHost_user_id, finalpoint);
                                stophandler();
                            }
                        });
                        handler = new Handler();

                        myRunnable = new Runnable() {
                            public void run() {
                                if (!condition) {
                                    cancelRequestToPlayers(c, finalHost_user_id, finalpoint);
                                    dialog.dismiss();
                                }
                            }
                        };
                        handler.postDelayed(myRunnable, 60000);

                        dialog.show();
                    }
                } catch (Exception e) {

                    S.E("checkException Here" + e);
                }

            }

        };
        return TableRequest;
    }

    public static void stophandler() {
        condition = true;
    }

    public static void cancelRequestToPlayers(final Context c1, String host_user_id, String finalpoint) {
        new JSONParser(c1).parseVollyStringRequest(Const.URL.cancelRequestToPlayers, 1, getCancelParam(host_user_id, finalpoint), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("cancelRequestToPlayers detail=======" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        S.I_clear(c1, QuizMainActivity.class, null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }

    public static Map<String, String> getCancelParam(String host_user_id, String finalpoint) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("host_user_id", host_user_id);
        stringStringHashMap.put("defender_user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        //stringStringHashMap.put(player_key, UserDataHelper.getInstance().getList().get(0).getUserId());
        stringStringHashMap.put("point", finalpoint);
        return stringStringHashMap;
    }

    public static void AcceptonTable(final Context c1, final String host_user_id, final String finalpoint, final String player_key) {
        new JSONParser(c1).parseVollyStringRequest(Const.URL.acceptRequestToPlayers, 1, getLoginParam(finalpoint, host_user_id, player_key), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("get all Login detail=======" + response);
                S.E("paramCheck" + getLoginParam(finalpoint, host_user_id, player_key));
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        Intent intent1 = new Intent(c1, QuizQuestionaryActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("quiz_id", quiz_id);
                        bundle.putString("time", quiz_time);
                        bundle.putString("topicId", quiz_topicId);
                        bundle.putString("ActivityCheck", "S Class");
                        bundle.putString("host_user_id", host_user_id);
                        bundle.putString("point", point);
                        bundle.putString("tableId", tableId);
                        bundle.putString("player_key", player_key);
                        intent1.putExtras(bundle);
                        c1.startActivity(intent1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }

    public static Map<String, String> getLoginParam(String finalpoint, String host_user_id, String player_key) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("host_user_id", host_user_id);
        //stringStringHashMap.put("defender_user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        stringStringHashMap.put(player_key, UserDataHelper.getInstance().getList().get(0).getUserId());
        stringStringHashMap.put("point", finalpoint);
        stringStringHashMap.put("tableId", tableId);
        stringStringHashMap.put("quiz_id", quiz_id);
        return stringStringHashMap;
    }

    public static void Accept_Multiplayer(final Context c1, final String host_user_id, String finalpoint, final String player_key) {
        new JSONParser(c1).parseVollyStringRequest(Const.URL.multiplierPlayerAccepterNotification, 1, getMultiplayerParam(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("Accept_Multiplayer detail=======" + response);
                S.E("Accept_Multiplayer params=======" + getMultiplayerParam());
                S.E("Accept_Multiplayer player_key=======" + player_key);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        SavedData.saveNotificationRequestGameStart("true");
                      String player_key1=  jsonObject.getString("player_key");
                        Intent intent1 = new Intent(c1, QuizPlayDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("quiz_id", quiz_id);
                        bundle.putString("time", quiz_time);
                        bundle.putString("topicId", quiz_topicId);
                        bundle.putString("ActivityCheck", "S Classmultiplayer");
                        bundle.putString("host_user_id", host_user_id);
                        bundle.putString("point", point);
                        bundle.putString("tableId", tableId);
                        bundle.putString("player_key", player_key1);
                        intent1.putExtras(bundle);
                        c1.startActivity(intent1);


/*
                        Bundle bundle = new Bundle();
                        bundle.putString("ActivityCheck", "S Classmultiplayer");
                        bundle.putString("quiz_id", quiz_id);
                        bundle.putString("quiz_time", quiz_time);
                        bundle.putString("quiz_topicId", QuizCategory_id);
                        bundle.putString("Subjecttopic_ID", Subjecttopic_ID);
                        bundle.putString("CategerySubject_ID", CategerySubject_ID);
                        bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
                        bundle.putString("Member_number", Member_number);
                        bundle.putString("points", points);
                        S.I(c1, QuizPlayDetailsActivity.class, bundle);*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
    }

    public static Map<String, String> getMultiplayerParam() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        stringStringHashMap.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
        stringStringHashMap.put("tableId", tableId);
        stringStringHashMap.put("point", point);
        stringStringHashMap.put("quiz_id", quiz_id);


        return stringStringHashMap;
    }
}