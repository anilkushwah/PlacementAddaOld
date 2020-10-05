package com.dollop.placementadda.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.database.model.QuestionaryModel;
import com.dollop.placementadda.model.SelectedListModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ShowResultStaticActivity extends BaseActivity implements OnChartValueSelectedListener, Thread.UncaughtExceptionHandler {

    Toolbar toolBar;
    PieChart piechart;
    CardView cdCheckAnsSheetId;
    ImageView categoryLogoIv;
    TextView categryNameTv;
    CardView cdSimilarId;
    CardView cdLeaderBoardId;
    private ArrayList<QuestionaryModel> questionaryList;
    private float correctAns = 0;
    private float notAttemp = 0;
    private float incorrectAns = 0;

    @Override
    protected int getContentResId() {
        return R.layout.activity_show_result_static;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(ShowResultStaticActivity.this);
        toolbar = findViewById(R.id.tool_bar);
        piechart = findViewById(R.id.piechart);
        cdCheckAnsSheetId = findViewById(R.id.cdCheckAnsSheetId);
        categoryLogoIv = findViewById(R.id.categoryLogoIv);
        categryNameTv = findViewById(R.id.categryNameTv);
        cdSimilarId = findViewById(R.id.cdSimilarId);
        cdLeaderBoardId = findViewById(R.id.cdLeaderBoardId);
        setToolbarWithBackButton("Statics");
        SelectedListModel selectedListModel = (SelectedListModel) getIntent().getSerializableExtra("key_list");
        questionaryList = selectedListModel.getQuestionaryModels();


        cdCheckAnsSheetId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                /* bundle.putString("quiz_id", getIntent().getStringExtra("quiz_id"));*/
                bundle.putSerializable("key_list", (SelectedListModel) getIntent().getSerializableExtra("key_list"));
                S.I(ShowResultStaticActivity.this, AnswerSheetActivity.class, bundle);


            }
        });


        cdLeaderBoardId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("quiz_id", getIntent().getStringExtra("quiz_id"));
                S.I(ShowResultStaticActivity.this, ResultLeaderBoardActivity.class, bundle);

            }
        });

        cdSimilarId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                bundle.putSerializable("key_list", (SelectedListModel) getIntent().getSerializableExtra("key_list"));
                S.I(ShowResultStaticActivity.this, SimilarTestActivity.class, bundle);

            }
        });
        PieChart pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);

        for (int i = 0; i < questionaryList.size(); i++) {

            if (questionaryList.get(i).getMyAns().equals("notAttempt")) {

                ++notAttemp;
            } else if (questionaryList.get(i).getMyAns().equals(questionaryList.get(i).getAns())) {

                ++correctAns;
            } else {

                ++incorrectAns;
            }


        }

if(notAttemp==questionaryList.size()){
    ArrayList<Entry> yvalues = new ArrayList<Entry>();

    yvalues.add(new Entry(notAttemp, 2));


    PieDataSet dataSet = new PieDataSet(yvalues, "");

    ArrayList<String> xVals = new ArrayList<String>();


    xVals.add("Not Attempt");

    PieData data = new PieData(xVals, dataSet);
    data.setValueFormatter(new PercentFormatter());
    pieChart.setData(data);
    pieChart.setDescription("Your result");

    pieChart.setDrawHoleEnabled(true);
    pieChart.setTransparentCircleRadius(25f);
    pieChart.setHoleRadius(25f);

    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
    data.setValueTextSize(13f);
    data.setValueTextColor(Color.WHITE);
    pieChart.setOnChartValueSelectedListener(this);

    pieChart.animateXY(1400, 1400);


}else {
    ArrayList<Entry> yvalues = new ArrayList<Entry>();
    yvalues.add(new Entry(correctAns, 0));
    yvalues.add(new Entry(incorrectAns, 1));
    yvalues.add(new Entry(notAttemp, 2));


    PieDataSet dataSet = new PieDataSet(yvalues, "");

    ArrayList<String> xVals = new ArrayList<String>();

    xVals.add("Correct");
    xVals.add("Incorrect");
    xVals.add("Not Attempt");

    PieData data = new PieData(xVals, dataSet);
    data.setValueFormatter(new PercentFormatter());
    pieChart.setData(data);
    pieChart.setDescription("Your result");

    pieChart.setDrawHoleEnabled(true);
    pieChart.setTransparentCircleRadius(25f);
    pieChart.setHoleRadius(25f);

    dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
    data.setValueTextSize(13f);
    data.setValueTextColor(Color.DKGRAY);
    pieChart.setOnChartValueSelectedListener(this);

    pieChart.animateXY(1400, 1400);
}

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(ShowResultStaticActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
 LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.QuizResult));

        if (com.dollop.placementadda.sohel.SavedData.getNotificationRequestForGameStart().equals("true")) {
            S.E("checkIts  Come");

                try {
                    JSONObject jsonObject = new JSONObject(com.dollop.placementadda.sohel.SavedData.getNotificationJson());
                    JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                    userProfilePic = jsonObject1.getString("userProfilePic");
                    userName = jsonObject1.getString("userName");

                    Log.e("receiver", "Got message: " + com.dollop.placementadda.sohel.SavedData.getNotificationJson());

                    final Dialog dialog = new Dialog(ShowResultStaticActivity.this);
                    dialog.setContentView(R.layout.winner_popup);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCanceledOnTouchOutside(false);
                    CircleImageView comment_user_circullerimage;
                    ImageView cancel_img, full_image;
                    Button continue_btn;
                    TextView CommentUserName_tv;
                    /* GifView gifView1 = (GifView) dialog.findViewById(R.id.gif1);*/
                    comment_user_circullerimage = (CircleImageView) dialog.findViewById(R.id.comment_user_circullerimage);
                    CommentUserName_tv = (TextView) dialog.findViewById(R.id.CommentUserName_tv);
                    continue_btn = (Button) dialog.findViewById(R.id.continue_btn);
                    CommentUserName_tv.setText(userName);

                    if (!userProfilePic.equals("") && !userProfilePic.equals("null")) {
                        Picasso.with(ShowResultStaticActivity.this).
                                load(Const.URL.IMAGE_URL + userProfilePic)
                                .into(comment_user_circullerimage);

                    }
                    continue_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                          // S.I_clear(ShowResultStaticActivity.this, QuizMainActivity.class, null);

                        }
                    });


                    dialog.show();

            } catch (Exception e) {
                S.E("checkNotification2 Exception" + e);
            }
            SavedData.saveNotificationRequestGameStart("false");
        } else {

            S.E("checkIts Not Come");
        }
    }

    private String userProfilePic = "";
    private String userName = "";
    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            S.E("message recicver is working" + intent.getStringExtra("message"));
            try {
                JSONObject jsonObject = new JSONObject(intent.getStringExtra("message"));
                JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                userProfilePic = jsonObject1.getString("userProfilePic");
                userName = jsonObject1.getString("userName");

                Log.e("receiver", "Got message: ");


                final Dialog dialog = new Dialog(ShowResultStaticActivity.this);
                dialog.setContentView(R.layout.winner_popup);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCanceledOnTouchOutside(false);
                CircleImageView comment_user_circullerimage;
                ImageView cancel_img, full_image;
                Button continue_btn;
                TextView CommentUserName_tv;

                comment_user_circullerimage = (CircleImageView) dialog.findViewById(R.id.comment_user_circullerimage);
                CommentUserName_tv = (TextView) dialog.findViewById(R.id.CommentUserName_tv);

                continue_btn = (Button) dialog.findViewById(R.id.continue_btn);
                CommentUserName_tv.setText(userName);


                if (!userProfilePic.equals("") && !userProfilePic.equals("null")) {
                    Picasso.with(ShowResultStaticActivity.this).
                            load(Const.URL.IMAGE_URL + userProfilePic)
                            .into(comment_user_circullerimage);

                }
                continue_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                      //  S.I_clear(ShowResultStaticActivity.this, QuizMainActivity.class, null);

                    }
                });


                dialog.show();
            } catch (Exception e) {
            }
        }
    };


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void finish() {
        S.I_clear(ShowResultStaticActivity.this, QuizMainActivity.class, null);
    }

    @Override
    public void uncaughtException(Thread t, Throwable ex) {
        if (ex.getClass().equals(OutOfMemoryError.class)) {
            try {
                android.os.Debug.dumpHprofData("/sdcard/dump.hprof");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ex.printStackTrace();
    }

}

