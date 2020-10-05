package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.QuizSubsCategoryAdapter;
import com.dollop.placementadda.model.QuizCategoryModel;
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

public class QuizSubCategoryActivity extends BaseActivity {
    RecyclerView categoryRV;
    private GridLayoutManager gaggeredGridLayoutManager;
    List<QuizCategoryModel> quizCategoryModelList = new ArrayList<>();
    private String SelectedQuizCategeryId = "", OtherUserId = "", OtherUserName = "", OtherUserProfilePic = "", points = "", Member_number = "", OtherCity = "", OtherGender = "";
    private String ActivityCheck;
    String OtherSecondUserId = "", OtherSecondUserName = "", OtherSecondCity = "", OtherSecondGender = "", OtherSecondUserProfilePic = "";
    String OtherThirdUserId = "", OtherThirdUserName = "", OtherThirdCity = "", OtherThirdGender = "", OtherThirdUserProfilePic = "";

    @Override
    protected int getContentResId() {
        return R.layout.activity_quiz_sub_category;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(QuizSubCategoryActivity.this);
        setToolbarWithBackButton("Quiz Subject");
        categoryRV = (RecyclerView) findViewById(R.id.categoryRV);
        int noOfColumns = 2;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("ActivityCheck").equals("QuizCategery")) {
                ActivityCheck = bundle.getString("ActivityCheck");
                SelectedQuizCategeryId = bundle.getString("SelectedQuizCategeryId");
                OtherUserId = bundle.getString("OtherUserId");
            } else if (bundle.getString("ActivityCheck").equals("Twoplayer")) {

                if (bundle.getString("Member_number").equals("2")) {
                    ActivityCheck = bundle.getString("ActivityCheck");
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
                    SelectedQuizCategeryId = bundle.getString("SelectedQuizCategeryId");
                    OtherUserId = bundle.getString("OtherUserId");
                    OtherUserName = bundle.getString("OtherUserName");
                    OtherUserProfilePic = bundle.getString("OtherUserProfilePic");

                    OtherSecondUserId = bundle.getString("OtherSecondUserId");
                    OtherSecondUserName = bundle.getString("OtherSecondUserName");
                    OtherSecondCity = bundle.getString("OtherSecondCity");
                    OtherSecondGender = bundle.getString("OtherSecondGender");
                    OtherSecondUserProfilePic = bundle.getString("OtherSecondUserProfilePic");

                    points = bundle.getString("points");
                    Member_number = bundle.getString("Member_number");
                    OtherCity = bundle.getString("OtherCity");
                    OtherGender = bundle.getString("OtherGender");

                } else if (bundle.getString("Member_number").equals("4")) {
                    ActivityCheck = bundle.getString("ActivityCheck");
                    SelectedQuizCategeryId = bundle.getString("SelectedQuizCategeryId");
                    OtherUserId = bundle.getString("OtherUserId");
                    OtherUserName = bundle.getString("OtherUserName");
                    OtherUserProfilePic = bundle.getString("OtherUserProfilePic");

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

                    points = bundle.getString("points");
                    Member_number = bundle.getString("Member_number");
                    OtherCity = bundle.getString("OtherCity");
                    OtherGender = bundle.getString("OtherGender");
                }


            } else if (bundle.getString("ActivityCheck").equals("QuizMultiplier")) {
                ActivityCheck = bundle.getString("ActivityCheck");
                SelectedQuizCategeryId = bundle.getString("SelectedQuizCategeryId");
                Member_number = bundle.getString("Member_number");
                points = bundle.getString("points");
            }
        }

        try {
            gaggeredGridLayoutManager = new GridLayoutManager(this, noOfColumns);
            categoryRV.setLayoutManager(gaggeredGridLayoutManager);
            categoryRV.setHasFixedSize(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(QuizSubCategoryActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));

        getCateGoryList();
    }

    private void getCateGoryList() {
        new JSONParser(QuizSubCategoryActivity.this).parseVollyStringRequest(Const.URL.GetCategory, 1, getParms(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");

                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        quizCategoryModelList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            QuizCategoryModel quizCategoryModel = new QuizCategoryModel();
                            String categoty_id = jsonObject1.getString("categoty_id");
                            String categoryName = jsonObject1.getString("categoryName");
                            String category_image = jsonObject1.getString("category_image");
                            quizCategoryModel.setCategoryId(categoty_id);
                            quizCategoryModel.setCategoryName(categoryName);
                            quizCategoryModel.setCategory_Image(category_image);
                            quizCategoryModelList.add(quizCategoryModel);
                        }
                        QuizSubsCategoryAdapter quizCategoryAdapter = new QuizSubsCategoryAdapter(QuizSubCategoryActivity.this, quizCategoryModelList);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(quizCategoryAdapter);
                        categoryRV.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

                    } else {
                        quizCategoryModelList.clear();
                        QuizSubsCategoryAdapter quizCategoryAdapter = new QuizSubsCategoryAdapter(QuizSubCategoryActivity.this, quizCategoryModelList);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(quizCategoryAdapter);
                        categoryRV.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParms() {
        HashMap<String, String> params = new HashMap<>();
        params.put("parent_id", SelectedQuizCategeryId);
        return params;
    }

    public void getSelectedCategerySubject(String CategerySubject_ID) {
        if (ActivityCheck.equals("QuizCategery")) {
            Bundle bundle = new Bundle();
            bundle.putString("ActivityCheck", "QuizCategery");
            bundle.putString("CategerySubject_ID", CategerySubject_ID);
            bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
            bundle.putString("OtherUserId", OtherUserId);
            S.I(QuizSubCategoryActivity.this, QuizTopicActivity.class, bundle);
        } else if (ActivityCheck.equals("Twoplayer")) {
            if (Member_number.equals("2")) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "Twoplayer");
                bundle.putString("CategerySubject_ID", CategerySubject_ID);
                bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
                bundle.putString("OtherUserId", OtherUserId);
                bundle.putString("OtherUserName", OtherUserName);
                bundle.putString("OtherGender", OtherGender);
                bundle.putString("OtherCity", OtherCity);
                bundle.putString("OtherUserProfilePic", OtherUserProfilePic);
                bundle.putString("points", points);
                bundle.putString("Member_number", Member_number);
                S.I(QuizSubCategoryActivity.this, QuizTopicActivity.class, bundle);
            } else if (Member_number.equals("3")) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "Twoplayer");
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
                S.I(QuizSubCategoryActivity.this, QuizTopicActivity.class, bundle);
            } else if (Member_number.equals("4")) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "Twoplayer");
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
                S.I(QuizSubCategoryActivity.this, QuizTopicActivity.class, bundle);
            }
        } else if (ActivityCheck.equals("QuizMultiplier")) {
            Bundle bundle = new Bundle();
            bundle.putString("ActivityCheck", "QuizMultiplier");
            bundle.putString("CategerySubject_ID", CategerySubject_ID);
            bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
            bundle.putString("Member_number", Member_number);
            bundle.putString("points", points);
            S.I(QuizSubCategoryActivity.this, QuizTopicActivity.class, bundle);
        }
    }
}
