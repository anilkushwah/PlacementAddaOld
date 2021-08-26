package com.dollop.placementadda.activity.basic;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dollop.placementadda.R;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OtpVerificationActivity extends BaseActivity {
    TextInputLayout hideHintAlsoId;
    EditText editOtpId;
    TextView tvSetMobileNoId, resendOtpTv;
    Button btnNext;
    // [END declare_auth]
    PhoneAuthCredential credentials;
    private String mobileNumber, imeiNumber, otp;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected int getContentResId() {
        return R.layout.activity_otp_verification;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hideHintAlsoId = findViewById(R.id.hideHintAlsoId);
        //  FirebaseAuth.getInstance().getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        mAuth = FirebaseAuth.getInstance();
        editOtpId = findViewById(R.id.editOtpId);
        tvSetMobileNoId = findViewById(R.id.tvSetMobileNoId);
        btnNext = findViewById(R.id.btnNext);
        resendOtpTv = findViewById(R.id.resendOtpTv);
        Bundle bundle = getIntent().getExtras();
        mobileNumber = bundle.getString("mobile_num");
        imeiNumber = bundle.getString("imei_num");
        otp = bundle.getString("data");

        S.E("mobile number from signmu" + mobileNumber);
        S.E("imei number from signmu" + imeiNumber);
        S.E("otp" + otp);

        resendOtpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode("+91" + mobileNumber, mResendToken);
            }
        });

    /* SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.e("Text", messageText);
                String[] messageOtp = messageText.split(" ");
                Log.e("+++++++++++", messageOtp[0]);
                editOtpId.setText(messageOtp[0]);
            }
        });*/

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserAccount.isEmpty(editOtpId)) {

                    if (editOtpId.getText().toString().length() == 6)
                        verifyPhoneNumberWithCode(mVerificationId, editOtpId.getText().toString());
                    else{
                        UserAccount.EditTextPointer.setError("Please Enter Correct Otp");
                        UserAccount.EditTextPointer.requestFocus();
                    }

                } else {
                    UserAccount.EditTextPointer.setError(UserAccount.errorMessage);
                    UserAccount.EditTextPointer.requestFocus();
                }
            }
        });


        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Initialize phone auth callbacks

        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d("TAG", "onVerificationCompleted:" + credential);

                //
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("TAG", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("TAG", "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
        startPhoneNumberVerification("+91" + mobileNumber);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            getMatchOtp();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
        // [END verify_with_code]
    }


    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void getResendOtp() {
        new JSONParser(OtpVerificationActivity.this).parseVollyStringRequest(Const.URL.ResendOtp, 1, getResendOtpParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                final Dialog dialog = S.initProgressDialog(OtpVerificationActivity.this);
                try {
                    dialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    S.E("get Params of Otp" + getResendOtpParams());

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
                    S.E("get Params of Otp" + getOtpParams());

                    if (jsonObject.getString("status").equals("200")) {
                        Intent intent = new Intent(OtpVerificationActivity.this, LoginsActivity.class);

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
        params.put("otp", otp);
        params.put("user_imei_number", imeiNumber);
        return params;
    }
}
