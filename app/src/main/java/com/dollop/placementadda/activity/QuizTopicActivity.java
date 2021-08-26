package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.QuizTopicAdapter;
import com.dollop.placementadda.model.QuizCategoryModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class QuizTopicActivity extends BaseActivity {
    RecyclerView categoryRV;
    private GridLayoutManager gaggeredGridLayoutManager;
    List<QuizCategoryModel> quizCategoryModelList = new ArrayList<>();
    private String subcategoryID = "";
    private String CategerySubject_ID = "", SelectedQuizCategeryId = "", OtherUserId = "", OtherUserName = "", OtherUserProfilePic = "", points = "", Member_number = "", OtherCity = "", OtherGender = "";
    private String ActivityCheck;
    String OtherSecondUserId = "", OtherSecondUserName = "", OtherSecondCity = "", OtherSecondGender = "", OtherSecondUserProfilePic = "";
    String OtherThirdUserId = "", OtherThirdUserName = "", OtherThirdCity = "", OtherThirdGender = "", OtherThirdUserProfilePic = "";

    @Override
    protected int getContentResId() {
        return R.layout.activity_quiz_topic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(QuizTopicActivity.this);
        setToolbarWithBackButton("Quiz Topics");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("ActivityCheck").equals("QuizCategery")) {
                ActivityCheck = bundle.getString("ActivityCheck");
                CategerySubject_ID = bundle.getString("CategerySubject_ID");
                SelectedQuizCategeryId = bundle.getString("SelectedQuizCategeryId");
                OtherUserId = bundle.getString("OtherUserId");
            } else if (bundle.getString("ActivityCheck").equals("Twoplayer")) {
                if (bundle.getString("Member_number").equals("2")) {
                    ActivityCheck = bundle.getString("ActivityCheck");
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
                CategerySubject_ID = bundle.getString("CategerySubject_ID");
                SelectedQuizCategeryId = bundle.getString("SelectedQuizCategeryId");
                Member_number = bundle.getString("Member_number");
                points = bundle.getString("points");
            }
        }

        categoryRV = (RecyclerView) findViewById(R.id.categoryRV);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManagerWithSmoothScroller(QuizTopicActivity.this, LinearLayoutManager.VERTICAL,false);

        try {
          /*  int noOfColumns = 2;
            gaggeredGridLayoutManager = ne  w GridLayoutManager(this, noOfColumns);*/
            categoryRV.setLayoutManager(layoutManager);
            categoryRV.setHasFixedSize(true);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(QuizTopicActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
        getCateGoryList();
    }

    private void getCateGoryList() {
        new JSONParser(QuizTopicActivity.this).parseVollyStringRequest(Const.URL.GetCategory, 1, getParms(), new Helper() {

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
                            SavedData.saveCategoryImage(category_image);
                            quizCategoryModelList.add(quizCategoryModel);
                        }
                        QuizTopicAdapter quizCategoryAdapter = new QuizTopicAdapter(QuizTopicActivity.this, quizCategoryModelList);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(quizCategoryAdapter);
                        categoryRV.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

                    } else {
                        quizCategoryModelList.clear();
                        QuizTopicAdapter quizCategoryAdapter = new QuizTopicAdapter(QuizTopicActivity.this, quizCategoryModelList);
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
        params.put("parent_id", CategerySubject_ID);
        return params;
    }

    public void getSelectedTopicId(String TopicID) {
        if (ActivityCheck.equals("QuizCategery")) {
            Bundle bundle = new Bundle();
            bundle.putString("ActivityCheck", "QuizCategery");
            bundle.putString("CategerySubject_ID", CategerySubject_ID);
            bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
            bundle.putString("OtherUserId", OtherUserId);
            bundle.putString("Subjecttopic_ID", TopicID);
            S.E("QuizTopicActivityTopicID::"+TopicID);
            S.I(QuizTopicActivity.this, QuizesActivity.class, bundle);
        } else if (ActivityCheck.equals("Twoplayer")) {
            if (Member_number.equals("2")) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "Twoplayer");
                bundle.putString("Subjecttopic_ID", TopicID);
                bundle.putString("CategerySubject_ID", CategerySubject_ID);
                bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
                bundle.putString("OtherUserId", OtherUserId);
                bundle.putString("OtherUserName", OtherUserName);
                bundle.putString("OtherGender", OtherGender);
                bundle.putString("OtherCity", OtherCity);
                S.E("QuizTopicActivityTwoplayerTopicID::"+TopicID);
                bundle.putString("OtherUserProfilePic", OtherUserProfilePic);
                bundle.putString("points", points);
                bundle.putString("Member_number", Member_number);

                S.I(QuizTopicActivity.this, QuizesActivity.class, bundle);
            } else if (Member_number.equals("3")) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "Twoplayer");
                bundle.putString("Subjecttopic_ID", TopicID);
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
                S.I(QuizTopicActivity.this, QuizesActivity.class, bundle);

            } else if (Member_number.equals("4")) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "Twoplayer");
                bundle.putString("Subjecttopic_ID", TopicID);
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

                S.I(QuizTopicActivity.this, QuizesActivity.class, bundle);
            }
        } else if (ActivityCheck.equals("QuizMultiplier")) {
            Bundle bundle = new Bundle();
            bundle.putString("ActivityCheck", "QuizMultiplier");
            bundle.putString("CategerySubject_ID", CategerySubject_ID);
            bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
            bundle.putString("Member_number", Member_number);
            bundle.putString("Subjecttopic_ID", TopicID);
            bundle.putString("points", points);
            S.I(QuizTopicActivity.this, QuizesActivity.class, bundle);
        }
    }
}
