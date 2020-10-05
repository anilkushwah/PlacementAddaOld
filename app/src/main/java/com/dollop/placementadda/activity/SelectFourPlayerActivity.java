package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.activity.category.QuizCategoryActivity;
import com.dollop.placementadda.adapter.SelectFourPlayerAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
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
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class SelectFourPlayerActivity extends BaseActivity implements SearchView.OnQueryTextListener {
    Toolbar toolBar;
    SearchView searchView;
    RecyclerView fourPlayerRv;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    Button playQuizBtn;
    private SelectFourPlayerAdapter pdfAdapter;
    private ArrayList<LeaderBoardModel> leaderBoardModelArrayList = new ArrayList<>();
    private String u_id;

    @Override
    protected int getContentResId() {
        return R.layout.activity_select_four_player;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(SelectFourPlayerActivity.this);
        setToolbarWithBackButton("Four Player");
        toolBar = findViewById(R.id.tool_bar);
        fourPlayerRv = (RecyclerView) findViewById(R.id.fourPlayerRv);
        playQuizBtn = findViewById(R.id.playQuizBtn);
        try {
            fourPlayerRv.setHasFixedSize(true);
            recyclerViewlayoutManager = new LinearLayoutManager(this);
            fourPlayerRv.setLayoutManager(recyclerViewlayoutManager);
        }catch (Exception e)
        {e.printStackTrace();}
        pdfAdapter = new SelectFourPlayerAdapter(this, leaderBoardModelArrayList);
        playQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("selectTwoPlaterId", UserDataHelper.getInstance().getList().get(0).getUserId());
                S.I(SelectFourPlayerActivity.this, QuizCategoryActivity.class, null);
            }
        });
        getUserList();
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(SelectFourPlayerActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }

    private void getUserList() {
        new JSONParser(SelectFourPlayerActivity.this).parseVollyStringRequest(Const.URL.GetAllUserByRank, 1, getParmsans(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    fourPlayerRv.setVisibility(View.VISIBLE);
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");

                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            LeaderBoardModel leaderBoardModel = new LeaderBoardModel();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            u_id = leaderBoardModel.setUser_id(jsonObject1.getString("user_id"));
                            leaderBoardModel.setUserProfilePic(jsonObject1.getString("userProfilePic"));
                            leaderBoardModel.setUserName(jsonObject1.getString("userName"));
                            leaderBoardModel.setRankingPoints(jsonObject1.getString("rankingPoints"));
                            leaderBoardModelArrayList.add(leaderBoardModel);

                        }
                        fourPlayerRv.setLayoutManager(recyclerViewlayoutManager);
                        pdfAdapter = new SelectFourPlayerAdapter(SelectFourPlayerActivity.this, leaderBoardModelArrayList);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(pdfAdapter);
                        fourPlayerRv.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
                    } else {
                        S.T(SelectFourPlayerActivity.this, "" + message);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(SelectFourPlayerActivity.this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setToolbarWithBackButton("Four Player");
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(myActionMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                pdfAdapter.setFilter(leaderBoardModelArrayList);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchView.setIconified(true);
                searchView.onActionViewCollapsed();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<LeaderBoardModel> filteredModelList = filter(leaderBoardModelArrayList, newText);
        pdfAdapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<LeaderBoardModel> filter(List<LeaderBoardModel> models, String query) {
        query = query.toLowerCase();

        final List<LeaderBoardModel> filteredModelList = new ArrayList<>();
        for (LeaderBoardModel model : models) {
            final String text = model.getUserName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onBackPressed() {
        searchView.onActionViewCollapsed();
        super.onBackPressed();
    }

}
