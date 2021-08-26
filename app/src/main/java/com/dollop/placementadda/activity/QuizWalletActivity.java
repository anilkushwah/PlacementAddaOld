package com.dollop.placementadda.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cunoraz.gifview.library.GifView;
import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.TimeLineCommentModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;

import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class QuizWalletActivity extends BaseActivity   {
    Toolbar toolBar;
    ImageView freeVideoBtn;
    ImageView sharedBtn;
    ImageView moreCoinsBtn;
    TextView totalBalance;
    private RewardedAd mRewardedAd;
    boolean flag=false;
    LinearLayout watchVideoLayout;
    ProgressDialog progressDialog;
    private final static int REQUEST_READ_PHONE_STATE = 1;
    private double coins=10.00;

    @Override
    protected int getContentResId() {
        return R.layout.activity_quiz_wallet;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(QuizWalletActivity.this);
        setToolbarWithBackButton("Coins");
        sharedBtn = (ImageView) findViewById(R.id.sharedBtn);
        freeVideoBtn = (ImageView) findViewById(R.id.freeVideoBtn);
        moreCoinsBtn = (ImageView) findViewById(R.id.moreCoinsBtn);
        totalBalance = (TextView) findViewById(R.id.totalBalance);
        watchVideoLayout=findViewById(R.id.watchVideoLayout);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-3496220705060137/5157183364",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("TAG", loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        progressDialog.dismiss();
                        mRewardedAd = rewardedAd;
                        if(flag){
                            mRewardedAd.show(QuizWalletActivity.this, new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    flag=false;
                                    AddCoin("10", "Watch a video");
                                }
                            });
                        }
                        Log.d("TAG", "Ad was loaded.");
                    }
                });


        String abc = SavedData.getCoins();

        getWalletAmount();
        //getAddAmountInWallet();
        SavedData.saveTotalCoins(abc);

        Animation ani = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        ani.setRepeatCount(Animation.INFINITE);
        moreCoinsBtn.startAnimation(ani);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        sharedBtn.startAnimation(anim);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        freeVideoBtn.startAnimation(animation);

        watchVideoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (hasGetReadPhoneStatePermission()) {
//                    progressDialog.setMessage("Loading..."); // Setting Message
//                    progressDialog.setTitle("Video loading"); // Setting Title
//                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//                    progressDialog.show(); // Display Progress Dialog
//                    progressDialog.setCancelable(false);
                    loadRewardedVideoAd();
//                  } else {
//                    requestReadPhoneStatePermission();
//                }
            }
        });
        sharedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        moreCoinsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizWalletActivity.this, MoreCoinsActivity.class);
                startActivity(intent);
            }
        });

        /*MobileAds.initialize(this, "ca-app-pub-9640495884863409~1242406785");
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("02C53424006E9E58B04EA68DD69C2521").build();
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        freeVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
            }
        });*/
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(QuizWalletActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }
    private boolean hasGetReadPhoneStatePermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadPhoneStatePermission() {
        //You can choose a more friendly notice text. And you can choose any view you like, such as dialog.
        //Toast.makeText(this, "Only grant the permission, can you start the mission!", Toast.LENGTH_SHORT).show();
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
    }

    private void loadRewardedVideoAd() {
        flag=true;
        if (mRewardedAd == null) {
            progressDialog.setMessage("Loading..."); // Setting Message
            progressDialog.setTitle("Video loading"); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        } else
            mRewardedAd.show(this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.

                    AddCoin("10", "Watch a video");
                }
            });
    }


    private void getWalletAmount() {
        new JSONParser(QuizWalletActivity.this).parseVollyStringRequest(Const.URL.GET_WALLET, 1, getParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200 && message.equals("success")) {
                        JSONObject data = mainobject.getJSONObject("data");
                        String amount = data.getString("wallet_amount");
                        totalBalance.setText(amount);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());

        return params;
    }

    private void getAddAmountInWallet() {
        new JSONParser(QuizWalletActivity.this).parseVollyStringRequest(Const.URL.ADD_MONEY_WALLET, 1, getParam(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                      JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200 && message.equals("success")) {
                        JSONObject data = mainobject.getJSONObject("data");
                        data.getString("result");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private Map<String, String> getParam() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        params.put("money", SavedData.getCoins());
        return params;
    }

    private void AddCoin(final String amount, final String Status) {
        new JSONParser(QuizWalletActivity.this).parseVollyStringRequest(Const.URL.addWalletAmount, 1, getCommentParams(amount, Status), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    ArrayList<TimeLineCommentModel> timeLineCommentModelslist = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    S.E("get Amount after ads"+response);
                    S.E("add amount after ads param"+getCommentParams(amount,Status));
                    if (jsonObject.getString("message").equals("success")) {
                        getWalletAmount();
                        ConfirmPopup();
                    } else {
                        S.T(QuizWalletActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                }
            }
        });
    }

    protected Map<String, String> getCommentParams(String amount, String Status) {
        Map<String, String> param = new HashMap<>();
        param.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        param.put("amount",""+ coins);
        param.put("for", Status);
        return param;
    }

    public void ConfirmPopup() {
        final Dialog dialog = new Dialog(QuizWalletActivity.this);
        dialog.setContentView(R.layout.paymentsucess_popup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        ImageView cancel_img, full_image;
        Button continue_btn;
        TextView textview;
        GifView gifView1 = (GifView) dialog.findViewById(R.id.gif1);
        cancel_img = (ImageView) dialog.findViewById(R.id.cancel_img);
        continue_btn = (Button) dialog.findViewById(R.id.continue_btn);
        textview = (TextView) dialog.findViewById(R.id.textview);
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
