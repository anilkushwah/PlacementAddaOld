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

public class LiveQuizActivity extends BaseActivity {
    RecyclerView subCategoryRV;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    List<QuizListModel> quizSubCatModelList = new ArrayList<>();
    private String topicId = "";
    TextView tvNoDataFoundId;
    private String ActivityCheck;

    @Override
    protected int getContentResId() {
        return R.layout.activity_quizes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       checkNetworkAvailability(LiveQuizActivity.this);
        setToolbarWithBackButton("Live Quiz");
     Bundle bundle=getIntent().getExtras();
     if (bundle != null){
         ActivityCheck=bundle.getString("ActivityCheck");
     }
        subCategoryRV = (RecyclerView) findViewById(R.id.categoryRV);
        tvNoDataFoundId = (TextView) findViewById(R.id.tvNoDataFoundId);
        subCategoryRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        getQuizList();

        BroadcastReceiver broadcastReceiver= S.LocalBroadcastReciver(LiveQuizActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }
    private void getQuizList() {
        new JSONParser(LiveQuizActivity.this).parseVollyStringRequest(Const.URL.GetLiveQuiz, 1, getParms(), new Helper() {

            @Override
            public void backResponse(String response) {
                S.E("checkHere response::"+response);
    try {
      S.E("checkHere GetQuizLIst::"+getParms());
      S.E("checkHere Response::"+response);
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

                        QuizesLiveAdapter quizCategoryAdapter = new QuizesLiveAdapter(LiveQuizActivity.this, quizSubCatModelList,ActivityCheck);
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
                    S.E("checkLiveQuiz::"+e);
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
