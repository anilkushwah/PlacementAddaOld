package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.QuizAnswerAdapter;
import com.dollop.placementadda.model.SelectedListModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.S;

import me.relex.circleindicator.CircleIndicator;

//create by Aniruddha

public class AnswerSheetActivity extends BaseActivity {
    Toolbar toolBar;
    TextView totalTimeId;
    TextView tvTotalQueId;
    TextView tvRunningId;
    TextView tvRunningQuestionAttempId;
    CardView cardId;
    ViewPager pager;
    CircleIndicator indicator;
    CardView liveQuizeId;
    ImageView ivBackId;
    RecyclerView rvPositionId;
    ImageView ivNextId;
    private ViewPager Pager;

    @Override
    protected int getContentResId() {
        return R.layout.activity_answer_sheet;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(AnswerSheetActivity.this);
        toolbar = findViewById(R.id.tool_bar);
        totalTimeId = findViewById(R.id.totalTimeId);
        tvTotalQueId = findViewById(R.id.tvTotalQueId);
        tvRunningId = findViewById(R.id.tvRunningId);
        tvRunningQuestionAttempId = findViewById(R.id.tvRunningQuestionAttempId);
        cardId = findViewById(R.id.cardId);
        pager = findViewById(R.id.pager);
        indicator = findViewById(R.id.indicator);
        liveQuizeId = findViewById(R.id.liveQuizeId);
        ivBackId = findViewById(R.id.ivBackId);
        rvPositionId = findViewById(R.id.rvPositionId);
        ivNextId = findViewById(R.id.ivNextId);

        final SelectedListModel selectedListModel = (SelectedListModel) getIntent().getSerializableExtra("key_list");
        //Selection List model get from intent its come from questionary activity we have all question and correct ans their

        setToolbarWithBackButton("Answer Sheet");
        Pager = (ViewPager) findViewById(R.id.pager);
        //Pager is use for switching question one by one
        Pager.setAdapter(new QuizAnswerAdapter(AnswerSheetActivity.this, selectedListModel.getQuestionaryModels()));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        //Circular indicator switching on page switching notify by by this pager's method
        indicator.setViewPager(Pager);

        Pager.setOffscreenPageLimit(selectedListModel.getQuestionaryModels().size());

        ivBackId.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (pager.getCurrentItem() != 0) {   //back to page if we are not on first page
                                                pager.setCurrentItem(pager.getCurrentItem() - 1);
                                            }
                                        }
                                    }
        );
        //Switch questions to backword from currnet
        ivNextId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pager.getCurrentItem() != selectedListModel.getQuestionaryModels().size()) {//next to page if we are not on last page
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
            }
        });
        //setContentView();
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(AnswerSheetActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }
}
