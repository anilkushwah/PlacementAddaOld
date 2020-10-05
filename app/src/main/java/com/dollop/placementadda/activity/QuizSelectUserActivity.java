package com.dollop.placementadda.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.QuizSelectUserAdapter;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.S;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class QuizSelectUserActivity extends BaseActivity {
    ArrayList<String> setDataList = new ArrayList<String>();
    ArrayList<String> getDataList = new ArrayList<String>();
    TextView selectUserNamefirst;
    TextView selectUserNameSecond;
    TextView selectUserNameThird;
    TextView selectUserNameFourth;
    TextView selectUserNamefifth;
    Button playQuizBtn;
    RecyclerView quizSelectUserRv;

    @Override
    protected int getContentResId() {
        return R.layout.activity_quiz_select_user;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(QuizSelectUserActivity.this);
        setToolbarWithBackButton("Selected User");
        toolbar=findViewById(R.id.tool_bar);
        selectUserNamefirst=findViewById(R.id.selectUserNamefirst);
        selectUserNameSecond=findViewById(R.id.selectUserNameSecond);
        selectUserNameThird=findViewById(R.id.selectUserNameThird);
        selectUserNameFourth=findViewById(R.id.selectUserNameFourth);
        selectUserNamefifth=findViewById(R.id.selectUserNamefifth);
        playQuizBtn=findViewById(R.id.playQuizBtn);
        quizSelectUserRv=findViewById(R.id.quizSelectUserRv);

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        getDataList  = bundle.getStringArrayList("datas");
        String ids = bundle.getString("ids");
        setDataList.add(String.valueOf(getDataList));
        setDataList.size();
        playQuizBtn = (Button) findViewById(R.id.playQuizBtn);

        playQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        BroadcastReceiver broadcastReceiver= S.LocalBroadcastReciver(QuizSelectUserActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }
}
