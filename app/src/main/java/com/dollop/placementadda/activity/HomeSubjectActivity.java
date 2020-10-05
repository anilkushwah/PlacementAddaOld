package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.QuizSubjectAdapter;
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

public class HomeSubjectActivity extends BaseActivity {
    RecyclerView categoryRV;
    private GridLayoutManager gaggeredGridLayoutManager;
    List<QuizCategoryModel> quizCategoryModelList = new ArrayList<>();
    private String categoryID = "";
    @Override
    protected int getContentResId() {
        return R.layout.activity_quiz_sub_category;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(HomeSubjectActivity.this);
        setToolbarWithBackButton("Quiz Subject");
        categoryRV = (RecyclerView) findViewById(R.id.categoryRV);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManagerWithSmoothScroller(HomeSubjectActivity.this, LinearLayoutManager.VERTICAL,false);
        try {

           /* int noOfColumns = 2;
            gaggeredGridLayoutManager = new GridLayoutManager(this, noOfColumns);*/
            categoryRV.setLayoutManager(layoutManager);
            categoryRV.setHasFixedSize(true);
            categoryID = getIntent().getStringExtra("id");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        getCateGoryList();
        BroadcastReceiver broadcastReceiver= S.LocalBroadcastReciver(HomeSubjectActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }

    private void getCateGoryList() {
        new JSONParser(HomeSubjectActivity.this).parseVollyStringRequest(Const.URL.GetSubjects, 1, getParms(), new Helper() {

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
                        QuizSubjectAdapter quizCategoryAdapter = new QuizSubjectAdapter(HomeSubjectActivity.this, quizCategoryModelList);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(quizCategoryAdapter);
                        categoryRV.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

                    } else {
                        S.T(HomeSubjectActivity.this,""+message);
                        quizCategoryModelList.clear();
                        QuizSubjectAdapter quizCategoryAdapter = new QuizSubjectAdapter(HomeSubjectActivity.this, quizCategoryModelList);
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
        return params;
    }
}
