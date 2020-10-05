package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.QuizesListAdapter;
import com.dollop.placementadda.adapter.SimilarQuizesListAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.QuizListModel;
import com.dollop.placementadda.model.SelectedListModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class SimilarTestActivity extends BaseActivity {
    RecyclerView subCategoryRV;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    List<QuizListModel> quizSubCatModelList = new ArrayList<>();


    private String Subjecttopic_ID = "", CategerySubject_ID = "", SelectedQuizCategeryId = "", OtherUserId = "", OtherUserName = "", OtherUserProfilePic = "", points = "", Member_number = "", OtherCity = "", OtherGender = "";
    private String ActivityCheck;
    String OtherSecondUserId = "", OtherSecondUserName = "", OtherSecondCity = "", OtherSecondGender = "", OtherSecondUserProfilePic = "";
    String OtherThirdUserId = "", OtherThirdUserName = "", OtherThirdCity = "", OtherThirdGender = "", OtherThirdUserProfilePic = "";


    TextView tvNoDataFoundId;
    private String topicId = "";

    @Override
    protected int getContentResId() {
        return R.layout.activity_quizes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(SimilarTestActivity.this);
        setToolbarWithBackButton("Quizzes List");
        tvNoDataFoundId=findViewById(R.id.tvNoDataFoundId);

        Bundle bundle = getIntent().getExtras();
        // topicId =  bundle.getString("Subjecttopic_ID");

        if (bundle != null) {
            if (bundle.getString("ActivityCheck").equals("QuizCategery")) {
                ActivityCheck = bundle.getString("ActivityCheck");
                Subjecttopic_ID = bundle.getString("Subjecttopic_ID");
                CategerySubject_ID = bundle.getString("CategerySubject_ID");
                SelectedQuizCategeryId = bundle.getString("SelectedQuizCategeryId");
                OtherUserId = bundle.getString("OtherUserId");
            } else if (bundle.getString("ActivityCheck").equals("Twoplayer")) {
                if (bundle.getString("Member_number").equals("2")) {
                    ActivityCheck = bundle.getString("ActivityCheck");
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
            } else if (bundle.getString("ActivityCheck").equals("QuizMultiplier")) {
                ActivityCheck = bundle.getString("ActivityCheck");
                Subjecttopic_ID = bundle.getString("Subjecttopic_ID");
                CategerySubject_ID = bundle.getString("CategerySubject_ID");
                SelectedQuizCategeryId = bundle.getString("SelectedQuizCategeryId");
                Member_number = bundle.getString("Member_number");
                points = bundle.getString("points");
            }
        }



        SelectedListModel selectedListModel = (SelectedListModel) getIntent().getSerializableExtra("key_list");
        topicId = selectedListModel.getTopicId();
        S.E("topicId::"+topicId);

        subCategoryRV = (RecyclerView) findViewById(R.id.categoryRV);

        try {
            recyclerViewlayoutManager = new LinearLayoutManager(this);
            subCategoryRV.setLayoutManager(recyclerViewlayoutManager);
            subCategoryRV.setHasFixedSize(true);
        }catch (Exception e)
        {e.printStackTrace();}
        getQuizList();

        BroadcastReceiver broadcastReceiver= S.LocalBroadcastReciver(SimilarTestActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }


    private void getQuizList() {
        new JSONParser(SimilarTestActivity.this).parseVollyStringRequest(Const.URL.GetQuiz, 1, getParms(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    S.E("similartestresponse"+response);

                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");


                    if (status == 200) {

                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            QuizListModel quizListModel = new QuizListModel();

                            quizListModel.setQuiz_id(jsonObject1.getString("quiz_id"));
                            quizListModel.setQuizName(jsonObject1.getString("quizName"));
                            quizListModel.setQuizType(jsonObject1.getString("quizType"));
                            quizListModel.setQuizTotalQuestion(jsonObject1.getString("quizTotalQuestion"));
                            quizListModel.setQuizTime(jsonObject1.getString("quizTime"));
                            quizListModel.setQuizService_paid(jsonObject1.getString("quizService"));
                            quizListModel.setAmount(jsonObject1.getString("quizAmount"));
                            quizListModel.setQuizCategory_id(topicId);
                            quizSubCatModelList.add(quizListModel);
                        }

                        SimilarQuizesListAdapter quizCategoryAdapter = new SimilarQuizesListAdapter(SimilarTestActivity.this, quizSubCatModelList);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(quizCategoryAdapter);
                        subCategoryRV.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
                        tvNoDataFoundId.setVisibility(View.GONE);
                        subCategoryRV.setVisibility(View.VISIBLE);

                    } else {
                        subCategoryRV.setVisibility(View.GONE);
                        tvNoDataFoundId.setVisibility(View.VISIBLE);
                        tvNoDataFoundId.setText(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("error", "++++++--error--++++" + e);
                }
            }
        });
    }


    private Map<String, String> getParms() {

        HashMap<String, String> params = new HashMap<>();
        params.put("topic_id", topicId);
        params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());

        return params;
    }

    public void getSelectedQuiz(String Quiz_id, String Quiz_time, String QuizCategory_id) {
        if (ActivityCheck.equals("QuizCategery")) {
            Bundle bundle = new Bundle();
            bundle.putString("ActivityCheck", "QuizCategery");
            bundle.putString("quiz_id", Quiz_id);
            bundle.putString("quiz_time", Quiz_time);
            bundle.putString("quiz_topicId", QuizCategory_id);
            bundle.putString("Subjecttopic_ID", Subjecttopic_ID);
            bundle.putString("CategerySubject_ID", CategerySubject_ID);
            bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
            bundle.putString("OtherUserId", OtherUserId);
            S.I(SimilarTestActivity.this, ExamInstructionActivity.class, bundle);
        } else if (ActivityCheck.equals("Twoplayer")) {
            if (Member_number.equals("2")) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "Twoplayer");
                bundle.putString("quiz_id", Quiz_id);
                bundle.putString("quiz_time", Quiz_time);
                bundle.putString("quiz_topicId", QuizCategory_id);
                bundle.putString("Subjecttopic_ID", Subjecttopic_ID);
                S.E("Subjecttopic_ID:::"+Subjecttopic_ID);
                bundle.putString("CategerySubject_ID", CategerySubject_ID);
                bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
                bundle.putString("OtherUserId", OtherUserId);
                bundle.putString("OtherUserName", OtherUserName);
                bundle.putString("OtherGender", OtherGender);
                bundle.putString("OtherCity", OtherCity);
                bundle.putString("OtherUserProfilePic", OtherUserProfilePic);
                bundle.putString("points", points);
                bundle.putString("Member_number", Member_number);
                S.E("otherDetailOn QuizActivity::" + OtherGender + "::" + OtherUserName);
                S.I(SimilarTestActivity.this, QuizPlayDetailsActivity.class, bundle);
            } else if (Member_number.equals("3")) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "Twoplayer");
                bundle.putString("quiz_id", Quiz_id);
                bundle.putString("quiz_time", Quiz_time);
                bundle.putString("quiz_topicId", QuizCategory_id);
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
                S.I(SimilarTestActivity.this, QuizPlayDetailsActivity.class, bundle);
            } else if (Member_number.equals("4")) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "Twoplayer");
                bundle.putString("quiz_id", Quiz_id);
                bundle.putString("quiz_time", Quiz_time);
                bundle.putString("quiz_topicId", QuizCategory_id);
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
                S.I(SimilarTestActivity.this, QuizPlayDetailsActivity.class, bundle);
            }
        } else if (ActivityCheck.equals("QuizMultiplier")) {
            Bundle bundle = new Bundle();
            bundle.putString("ActivityCheck", "QuizMultiplier");
            bundle.putString("quiz_id", Quiz_id);
            bundle.putString("quiz_time", Quiz_time);
            bundle.putString("quiz_topicId", QuizCategory_id);
            bundle.putString("Subjecttopic_ID", Subjecttopic_ID);
            bundle.putString("CategerySubject_ID", CategerySubject_ID);
            bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
            bundle.putString("Member_number", Member_number);
            bundle.putString("points", points);
            S.I(SimilarTestActivity.this, QuizPlayDetailsActivity.class, bundle);
        }
    }
}
