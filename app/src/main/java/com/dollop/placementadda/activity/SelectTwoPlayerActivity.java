package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.activity.category.QuizCategoryActivity;
import com.dollop.placementadda.adapter.SelectTwoPlayerAdapter;
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

public class SelectTwoPlayerActivity extends BaseActivity implements SearchView.OnQueryTextListener{
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    Toolbar toolBar;
    Button playQuizBtn;
    RecyclerView twoPlayerRv;
     SearchView searchView;
    ArrayList<String> searchList = new ArrayList<>();
    private List<LeaderBoardModel> leaderBoardModelArrayList = new ArrayList<>();
    private String u_id;
    private RecyclerView.Adapter mAdapter;
    private String OtherUserId ="",OtherUserName="",OtherUserProfilePic="",OtherCity="",OtherGender="";
String points,Member_number;
    ArrayList<String> user_IdList=new ArrayList<>();
    ArrayList<String> user_nameList=new ArrayList<>();
    ArrayList<String> user_Profile_picList=new ArrayList<>();
    ArrayList<String> user_CityList=new ArrayList<>();
    private SelectTwoPlayerAdapter timeLineAdapter;

    @Override
    protected int getContentResId() {
        return R.layout.activity_select_two_player;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(SelectTwoPlayerActivity.this);
        toolbar=findViewById(R.id.tool_bar);
        Bundle bundle=getIntent().getExtras();
        if (bundle != null){
        points=bundle.getString("points");
        Member_number=bundle.getString("Member_number");
            setToolbarWithBackButton(bundle.getString("Member_number")+" Player");}
        twoPlayerRv = (RecyclerView) findViewById(R.id.twoPlayerRv);
        playQuizBtn=(Button)findViewById(R.id.playQuizBtn);
        try {
            playQuizBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Member_number.equals("2")) {
                        if (user_IdList.size() != 0) {
                            if (!OtherUserId.equals(user_IdList.get(0))) {
                                Bundle bundle = new Bundle();
                                bundle.putString("OtherUserId", user_IdList.get(0));
                                bundle.putString("OtherUserName", user_nameList.get(0));
                                bundle.putString("OtherCity", user_CityList.get(0));
                                bundle.putString("OtherGender", OtherGender);
                                bundle.putString("OtherUserProfilePic", user_Profile_picList.get(0));
                                bundle.putString("points", points);
                                bundle.putString("Member_number", Member_number);
                                bundle.putString("ActivityCheck", "Twoplayer");
                                S.E("check OtherGender" + OtherGender);

                                S.I(SelectTwoPlayerActivity.this, QuizCategoryActivity.class, bundle);
                            } else {
                                S.T(SelectTwoPlayerActivity.this, "Please Select a One member");
                            }
                        } else if (Member_number.equals("3")) {
                            if (user_IdList.size() < 2) {
                                S.T(SelectTwoPlayerActivity.this, "Please Select a 2 member");
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString("OtherUserId", user_IdList.get(0));
                                bundle.putString("OtherUserName", user_nameList.get(0));
                                bundle.putString("OtherCity", user_CityList.get(0));
                                bundle.putString("OtherGender", OtherGender);
                                bundle.putString("OtherUserProfilePic", user_Profile_picList.get(0));

                                bundle.putString("OtherSecondUserId", user_IdList.get(1));
                                bundle.putString("OtherSecondUserName", user_nameList.get(1));
                                bundle.putString("OtherSecondCity", user_CityList.get(1));
                                bundle.putString("OtherSecondGender", OtherGender);
                                bundle.putString("OtherSecondUserProfilePic", user_Profile_picList.get(1));


                                bundle.putString("points", points);
                                bundle.putString("Member_number", Member_number);
                                bundle.putString("ActivityCheck", "Twoplayer");
                                S.I(SelectTwoPlayerActivity.this, QuizCategoryActivity.class, bundle);
                            }


                        } else if (Member_number.equals("4")) {
                            if (user_IdList.size() < 3) {
                                S.T(SelectTwoPlayerActivity.this, "Please Select a 3 member");
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString("OtherUserId", user_IdList.get(0));
                                bundle.putString("OtherUserName", user_nameList.get(0));
                                bundle.putString("OtherCity", user_CityList.get(0));
                                bundle.putString("OtherGender", OtherGender);
                                bundle.putString("OtherUserProfilePic", user_Profile_picList.get(0));

                                bundle.putString("OtherSecondUserId", user_IdList.get(1));
                                bundle.putString("OtherSecondUserName", user_nameList.get(1));
                                bundle.putString("OtherSecondCity", user_CityList.get(1));
                                bundle.putString("OtherSecondGender", OtherGender);
                                bundle.putString("OtherSecondUserProfilePic", user_Profile_picList.get(1));

                                bundle.putString("OtherThirdUserId", user_IdList.get(2));
                                bundle.putString("OtherThirdUserName", user_nameList.get(2));
                                bundle.putString("OtherThirdCity", user_CityList.get(2));
                                bundle.putString("OtherThirdGender", OtherGender);
                                bundle.putString("OtherThirdUserProfilePic", user_Profile_picList.get(2));

                                bundle.putString("points", points);
                                bundle.putString("Member_number", Member_number);
                                bundle.putString("ActivityCheck", "Twoplayer");
                                S.I(SelectTwoPlayerActivity.this, QuizCategoryActivity.class, bundle);
                            }
                        }
                    }else{
                        Toast.makeText(SelectTwoPlayerActivity.this,"Please Select User",Toast.LENGTH_LONG).show();
                    }  }
            });
            twoPlayerRv.setOnScrollChangeListener(null);
        }catch (Exception e){}
        //twoPlayerRv.setHasFixedSize(true);
        BroadcastReceiver broadcastReceiver= S.LocalBroadcastReciver(SelectTwoPlayerActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
        getUserList();
    }


    private void getUserList() {
        new JSONParser(SelectTwoPlayerActivity.this).parseVollyStringRequest(Const.URL.getWalletAmount, 1, getParmsans(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    twoPlayerRv.setVisibility(View.VISIBLE);
                    S.E("checkREsponse" + response);
                    S.E("checkParams" + getParmsans());
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            LeaderBoardModel leaderBoardModel = new LeaderBoardModel();
                            if (jsonObject1.getString("user_id").equals(UserDataHelper.getInstance().getList().get(0).getUserId())){}
                            else {
                             leaderBoardModel.setUser_id(jsonObject1.getString("user_id"));
                            leaderBoardModel.setUserProfilePic(jsonObject1.getString("profile_pic"));
                            leaderBoardModel.setUserName(jsonObject1.getString("user"));
                            leaderBoardModel.setRankingPoints(jsonObject1.getString("wallet"));
                            leaderBoardModel.setUserGender(jsonObject1.getString("userGender"));
                            leaderBoardModel.setUserCity(jsonObject1.getString("userCity"));
                            leaderBoardModel.setSelected(false);
                            leaderBoardModelArrayList.add(leaderBoardModel);
                            searchList.add(jsonObject1.getString("user_id"));

                                S.E("userGenderModelArrayList"+jsonObject1.getString("userGender"));
                            }


                        }
                        S.E("leaderBoardModelArrayList"+leaderBoardModelArrayList.size());

                         timeLineAdapter = new SelectTwoPlayerAdapter(SelectTwoPlayerActivity.this,leaderBoardModelArrayList,"",Member_number);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        twoPlayerRv.setLayoutManager(mLayoutManager);
                        twoPlayerRv.setItemAnimator(new DefaultItemAnimator());
                        twoPlayerRv.setAdapter(timeLineAdapter);
                        timeLineAdapter.notifyDataSetChanged();
                    } else {
                        S.T(SelectTwoPlayerActivity.this, "" + message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private Map<String, String> getParmsans() {
        HashMap<String, String> params = new HashMap<>();
        params.put("amount", points);
        return params;
    }

    public void  GetUserId(int position, String user_id, String UserName, String Profile_pic, String City, String Gender,String checkedUnChecked)
    {
        S.E("check UnChecked1::"+checkedUnChecked);
        if (checkedUnChecked.equals("Checked")){
            S.E("check UnChecked2::"+checkedUnChecked);
            if (user_IdList.size()<1){
                user_IdList.add(user_id);
                user_nameList.add(UserName);
                user_Profile_picList.add(Profile_pic);
                user_CityList.add(City);
                LeaderBoardModel leaderBoardModel=leaderBoardModelArrayList.get(position);
                leaderBoardModel.setSelected(true);
                leaderBoardModelArrayList.set(position,leaderBoardModel);}
            else {
                timeLineAdapter = new SelectTwoPlayerAdapter(SelectTwoPlayerActivity.this,leaderBoardModelArrayList,user_id,Member_number);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                twoPlayerRv.setLayoutManager(mLayoutManager);
                twoPlayerRv.setItemAnimator(new DefaultItemAnimator());
                twoPlayerRv.setAdapter(timeLineAdapter);
                timeLineAdapter.notifyDataSetChanged();
                S.T(SelectTwoPlayerActivity.this,"Please UnSelect a other user and then select !");
            }
        }else if(checkedUnChecked.equals("notChecked")){
            S.E("check UnChecked3::"+checkedUnChecked);
            user_IdList.remove(0);
            user_nameList.remove(0);
            user_Profile_picList.remove(0);
            user_CityList.remove(0);
            LeaderBoardModel leaderBoardModel=leaderBoardModelArrayList.get(position);
            leaderBoardModel.setSelected(false);
            leaderBoardModelArrayList.set(position,leaderBoardModel);

        }
    }
    public void ThreeGetUserId(String user_id,String UserName,String Profile_pic,String City,String Gender,String Check,int Position)
    {
       /* if (user_IdList.size()<2){*/
        if (Check.equals("Checked")){
            if (user_IdList.size()<2){
            user_IdList.add(user_id);
            user_nameList.add(UserName);
            user_Profile_picList.add(Profile_pic);
            user_CityList.add(City);
            LeaderBoardModel leaderBoardModel=leaderBoardModelArrayList.get(Position);
            leaderBoardModel.setSelected(true);
            leaderBoardModelArrayList.set(Position,leaderBoardModel);}
            else {
                timeLineAdapter = new SelectTwoPlayerAdapter(SelectTwoPlayerActivity.this,leaderBoardModelArrayList,user_id,Member_number);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                twoPlayerRv.setLayoutManager(mLayoutManager);
                twoPlayerRv.setItemAnimator(new DefaultItemAnimator());
                twoPlayerRv.setAdapter(timeLineAdapter);
                timeLineAdapter.notifyDataSetChanged();
                S.T(SelectTwoPlayerActivity.this,"Please UnSelect a other user and then select !");
            }
        }else if(Check.equals("notChecked")){
            user_IdList.remove(user_id);
            user_nameList.remove(UserName);
            user_Profile_picList.remove(Profile_pic);
            user_CityList.remove(City);
            LeaderBoardModel leaderBoardModel=leaderBoardModelArrayList.get(Position);
            leaderBoardModel.setSelected(false);
            leaderBoardModelArrayList.set(Position,leaderBoardModel);

        }


    }
    public void GetFourUser_id(String user_id,String UserName,String Profile_pic,String City,String Gender,String Check,int Position){
        if (Check.equals("Checked")){
            if (user_IdList.size()<3){
                user_IdList.add(user_id);
                user_nameList.add(UserName);
                user_Profile_picList.add(Profile_pic);
                user_CityList.add(City);
                LeaderBoardModel leaderBoardModel=leaderBoardModelArrayList.get(Position);
                leaderBoardModel.setSelected(true);
                leaderBoardModelArrayList.set(Position,leaderBoardModel);}
            else {
                timeLineAdapter = new SelectTwoPlayerAdapter(SelectTwoPlayerActivity.this,leaderBoardModelArrayList,user_id,Member_number);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                twoPlayerRv.setLayoutManager(mLayoutManager);
                twoPlayerRv.setItemAnimator(new DefaultItemAnimator());
                twoPlayerRv.setAdapter(timeLineAdapter);
                timeLineAdapter.notifyDataSetChanged();
                S.T(SelectTwoPlayerActivity.this,"Please UnSelect a other user and then select !");
            }
        }else if(Check.equals("notChecked")){
            user_IdList.remove(user_id);
            user_nameList.remove(UserName);
            user_Profile_picList.remove(Profile_pic);
            user_CityList.remove(City);
            LeaderBoardModel leaderBoardModel=leaderBoardModelArrayList.get(Position);
            leaderBoardModel.setSelected(false);
            leaderBoardModelArrayList.set(Position,leaderBoardModel);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView =(SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(SelectTwoPlayerActivity.this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setToolbarWithBackButton("Two Player");
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(myActionMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                timeLineAdapter.setFilter(leaderBoardModelArrayList);
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
        timeLineAdapter.setFilter(filteredModelList);
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
        S.I(SelectTwoPlayerActivity.this,QuizMainActivity.class,null);
        super.onBackPressed();
    }


}

