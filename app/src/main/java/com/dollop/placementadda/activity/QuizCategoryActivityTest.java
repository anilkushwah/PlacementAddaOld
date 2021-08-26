package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.QuizCategoryAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.QuizCategoryModel;
import com.dollop.placementadda.model.QuizSubCategoryModel;
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

/*Created By Aniruddha*/

public class QuizCategoryActivityTest extends BaseActivity {
    RecyclerView categoryRV;
    private GridLayoutManager gaggeredGridLayoutManager;
    List<QuizCategoryModel> quizCategoryModelList = new ArrayList<>();
    Bundle bundle;
    String userId, ActivityCheck;
    private String OtherUserId = "", OtherUserName = "", OtherUserProfilePic = "", points = "", Member_number = "", OtherCity = "", OtherGender = "";
    String OtherSecondUserId = "", OtherSecondUserName = "", OtherSecondCity = "", OtherSecondGender = "", OtherSecondUserProfilePic = "";
    String OtherThirdUserId = "", OtherThirdUserName = "", OtherThirdCity = "", OtherThirdGender = "", OtherThirdUserProfilePic = "";

    @Override
    protected int getContentResId() {
        return R.layout.activity_quiz_category;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(QuizCategoryActivityTest.this);
        setToolbarWithBackButton("Quiz Category");

        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("ActivityCheck").equals("QuizCategery")) {
                ActivityCheck = bundle.getString("ActivityCheck");
            } else if (bundle.getString("ActivityCheck").equals("Twoplayer")) {
                if (bundle.getString("Member_number").equals("2")) {
                    ActivityCheck = bundle.getString("ActivityCheck");
                    OtherUserId = bundle.getString("OtherUserId");
                    OtherUserName = bundle.getString("OtherUserName");
                    OtherUserProfilePic = bundle.getString("OtherUserProfilePic");
                    points = bundle.getString("points");
                    Member_number = bundle.getString("Member_number");
                    OtherCity = bundle.getString("OtherCity");
                    OtherGender = bundle.getString("OtherGender");
                } else if (bundle.getString("Member_number").equals("3")) {
                    ActivityCheck = bundle.getString("ActivityCheck");
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
                Member_number = bundle.getString("Member_number");
                points = bundle.getString("points");
            }
        }


        categoryRV = (RecyclerView) findViewById(R.id.categoryRV);
        try {


            int noOfColumns = 2;
            //Use for show grid countity in one row
            gaggeredGridLayoutManager = new GridLayoutManager(this, noOfColumns);
            //Convert recycle view by grid manager
            categoryRV.setLayoutManager(gaggeredGridLayoutManager);
            categoryRV.setHasFixedSize(true);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        //Fixed size is use which we provide show total on list
        getCateGoryList();
        // getNOtification();
        //getCateGoryList use for get category list from server by api for function
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(QuizCategoryActivityTest.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }

    private void getNOtification() {
        new JSONParser(QuizCategoryActivityTest.this).parseVollyStringRequest(Const.URL.SEND_QUIZ_REQUEST, 1, getParams(), new Helper() {

            @Override
            public void backResponse(String response) {
                //Json parser use for get category method is use for gate category ac croding to get param from param we pass 0 for get all category
                try {

                    JSONObject mainobject = new JSONObject(response);
                    //mainobjec
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");


                    if (status == 200 && message.equals("success")) {
                        JSONObject data = mainobject.getJSONObject("data");
                        data.getString("result");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("error", "++++++--error--++++" + e);
                }
            }
        });
    }


    private Map<String, String> getParams() {

        HashMap<String, String> params = new HashMap<>();
        params.put("quiz_request_sender_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        params.put("quiz_request_reciver_id", SavedData.getTwoPlayerUserID());

        return params;
    }


    private void getCateGoryList() {
        new JSONParser(QuizCategoryActivityTest.this).parseVollyStringRequest(Const.URL.GetCategory, 1, getParms(), new Helper() {

            @Override
            public void backResponse(String response) {
                //Json parser use for get category method is use for gate category accroding to get param from param we pass 0 for get all category
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
                            quizCategoryModel.setQuizSubCatgoryModelsList(getSubCateGoryList(categoty_id));
                            quizCategoryModelList.add(quizCategoryModel);
                        }
                        QuizCategoryAdapter quizCategoryAdapter = new QuizCategoryAdapter(QuizCategoryActivityTest.this, quizCategoryModelList);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(quizCategoryAdapter);
                        categoryRV.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

                    } else {
                        quizCategoryModelList.clear();
                        QuizCategoryAdapter quizCategoryAdapter = new QuizCategoryAdapter(QuizCategoryActivityTest.this, quizCategoryModelList);
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
        params.put("parent_id", "0");
        return params;
    }


    private  ArrayList<QuizSubCategoryModel> getSubCateGoryList(String SelectedQuizCategeryId) {
        final ArrayList<QuizSubCategoryModel>quizSubCategoryModelsList=new ArrayList<>();
        new JSONParser(QuizCategoryActivityTest.this).parseVollyStringRequest(Const.URL.GetCategory, 1, getParmsSUbcategory(SelectedQuizCategeryId), new Helper() {

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
                            QuizSubCategoryModel quizCategoryModel = new QuizSubCategoryModel();
                            String categoty_id = jsonObject1.getString("categoty_id");
                            String categoryName = jsonObject1.getString("categoryName");
                            String category_image = jsonObject1.getString("category_image");
                            quizCategoryModel.categoty_id=categoty_id;
                            quizCategoryModel.categoryName=categoryName;
                            quizCategoryModel.category_image=category_image;
                            quizSubCategoryModelsList.add(quizCategoryModel);
                        }

                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
   return quizSubCategoryModelsList;
     }

    private Map<String, String> getParmsSUbcategory(String selectedQuizCategeryId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("parent_id", selectedQuizCategeryId);
        return params;
    }

    public void getSelected(String SelectedQuizCategeryId) {
        if (ActivityCheck.equals("QuizCategery")) {
            Bundle bundle = new Bundle();
            bundle.putString("ActivityCheck", "QuizCategery");
            bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
            bundle.putString("OtherUserId", OtherUserId);
            S.I(QuizCategoryActivityTest.this, QuizSubCategoryActivity.class, bundle);
        } else if (ActivityCheck.equals("Twoplayer")) {
            if (Member_number.equals("2")) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "Twoplayer");
                bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
                bundle.putString("OtherUserId", OtherUserId);
                bundle.putString("OtherUserName", OtherUserName);
                bundle.putString("OtherCity", OtherCity);
                bundle.putString("OtherGender", OtherGender);
                bundle.putString("OtherUserProfilePic", OtherUserProfilePic);
                bundle.putString("points", points);
                bundle.putString("Member_number", Member_number);
                S.I(QuizCategoryActivityTest.this, QuizSubCategoryActivity.class, bundle);
            } else if (Member_number.equals("3")) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "Twoplayer");
                bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
                bundle.putString("OtherUserId", OtherUserId);
                bundle.putString("OtherUserName", OtherUserName);
                bundle.putString("OtherCity", OtherCity);
                bundle.putString("OtherGender", OtherGender);
                bundle.putString("OtherUserProfilePic", OtherUserProfilePic);

                bundle.putString("OtherSecondUserId", OtherSecondUserId);
                bundle.putString("OtherSecondUserName", OtherSecondUserName);
                bundle.putString("OtherSecondCity", OtherSecondCity);
                bundle.putString("OtherSecondGender", OtherSecondGender);
                bundle.putString("OtherSecondUserProfilePic", OtherSecondUserProfilePic);

                bundle.putString("points", points);
                bundle.putString("Member_number", Member_number);
                S.I(QuizCategoryActivityTest.this, QuizSubCategoryActivity.class, bundle);
            } else if (Member_number.equals("4")) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "Twoplayer");
                bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
                bundle.putString("OtherUserId", OtherUserId);
                bundle.putString("OtherUserName", OtherUserName);
                bundle.putString("OtherCity", OtherCity);
                bundle.putString("OtherGender", OtherGender);
                bundle.putString("OtherUserProfilePic", OtherUserProfilePic);

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

                bundle.putString("points", points);
                bundle.putString("Member_number", Member_number);
                S.I(QuizCategoryActivityTest.this, QuizSubCategoryActivity.class, bundle);
            }
        } else if (ActivityCheck.equals("QuizMultiplier")) {
            Bundle bundle = new Bundle();
            bundle.putString("ActivityCheck", "QuizMultiplier");
            bundle.putString("SelectedQuizCategeryId", SelectedQuizCategeryId);
            bundle.putString("Member_number", Member_number);
            bundle.putString("points", points);
            S.I(QuizCategoryActivityTest.this, QuizSubCategoryActivity.class, bundle);
        }
    }
}
