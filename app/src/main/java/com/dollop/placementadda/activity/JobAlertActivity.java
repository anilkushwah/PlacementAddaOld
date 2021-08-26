package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.JobAlertAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.JobAlertModel;
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

public class JobAlertActivity extends BaseActivity {
    RecyclerView job_alert_recycleview;
    ArrayList<JobAlertModel> jobAlertModels = new ArrayList<>();
    private String message;

    @Override
    protected int getContentResId() {
        return R.layout.activity_job_alert;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(JobAlertActivity.this);
        setToolbarWithBackButton("Job Alert");
        job_alert_recycleview = (RecyclerView) findViewById(R.id.job_alert_recycleview);
        JobAlertList();
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(JobAlertActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }

    private void JobAlertList() {
        new JSONParser(JobAlertActivity.this).parseVollyStringRequest(Const.URL.getJobAlertList, 1, getParms(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    S.E("checkJobAlertHere::"+response);
                    S.E("checkJobAlertPost::"+getParms());
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    message = mainobject.getString("message");
                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        jobAlertModels.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JobAlertModel newBatchesModel = new JobAlertModel();
                            newBatchesModel.setId(jsonObject1.getString("id"));
                            newBatchesModel.setJob_title(jsonObject1.getString("job_title"));
                            newBatchesModel.setJob_type(jsonObject1.getString("job_type"));
                            newBatchesModel.setExperience(jsonObject1.getString("experience"));
                            newBatchesModel.setJob_location(jsonObject1.getString("job_location"));
                            newBatchesModel.setDescription(jsonObject1.getString("description"));
                            newBatchesModel.setEducation(jsonObject1.getString("education"));
                            newBatchesModel.setContact_detail(jsonObject1.getString("contact_detail"));
                            newBatchesModel.setIsActive(jsonObject1.getString("isActive"));
                            newBatchesModel.setCompany_logo(jsonObject1.getString("company_logo"));
                            newBatchesModel.setCompany_name(jsonObject1.getString("company_name"));
                            newBatchesModel.setSkill_required(jsonObject1.getString("skill_required"));
                            newBatchesModel.setCreated_date(jsonObject1.getString("created_date"));
                            newBatchesModel.setImage(jsonObject1.getString("image"));
                            jobAlertModels.add(newBatchesModel);
                        }
                        if (jobAlertModels.size() > 0) {
                            JobAlertAdapter mAdapter = new JobAlertAdapter(JobAlertActivity.this, jobAlertModels);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            job_alert_recycleview.setLayoutManager(mLayoutManager);
                            job_alert_recycleview.setItemAnimator(new DefaultItemAnimator());
                            job_alert_recycleview.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }

                    } else {
                        S.T(JobAlertActivity.this, "There are no jobs here..");
                    }


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
