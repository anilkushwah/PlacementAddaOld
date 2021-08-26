package com.dollop.placementadda.activity.intro;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.LinearLayoutManagerWithSmoothScroller;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.StudentTopicAdapter;
import com.dollop.placementadda.model.QuizCategoryModel;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class SubjectTopicActivity extends BaseActivity {
    RecyclerView categoryRV;
    private GridLayoutManager gaggeredGridLayoutManager;
    List<QuizCategoryModel> quizCategoryModelList = new ArrayList<>();
    private String subcategoryID = "";

    @Override
    protected int getContentResId() {
        return R.layout.activity_subject_topic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("Topic");
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManagerWithSmoothScroller(SubjectTopicActivity.this, LinearLayoutManager.VERTICAL,false);

        categoryRV = (RecyclerView) findViewById(R.id.categoryRV);
       /* int noOfColumns = 2;
        gaggeredGridLayoutManager = new GridLayoutManager(this, noOfColumns);*/
        categoryRV.setLayoutManager(layoutManager);
        categoryRV.setHasFixedSize(true);
        subcategoryID = getIntent().getStringExtra("id");
        getCateGoryList();
    }
    private void getCateGoryList() {
        new JSONParser(SubjectTopicActivity.this).parseVollyStringRequest(Const.URL.GetCategory, 1, getParms(), new Helper() {

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
                        StudentTopicAdapter quizCategoryAdapter = new StudentTopicAdapter(SubjectTopicActivity.this, quizCategoryModelList);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(quizCategoryAdapter);
                        categoryRV.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

                    } else {
                        quizCategoryModelList.clear();
                        StudentTopicAdapter quizCategoryAdapter = new StudentTopicAdapter(SubjectTopicActivity.this, quizCategoryModelList);
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
        params.put("parent_id", subcategoryID);

        return params;
    }

}
