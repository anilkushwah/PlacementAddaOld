package com.dollop.placementadda.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.CreateGroupAdapter;
import com.dollop.placementadda.adapter.SelectThreePlayerAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.CreateGroupModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class QuizPlayDetailsActivity extends BaseActivity {
    String uid;
    Toolbar toolBar;
    Button playDuelQuizBtn;
    CircleImageView ourImageView, otherSecondIv, otherthirdIv;
    CircleImageView otherIv;
    String multiPlayerTableId = "";
    TextView ourNameTv, otherSecondNameTv, otherthirdNameTv;
    TextView otherCityTv, otherSecondCityTv, otherthirdCityTv;
    TextView ourGenderTv, otherSecondGenderTv, otherthirdGenderTv;
    TextView otherNameTv;
    TextView ourCityTv;
    TextView otherGenderTv;
    private String quiz_id = "", quiz_time = "", quiz_topicId = "", Subjecttopic_ID = "", CategerySubject_ID = "", SelectedQuizCategeryId = "", OtherUserId = "", OtherUserName = "", OtherUserProfilePic = "", points = "", Member_number = "", OtherCity = "", OtherGender = "";
    private String ActivityCheck;
    String OtherSecondUserId = "", OtherSecondUserName = "", OtherSecondCity = "", OtherSecondGender = "", OtherSecondUserProfilePic = "";
    String OtherThirdUserId = "", OtherThirdUserName = "", OtherThirdCity = "", OtherThirdGender = "", OtherThirdUserProfilePic = "";
    LinearLayout mainId4, threeplayer_layout, fourplayer_layout;
    private Dialog materialDialog;
    private Bundle bundleForStartGame;
    private String host_user_id="";
    private String quizPoint="";
    private String tableId;
    private String playerKeyDetail;

    @Override
    protected int getContentResId() {
        return R.layout.activity_quiz_play_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(QuizPlayDetailsActivity.this);
        setToolbarWithBackButton("Quiz Details");
        toolbar = findViewById(R.id.tool_bar);
        otherIv = (CircleImageView) findViewById(R.id.otherIv);
        otherSecondIv = (CircleImageView) findViewById(R.id.otherSecondIv);
        otherthirdIv = (CircleImageView) findViewById(R.id.otherthirdIv);
        ourImageView = (CircleImageView) findViewById(R.id.ourImageView);
        otherNameTv = (TextView) findViewById(R.id.otherNameTv);
        otherSecondNameTv = (TextView) findViewById(R.id.otherSecondNameTv);
        otherthirdNameTv = (TextView) findViewById(R.id.otherthirdNameTv);
        ourCityTv = (TextView) findViewById(R.id.ourCityTv);
        otherSecondCityTv = (TextView) findViewById(R.id.otherSecondCityTv);
        otherthirdCityTv = (TextView) findViewById(R.id.otherthirdCityTv);
        otherGenderTv = (TextView) findViewById(R.id.otherGenderTv);
        otherthirdGenderTv = (TextView) findViewById(R.id.otherthirdGenderTv);
        playDuelQuizBtn = findViewById(R.id.playDuelQuizBtn);
        ourNameTv = (TextView) findViewById(R.id.ourNameTv);
        otherCityTv = (TextView) findViewById(R.id.otherCityTv);
        ourGenderTv = (TextView) findViewById(R.id.ourGenderTv);
        otherSecondGenderTv = (TextView) findViewById(R.id.otherSecondGenderTv);
        mainId4 = (LinearLayout) findViewById(R.id.mainId4);
        threeplayer_layout = (LinearLayout) findViewById(R.id.threeplayer_layout);
        fourplayer_layout = (LinearLayout) findViewById(R.id.fourplayer_layout);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            S.E("checkActivityCheck::"+bundle.getString("ActivityCheck"));
            if (bundle.getString("ActivityCheck").equals("Twoplayer")) {

                if (bundle.getString("Member_number").equals("2")) {



                    fourplayer_layout.setVisibility(View.GONE);
                    threeplayer_layout.setVisibility(View.GONE);
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
                    S.E("checkAmountWillPlay::"+points);
                    Member_number = bundle.getString("Member_number");
                    OtherCity = bundle.getString("OtherCity");
                    OtherGender = bundle.getString("OtherGender");
                    try {
                        Picasso.with(QuizPlayDetailsActivity.this).
                                load(OtherUserProfilePic)
                                .into(otherIv);
                        otherNameTv.setText(OtherUserName);

                        otherCityTv.setText("Point :" +points);
                        otherGenderTv.setText(OtherGender);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (bundle.getString("Member_number").equals("3")) {
                    threeplayer_layout.setVisibility(View.VISIBLE);
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
                    Picasso.with(QuizPlayDetailsActivity.this).
                            load(OtherUserProfilePic)
                            .into(otherIv);
                    otherNameTv.setText(OtherUserName);
                    otherCityTv.setText(OtherCity);
                    otherGenderTv.setText(OtherGender);

                    Picasso.with(QuizPlayDetailsActivity.this).
                            load(OtherSecondUserProfilePic)
                            .into(otherSecondIv);
                    otherSecondNameTv.setText(OtherSecondUserName);
                    otherSecondCityTv.setText("Point :" +points);
                    otherSecondGenderTv.setText(OtherSecondGender);
                } else if (bundle.getString("Member_number").equals("4")) {
                    fourplayer_layout.setVisibility(View.VISIBLE);
                    threeplayer_layout.setVisibility(View.VISIBLE);
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
                    Picasso.with(QuizPlayDetailsActivity.this).
                            load(OtherUserProfilePic)
                            .into(otherIv);
                    otherNameTv.setText(OtherUserName);
                    otherCityTv.setText(OtherCity);
                    otherGenderTv.setText(OtherGender);
                    Picasso.with(QuizPlayDetailsActivity.this).
                            load(OtherSecondUserProfilePic)
                            .into(otherSecondIv);
                    otherSecondNameTv.setText(OtherSecondUserName);
                    otherSecondCityTv.setText(OtherSecondCity);
                    otherSecondGenderTv.setText(OtherSecondGender);
                    OtherThirdUserId = bundle.getString("OtherThirdUserId");
                    OtherThirdUserName = bundle.getString("OtherThirdUserName");
                    OtherThirdCity = bundle.getString("OtherThirdCity");

                    OtherThirdGender = bundle.getString("OtherThirdGender");
                    OtherThirdUserProfilePic = bundle.getString("OtherThirdUserProfilePic");
                    Picasso.with(QuizPlayDetailsActivity.this).
                            load(OtherThirdUserProfilePic)
                            .into(otherthirdIv);
                    otherthirdNameTv.setText(OtherThirdUserName);
                    otherthirdCityTv.setText("Point :" +points);
                    otherthirdGenderTv.setText(OtherThirdGender);
                }
            } else if (bundle.getString("ActivityCheck").equals("QuizMultiplier")) {
                if (bundle.getString("Member_number").equals("2")) {
                    threeplayer_layout.setVisibility(View.VISIBLE);
                } else if (bundle.getString("Member_number").equals("4")) {
                    threeplayer_layout.setVisibility(View.VISIBLE);
                }
                ActivityCheck = bundle.getString("ActivityCheck");
                quiz_id = bundle.getString("quiz_id");
                quiz_time = bundle.getString("quiz_time");
                quiz_topicId = bundle.getString("quiz_topicId");
                Subjecttopic_ID = bundle.getString("Subjecttopic_ID");
                CategerySubject_ID = bundle.getString("CategerySubject_ID");
                SelectedQuizCategeryId = bundle.getString("SelectedQuizCategeryId");
                Member_number = bundle.getString("Member_number");
                points = bundle.getString("points");


                otherNameTv.setText("Unknown Online");
                otherCityTv.setText("Point :"+points);
                otherGenderTv.setText("unknown");


                otherSecondIv.setVisibility(View.GONE);
                otherSecondNameTv.setText(OtherSecondUserName);
                otherSecondNameTv.setVisibility(View.GONE);
                otherSecondCityTv.setText(OtherSecondCity);
                otherSecondCityTv.setVisibility(View.GONE);
                otherSecondGenderTv.setText(OtherSecondGender);
                otherSecondGenderTv.setVisibility(View.GONE);
            } else if (bundle.getString("ActivityCheck").equals("S Classmultiplayer")) {
                ActivityCheck=bundle.getString("ActivityCheck");
                materialDialog = S.initProgressDialog(this);
                if (SavedData.readyToPlayStore().equals("yes")) {
                    SavedData.saveReadyToPlay("no");
                    materialDialog.dismiss();
                }
                multiPlayerTableId = bundle.getString("tableId");
            quiz_id=    bundle.getString("quiz_id");
              quiz_time=  bundle.getString("time");
                quiz_topicId=  bundle.getString("topicId");
                ActivityCheck=  bundle.getString("ActivityCheck", "S Classmultiplayer");
               host_user_id= bundle.getString("host_user_id");
                quizPoint=bundle.getString("point");
                tableId=bundle.getString("tableId");
              playerKeyDetail=  bundle.getString("player_key");

                getAllPlayerInfoByTableId();



            }
            try {
                String image = UserDataHelper.getInstance().getList().get(0).getProfile_pic();
                Picasso.with(QuizPlayDetailsActivity.this).load(Const.URL.IMAGE_URL+image).error(R.drawable.ic_user).into(ourImageView);
                ourNameTv.setText(UserDataHelper.getInstance().getList().get(0).getUserName());
                ourCityTv.setText("Point :"+points);
                ourGenderTv.setText(UserDataHelper.getInstance().getList().get(0).getUserGender());
            } catch (Exception e) {
                e.getMessage();

            }
        }
        playDuelQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCheck.equals("S Classmultiplayer")){
                    S.T(QuizPlayDetailsActivity.this,"Only Host Can Start Match");

                }else if (ActivityCheck.equals("Twoplayer")) {
                    if (Member_number.equals("2")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("ActivityCheck", "Twoplayer");
                        bundle.putString("quiz_id", quiz_id);
                        bundle.putString("quiz_time", quiz_time);
                        bundle.putString("quiz_topicId", quiz_topicId);
                        S.E("quiz_topicId::"+quiz_topicId);
                        bundle.putString("Subjecttopic_ID", Subjecttopic_ID);
                        bundle.putString("CategerySubject_ID", CategerySubject_ID);
                        bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
                        bundle.putString("OtherUserId", OtherUserId);
                        bundle.putString("OtherUserName", OtherUserName);
                        bundle.putString("OtherGender", OtherGender);
                        bundle.putString("OtherCity", OtherCity);
                        bundle.putString("OtherUserProfilePic", OtherUserProfilePic);
                        bundle.putString("points", points);
                        bundle.putString("Member_number", Member_number);
                        S.I(QuizPlayDetailsActivity.this, ExamInstructionActivity.class, bundle);
                    } else if (Member_number.equals("3")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("ActivityCheck", "Twoplayer");
                        bundle.putString("quiz_id", quiz_id);
                        bundle.putString("quiz_time", quiz_time);
                        bundle.putString("quiz_topicId", quiz_topicId);
                        bundle.putString("Subjecttopic_ID", Subjecttopic_ID);
                        bundle.putString("CategerySubject_ID", CategerySubject_ID);
                        bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
                        bundle.putString("OtherUserId", OtherUserId);
                        bundle.putString("OtherUserName", OtherUserName);
                        bundle.putString("OtherGender", OtherGender);
                        bundle.putString("OtherCity", OtherCity);
                        bundle.putString("OtherUserProfilePic", OtherUserProfilePic);
                        bundle.putString("points", points);
                        bundle.putString("Member_number", Member_number);

                        bundle.putString("OtherSecondUserId", OtherSecondUserId);
                        bundle.putString("OtherSecondUserName", OtherSecondUserName);
                        bundle.putString("OtherSecondCity", OtherSecondCity);
                        bundle.putString("OtherSecondGender", OtherSecondGender);
                        bundle.putString("OtherSecondUserProfilePic", OtherSecondUserProfilePic);

                        S.I(QuizPlayDetailsActivity.this, ExamInstructionActivity.class, bundle);
                    } else if (Member_number.equals("4")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("ActivityCheck", "Twoplayer");
                        bundle.putString("quiz_id", quiz_id);
                        bundle.putString("quiz_time", quiz_time);
                        bundle.putString("quiz_topicId", quiz_topicId);
                        bundle.putString("Subjecttopic_ID", Subjecttopic_ID);
                        bundle.putString("CategerySubject_ID", CategerySubject_ID);
                        bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
                        bundle.putString("OtherUserId", OtherUserId);
                        bundle.putString("OtherUserName", OtherUserName);
                        bundle.putString("OtherGender", OtherGender);
                        bundle.putString("OtherCity", OtherCity);
                        bundle.putString("OtherUserProfilePic", OtherUserProfilePic);
                        bundle.putString("points", points);
                        bundle.putString("Member_number", Member_number);

                        bundle.putString("OtherSecondUserId", OtherSecondUserId);
                        bundle.putString("OtherSecondUserName", OtherSecondUserName);
                        bundle.putString("OtherSecondCity", OtherSecondCity);
                        bundle.putString("OtherSecondGender", OtherSecondGender);
                        bundle.putString("OtherSecondUserProfilePic", OtherSecondUserProfilePic);

                        bundle.putString("OtherThirdUserId", OtherThirdUserId);
                        bundle.putString("OtherThirdUserName", OtherThirdUserName);
                        bundle.putString("OtherThirdCity", OtherThirdCity);
                        bundle.putString("OtherThirdGender", OtherGender);
                        bundle.putString("OtherThirdUserProfilePic", OtherThirdUserProfilePic);

                        S.I(QuizPlayDetailsActivity.this, ExamInstructionActivity.class, bundle);
                    }
                } else if (ActivityCheck.equals("QuizMultiplier")) {
                    S.E("checkClickHere::");
                    if (playDuelQuizBtn.getText().toString().equals("Play Quiz")) {

                        playDuelQuizBtn.setText("Start Quiz");
                        SendAllPeopleNotification();
                    } else {
                        startGameByHost();

                    }
                }
            }
        });
        uid = SavedData.getTwoPlayerUserID();
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(QuizPlayDetailsActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.gameStartByHost));

        LocalBroadcastManager.getInstance(this).registerReceiver(ReadyToPlay,
                new IntentFilter(Config.readyToPlay));


    }

    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            // handler.removeCallbacksAndMessages(null);


            S.E("checkItsComeHere intentValue::" + intent.getStringExtra("message"));
            try {

                JSONObject jsonObject = new JSONObject(intent.getStringExtra("message"));
                JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                String defender_id = jsonObject1.getString("defender_id");
                String host_user_id = jsonObject1.getString("host_user_id");
                String point = jsonObject1.getString("point");
                String player_key2 = jsonObject1.getString("player_key");
                String tableId = jsonObject1.getString("tableId");

                bundleForStartGame = new Bundle();
                bundleForStartGame.putString("quiz_id", quiz_id);
                bundleForStartGame.putString("time", quiz_time);
                bundleForStartGame.putString("topicId", quiz_topicId);
                bundleForStartGame.putString("ActivityCheck", "S Class");
                bundleForStartGame.putString("defender_id", defender_id);
                bundleForStartGame.putString("host_user_id", host_user_id);
                bundleForStartGame.putString("point", point);
                bundleForStartGame.putString("tableId", tableId);
                bundleForStartGame.putString("player_key", player_key2);
              S.I_clear(QuizPlayDetailsActivity.this,QuizQuestionaryActivity.class,bundleForStartGame);


            } catch (Exception e) {
                S.E("checkItsComeHere error::" + e);
            }
        }
    };
    private BroadcastReceiver ReadyToPlay = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            S.E("checkReadyToPlayQuiz On Activity");
            try {
                materialDialog.dismiss();
            } catch (Exception e) {

            }


            getAllPlayerInfoByTableId();



            S.E("checkItsComeHere intentValue::" + intent.getStringExtra("message"));

        }
    };
    private BroadcastReceiver table_expaire = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

        }

    };

    private void SendAllPeopleNotification() {
        new JSONParser(QuizPlayDetailsActivity.this).parseVollyStringRequest(Const.URL.multiplierPlayerNotification, 1, getParams(), new Helper() {

            @Override
            public void backResponse(String response) {
                S.E("checkResponse" + response);
                S.E("checkParams" + getParams());
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {
                        JSONObject jsonObjectData= mainobject.getJSONObject("data");
                        multiPlayerTableId=jsonObjectData.getString("tableId");


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("host_user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        params.put("tableNo", Member_number);
        params.put("category_id", SelectedQuizCategeryId);
        params.put("subject_id", CategerySubject_ID);
        params.put("topic_id", Subjecttopic_ID);
        params.put("quiz_id", quiz_id);
        params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
        params.put("quiz_time", quiz_time);
        params.put("point", points);
        return params;


    }

    private void getAllPlayerInfoByTableId() {
        new JSONParser(QuizPlayDetailsActivity.this).parseVollyStringRequest(Const.URL.getAllPlayerInfoByTableId, 1, getTableParam(), new Helper() {

            @Override
            public void backResponse(String response) {
                S.E("checkResponse" + response);
                S.E("checkParams" + getParams());
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {


                        JSONObject jsonData = mainobject.getJSONObject("data");


                            fourplayer_layout.setVisibility(View.VISIBLE);
                            threeplayer_layout.setVisibility(View.VISIBLE);
                        String members = jsonData.getString("members");
                        if(members.equals("4")) {
                            String firstUserDetail = jsonData.getString("1");

                            String secUserDetail = jsonData.getString("2");
                            String thirdUserDetail = jsonData.getString("3");
                            String fourUserDetail = jsonData.getString("4");
                            String[] firstUserdetailArray = firstUserDetail.split("::");
                            String[] secUserdetailArray = secUserDetail.split("::");
                            String[] thirdUserdetailArray = thirdUserDetail.split("::");
                            String[] fourUserdetailArray = fourUserDetail.split("::");


                            Picasso.with(QuizPlayDetailsActivity.this).load(Const.URL.IMAGE_URL + firstUserdetailArray[1]).error(R.drawable.ic_user).into(ourImageView);
                            ourNameTv.setText(firstUserdetailArray[0]);


                            Picasso.with(QuizPlayDetailsActivity.this).
                                    load(Const.URL.IMAGE_URL + secUserdetailArray[1])
                                    .into(otherIv);
                            otherNameTv.setText(secUserdetailArray[0]);

                            Picasso.with(QuizPlayDetailsActivity.this).
                                    load(Const.URL.IMAGE_URL + thirdUserdetailArray[1])
                                    .into(otherSecondIv);
                            otherSecondIv.setVisibility(View.VISIBLE);
                            otherSecondNameTv.setText(thirdUserdetailArray[0]);
                            otherSecondNameTv.setVisibility(View.VISIBLE);

                            Picasso.with(QuizPlayDetailsActivity.this).
                                    load(Const.URL.IMAGE_URL + fourUserdetailArray[1])
                                    .into(otherthirdIv);
                            otherthirdNameTv.setText(fourUserdetailArray[0]);
                        }else if(members.equals("2")){
                            mainId4.setVisibility(View.GONE);
                            String firstUserDetail = jsonData.getString("1");

                            String secUserDetail = jsonData.getString("2");

                            String[] firstUserdetailArray = firstUserDetail.split("::");
                            String[] secUserdetailArray = secUserDetail.split("::");



                            Picasso.with(QuizPlayDetailsActivity.this).load(Const.URL.IMAGE_URL + firstUserdetailArray[1]).error(R.drawable.ic_user).into(ourImageView);
                            ourNameTv.setText(firstUserdetailArray[0]);


                            Picasso.with(QuizPlayDetailsActivity.this).
                                    load(Const.URL.IMAGE_URL + secUserdetailArray[1])
                                    .into(otherIv);
                            otherNameTv.setText(secUserdetailArray[0]);



                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getTableParam() {
        Map<String, String> stringParams = new HashMap();
       /* user_id
                tableId
        user_imei_number*/


        stringParams.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        stringParams.put("tableId", multiPlayerTableId);
        stringParams.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
        return stringParams;
    }
    private void startGameByHost() {
        new JSONParser(QuizPlayDetailsActivity.this).parseVollyStringRequest(Const.URL.startGame, 1, getDetailStartGameParam(), new Helper() {

            @Override
            public void backResponse(String response) {
                S.E("checkResponseGameStart" + response);
                S.E("checkParamsGameStart" + getDetailStartGameParam());
                try {
                    JSONObject mainobject = new JSONObject(response);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String,String> getDetailStartGameParam() {
        Map<String,String>getGameStartParams=new HashMap<>();
        getGameStartParams.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        getGameStartParams.put("tableId", multiPlayerTableId);
        getGameStartParams.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
        getGameStartParams.put("point",points);
        getGameStartParams.put("quiz_id",quiz_id);


        return getGameStartParams;
    }

}
