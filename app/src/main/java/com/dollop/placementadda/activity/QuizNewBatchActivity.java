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
import com.dollop.placementadda.adapter.NewBatchesAdapter;
import com.dollop.placementadda.adapter.QuizSubsCategoryAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.NewBatchesModel;
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

public class QuizNewBatchActivity extends BaseActivity {
    RecyclerView categoryRV;
    private LinearLayoutManager gaggeredGridLayoutManager;
    List<NewBatchesModel> quizCategoryModelList = new ArrayList<>();
    private String categoryID = "";

    @Override
    protected int getContentResId() {
        return R.layout.activity_quiz_sub_category;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(QuizNewBatchActivity.this);
        setToolbarWithBackButton("Training");

        categoryRV = (RecyclerView) findViewById(R.id.categoryRV);

        try {
            gaggeredGridLayoutManager = new LinearLayoutManager(this);
            categoryRV.setLayoutManager(gaggeredGridLayoutManager);
            categoryRV.setHasFixedSize(true);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        getNewBatches();
        BroadcastReceiver broadcastReceiver= S.LocalBroadcastReciver(QuizNewBatchActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }

    private void getNewBatches() {
        new JSONParser(QuizNewBatchActivity.this).parseVollyStringRequest(Const.URL.getNewBatches, 1, getParms(), new Helper() {

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
                            NewBatchesModel newBatchesModel = new NewBatchesModel();
                            newBatchesModel.setBatch_id(jsonObject1.getString("batch_id"));
                            newBatchesModel.setBatch_title(jsonObject1.getString("batchTitle"));
                            newBatchesModel.setBatch_detail(jsonObject1.getString("batchDetail"));
                            newBatchesModel.setBatch_start_date(jsonObject1.getString("batchStartDate"));
                            newBatchesModel.setNewBacthTime(jsonObject1.getString("batchStartTime"));
                            newBatchesModel.setDateChange("SameDate");
                            newBatchesModel.setNotificationorBatch("1");

                            S.E("batchStartTime::"+jsonObject1.getString("batchStartTime"));
                            quizCategoryModelList.add(newBatchesModel);

                        }
                        NewBatchesAdapter quizCategoryAdapter = new NewBatchesAdapter(QuizNewBatchActivity.this, quizCategoryModelList);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(quizCategoryAdapter);
                        categoryRV.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

                    } else {
                        quizCategoryModelList.clear();
                        NewBatchesAdapter quizCategoryAdapter = new NewBatchesAdapter (QuizNewBatchActivity.this, quizCategoryModelList);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(quizCategoryAdapter);
                        categoryRV.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
                        S.T(QuizNewBatchActivity.this,message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private Map<String, String> getParms() {

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
        return params;
    }
}
