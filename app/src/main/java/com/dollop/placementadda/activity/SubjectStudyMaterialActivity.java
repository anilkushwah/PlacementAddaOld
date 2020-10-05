package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.StudyMaterialAdapter;
import com.dollop.placementadda.adapter.StudyMaterialAdapter;
import com.dollop.placementadda.model.QuizCategoryModel;
import com.dollop.placementadda.model.StudyMaterialModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class SubjectStudyMaterialActivity extends BaseActivity {


    Toolbar toolBar;

    RecyclerView categoryRV;
    private LinearLayoutManager gaggeredGridLayoutManager;
    private String categoryID;
    ArrayList<StudyMaterialModel> quizCategoryModelList = new ArrayList<>();

    @Override
    protected int getContentResId() {
        return R.layout.activity_subject_study_material;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_study_material);

        setToolbarWithBackButton("Study Material");
        toolbar = findViewById(R.id.tool_bar);
        categoryRV = findViewById(R.id.categoryRV);
        try {
            gaggeredGridLayoutManager = new LinearLayoutManager(this);
            categoryRV.setLayoutManager(gaggeredGridLayoutManager);
            categoryRV.setHasFixedSize(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        categoryID = getIntent().getStringExtra("id");
        getCateGoryList();

        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(SubjectStudyMaterialActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }

    private void getCateGoryList() {
        new JSONParser(SubjectStudyMaterialActivity.this).parseVollyStringRequest(Const.URL.SubjectTheory, 1, getParms(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    S.E("checkResponse::"+response);
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");


                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            StudyMaterialModel quizCategoryModel = new StudyMaterialModel();
                            quizCategoryModel.setSubjectTheoryId(jsonObject1.getString("subjectTheoryId"));
                            quizCategoryModel.setCategoty_id(jsonObject1.getString("categoty_id"));
                            quizCategoryModel.setSubjectTheoryHeading(jsonObject1.getString("subjectTheoryHeading"));
                            quizCategoryModel.setSubjectTheoryContant(jsonObject1.getString("subjectTheoryContant"));
                            quizCategoryModel.setSubjectTheoryCreatedDate(jsonObject1.getString("subjectTheoryCreatedDate"));

                            quizCategoryModelList.add(quizCategoryModel);

                        }
                        StudyMaterialAdapter quizCategoryAdapter = new StudyMaterialAdapter(SubjectStudyMaterialActivity.this, quizCategoryModelList);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(quizCategoryAdapter);
                        categoryRV.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

                    } else {
                        StudyMaterialAdapter quizCategoryAdapter = new StudyMaterialAdapter(SubjectStudyMaterialActivity.this, quizCategoryModelList);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(quizCategoryAdapter);
                        categoryRV.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));


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
        params.put("categoty_id", categoryID);

        return params;
    }
}
