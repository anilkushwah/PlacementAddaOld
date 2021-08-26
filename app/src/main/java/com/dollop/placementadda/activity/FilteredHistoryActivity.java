package com.dollop.placementadda.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.FilteredHistoryAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.FilteredHistory;
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

public class FilteredHistoryActivity extends BaseActivity {
    private List<FilteredHistory> filteredHistoryArrayList = new ArrayList<>();
    RecyclerView filteredHistryRv;
    FilteredHistoryAdapter filteredHistoryAdapter;

    @Override
    protected int getContentResId() {
        return R.layout.activity_filtered_history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("History");
        filteredHistryRv = findViewById(R.id.filteredHistryRv);
        try {
            filteredHistoryAdapter = new FilteredHistoryAdapter(FilteredHistoryActivity.this, filteredHistoryArrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            filteredHistryRv.setLayoutManager(mLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        filteredHistryRv.setItemAnimator(new DefaultItemAnimator());
        filteredHistryRv.setAdapter(filteredHistoryAdapter);
        testHistoryDate();
    }

    private void testHistoryDate() {
        new JSONParser(FilteredHistoryActivity.this).parseVollyStringRequest(Const.URL.getUserPlayedTest, 1, getParmsans(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    S.E("checkHistoryList::"+response);
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");


                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            FilteredHistory filteredHistory = new FilteredHistory();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            filteredHistory.setTopic_name(jsonObject1.getString("topic_name"));
                            filteredHistory.setTest_date(jsonObject1.getString("test_date"));
                            filteredHistory.setTest_time(jsonObject1.getString("test_time"));
                            filteredHistory.setCategory_image(jsonObject1.getString("category_image"));
                            filteredHistory.setSubject_name(jsonObject1.getString("subject_name"));
                            filteredHistory.setCategory_name(jsonObject1.getString("category_name"));
                            filteredHistory.setTopic_id(jsonObject1.getString("topic_id"));
                            filteredHistory.setSubject_id(jsonObject1.getString("subject_id"));
                            filteredHistory.setCategory_id(jsonObject1.getString("category_id"));
                            filteredHistoryArrayList.add(filteredHistory);
                        }
                        filteredHistoryAdapter = new FilteredHistoryAdapter(FilteredHistoryActivity.this, filteredHistoryArrayList);
                        filteredHistryRv.setAdapter(filteredHistoryAdapter);
                        filteredHistoryAdapter.notifyDataSetChanged();
                    } else {
                        S.T(FilteredHistoryActivity.this, "" + message);
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
