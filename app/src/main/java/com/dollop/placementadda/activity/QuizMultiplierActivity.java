
package com.dollop.placementadda.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.activity.category.QuizCategoryActivity;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.S;

public class QuizMultiplierActivity extends BaseActivity {
    CardView fourPlayerCardview, twoPlayerCardview;

    @Override
    protected int getContentResId() {
        return R.layout.activity_quiz_multiplier;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(QuizMultiplierActivity.this);
        setToolbarWithBackButton("Multiplier Quiz");
        twoPlayerCardview = (CardView) findViewById(R.id.twoPlayerCardview);
        fourPlayerCardview = (CardView) findViewById(R.id.fourPlayerCardview);

        twoPlayerCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCoinsDialogBox("2");
            }
        });
        fourPlayerCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCoinsDialogBox("4");     }
        });
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(QuizMultiplierActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }

    public void getCoinsDialogBox(final String Member_number) {
        final Dialog dialog = new Dialog(QuizMultiplierActivity.this);
        dialog.setContentView(R.layout.challege_table_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView cancel_btn = (ImageView) dialog.findViewById(R.id.cancel_img);
        LinearLayout play25 = (LinearLayout) dialog.findViewById(R.id.play25);
        LinearLayout play50 = (LinearLayout) dialog.findViewById(R.id.play50);
        LinearLayout play100 = (LinearLayout) dialog.findViewById(R.id.play100);
        LinearLayout play200 = (LinearLayout) dialog.findViewById(R.id.play200);
        LinearLayout play400 = (LinearLayout) dialog.findViewById(R.id.play400);
        LinearLayout play500 = (LinearLayout) dialog.findViewById(R.id.play500);
        LinearLayout play1000 = (LinearLayout) dialog.findViewById(R.id.play1000);
        LinearLayout play2500 = (LinearLayout) dialog.findViewById(R.id.play2500);
        LinearLayout play5000 = (LinearLayout) dialog.findViewById(R.id.play5000);
        play25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("Member_number", Member_number);
                bundle.putString("points", "25");
                bundle.putString("ActivityCheck", "QuizMultiplier");
                S.I(QuizMultiplierActivity.this, QuizCategoryActivity.class, bundle);
                dialog.dismiss();

            }
        });
        play50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("points", "50");
                bundle.putString("Member_number", Member_number);
                bundle.putString("ActivityCheck", "QuizMultiplier");
                S.I(QuizMultiplierActivity.this, QuizCategoryActivity.class, bundle);
                dialog.dismiss();
            }
        });
        play100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("points", "100");
                bundle.putString("Member_number", Member_number);
                bundle.putString("ActivityCheck", "QuizMultiplier");
                S.I(QuizMultiplierActivity.this, QuizCategoryActivity.class, bundle);
                dialog.dismiss();
            }
        });
        play200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("points", "200");
                bundle.putString("Member_number", Member_number);
                bundle.putString("ActivityCheck", "QuizMultiplier");
                S.I(QuizMultiplierActivity.this, QuizCategoryActivity.class, bundle);
                dialog.dismiss();
            }
        });
        play400.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("points", "400");
                bundle.putString("Member_number", Member_number);
                bundle.putString("ActivityCheck", "QuizMultiplier");
                S.I(QuizMultiplierActivity.this, QuizCategoryActivity.class, bundle);
                dialog.dismiss();
            }
        });
        play500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("points", "500");
                bundle.putString("Member_number", Member_number);
                bundle.putString("ActivityCheck", "QuizMultiplier");
                S.I(QuizMultiplierActivity.this, QuizCategoryActivity.class, bundle);
                dialog.dismiss();
            }
        });
        play1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("points", "1000");
                bundle.putString("Member_number", Member_number);
                bundle.putString("ActivityCheck", "QuizMultiplier");
                S.I(QuizMultiplierActivity.this, QuizCategoryActivity.class, bundle);
                dialog.dismiss();
            }
        });
        play2500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("points", "2500");
                bundle.putString("Member_number", Member_number);
                bundle.putString("ActivityCheck", "QuizMultiplier");
                S.I(QuizMultiplierActivity.this, QuizCategoryActivity.class, bundle);
                dialog.dismiss();
            }
        });
        play5000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("points", "5000");
                bundle.putString("Member_number", Member_number);
                bundle.putString("ActivityCheck", "QuizMultiplier");
                S.I(QuizMultiplierActivity.this, QuizCategoryActivity.class, bundle);
                dialog.dismiss();
            }
        });


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
