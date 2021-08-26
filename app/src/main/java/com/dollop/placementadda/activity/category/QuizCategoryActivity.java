package com.dollop.placementadda.activity.category;

import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.QuizTopicActivity;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.model.QuizCategoryModel;
import com.dollop.placementadda.model.QuizSubCategoryModel;
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

public class QuizCategoryActivity extends BaseActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle=new ArrayList<>();
    HashMap<String, List<QuizSubCategoryModel>> expandableListDetail=new HashMap<>();

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
       // setContentView();
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        setToolbarWithBackButton("Category");
         ActivityCheck = getIntent().getStringExtra("ActivityCheck");

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


     /*   Bundle bundle = new Bundle();
        bundle.putString("ActivityCheck", "QuizCategery");
        S.I(getActivity(), QuizCategoryActivity.class, bundle);*/

     //   expandableListDetail = ExpandableListDataPump.getData();
//        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        getCateGoryList();

      /*  expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);*/

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
               /* Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
               /* Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();
*/
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
               /* Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();*/
                return false;
            }
        });


        for(int i=0;i< expandableListTitle.size();i++) {
            expandableListView.expandGroup(i);
        }


    }

    private void getCateGoryList() {
        new JSONParser(QuizCategoryActivity.this).parseVollyStringRequest(Const.URL.GetCategory, 1, getParms(), new Helper() {

            @Override
            public void backResponse(String response) {
                //Json parser use for get category method is use for gate category accroding to get param from param we pass 0 for get all category
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        expandableListTitle.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            QuizCategoryModel quizCategoryModel = new QuizCategoryModel();
                            String categoty_id = jsonObject1.getString("categoty_id");
                            String categoryName = jsonObject1.getString("categoryName");
                            String category_image = jsonObject1.getString("category_image");
                            quizCategoryModel.setCategoryId(categoty_id);
                            quizCategoryModel.setCategoryName(categoryName);
                            quizCategoryModel.setCategory_Image(category_image);
                            expandableListTitle.add(categoryName);



                            expandableListDetail.put(categoryName, getSubCateGoryList(categoty_id));
//                            quizCategoryModel.setQuizSubCatgoryModelsList();
//                            quizCategoryModelList.add(quizCategoryModel);
                        }

                        expandableListAdapter = new CustomExpandableListAdapter(QuizCategoryActivity.this, expandableListTitle, expandableListDetail);
                        expandableListView.setAdapter(expandableListAdapter);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                expandableListView.expandGroup(0);
                            }
                        }, 1000);



                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    S.E("checkExceptionCategory::"+e);
                }
            }
        });
    }

    private Map<String, String> getParms() {
        HashMap<String, String> params = new HashMap<>();
        params.put("parent_id", "0");
        return params;
    }
    private  ArrayList<QuizSubCategoryModel> getSubCateGoryList(final String SelectedQuizCategeryId) {
        final ArrayList<QuizSubCategoryModel>quizSubCategoryModelsList=new ArrayList<>();
        new JSONParser(QuizCategoryActivity.this).parseVollyStringRequest(Const.URL.GetCategory, 1, getParmsSUbcategory(SelectedQuizCategeryId), new Helper() {

            @Override
            public void backResponse(String response) {

                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");

                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        quizSubCategoryModelsList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            QuizSubCategoryModel quizCategoryModel = new QuizSubCategoryModel();
                            String categoty_id = jsonObject1.getString("categoty_id");
                            String categoryName = jsonObject1.getString("categoryName");
                            String category_image = jsonObject1.getString("category_image");
                            quizCategoryModel.categoty_id=categoty_id;
                            quizCategoryModel.categoryName=categoryName;
                            quizCategoryModel.category_image=category_image;
                            quizCategoryModel.actual_categoty_id=SelectedQuizCategeryId;
                            S.E("QuizCategoryImage::"+category_image);

                            quizSubCategoryModelsList.add(quizCategoryModel);
                        }

                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    S.E("checkExceptionCategory::"+e);
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
    public void getSelectedCategerySubject(String CategerySubject_ID,String categoryId) {
        if (ActivityCheck.equals("QuizCategery")) {
            Bundle bundle = new Bundle();
            bundle.putString("ActivityCheck", "QuizCategery");
            bundle.putString("CategerySubject_ID", CategerySubject_ID);
            bundle.putString("SelectedQuizCategeryId", categoryId);
            bundle.putString("OtherUserId", OtherUserId);
            S.I(QuizCategoryActivity.this, QuizTopicActivity.class, bundle);
        } else if (ActivityCheck.equals("Twoplayer")) {
            if (Member_number.equals("2")) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "Twoplayer");
                bundle.putString("CategerySubject_ID", CategerySubject_ID);
                bundle.putString("SelectedQuizCategeryId", categoryId);
                bundle.putString("OtherUserId", OtherUserId);
                bundle.putString("OtherUserName", OtherUserName);
                bundle.putString("OtherGender", OtherGender);
                bundle.putString("OtherCity", OtherCity);
                bundle.putString("OtherUserProfilePic", OtherUserProfilePic);
                bundle.putString("points", points);
                bundle.putString("Member_number", Member_number);
                S.I(QuizCategoryActivity.this, QuizTopicActivity.class, bundle);
            } else if (Member_number.equals("3")) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "Twoplayer");
                bundle.putString("CategerySubject_ID", CategerySubject_ID);
                bundle.putString("SelectedQuizCategeryId", categoryId);
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
                S.I(QuizCategoryActivity.this, QuizTopicActivity.class, bundle);
            } else if (Member_number.equals("4")) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "Twoplayer");
                bundle.putString("CategerySubject_ID", CategerySubject_ID);
                bundle.putString("SelectedQuizCategeryId", categoryId);
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
                S.I(QuizCategoryActivity.this, QuizTopicActivity.class, bundle);
            }
        } else if (ActivityCheck.equals("QuizMultiplier")) {
            Bundle bundle = new Bundle();
            bundle.putString("ActivityCheck", "QuizMultiplier");
            bundle.putString("CategerySubject_ID", CategerySubject_ID);
            bundle.putString("SelectedQuizCategeryId", categoryId);
            bundle.putString("Member_number", Member_number);
            bundle.putString("points", points);
            S.I(QuizCategoryActivity.this, QuizTopicActivity.class, bundle);
        }
    }


}
