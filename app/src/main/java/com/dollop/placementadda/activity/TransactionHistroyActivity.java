package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.Transction_Adapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.TransctionModel;
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

public class TransactionHistroyActivity extends BaseActivity {
    ArrayList<TransctionModel> transctionModelslist = new ArrayList<>();
    RecyclerView transaction_recycleview;
    TextView nodatafound_tv;

    @Override
    protected int getContentResId() {
        return R.layout.activity_transaction_histroy;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(TransactionHistroyActivity.this);
        setToolbarWithBackButton("Transction History");
        transaction_recycleview = (RecyclerView) findViewById(R.id.transaction_recycleview);
        nodatafound_tv = (TextView) findViewById(R.id.nodatafound_tv);
        GetTransction_history();
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(TransactionHistroyActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));

    }

    private void GetTransction_history() {
        new JSONParser(TransactionHistroyActivity.this).parseVollyStringRequest(Const.URL.walletHistory, 1, getParmsans(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {

                    S.E("GetTransction_historyREsponse" + response);
                    S.E("GetTransction_historyParams" + getParmsans());
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        String dateChangeCheck = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            TransctionModel leaderBoardModel = new TransctionModel();
                            leaderBoardModel.setTransction_id(jsonObject1.getString("status"));
                            leaderBoardModel.setTransction_date(jsonObject1.getString("created_date"));
                            leaderBoardModel.setTransction_type(jsonObject1.getString("title"));
                            leaderBoardModel.setTransction_amount(jsonObject1.getString("amount"));
                            String[] dateTransaction = jsonObject1.getString("created_date").split(" ");
                            String dateInString = dateTransaction[0];

                            S.E("checkDate In "+dateChangeCheck);
                            S.E("checkDate dateInString In "+dateInString);
                            if(dateChangeCheck.equals(dateInString)){
                                leaderBoardModel.setDateChangeCheck("SameDate");


                            }else{
                                dateChangeCheck=dateInString;
                                leaderBoardModel.setDateChangeCheck(dateInString);

                            }

                            transctionModelslist.add(leaderBoardModel);

                        }
                        S.E("leaderBoardModelArrayList" + transctionModelslist.size());
                        if (transctionModelslist.size() > 0) {
                            transaction_recycleview.setVisibility(View.VISIBLE);
                            nodatafound_tv.setVisibility(View.GONE);
                            Transction_Adapter timeLineAdapter = new Transction_Adapter(TransactionHistroyActivity.this, transctionModelslist);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            transaction_recycleview.setLayoutManager(mLayoutManager);
                            transaction_recycleview.setItemAnimator(new DefaultItemAnimator());
                            transaction_recycleview.setAdapter(timeLineAdapter);
                            timeLineAdapter.notifyDataSetChanged();
                        } else {
                            transaction_recycleview.setVisibility(View.GONE);
                            nodatafound_tv.setVisibility(View.VISIBLE);
                        }
                    } else {
                        S.T(TransactionHistroyActivity.this, "" + message);
                        transaction_recycleview.setVisibility(View.GONE);
                        nodatafound_tv.setVisibility(View.VISIBLE);
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
        return params;
    }
}
