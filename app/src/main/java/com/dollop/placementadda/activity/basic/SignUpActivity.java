package com.dollop.placementadda.activity.basic;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.dollop.placementadda.R;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;
import com.dollop.placementadda.sohel.UserAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
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
        askForPermission(Manifest.permission.READ_PHONE_STATE, 101);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        telephonyManagerStr = telephonyManager.getDeviceId();
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
            S.E("get SignUP Param"+getAddTeacher());
            S.E("comes into try block"+response);
            try {
                dialog.dismiss();
                JSONObject jsonObject = new JSONObject(response);


                if (jsonObject.getInt("status")==200) {
                    Intent intent=new Intent(SignUpActivity.this,OtpVerificationActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("mobile_num",etSignUpPhone.getText().toString());
                    bundle.putString("imei_num",telephonyManagerStr);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    //   S.I(SignUpActivity.this, OtpVerificationActivity.class, null);
                    // SavedData.saveProfileUpdate("updatenow");

                } else {
                    S.T(SignUpActivity.this, jsonObject.getString("message"));
                }
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

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(SignUpActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }
}
