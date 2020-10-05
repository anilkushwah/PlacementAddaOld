package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.BottomNoListAdapter;
import com.dollop.placementadda.adapter.LeaderBoardAdapter;
import com.dollop.placementadda.adapter.MainFragmentAdapter;
import com.dollop.placementadda.adapter.QuizQuetionaryAdapter;
import com.dollop.placementadda.adapter.ScoresAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.LeaderBoardModel;
import com.dollop.placementadda.model.MainFragModel;
import com.dollop.placementadda.model.SelectedListModel;
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
import me.relex.circleindicator.CircleIndicator;

public class LeaderBoardActivity extends BaseActivity {
    //Leader Board Activity is use for show user rank place in this app total quiz result
    int mypostion = 0;
    ListView landingRecyclerView;
    //RecycleView landingRecyclerView is use for show listing
    private List<LeaderBoardModel> leaderBoardModelArrayList = new ArrayList<>();
    private LinearLayoutManager linearlayoutManage;
    private ScoresAdapter pdfAdapter;

    @Override
    protected int getContentResId() {

        return R.layout.activity_leader_board;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(LeaderBoardActivity.this);

        landingRecyclerView = findViewById(R.id.landingRecyclerView);
        getLeaderBoardRanks();
        //start by this linge
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(LeaderBoardActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }

    private void getLeaderBoardRanks() {
        new JSONParser(LeaderBoardActivity.this).parseVollyStringRequest(Const.URL.GetAllUserByRank, 1, getParmsans(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            LeaderBoardModel leaderBoardModel = new LeaderBoardModel();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            if (jsonObject1.getString("user_id").equals(UserDataHelper.getInstance().getList().get(0).getUserId())) {
                                mypostion = i;
                                final int h1 = landingRecyclerView.getHeight();
                                final int h2 = landingRecyclerView.getHeight();
                                landingRecyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        S.E("now : 2 + " + mypostion);
                                        landingRecyclerView.smoothScrollToPositionFromTop(mypostion - 3, h1 / 2 - h2 / 2);
                                    }
                                }, 500);
                            }
                            leaderBoardModel.setUser_id(jsonObject1.getString("user_id"));
                            leaderBoardModel.setUserProfilePic(jsonObject1.getString("userProfilePic"));
                            leaderBoardModel.setUserName(jsonObject1.getString("userName"));
                            leaderBoardModel.setRankingPoints(jsonObject1.getString("rankingPoints"));
                            leaderBoardModelArrayList.add(leaderBoardModel);
                        }
                        pdfAdapter = new ScoresAdapter(LeaderBoardActivity.this, R.layout.item_leader_list, leaderBoardModelArrayList);
                        landingRecyclerView.setAdapter(pdfAdapter);
                        pdfAdapter.notifyDataSetChanged();
                    } else {
                        S.T(LeaderBoardActivity.this, "" + message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParmsans() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
        return params;
    }

}
