package com.dollop.placementadda.activity.basic;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ImageView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.S;
import com.squareup.picasso.Picasso;


public class ImagePreviewActivity2 extends BaseActivity {
    ImageView ivPreviewId;
    public String userProfilePic;

    @Override
    protected int getContentResId() {
        return R.layout.activity_image_preview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //checkNetworkAvailability(ImagePreviewActivity2.this);
        setToolbarWithBackButton("Full Preview");
        ivPreviewId=findViewById(R.id.ivPreviewId);
        userProfilePic = getIntent().getStringExtra("userProfilePic2");
        S.E("checkIts come OR not"+userProfilePic);
            Picasso.with(this).load( userProfilePic).error(R.drawable.ic_user).into(ivPreviewId);
            BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(ImagePreviewActivity2.this);
            LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                    new IntentFilter(Config.QuizRequest));

    }
}
