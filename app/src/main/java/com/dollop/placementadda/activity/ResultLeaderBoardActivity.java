package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.LeaderBoardAdapter;
import com.dollop.placementadda.adapter.TopicWiseLeadershipAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.FilteredHistory;
import com.dollop.placementadda.model.LeaderBoardModel;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ResultLeaderBoardActivity extends BaseActivity {
    RecyclerView leaderBoardRecyclerView;

    int mypostion = 0;
    private ArrayList<LeaderBoardModel> leaderBoardModelArrayList = new ArrayList<>();

    private LinearLayoutManager linearlayoutManage;
    TopicWiseLeadershipAdapter topicWiseLeadershipAdapter;
    private LeaderBoardAdapter pdfAdapter;
    private TextView rankTV;
    private TextView UserName;
    private TextView totalNumberTV;
    CircleImageView ic_user_imageview;

    @Override
    protected int getContentResId() {
        return R.layout.activity_topic_wise_leader_ship;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(ResultLeaderBoardActivity.this);
        setToolbarWithBackButton("Ranking");
//        topicWiseRv = findViewById(R.id.topicWiseRv);
        Bundle b = getIntent().getExtras();
        final FilteredHistory filteredHistory = (FilteredHistory) b.getSerializable("AllData");

        getLeaderBoardRanks();
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(ResultLeaderBoardActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
        rankTV = findViewById(R.id.rankTV);
        UserName = findViewById(R.id.UserName);
        totalNumberTV = findViewById(R.id.totalNumberTV);
        ic_user_imageview =findViewById(R.id.ic_user_imageview);
        leaderBoardRecyclerView =(RecyclerView) findViewById(R.id.leaderBoardRecyclerView);
        leaderBoardRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }
    private void getLeaderBoardRanks() {
        new JSONParser(ResultLeaderBoardActivity.this).parseVollyStringRequest(Const.URL.GetAllUserByRank, 1, getParmsans(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    S.E("checkREsponse" + response);
                    S.E("checkParams" + getParmsans());

                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");

                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            LeaderBoardModel leaderBoardModel = new LeaderBoardModel();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (i < 10) {
                                leaderBoardModel.setUser_id(jsonObject1.getString("user_id"));
                                leaderBoardModel.setUserProfilePic(jsonObject1.getString("userProfilePic"));
                                leaderBoardModel.setUserName(jsonObject1.getString("userName"));
                                leaderBoardModel.setRankingPoints(jsonObject1.getString("rankingPoints"));
                                leaderBoardModelArrayList.add(leaderBoardModel);
                            }

                         /*   if (jsonObject1.getString("user_id").equals(UserDataHelper.getInstance().getList().get(0).getUserId())) {
                                mypostion = i;
                                final int h1 = leaderBoardRecyclerView.getHeight();
                                final int h2 = leaderBoardRecyclerView.getHeight();
                                leaderBoardRecyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        S.E("now : 2 + " + mypostion);
                                        leaderBoardRecyclerView.smoothScrollToPositionFromTop(mypostion - 3, h1 / 2 - h2 / 2);
                                    }
                                }, 500);
                            }*/

                            if (jsonObject1.getString("user_id").equals(UserDataHelper.getInstance().getList().get(0).getUserId())) {

                                leaderBoardModel.setUserProfilePic(jsonObject1.getString("userProfilePic"));
                                String username = jsonObject1.getString("userName");
                                S.E("username" + username);
                                String ranking = jsonObject1.getString("rankingPoints");
                                S.E("ranking" + ranking);

                                UserName.setText(username);
                                totalNumberTV.setText(ranking);
                                int totalMyRank=i+1;
                                rankTV.setText(""+totalMyRank);

                            }

                        }
                        pdfAdapter = new LeaderBoardAdapter(ResultLeaderBoardActivity.this, leaderBoardModelArrayList,0);
                        leaderBoardRecyclerView.setLayoutManager(new LinearLayoutManager(ResultLeaderBoardActivity.this));
                        leaderBoardRecyclerView.setAdapter(pdfAdapter);
                    } else {
                        S.T(ResultLeaderBoardActivity.this, "" + message);
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
