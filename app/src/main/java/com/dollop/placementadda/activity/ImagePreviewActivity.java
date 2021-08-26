package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.widget.ImageView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.S;
import com.squareup.picasso.Picasso;

public class ImagePreviewActivity extends BaseActivity {
    ImageView ivPreviewId;
    public String userProfilePic;

    @Override
    protected int getContentResId() {
        return R.layout.activity_image_preview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(ImagePreviewActivity.this);
        setToolbarWithBackButton("Full Preview");
        ivPreviewId=findViewById(R.id.ivPreviewId);
        userProfilePic = getIntent().getStringExtra("userProfilePic");
        Picasso.with(this).load(Const.URL.IMAGE_URL + userProfilePic).error(R.drawable.ic_user).into(ivPreviewId);
        BroadcastReceiver broadcastReceiver= S.LocalBroadcastReciver(ImagePreviewActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }
}
