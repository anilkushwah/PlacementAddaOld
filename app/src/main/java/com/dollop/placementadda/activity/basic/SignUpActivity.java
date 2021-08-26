package com.dollop.placementadda.activity.basic;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.dollop.placementadda.R;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;
import com.dollop.placementadda.sohel.UserAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends BaseActivity {
    Button signUpBtn;
    EditText etSignUpName, etSignUpEmail, etSignUpPhone;
    LinearLayout parentSignUpLayout;
    Animation uptodown, downtoup;
    Spinner collegeNameSp;
    RadioButton maleRB, femaleRB;
    private String telephonyManagerStr = "";


    @Override
    protected int getContentResId() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        parentSignUpLayout = (LinearLayout) findViewById(R.id.parentSignUpLayout);
        etSignUpName = (EditText) findViewById(R.id.etSignUpName);
        etSignUpEmail = (EditText) findViewById(R.id.etSignUpEmail);
        etSignUpPhone = (EditText) findViewById(R.id.etSignUpPhone);
        collegeNameSp = (Spinner) findViewById(R.id.collegeNameSp);
        maleRB = (RadioButton) findViewById(R.id.maleRB);
        femaleRB = (RadioButton) findViewById(R.id.femaleRB);
//        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
//        downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);


        telephonyManagerStr = SavedData.getIMEI_NUMBER();
        //   parentSignUpLayout.setAnimation(downtoup);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserAccount.isEmpty(etSignUpName, etSignUpEmail, etSignUpPhone)) {

                    if (UserAccount.isEmailValid(etSignUpEmail)) {
                        if (UserAccount.isPhoneNumberLength(etSignUpPhone)) {
                            userSignUp();
                        } else {
                            S.T(SignUpActivity.this, "Invalid Phone No");
                        }
                    } else {
                        S.T(SignUpActivity.this, "Invalid Email");
                    }
                } else {
                    S.T(SignUpActivity.this, "Field Can't Be empty");
                }
            }
        });
    }


    private void userSignUp() {
        new JSONParser(SignUpActivity.this).parseVollyStringRequest(Const.URL.SIGNUP_URL, 1, getAddTeacher(), new Helper() {
            @Override
            public void backResponse(String response) {
                final Dialog dialog = S.initProgressDialog(SignUpActivity.this);
                S.E("get SignUP Param" + getAddTeacher());
                S.E("comes into try block" + response);
                try {
                    dialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);

                    S.E("data:::"+jsonObject.getString("data"));
                   /* if (jsonObject.getInt("status") == 200) {
                        Intent intent = new Intent(SignUpActivity.this, OtpVerificationActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("mobile_num", etSignUpPhone.getText().toString());
                        bundle.putString("imei_num", telephonyManagerStr);
                        bundle.putString("data",jsonObject.getString("data"));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        //   S.I(SignUpActivity.this, OtpVerificationActivity.class, null);
                        // SavedData.saveProfileUpdate("updatenow");

                    } else {
                        S.T(SignUpActivity.this, jsonObject.getString("message"));
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getAddTeacher() {
        HashMap<String, String> params = new HashMap<>();
        params.put("userName", etSignUpName.getText().toString());
        params.put("userEmail", etSignUpEmail.getText().toString());
        params.put("userPhone", etSignUpPhone.getText().toString());
        params.put("user_imei_number", telephonyManagerStr);
        return params;
    }


}
