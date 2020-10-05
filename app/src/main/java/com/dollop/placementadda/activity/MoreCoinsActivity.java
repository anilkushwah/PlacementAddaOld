package com.dollop.placementadda.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cunoraz.gifview.library.GifView;
import com.dollop.placementadda.activity.basic.BaseActivity;


import com.dollop.placementadda.R;
import com.dollop.placementadda.adapter.MoreCoinsAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.library.LuckyWheelView;
import com.dollop.placementadda.library.model.LuckyItem;
import com.dollop.placementadda.model.MoreCoinsModel;
import com.dollop.placementadda.model.TimeLineCommentModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;
import com.dollop.placementadda.sohel.UserAccount;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoreCoinsActivity extends BaseActivity implements RewardedVideoAdListener {
    private List<MoreCoinsModel> moreCoinsModelArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoreCoinsAdapter mAdapter;
    CardView watchvideo_cardview, buy_cardview;
    private final static int REQUEST_READ_PHONE_STATE = 1;
    private RewardedVideoAd mRewardedVideoAd;
    private String amount= String.valueOf(10);
    private double coins=10.00;

    ProgressDialog progressDialog;

    @Override
    protected int getContentResId() {
        return R.layout.activity_more_coins;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(MoreCoinsActivity.this);
        setToolbarWithBackButton("More Coins");
        recyclerView = (RecyclerView) findViewById(R.id.moreCoinsRV);
        watchvideo_cardview = (CardView) findViewById(R.id.watchvideo_cardview);
        buy_cardview = (CardView) findViewById(R.id.buy_cardview);
        progressDialog = new ProgressDialog(MoreCoinsActivity.this);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        mAdapter = new MoreCoinsAdapter(MoreCoinsActivity.this, moreCoinsModelArrayList);
        try {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
        }catch (Exception e)
        {e.printStackTrace();}
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        coinsData();

        watchvideo_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasGetReadPhoneStatePermission()) {
                    progressDialog.setMessage("Loading..."); // Setting Message
                    progressDialog.setTitle("Video loading"); // Setting Title
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                    progressDialog.show(); // Display Progress Dialog
                    progressDialog.setCancelable(false);
                    loadRewardedVideoAd();
                } else {
                    requestReadPhoneStatePermission();
                }
            }
        });
        buy_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCoinsDialogBox("", "Activity");
            }
        });
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(MoreCoinsActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }

    private void coinsData() {
        MoreCoinsModel moreCoinsModel = new MoreCoinsModel("100", "10");
        moreCoinsModelArrayList.add(moreCoinsModel);
        moreCoinsModel = new MoreCoinsModel("200", "20");
        moreCoinsModelArrayList.add(moreCoinsModel);
        moreCoinsModel = new MoreCoinsModel("300", "30");
        moreCoinsModelArrayList.add(moreCoinsModel);
        moreCoinsModel = new MoreCoinsModel("400", "40");
        moreCoinsModelArrayList.add(moreCoinsModel);
        moreCoinsModel = new MoreCoinsModel("500", "50");
        moreCoinsModelArrayList.add(moreCoinsModel);
        moreCoinsModel = new MoreCoinsModel("600", "60");
        moreCoinsModelArrayList.add(moreCoinsModel);
        moreCoinsModel = new MoreCoinsModel("700", "70");
        moreCoinsModelArrayList.add(moreCoinsModel);
        moreCoinsModel = new MoreCoinsModel("800", "80");
        moreCoinsModelArrayList.add(moreCoinsModel);
        moreCoinsModel = new MoreCoinsModel("1000", "100");
        moreCoinsModelArrayList.add(moreCoinsModel);
        moreCoinsModel = new MoreCoinsModel("2000", "200");
        moreCoinsModelArrayList.add(moreCoinsModel);
        moreCoinsModel = new MoreCoinsModel("5000", "500");
        moreCoinsModelArrayList.add(moreCoinsModel);
        moreCoinsModel = new MoreCoinsModel("8000", "800");
        moreCoinsModelArrayList.add(moreCoinsModel);
        moreCoinsModel = new MoreCoinsModel("10000", "1000");
        moreCoinsModelArrayList.add(moreCoinsModel);
        mAdapter.notifyDataSetChanged();
    }

    public void getCoinsDialogBox(final String Rupees, String Check) {
        final Dialog dialog = new Dialog(MoreCoinsActivity.this);
        dialog.setContentView(R.layout.confirmation_dailog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText amount_et = (EditText) dialog.findViewById(R.id.amount_et);
        Button continue_btn = (Button) dialog.findViewById(R.id.continue_btn);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final TextView coins_tv = (TextView) dialog.findViewById(R.id.coins_tv);
        if (Check.equals("Adapter")) {
            amount_et.setText(Rupees);

            int coins = Integer.parseInt(Rupees) * 10;
            coins_tv.setText("Get " + coins + " Coins");
        } else {
            amount_et.setEnabled(true);
        }
        amount_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (amount_et.getText().toString().length() == 0) {
                    coins_tv.setText("Get " + "0" + " Coins");
                } else {
                    try {
                         coins = Double.valueOf(amount_et.getText().toString()).doubleValue() * 10;
                        coins_tv.setText("Get " + coins + " Coins");
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (amount_et.getText().toString().length() == 0) {
                    coins_tv.setText("Get " + "0" + " Coins");
                } else {
                    try {
                         coins = Double.valueOf(amount_et.getText().toString()).doubleValue() * 10;
                        coins_tv.setText("Get " + coins + " Coins");
                    } catch (Exception e) {
                    }
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserAccount.isEmpty(amount_et)) {
                    UPI_Payment(amount_et.getText().toString());
                    dialog.dismiss();
                } else {
                    UserAccount.EditTextPointer.setError(" Please Enter a Amount ");
                }
            }
        });
        dialog.show();
    }

    private boolean hasGetReadPhoneStatePermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadPhoneStatePermission() {
        //You can choose a more friendly notice text. And you can choose any view you like, such as dialog.
        //Toast.makeText(this, "Only grant the permission, can you start the mission!", Toast.LENGTH_SHORT).show();
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
    }

    public void UPI_Payment(String Amount) {
        try {
            String payeeAddress = "8962370106@ybl";
            String payeeName = "Kamal Gupta";
            String transactionNote = "Dollop Quiz";
            amount = Amount;
            String currencyUnit = "INR";
            Uri uri = Uri.parse("upi://pay?pa=" + payeeAddress + "&pn=" + payeeName + "&tn=" + transactionNote +
                    "&am=" + amount + "&cu=" + currencyUnit);
            Log.e("Log", "onClick: uri: " + uri);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String res = data.getStringExtra("response");
            String search = "SUCCESS";
            if (res.toLowerCase().contains(search.toLowerCase())) {
                Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
                AddCoin(amount, "Online");
            } else {
                Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadRewardedVideoAd() {
           mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        progressDialog.dismiss();
//        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
//                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        progressDialog.dismiss();
     /*   Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
*/    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        AddCoin("10", "Watch a video");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        progressDialog.dismiss();
     /*   Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
        progressDialog.dismiss();
/*
        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
*/
    }

    @Override
    public void onRewardedVideoAdOpened() {
    }

    @Override
    public void onRewardedVideoStarted() {
    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    private void AddCoin(final String amount, final String Status) {
        new JSONParser(MoreCoinsActivity.this).parseVollyStringRequest(Const.URL.addWalletAmount, 1, getCommentParams(amount, Status), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    ArrayList<TimeLineCommentModel> timeLineCommentModelslist = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    S.E("get Amount after ads"+response);
                    S.E("add amount after ads param"+getCommentParams(amount,Status));
                    if (jsonObject.getString("message").equals("success")) {
                        ConfirmPopup();
                    } else {
                        S.T(MoreCoinsActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                }
            }/*167222*/
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
        final Dialog dialog = new Dialog(MoreCoinsActivity.this);
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
