package com.dollop.placementadda.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cunoraz.gifview.library.GifView;
import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.BottomNoListAdapter;
import com.dollop.placementadda.adapter.QuizQuetionaryAdapter;
import com.dollop.placementadda.database.datahelper.QuestionaryListHelper;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.database.model.QuestionaryModel;
import com.dollop.placementadda.model.AttempedQuesModel;
import com.dollop.placementadda.model.ItemSelectedModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class ExamInstructionActivity extends BaseActivity {
    //Examination instruction activity use for show statics instruction of examination
    TextView examNameTv;
    Button loginBtn;
    private String quiz_id = "", quiz_time = "", quiz_topicId = "", Subjecttopic_ID = "", CategerySubject_ID = "", SelectedQuizCategeryId = "", OtherUserId = "", OtherUserName = "", OtherUserProfilePic = "", points = "", Member_number = "", OtherCity = "", OtherGender = "";
    Dialog dialog;
    private Handler handler;
    private String ActivityCheck;
    String OtherSecondUserId = "", OtherSecondUserName = "", OtherSecondCity = "", OtherSecondGender = "", OtherSecondUserProfilePic = "";
    String OtherThirdUserId = "", OtherThirdUserName = "", OtherThirdCity = "", OtherThirdGender = "", OtherThirdUserProfilePic = "";
    private String tableId;
    private Runnable myRunnable;
    Boolean condition = false;

    @Override
    protected int getContentResId() {
        return R.layout.activity_exam_instruction;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(ExamInstructionActivity.this);
        setToolbarWithBackButton("Exam Instruction");
        toolbar = findViewById(R.id.tool_bar);
        examNameTv = findViewById(R.id.examNameTv);
        loginBtn = findViewById(R.id.loginBtn);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("ActivityCheck").equals("QuizCategery")) {
                ActivityCheck = bundle.getString("ActivityCheck");
                quiz_id = bundle.getString("quiz_id");
                quiz_time = bundle.getString("quiz_time");
                quiz_topicId = bundle.getString("quiz_topicId");
                Subjecttopic_ID = bundle.getString("Subjecttopic_ID");
                CategerySubject_ID = bundle.getString("CategerySubject_ID");
                SelectedQuizCategeryId = bundle.getString("SelectedQuizCategeryId");
                OtherUserId = bundle.getString("OtherUserId");
            } else if (bundle.getString("ActivityCheck").equals("Twoplayer")) {
                if (bundle.getString("Member_number").equals("2")) {
                    ActivityCheck = bundle.getString("ActivityCheck");
                    quiz_id = bundle.getString("quiz_id");
                    quiz_time = bundle.getString("quiz_time");
                    quiz_topicId = bundle.getString("quiz_topicId");
                    Subjecttopic_ID = bundle.getString("Subjecttopic_ID");
                    CategerySubject_ID = bundle.getString("CategerySubject_ID");
                    SelectedQuizCategeryId = bundle.getString("SelectedQuizCategeryId");
                    OtherUserId = bundle.getString("OtherUserId");
                    OtherUserName = bundle.getString("OtherUserName");
                    OtherUserProfilePic = bundle.getString("OtherUserProfilePic");
                    points = bundle.getString("points");
                    Member_number = bundle.getString("Member_number");
                    OtherCity = bundle.getString("OtherCity");
                    OtherGender = bundle.getString("OtherGender");
                } else if (bundle.getString("Member_number").equals("3")) {
                    ActivityCheck = bundle.getString("ActivityCheck");
                    quiz_id = bundle.getString("quiz_id");
                    quiz_time = bundle.getString("quiz_time");
                    quiz_topicId = bundle.getString("quiz_topicId");
                    Subjecttopic_ID = bundle.getString("Subjecttopic_ID");
                    CategerySubject_ID = bundle.getString("CategerySubject_ID");
                    SelectedQuizCategeryId = bundle.getString("SelectedQuizCategeryId");
                    OtherUserId = bundle.getString("OtherUserId");
                    OtherUserName = bundle.getString("OtherUserName");
                    OtherUserProfilePic = bundle.getString("OtherUserProfilePic");
                    points = bundle.getString("points");
                    Member_number = bundle.getString("Member_number");
                    OtherCity = bundle.getString("OtherCity");
                    OtherGender = bundle.getString("OtherGender");
                    OtherSecondUserId = bundle.getString("OtherSecondUserId");
                    OtherSecondUserName = bundle.getString("OtherSecondUserName");
                    OtherSecondCity = bundle.getString("OtherSecondCity");
                    OtherSecondGender = bundle.getString("OtherSecondGender");
                    OtherSecondUserProfilePic = bundle.getString("OtherSecondUserProfilePic");
                } else if (bundle.getString("Member_number").equals("4")) {
                    ActivityCheck = bundle.getString("ActivityCheck");
                    quiz_id = bundle.getString("quiz_id");
                    quiz_time = bundle.getString("quiz_time");
                    quiz_topicId = bundle.getString("quiz_topicId");
                    Subjecttopic_ID = bundle.getString("Subjecttopic_ID");
                    CategerySubject_ID = bundle.getString("CategerySubject_ID");
                    SelectedQuizCategeryId = bundle.getString("SelectedQuizCategeryId");
                    OtherUserId = bundle.getString("OtherUserId");
                    OtherUserName = bundle.getString("OtherUserName");
                    OtherUserProfilePic = bundle.getString("OtherUserProfilePic");
                    points = bundle.getString("points");
                    Member_number = bundle.getString("Member_number");
                    OtherCity = bundle.getString("OtherCity");
                    OtherGender = bundle.getString("OtherGender");

                    OtherSecondUserId = bundle.getString("OtherSecondUserId");
                    OtherSecondUserName = bundle.getString("OtherSecondUserName");
                    OtherSecondCity = bundle.getString("OtherSecondCity");
                    OtherSecondGender = bundle.getString("OtherSecondGender");
                    OtherSecondUserProfilePic = bundle.getString("OtherSecondUserProfilePic");

                    OtherThirdUserId = bundle.getString("OtherThirdUserId");
                    OtherThirdUserName = bundle.getString("OtherThirdUserName");
                    OtherThirdCity = bundle.getString("OtherThirdCity");
                    OtherThirdGender = bundle.getString("OtherThirdGender");
                    OtherThirdUserProfilePic = bundle.getString("OtherThirdUserProfilePic");
                }
            }
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCheck.equals("QuizCategery")) {
                    checkAvilablityOfUser();
                } else if (ActivityCheck.equals("Twoplayer")) {
                    SendNotificationUser();
                }

            }
        });
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(ExamInstructionActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiverCancel,
                new IntentFilter(Config.CANCEL_REQUEST));

     /*   LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.MultiPlayer_defender_Accept));*/
    }

    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            // handler.removeCallbacksAndMessages(null);
            stophandler();
            S.E("MultiplayerAccept Here::"+intent.getStringExtra("message"));

            try {
                dialog.dismiss();
                JSONObject jsonObject = new JSONObject(intent.getStringExtra("message"));
                JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                String defender_id = jsonObject1.getString("defender_id");
                String host_user_id = jsonObject1.getString("host_user_id");
                String point = jsonObject1.getString("point");
                String player_key = jsonObject1.getString("player_key");
                Intent intent1 = new Intent(ExamInstructionActivity.this, QuizQuestionaryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("quiz_id", quiz_id);
                bundle.putString("time", quiz_time);
                bundle.putString("topicId", quiz_topicId);
                bundle.putString("ActivityCheck", "DirectPlay");
                bundle.putString("defender_id", defender_id);
                bundle.putString("host_user_id", host_user_id);
                bundle.putString("point", point);
                bundle.putString("tableId", tableId);
                bundle.putString("player_key", player_key);
                intent1.putExtras(bundle);
                startActivity(intent1);
            } catch (Exception e) {
            }
        }
    };
    private BroadcastReceiver mRegistrationBroadcastReceiverCancel = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            // handler.removeCallbacksAndMessages(null);
            stophandler();
            dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(intent.getStringExtra("message"));
                JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                String defender_id = jsonObject1.getString("defender_id");
                String userName = jsonObject1.getString("userName");
                String userProfilePic = jsonObject1.getString("userProfilePic");
                CancelPopup(userName, userProfilePic);
            } catch (Exception e) {
            }
        }
    };

    private void checkAvilablityOfUser() {
        new JSONParser(ExamInstructionActivity.this).parseVollyStringRequest(Const.URL.GetQuestions, 1, getParms(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");

                    if (status == 200) {

                        if (message.equals("already played")) {
                            String data = mainobject.getString("data");
                            Watch_Popup(data);
                        } else {
                            if (ActivityCheck.equals("QuizCategery")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("ActivityCheck", "QuizCategery");
                                bundle.putString("quiz_id", quiz_id);
                                bundle.putString("time", quiz_time);
                                bundle.putString("topicId", quiz_topicId);
                                S.E("quiz_topicId:::"+quiz_topicId);
                                S.I_clear(ExamInstructionActivity.this, QuizQuestionaryActivity.class, bundle);
                            } else if (ActivityCheck.equals("Twoplayer")) {
                                SendNotificationUser();
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParms() {
        HashMap<String, String> params = new HashMap<>();
        params.put("quiz_id", quiz_id);
        params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
        return params;
    }


    public void ConfirmPopup() {
        CircleImageView otherIv, secondIv, thirdIv;
        dialog = new Dialog(ExamInstructionActivity.this);
        if (Member_number.equals("2")) {
            dialog.setContentView(R.layout.twoplayerloading_popup);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCanceledOnTouchOutside(false);
            otherIv = (CircleImageView) dialog.findViewById(R.id.otherIv);

            if (!OtherUserProfilePic.equals("")) {
                Picasso.with(ExamInstructionActivity.this).
                        load(OtherUserProfilePic)
                        .into(otherIv);
            }
        } else if (Member_number.equals("4")) {
            dialog.setContentView(R.layout.threeplayerloading_popup);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCanceledOnTouchOutside(false);
            otherIv = (CircleImageView) dialog.findViewById(R.id.otherIv);
            secondIv = (CircleImageView) dialog.findViewById(R.id.secondIv);
            thirdIv = (CircleImageView) dialog.findViewById(R.id.thirdIv);
            if (!OtherUserProfilePic.equals("")) {
                Picasso.with(ExamInstructionActivity.this).
                        load(OtherUserProfilePic)
                        .into(otherIv);
            }
            if (!OtherSecondUserProfilePic.equals("")) {
                Picasso.with(ExamInstructionActivity.this).
                        load(OtherSecondUserProfilePic)
                        .into(secondIv);
            }
            if (!OtherThirdUserProfilePic.equals("")) {
                Picasso.with(ExamInstructionActivity.this).
                        load(OtherThirdUserProfilePic)
                        .into(thirdIv);
            }

        } else if (Member_number.equals("3")) {
            dialog.setContentView(R.layout.threeplayerloading_popup);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCanceledOnTouchOutside(false);
            LinearLayout third_layout;
            otherIv = (CircleImageView) dialog.findViewById(R.id.otherIv);
            secondIv = (CircleImageView) dialog.findViewById(R.id.secondIv);
            thirdIv = (CircleImageView) dialog.findViewById(R.id.thirdIv);
            third_layout = (LinearLayout) dialog.findViewById(R.id.third_layout);
            third_layout.setVisibility(View.GONE);
            if (!OtherUserProfilePic.equals("")) {
                Picasso.with(ExamInstructionActivity.this).
                        load(OtherUserProfilePic)
                        .into(otherIv);
            }
            if (!OtherSecondUserProfilePic.equals("")) {
                Picasso.with(ExamInstructionActivity.this).
                        load(OtherSecondUserProfilePic)
                        .into(secondIv);
            }

        }


        handler = new Handler();
        myRunnable = new Runnable() {
            public void run() {
                if (!condition) {
                    CancelTable();
                    dialog.dismiss();

                }
            }
        };
        handler.postDelayed(myRunnable, 60000);
        dialog.show();
    }

    public void CancelPopup(String Username, String profile_pic) {
        dialog = new Dialog(ExamInstructionActivity.this);
        dialog.setContentView(R.layout.cancel_request);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        CircleImageView otherIv;
        ImageView cancel_img, full_image;
        Button cancel_btn;
        TextView username_tv;

        otherIv = (CircleImageView) dialog.findViewById(R.id.otherIv);
        cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        username_tv = (TextView) dialog.findViewById(R.id.username_tv);
        username_tv.setText(Username + " Canceled Your Request On Table");
        if (!profile_pic.equals("")) {
            Picasso.with(ExamInstructionActivity.this).
                    load(Const.URL.IMAGE_URL + profile_pic)
                    .into(otherIv);
        }
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("points", points);
                bundle.putString("Member_number", Member_number);
                S.I_clear(ExamInstructionActivity.this, SelectTwoPlayerActivity.class, bundle);

            }
        });

        dialog.show();
    }

    private void SendNotificationUser() {
        new JSONParser(ExamInstructionActivity.this).parseVollyStringRequest(Const.URL.twoPlayerNotification, 1, getParmss(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    if (mainobject.getString("status").equals("200")) {
                        JSONObject jsonObject = mainobject.getJSONObject("data");
                        tableId = jsonObject.getString("tableId");
                        ConfirmPopup();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParmss() {
        HashMap<String, String> params = new HashMap<>();
        params.put("quiz_id", quiz_id);
        params.put("quiz_time", quiz_time);
        params.put("topic_id", Subjecttopic_ID);
        params.put("subject_id", CategerySubject_ID);
        params.put("category_id", SelectedQuizCategeryId);
        params.put("defender_user_id", OtherUserId);
        params.put("def1", OtherSecondUserId);
        params.put("def2", OtherThirdUserId);
        params.put("point", points);
        params.put("tableNo", Member_number);
        params.put("host_user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
        return params;
    }

    public void stophandler() {
        condition = true;
    }

    public void Watch_Popup(String data) {
        final Dialog dialog1 = new Dialog(ExamInstructionActivity.this);
        dialog1.setContentView(R.layout.watch_play_quiz_popup);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog1.setCanceledOnTouchOutside(false);
        Button continue_btn;
        TextView aftertime_tv;
        aftertime_tv = (TextView) dialog1.findViewById(R.id.aftertime_tv);
        continue_btn = (Button) dialog1.findViewById(R.id.continue_btn);
        aftertime_tv.setText(data);
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                S.I_clear(ExamInstructionActivity.this, QuizMainActivity.class, null);
            }
        });
        dialog1.show();
    }

    private void CancelTable() {
        new JSONParser(ExamInstructionActivity.this).parseVollyStringRequest(Const.URL.table_expire, 1, getParmsstable_expire(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    if (mainobject.getString("status").equals("200")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("points", points);
                        bundle.putString("Member_number", Member_number);
                        S.I_clear(ExamInstructionActivity.this, SelectTwoPlayerActivity.class, bundle);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParmsstable_expire() {
        HashMap<String, String> params = new HashMap<>();
        params.put("defender_user_id", OtherUserId);
        params.put("def1", OtherSecondUserId);
        params.put("def2", OtherThirdUserId);
        params.put("tableNo", Member_number);
        params.put("host_user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
        return params;
    }
}
