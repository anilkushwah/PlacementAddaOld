package com.dollop.placementadda.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;

import com.dollop.placementadda.adapter.BottomNoListAdapter;
import com.dollop.placementadda.adapter.QuizQuetionaryAdapter;
import com.dollop.placementadda.database.datahelper.QuestionaryListHelper;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.database.model.QuestionaryModel;
import com.dollop.placementadda.database.model.UserModel;
import com.dollop.placementadda.model.AttempedQuesModel;
import com.dollop.placementadda.model.ItemSelectedModel;
import com.dollop.placementadda.model.SelectedListModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.RecyclerItemClickListener;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class QuizQuestionaryActivity extends BaseActivity {
    public static String ActivityCheck;
    public static String quiz_id, quiz_time, quiz_topicId;
    Toolbar toolBar;
    TextView totalTimeId;
    TextView tvTotalQueId;
    TextView tvRunningId;
    TextView tvRunningQuestionAttempId;
    ViewPager pager;
    CircleIndicator indicator;
    CardView liveQuizeId;
    ImageView ivBackId;
    RecyclerView rvPositionId;
    ImageView ivNextId;
    ArrayList<QuestionaryModel> questionaryModels = new ArrayList<>();
    ArrayList<AttempedQuesModel> attempedQuesModels = new ArrayList<>();
    int j = 0;
    ArrayList<ItemSelectedModel> itemSelectedModelArrayList = new ArrayList<>();
    Dialog materialDialog;
    ArrayList<String> questionId = new ArrayList<>();
    ArrayList<String> myAnswerId = new ArrayList<>();
    ArrayList<String> answerId = new ArrayList<>();
    String points = "", host_user_id = "", defender_id = "", tableId = "", player_key = "";
    Bundle bundle;
    Dialog dialog;
    Boolean condition = false;
    Chronometer cmTimerShowRunningId;
    private ViewPager Pager;
    private LinearLayoutManager recyclerViewlayoutManager;
    private BottomNoListAdapter stringArrayAdapter;
    private String questionIds = "";
    private String allAnswerId = "";
    private String rightAnswerId = "";
    private String table = "";
    private long second;
    private String CheckStatus = "";
    private Handler handler;
    private Runnable myRunnable;
    private BroadcastReceiver table_expaire = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                dialog.dismiss();
                materialDialog.dismiss();
                S.I_clear(QuizQuestionaryActivity.this, QuizMainActivity.class, null);
            } catch (Exception e) {
            }

        }

    };

    private BroadcastReceiver mRegistrationBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                JSONObject jsonObject = new JSONObject(intent.getStringExtra("message"));
                JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                quiz_id = jsonObject1.getString("quiz_id");
                player_key = jsonObject1.getString("player_key");
                stophandler();
                cmTimerShowRunningId.setBase(SystemClock.elapsedRealtime());
                cmTimerShowRunningId.start();
                try {
                    materialDialog.dismiss();
                } catch (Exception e) {
                }

            } catch (Exception e) {
            }
            ImportantMethode();
        }

    };


    @Override
    protected int getContentResId() {
        return R.layout.activity_quiz_questionary;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(QuizQuestionaryActivity.this);
        setToolbarWithBackButton("Questionary");
        toolbar = findViewById(R.id.tool_bar);
        totalTimeId = findViewById(R.id.totalTimeId);
        tvTotalQueId = findViewById(R.id.tvTotalQueId);
        tvRunningId = findViewById(R.id.tvRunningId);
        cmTimerShowRunningId = findViewById(R.id.cmTimerShowRunningId);
        tvRunningQuestionAttempId = findViewById(R.id.tvRunningQuestionAttempId);
        pager = findViewById(R.id.pager);
        indicator = findViewById(R.id.indicator);
        liveQuizeId = findViewById(R.id.liveQuizeId);
        ivBackId = findViewById(R.id.ivBackId);
        rvPositionId = findViewById(R.id.rvPositionId);
        ivNextId = findViewById(R.id.ivNextId);
        cmTimerShowRunningId.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                second= time/1000;
                String t = (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
                chronometer.setText(t);
                String totalTimeInMin = getIntent().getStringExtra("time");
                int totalTime = Integer.parseInt(totalTimeInMin);
                int checkMin = (int) time / 60000;
                if (checkMin >= totalTime) {
                    try {

                            cmTimerShowRunningId.stop();


                    } catch (Exception e) {
                    }
                    questionIds = "";
                    allAnswerId = "";
                    rightAnswerId = "";
                // Collections.reverse(questionId);
                 Collections.reverse(myAnswerId);
               //   Collections.reverse(answerId);
                    for (int i = 0; i < questionId.size(); i++) {
                        if (i == 0) {
                            questionIds = questionIds + questionId.get(i);
                            allAnswerId = allAnswerId + myAnswerId.get(i);
                            rightAnswerId = rightAnswerId + answerId.get(i);
                        } else {
                            questionIds = questionIds + "," + questionId.get(i);
                            allAnswerId = allAnswerId + "," + myAnswerId.get(i);
                            rightAnswerId = rightAnswerId + "," + answerId.get(i);
                        }
                    }
                    //have a Add Video code
                    second= time/1000;
                    SubmitAns();

                }
            }
        });
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("ActivityCheck").equals("QuizCategery")) {
                ActivityCheck = bundle.getString("ActivityCheck");
                quiz_id = bundle.getString("quiz_id");
                quiz_time = bundle.getString("time");
                quiz_topicId = bundle.getString("topicId");
                ImportantMethode();
            } else if (bundle.getString("ActivityCheck").equals("FirebaseNotification_Throw")) {
                ActivityCheck = bundle.getString("ActivityCheck");
                dialog = new Dialog(QuizQuestionaryActivity.this);
                dialog.setContentView(R.layout.player_table_notification);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCanceledOnTouchOutside(false);
                CircleImageView comment_user_circullerimage;
                ImageView cancel_img, full_image;
                Button continue_btn, cancel_btn;
                TextView CommentUserName_tv, categery_id, subject_id, topic_id, points_tv;
                comment_user_circullerimage = (CircleImageView) dialog.findViewById(R.id.comment_user_circullerimage);
                CommentUserName_tv = (TextView) dialog.findViewById(R.id.CommentUserName_tv);
                categery_id = (TextView) dialog.findViewById(R.id.categery_id);
                subject_id = (TextView) dialog.findViewById(R.id.subject_id);
                topic_id = (TextView) dialog.findViewById(R.id.topic_id);
                points_tv = (TextView) dialog.findViewById(R.id.points_tv);
                continue_btn = (Button) dialog.findViewById(R.id.continue_btn);
                cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
                points_tv.setText(getIntent().getStringExtra("point") + " Coins to play this Quiz");
                CommentUserName_tv.setText(getIntent().getStringExtra("user_name"));
                categery_id.setText(getIntent().getStringExtra("category_name"));
                subject_id.setText(getIntent().getStringExtra("subject_name"));
                topic_id.setText(getIntent().getStringExtra("topic_name"));
                points = bundle.getString("point");
                table = "table";
                host_user_id = bundle.getString("host_user_id");
                defender_id = bundle.getString("defender_user_id");
                player_key = bundle.getString("player_key");
                quiz_id = getIntent().getStringExtra("quiz_id");
                quiz_time = bundle.getString("time");
                quiz_topicId = bundle.getString("topicId");
                tableId = getIntent().getStringExtra("tableId");

                if (!getIntent().getStringExtra("userProfilePic").equals("") && !getIntent().getStringExtra("userProfilePic").equals("null")) {
                    Picasso.with(QuizQuestionaryActivity.this).
                            load(Const.URL.IMAGE_URL + getIntent().getStringExtra("userProfilePic"))
                            .into(comment_user_circullerimage);

                }
                continue_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        stophandler();
                        if (bundle.getString("ComingCheck").equals("Multiplayer")) {
                            Accept_Multiplayer();
                        } else if (bundle.getString("ComingCheck").equals("PrivateTable")) {
                            ADDSubmit();
                        }

                    }

                });
                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        cancelRequestToPlayers();
                        stophandler();
                    }
                });
                handler = new Handler();
                myRunnable = new Runnable() {
                    public void run() {
                        if (!condition) {
                            cancelRequestToPlayers();
                            dialog.dismiss();
                        }
                    }
                };
                handler.postDelayed(myRunnable, 60000);

                dialog.show();
            } else if (bundle.getString("ActivityCheck").equals("DirectPlay")) {
                cmTimerShowRunningId.setBase(SystemClock.elapsedRealtime());
                cmTimerShowRunningId.start();
                ActivityCheck = bundle.getString("ActivityCheck");
                quiz_id = bundle.getString("quiz_id");
                quiz_time = bundle.getString("time");
                quiz_topicId = bundle.getString("topicId");
                host_user_id = bundle.getString("host_user_id");
                defender_id = bundle.getString("defender_id");
                points = bundle.getString("point");
                tableId = bundle.getString("tableId");
                player_key = bundle.getString("player_key");
                table = "table";
                ImportantMethode();
            } else if (bundle.getString("ActivityCheck").equals("S Class")) {
                ActivityCheck = bundle.getString("ActivityCheck");
                quiz_id = bundle.getString("quiz_id");
                quiz_time = bundle.getString("time");
                quiz_topicId = bundle.getString("topicId");
                host_user_id = bundle.getString("host_user_id");
                points = bundle.getString("point");
                tableId = bundle.getString("tableId");
                player_key = bundle.getString("player_key");
                table = "table";
                materialDialog = S.initProgressDialog(QuizQuestionaryActivity.this);
                handler = new Handler();
                myRunnable = new Runnable() {
                    public void run() {
                        if (!condition) {
                            try {
                                S.T(QuizQuestionaryActivity.this, "Any One is not Active");
                                materialDialog.dismiss();
                                dialog.dismiss();
                                S.I_clear(QuizQuestionaryActivity.this, QuizMainActivity.class, null);
                            } catch (Exception e) {

                            }
                        }
                    }
                };
                handler.postDelayed(myRunnable, 60000);
                if (SavedData.getNotificationRequestForGameStart().equals("true")) {
                    try {
                        cmTimerShowRunningId.setBase(SystemClock.elapsedRealtime());
                        cmTimerShowRunningId.start();
                        JSONObject jsonObject = new JSONObject(SavedData.getNotificationJson());
                        JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                        String Messagestr = jsonObject1.getString("msg");
                        if (Messagestr.equals("Please Ready To Play Quiz")) {
                            quiz_id = jsonObject1.getString("quiz_id");
                            player_key = jsonObject1.getString("player_key");
                            stophandler();
                            try {
                                materialDialog.dismiss();
                            } catch (Exception e) {

                            }
                        }
                    } catch (JSONException e) {
                    }
                    SavedData.saveNotificationRequestGameStart("false");
                } else {
                    }
                ImportantMethode();
            }
            else if (bundle.getString("ActivityCheck").equals("S Classmultiplayer")) {
                ActivityCheck = bundle.getString("ActivityCheck");
                quiz_id = bundle.getString("quiz_id");
                quiz_time = bundle.getString("time");
                quiz_topicId = bundle.getString("topicId");
                host_user_id = bundle.getString("host_user_id");
                points = bundle.getString("point");
                tableId = bundle.getString("tableId");
                player_key = bundle.getString("player_key");
                table = "table";

                if (SavedData.getNotificationRequestForGameStart().equals("true")) {
                    try {
                        cmTimerShowRunningId.setBase(SystemClock.elapsedRealtime());
                        cmTimerShowRunningId.start();
                        JSONObject jsonObject = new JSONObject(SavedData.getNotificationJson());
                        JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                        String Messagestr = jsonObject1.getString("msg");
                        if (Messagestr.equals("Please Ready To Play Quiz")) {
                            quiz_id = jsonObject1.getString("quiz_id");
                            player_key = jsonObject1.getString("player_key");
                            stophandler();
                            try {
                                materialDialog.dismiss();
                            } catch (Exception e) {

                            }
                        }
                    } catch (JSONException e) {
                    }
                    SavedData.saveNotificationRequestGameStart("false");
                } else {
                }
                ImportantMethode();
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver1,
                new IntentFilter(Config.GAME_START));
        LocalBroadcastManager.getInstance(this).registerReceiver(table_expaire,
                new IntentFilter(Config.Table_Expaire));

        rvPositionId.addOnItemTouchListener(new RecyclerItemClickListener(QuizQuestionaryActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                pager.setCurrentItem(position);
                //open question from click on below pagination show in bottom
            }
        }));
        ivBackId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pager.getCurrentItem() != 0) {
                    pager.setCurrentItem(pager.getCurrentItem() - 1);
                }
            }
        });
        //Switch questions to backword from currnet 8331893941

        ivNextId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pager.getCurrentItem() != questionaryModels.size() - 1) {
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                } else {
                    tvRunningId.setText(0 + ":" + 0 + ":" + 0);
                    try {
                        if (ActivityCheck.equals("QuizCategery")) {
                            cmTimerShowRunningId.stop();
                        }
                    } catch (Exception e) {
                    }
                    questionIds = "";
                    allAnswerId = "";
                    rightAnswerId = "";
                    Collections.reverse(myAnswerId);
                    for (int i = 0; i < questionId.size(); i++) {
                        if (i == 0) {
                            questionIds = questionIds + questionId.get(i);
                            allAnswerId = allAnswerId + myAnswerId.get(i);
                            rightAnswerId = rightAnswerId + answerId.get(i);
                        } else {
                            questionIds = questionIds + "," + questionId.get(i);
                            allAnswerId = allAnswerId + "," + myAnswerId.get(i);
                            rightAnswerId = rightAnswerId + "," + answerId.get(i);
                        }
                    }
                    SubmitAns();

                }
            }
        });
        if (ActivityCheck.equals("QuizCategery")) {
            cmTimerShowRunningId.setBase(SystemClock.elapsedRealtime());
            cmTimerShowRunningId.start();
            QuestionList();
        }
    }

    public void ImportantMethode() {
        totalTimeId.setText(getIntent().getStringExtra("time") + " M");
        QuestionList();
    }

    public void checkedSetValue(int position, String ansPosition) {
        //get radio button touch event on activity from adapter
        QuestionaryModel questionaryModel = questionaryModels.get(position);
        questionaryModel.setMyAns(ansPosition);
        if (ansPosition.equals("notAttempt")) {

        } else {
            if (pager.getCurrentItem() != questionaryModels.size()) {
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            }
        }  //on select any option it switch page by this method
        try {

            myAnswerId.set(position, ansPosition);
            AttempedQuesModel attempedQuesModel = new AttempedQuesModel();
            attempedQuesModel.setQuestionNo(questionaryModel.getQuestionstr());
            attempedQuesModel.setAns(ansPosition);
            attempedQuesModels.set(position, attempedQuesModel);
            stringArrayAdapter.setAttemped(position);
        } catch (Exception e) {
            S.E("Adapter excepton" + e);
        }
        j = 0;
        for (int i = 0; i < attempedQuesModels.size(); i++) {
            if (attempedQuesModels.get(i).getQuestionNo().equals("")) {

            } else {
                ++j;
            }
            //get attemped questions quantity
        }
        tvRunningQuestionAttempId.setText("" + j);
        QuestionaryListHelper.getInstance().insertData(questionaryModel);
        // set attemped question for user showing his status
    }

    private void QuestionList() {
        new JSONParser(QuizQuestionaryActivity.this).parseVollyStringRequestWithautProgressBar(Const.URL.GetQuestions, 1, getParms(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    S.E("QuestionList params" + getParms());
                    S.E("QuestionList response" + response);
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {

                        QuestionaryListHelper.getInstance().deleteAll();
                        itemSelectedModelArrayList.clear();
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            QuestionaryModel questionaryModel3 = new QuestionaryModel();
                            questionaryModel3.setQuizQuestion_id(jsonObject1.getString("quizQuestion_id"));
                            questionaryModel3.setQuestionstr(jsonObject1.getString("quizQuestion"));
                            questionaryModel3.setQuestionOptions(jsonObject1.getString("questionOptions"));
                            questionId.add(jsonObject1.getString("quizQuestion_id"));
                            answerId.add(jsonObject1.getString("ans"));
                            myAnswerId.add("notAttempt");
                            questionaryModel3.setOption1(jsonObject1.getString("option_A"));
                            questionaryModel3.setOption2(jsonObject1.getString("option_B"));
                            questionaryModel3.setOption3(jsonObject1.getString("option_C"));
                            questionaryModel3.setOption4(jsonObject1.getString("option_D"));
                            questionaryModel3.setAns(jsonObject1.getString("ans"));
                            questionaryModel3.setMyAns("notAttempt");
                            if (jsonObject1.getString("questionOptions").equals("5")) {
                                questionaryModel3.setOption5(jsonObject1.getString("option_E"));
                            }
                            AttempedQuesModel attempedQuesModel3 = new AttempedQuesModel();
                            attempedQuesModel3.setQuestionNo("");
                            attempedQuesModels.add(attempedQuesModel3);

                            ItemSelectedModel itemSelectedModel = new ItemSelectedModel();
                            int l = i + 1;
                            itemSelectedModel.setSelectedListNo("" + l);
                            itemSelectedModelArrayList.add(itemSelectedModel);
                            QuestionaryListHelper.getInstance().insertData(questionaryModel3);

                        }
                        questionaryModels = QuestionaryListHelper.getInstance().getList();
                        tvRunningQuestionAttempId.setText("" + j);
                        Pager = (ViewPager) findViewById(R.id.pager);
                        Pager.setAdapter(new QuizQuetionaryAdapter(QuizQuestionaryActivity.this, questionaryModels));
                        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
                        indicator.setViewPager(Pager);
                        Pager.setOffscreenPageLimit(questionaryModels.size());
                        tvTotalQueId.setText("" + questionaryModels.size());

                        recyclerViewlayoutManager = new LinearLayoutManager(QuizQuestionaryActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        rvPositionId.setLayoutManager(recyclerViewlayoutManager);

                        stringArrayAdapter = new BottomNoListAdapter(QuizQuestionaryActivity.this, itemSelectedModelArrayList);
                        rvPositionId.setAdapter(stringArrayAdapter);
                        if (itemSelectedModelArrayList.size() > 0) {
                            stringArrayAdapter.setSelected(0);
                        }

                        if (questionaryModels.size() == 1) {
                            ivNextId.setImageResource(R.drawable.ic_correct);
                        }
                        Pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            }

                            @Override
                            public void onPageSelected(int position) {
                                stringArrayAdapter.setSelected(position);
                                if (questionaryModels.size() - 1 == position) {
                                    ivNextId.setImageResource(R.drawable.ic_correct);
                                } else {
                                    ivNextId.setImageResource(R.drawable.ic_next_);
                                }
                                //on Switching questions set bottom selected page no positions.
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    S.T(QuizQuestionaryActivity.this, " Internet is Slow ! ");
                    SavedData.saveCheckQuestionary(false);
                    finish();
                }
            }
        });
    }

    private Map<String, String> getParms() {
        HashMap<String, String> params = new HashMap<>();
        params.put("quiz_id", quiz_id);
        params.put("isFromTable", table);
        params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
        return params;
    }

    private void SubmitAns() {
        try {
            new JSONParser(QuizQuestionaryActivity.this).parseVollyStringRequest(Const.URL.QuizSubmit, 1, getParmsans(), new Helper() {
                @Override
                public void backResponse(String response) {
                    try {
                        S.E("checkPointsHere::"+getParmsans());
                        JSONObject mainobject = new JSONObject(response);
                        S.E("response of answere"+response);
                        int status = mainobject.getInt("status");
                        String message = mainobject.getString("message");
                        S.E("anilCheakmyanswer::"+myAnswerId);
                        if (status==200) {
                            CheckStatus = "Sucess";
                            cmTimerShowRunningId.stop();
                            SavedData.saveCheckQuestionary(true);
                            SavedData.saveNotificationStatus("testing");
                            QuestionaryListHelper.getInstance().deleteAll();
                            SelectedListModel selectedListModel = new SelectedListModel();
                            selectedListModel.setQuiz_id(getIntent().getStringExtra("quiz_id"));
                            selectedListModel.setQuestionaryModels(questionaryModels);
                            S.E("questionaryModels"+questionaryModels);
                            selectedListModel.setTopicId(getIntent().getStringExtra("topicId"));
                            S.E("topicId:::"+getIntent().getStringExtra("topicId"));
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("key_list", selectedListModel);
                            S.I(QuizQuestionaryActivity.this, ShowResultStaticActivity.class, bundle);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        S.E("inside try block json error"+e);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            S.E("outside try bolck json error"+e);
        }
    }

    private Map<String, String> getParmsans() {
        HashMap<String, String> params = new HashMap<>();
        if (ActivityCheck.equals("DirectPlay")) {
            params.put("host_user_id", host_user_id);
            params.put("timeToPlay", String.valueOf(second));
            params.put("tableId", tableId);
            params.put("point", points);
        } else if (ActivityCheck.equals("FirebaseNotification_Throw")) {
            params.put(player_key, UserDataHelper.getInstance().getList().get(0).getUserId());
            params.put("timeToPlay", String.valueOf(second));
            params.put("tableId", tableId);
            params.put("point", points);
        } else if (ActivityCheck.equals("S Class")) {
            params.put(player_key, UserDataHelper.getInstance().getList().get(0).getUserId());
            params.put("timeToPlay", String.valueOf(second));
            params.put("tableId", tableId);
            params.put("point", points);
        }else if (ActivityCheck.equals("S Classmultiplayer")) {
            params.put(player_key, UserDataHelper.getInstance().getList().get(0).getUserId());
            params.put("timeToPlay", String.valueOf(second));
            params.put("tableId", tableId);
            params.put("point", points);
        }
        params.put("quiz_id", quiz_id);
        params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
        params.put("quizQuestion_ids", questionIds);
        params.put("allAnswer", allAnswerId);
        params.put("rightAnswer", rightAnswerId);

        return params;
    }


    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void ADDSubmit() {
        new JSONParser(QuizQuestionaryActivity.this).parseVollyStringRequest(Const.URL.acceptRequestToPlayers, 1, getLoginParam(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        if (ActivityCheck.equals("FirebaseNotification_Throw")) {
                            materialDialog = S.initProgressDialog(QuizQuestionaryActivity.this);
                            handler = new Handler();
                            myRunnable = new Runnable() {
                                public void run() {
                                    if (!condition) {
                                        try {
                                            S.T(QuizQuestionaryActivity.this, "Any One is not Active");
                                            materialDialog.dismiss();
                                            dialog.dismiss();
                                            S.I_clear(QuizQuestionaryActivity.this, QuizMainActivity.class, null);
                                        } catch (Exception e) {
                                        }
                                    }
                                }
                            };
                            handler.postDelayed(myRunnable, 60000);

                            materialDialog.dismiss();
                        } else if (ActivityCheck.equals("DirectPlay")) {
                        } else {
                            ImportantMethode();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }

    public void cancelRequestToPlayers() {
        new JSONParser(QuizQuestionaryActivity.this).parseVollyStringRequest(Const.URL.cancelRequestToPlayers, 1, getCancelParam(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        S.I_clear(QuizQuestionaryActivity.this, QuizMainActivity.class, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Map<String, String> getLoginParam() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("host_user_id", host_user_id);
        stringStringHashMap.put(player_key, UserDataHelper.getInstance().getList().get(0).getUserId());
        stringStringHashMap.put("point", points);
        stringStringHashMap.put("tableId", tableId);
        stringStringHashMap.put("quiz_id", quiz_id);
        return stringStringHashMap;
    }

    public Map<String, String> getCancelParam() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("host_user_id", host_user_id);
        stringStringHashMap.put("defender_user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        stringStringHashMap.put("point", points);
        return stringStringHashMap;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        final AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit From Quiz? If You Leave It Will Auto Submit Ans sheet.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            if (ActivityCheck.equals("QuizCategery")) {
                                cmTimerShowRunningId.stop();
//                                updateUIStopRun();
                            }
                        } catch (Exception e) {
                        }
                        questionIds = "";
                        allAnswerId = "";
                        rightAnswerId = "";
                        Collections.reverse(myAnswerId);
                        for (int i = 0; i < questionId.size(); i++) {
                            if (i == 0) {
                                questionIds = questionIds + questionId.get(i);
                                allAnswerId = allAnswerId + myAnswerId.get(i);
                                rightAnswerId = rightAnswerId + answerId.get(i);
                            } else {
                                questionIds = questionIds + "," + questionId.get(i);
                                allAnswerId = allAnswerId + "," + myAnswerId.get(i);
                                rightAnswerId = rightAnswerId + "," + answerId.get(i);
                            }
                        }
                        SubmitAns();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                })
                .show();
        alertbox.getButton(alertbox.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.red));
        alertbox.getButton(alertbox.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.red));
    }

    @Override
    public void finish() {
        super.finish();
        if (!ActivityCheck.equals("QuizCategery")) {
            if (CheckStatus.equals("")) {
                questionIds = "";
                allAnswerId = "";
                rightAnswerId = "";
                Collections.reverse(myAnswerId);
                for (int i = 0; i < questionId.size(); i++) {
                    if (i == 0) {
                        questionIds = questionIds + questionId.get(i);
                        allAnswerId = allAnswerId + myAnswerId.get(i);
                        rightAnswerId = rightAnswerId + answerId.get(i);
                    } else {
                        questionIds = questionIds + "," + questionId.get(i);
                        allAnswerId = allAnswerId + "," + myAnswerId.get(i);
                        rightAnswerId = rightAnswerId + "," + answerId.get(i);
                    }
                }
                SubmitAns();

            }
        } else {
        }
    }

    public void stophandler() {
        condition = true;
    }

    public void Accept_Multiplayer() {
        new JSONParser(QuizQuestionaryActivity.this).parseVollyStringRequest(Const.URL.multiplierPlayerAccepterNotification, 1, getMultiplayerParam(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Map<String, String> getMultiplayerParam() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        stringStringHashMap.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
        stringStringHashMap.put("tableId", tableId);
        return stringStringHashMap;
    }
}
