package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.view.MenuItemCompat;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.activity.category.QuizCategoryActivity;
import com.dollop.placementadda.adapter.SelectThreePlayerAdapter;
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

public class SelectThreePlayerActivity extends BaseActivity implements SearchView.OnQueryTextListener{
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView threePlayerRv;
    Button playQuizBtn;
    private SelectThreePlayerAdapter pdfAdapter;
    private List<LeaderBoardModel> leaderBoardModelArrayList = new ArrayList<>();
    private String u_id;
    private RecyclerView.Adapter mAdapter;
    SearchView searchView;
    ArrayList<String>stringsSelectedPlayer=new ArrayList<>();
    @Override
    protected int getContentResId() {
        return R.layout.activity_select_three_player;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(SelectThreePlayerActivity.this);
        setToolbarWithBackButton("Three Players");
        threePlayerRv=(RecyclerView)findViewById(R.id.threePlayerRv);
        playQuizBtn=findViewById(R.id.playQuizBtn);
//        S.E("three uids"+ SavedData.getTHREEPlayerUserID());
        playQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("selectTwoPlaterId", UserDataHelper.getInstance().getList().get(0).getUserId());
                S.E("user id"+UserDataHelper.getInstance().getList().get(0).getUserId());
                S.I(SelectThreePlayerActivity.this, QuizCategoryActivity.class,null);
            }
        });
        try {
            threePlayerRv.setHasFixedSize(true);
            recyclerViewlayoutManager = new LinearLayoutManager(this);
            threePlayerRv.setLayoutManager(recyclerViewlayoutManager);
            pdfAdapter = new SelectThreePlayerAdapter(this, leaderBoardModelArrayList);
        }catch (Exception e){
            e.printStackTrace();
        }
        getUserList();

        BroadcastReceiver broadcastReceiver= S.LocalBroadcastReciver(SelectThreePlayerActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }



    private void getUserList() {
        new JSONParser(SelectThreePlayerActivity.this).parseVollyStringRequest(Const.URL.GetAllUserByRank, 1, getParmsans(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    threePlayerRv.setVisibility(View.VISIBLE);

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
                            u_id = leaderBoardModel.setUser_id(jsonObject1.getString("user_id"));
                            leaderBoardModel.setUserProfilePic(jsonObject1.getString("userProfilePic"));
                            leaderBoardModel.setUserName(jsonObject1.getString("userName"));
                            leaderBoardModel.setRankingPoints(jsonObject1.getString("rankingPoints"));
                            leaderBoardModelArrayList.add(leaderBoardModel);
                           // stringsSelectedPlayer.add("-1");

                        }
                        S.E("2 teacher model list" + leaderBoardModelArrayList.size());
                        //  recyclerViewlayoutManager = new LinearLayoutManager(this);
                        threePlayerRv.setLayoutManager(recyclerViewlayoutManager);

                        pdfAdapter = new SelectThreePlayerAdapter(SelectThreePlayerActivity.this,leaderBoardModelArrayList);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(pdfAdapter);
                        threePlayerRv.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
                        pdfAdapter.notifyDataSetChanged();

                    } else {
                        S.T(SelectThreePlayerActivity.this, "" + message);
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
        searchView =(SearchView) myActionMenuItem.getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setToolbarWithBackButton("Three Players");
                return false;
            }
        });
        searchView.setOnQueryTextListener(SelectThreePlayerActivity.this);
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
            final String user_ids= String.valueOf(model.getUser_id());
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
            else if (user_ids.contains(query))
            {
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
