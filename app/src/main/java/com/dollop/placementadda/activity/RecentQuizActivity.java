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
import com.dollop.placementadda.adapter.QuizesLiveAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.QuizListModel;
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

public class RecentQuizActivity extends BaseActivity {
    RecyclerView subCategoryRV;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    List<QuizListModel> quizSubCatModelList = new ArrayList<>();
    TextView tvNoDataFoundId;
    private String topicId = "";
    String ActivityCheck;

    @Override
    protected int getContentResId() {
        return R.layout.activity_quizes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(RecentQuizActivity.this);
        setToolbarWithBackButton("Recent Quiz List");
        tvNoDataFoundId = findViewById(R.id.tvNoDataFoundId);
        Bundle bundle = getIntent().getExtras();
        ActivityCheck = bundle.getString("ActivityCheck");
        subCategoryRV = (RecyclerView) findViewById(R.id.categoryRV);
        try {
            recyclerViewlayoutManager = new LinearLayoutManager(this);
            subCategoryRV.setLayoutManager(recyclerViewlayoutManager);
            subCategoryRV.setHasFixedSize(true);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        getQuizList();
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(RecentQuizActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }

    private void getQuizList() {
        new JSONParser(RecentQuizActivity.this).parseVollyStringRequest(Const.URL.GetLatestQuiz, 1, getParms(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
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
                        if (quizSubCatModelList.size() > 0) {
                            tvNoDataFoundId.setVisibility(View.GONE);
                            subCategoryRV.setVisibility(View.VISIBLE);
                            QuizesLiveAdapter quizCategoryAdapter = new QuizesLiveAdapter(RecentQuizActivity.this, quizSubCatModelList, "QuizCategery");
                            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(quizCategoryAdapter);
                            subCategoryRV.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
                        } else {
                            subCategoryRV.setVisibility(View.GONE);
                            tvNoDataFoundId.setVisibility(View.VISIBLE);
                        }

                    } else {
                        subCategoryRV.setVisibility(View.GONE);
                        tvNoDataFoundId.setVisibility(View.VISIBLE);
                        tvNoDataFoundId.setText(message);
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
