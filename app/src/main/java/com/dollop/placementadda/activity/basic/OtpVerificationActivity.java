package com.dollop.placementadda.activity.basic;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dollop.placementadda.R;

import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OtpVerificationActivity extends BaseActivity {
    TextInputLayout hideHintAlsoId;
    EditText editOtpId;
    TextView tvSetMobileNoId,resendOtpTv;
    Button btnNext;
    private String mobileNumber,imeiNumber;

    @Override
    protected int getContentResId() {
        return R.layout.activity_otp_verification;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // hideHintAlsoId = findViewById(R.id.hideHintAlsoId);
        editOtpId = findViewById(R.id.editOtpId);
        tvSetMobileNoId = findViewById(R.id.tvSetMobileNoId);
        btnNext = findViewById(R.id.btnNext);
        resendOtpTv=findViewById(R.id.resendOtpTv);
   Bundle bundle = getIntent().getExtras();
        mobileNumber = bundle.getString("mobile_num");
        imeiNumber=bundle.getString("imei_num");

        S.E("mobile number from signmu" + mobileNumber);
        S.E("imei number from signmu" + imeiNumber);

        resendOtpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResendOtp();
            }
        });

     SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.e("Text", messageText);
                String[] messageOtp = messageText.split(" ");
                Log.e("+++++++++++", messageOtp[0]);
                editOtpId.setText(messageOtp[0]);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMatchOtp();
            }
        });
       // getMatchOtp();
    }


    private void getResendOtp() {
        new JSONParser(OtpVerificationActivity.this).parseVollyStringRequest(Const.URL.ResendOtp, 1, getResendOtpParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                final Dialog dialog = S.initProgressDialog(OtpVerificationActivity.this);
                try {
                    dialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    S.E("get Params of Otp"+getResendOtpParams());

                    if (jsonObject.getString("status").equals("200")) {
                     /*   Intent intent=new Intent(OtpVerificationActivity.this,LoginsActivity.class);
 Bundle bundle=new Bundle();
                        bundle.putString("mobile_num", etSignUpPhone.getText().toString());
                        bundle.putString("imei_num",telephonyManagerStr);
                        intent.putExtras(bundle);

                        startActivity(intent);*/
                        //   S.I(SignUpActivity.this, OtpVerificationActivity.class, null);
                        // SavedData.saveProfileUpdate("updatenow");
                    } else {
                        S.T(OtpVerificationActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getResendOtpParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("userPhone", mobileNumber);
        params.put("user_imei_number", imeiNumber);
        return params;
    }

    private void getMatchOtp() {
        new JSONParser(OtpVerificationActivity.this).parseVollyStringRequest(Const.URL.matchOtp, 1, getOtpParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                final Dialog dialog = S.initProgressDialog(OtpVerificationActivity.this);
                try {
                    dialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    S.E("get Params of Otp"+getOtpParams());

                    if (jsonObject.getString("status").equals("200")) {
                        Intent intent=new Intent(OtpVerificationActivity.this,LoginsActivity.class);

                        startActivity(intent);



                    } else {
                        S.T(OtpVerificationActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getOtpParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("userPhone", mobileNumber);
        params.put("otp", editOtpId.getText().toString());
        params.put("user_imei_number", imeiNumber);
        return params;
    }
}
