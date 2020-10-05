package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.CreateGroupAdapter;
import com.dollop.placementadda.adapter.QuizCategoryAdapter;
import com.dollop.placementadda.adapter.SelectTwoPlayerAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.CreateGroupModel;
import com.dollop.placementadda.model.LeaderBoardModel;
import com.dollop.placementadda.model.QuizCategoryModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

import static com.dollop.placementadda.sohel.Const.URL.getWalletAmount;

public class CreateGroupActivity extends BaseActivity {
    RecyclerView selectPlayerRv;
    RecyclerView categoryRV;
    private GridLayoutManager gaggeredGridLayoutManager;
    List<CreateGroupModel> quizCategoryModelList = new ArrayList<>();
    int amount=0;
    @Override
    protected int getContentResId() {
        return R.layout.activity_create_group;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(CreateGroupActivity.this);
        setToolbarWithBackButton("Select Player");
        selectPlayerRv = (RecyclerView) findViewById(R.id.selectPlayerRv);
        try {

            int noOfColumns = 2;
            //Use for show grid countity in one row
            gaggeredGridLayoutManager = new GridLayoutManager(this, noOfColumns);
            //Convert recycle view by grid manager
            selectPlayerRv.setLayoutManager(gaggeredGridLayoutManager);
            selectPlayerRv.setHasFixedSize(true);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(CreateGroupActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
        getWalletAmount();
        //getCateGoryList();
    }

    private void getWalletAmount() {
        new JSONParser(CreateGroupActivity.this).parseVollyStringRequest(Const.URL.GET_WALLET, 1, getParams(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    S.E("checkHere::Params"+getParams());
                    S.E("checkHere::Response"+response);
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200 && message.equals("success")) {
                        JSONObject data = mainobject.getJSONObject("data");
                        amount = data.getInt("wallet_amount");
                       // SavedData.saveAvalableBalance(amount);
                        S.E("wallet Balance"+amount);
                        getCateGoryList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());

        return params;
    }

    private void getCateGoryList() {
        new JSONParser(CreateGroupActivity.this).parseVollyStringRequest(Const.URL.GETPLAYERGROUP, 0, null, new Helper() {

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
                            CreateGroupModel quizCategoryModel = new CreateGroupModel();
                            String group_id = jsonObject1.getString("group_id");
                            String groupName = jsonObject1.getString("name");
                            String totalMember = jsonObject1.getString("total_member");
                            jsonObject1.getString("pic");
                            quizCategoryModel.setPlayerGroup_id(group_id);
                            quizCategoryModel.setName(groupName);
                            quizCategoryModel.setTotal_member(totalMember);
                            quizCategoryModelList.add(quizCategoryModel);
                        }
                        CreateGroupAdapter quizCategoryAdapter = new CreateGroupAdapter(CreateGroupActivity.this, quizCategoryModelList,amount);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(quizCategoryAdapter);
                        selectPlayerRv.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

                    } else {
                        quizCategoryModelList.clear();
                        CreateGroupAdapter quizCategoryAdapter = new CreateGroupAdapter(CreateGroupActivity.this, quizCategoryModelList,amount);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(quizCategoryAdapter);
                        selectPlayerRv.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}