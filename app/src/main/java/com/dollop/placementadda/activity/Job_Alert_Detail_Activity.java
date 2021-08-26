package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.model.JobAlertModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.S;

public class Job_Alert_Detail_Activity extends BaseActivity {
    TextView job_Title_tv, joblocation, job_type, description_tv, workexpriance_tv, education_tv, contactdetail_tv, View_btn;

    @Override

    protected int getContentResId() {
        return R.layout.activity_job__alert__detail_;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(Job_Alert_Detail_Activity.this);
        setToolbarWithBackButton("Job Detail");
        job_Title_tv = (TextView) findViewById(R.id.job_Title_tv);
        joblocation = (TextView) findViewById(R.id.joblocation);
        job_type = (TextView) findViewById(R.id.job_type);
        description_tv = (TextView) findViewById(R.id.description_tv);
        workexpriance_tv = (TextView) findViewById(R.id.workexpriance_tv);
        education_tv = (TextView) findViewById(R.id.education_tv);
        contactdetail_tv = (TextView) findViewById(R.id.contactdetail_tv);
        View_btn=(TextView)findViewById(R.id.View_btn);
        Bundle b = getIntent().getExtras();
        final JobAlertModel studentModel = (JobAlertModel) b.getSerializable("AllData");

        job_Title_tv.setText(studentModel.getJob_title());
        joblocation.setText(studentModel.getJob_location());
        job_type.setText(studentModel.getJob_type());
        description_tv.setText(studentModel.getDescription());
        workexpriance_tv.setText(studentModel.getExperience());
        education_tv.setText(studentModel.getEducation());
        contactdetail_tv.setText(studentModel.getContact_detail());

        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(Job_Alert_Detail_Activity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }
}
