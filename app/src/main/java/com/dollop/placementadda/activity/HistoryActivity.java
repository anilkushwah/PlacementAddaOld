package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryActivity extends BaseActivity {
    ArrayList<BarEntry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<String>();
    private BarDataSet bardataset;
    private BarChart barChart;

    @Override
    protected int getContentResId() {
        return R.layout.activity_history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(HistoryActivity.this);
        setToolbarWithBackButton("History Of Your Test");
        barChart = (BarChart) findViewById(R.id.barchart);
        bardataset = new BarDataSet(entries, "Cells");
        getHistoryOfAllTest();
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(HistoryActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }

    private void getHistoryOfAllTest() {
        new JSONParser(HistoryActivity.this).parseVollyStringRequest(Const.URL.MyHigestPointInTopic, 1, getParms(), new Helper() {

            @Override
            public void backResponse(String response) {

                try {
                    S.E("cheakparamhistory"+response);
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            float v = Float.parseFloat(jsonObject1.getString("maxMark"));
                            entries.add(new BarEntry(v, i));
                            labels.add(jsonObject1.getString("categoryName"));
                        }
                    } else {
                        S.T(HistoryActivity.this, "" + message);
                    }
                    BarData data = new BarData(labels, bardataset);
                    barChart.setData(data); // set the data and list of lables into chart
                    barChart.setDescription("Set Bar Chart Description");  // set the description
                    bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                    barChart.animateY(5000);
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

