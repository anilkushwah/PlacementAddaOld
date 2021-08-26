package com.dollop.placementadda.activity.basic;

import static android.Manifest.permission.READ_CONTACTS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentSender;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.QuizMainActivity;
import com.dollop.placementadda.activity.UpdateProfileActivity;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.database.model.UserModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;
import com.dollop.placementadda.sohel.UserAccount;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginsActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int REQUEST_READ_PHONE = 101;
    private static final int REQUEST_READ_SMS = 102;
    private static final int REQUEST_RECIVIS = 103;
    private static final int SENDSMS = 104;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 116;
    // private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;
    EditText etSignUpName, etSignUpEmail, etSignUpPhone;
    LinearLayout parentSignUpLayout;
    Animation uptodown, downtoup;
    Spinner collegeNameSp;
    RadioButton maleRB, femaleRB;
    ScrollView signupScrollView;
    AppUpdateManager mAppUpdateManager;
    InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED) {
                        popupSnackbarForCompleteUpdate();
                    } else if (state.installStatus() == InstallStatus.INSTALLED) {
                        if (mAppUpdateManager != null) {
                            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                        }
                    } else {
                        Log.e("please check", "InstallStateUpdatedListener: state: " + state.installStatus());
                    }
                }
            };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button loginSignUpBtn;
    private Button mEmailSignInButton;
    private String imeiNoStr = "";
    private SharedPreferences pref;
    private String loginuserID, name, email, userPhone, userCollegeName, username, photo, lang;
    private ProgressDialog mProgressDialog;
    private String userDOB;
    private LinearLayout signUpLinearLayout;
    private LinearLayout loginLinearLayout;
    private Button signUploginginBtn;
    private Button signUpBtn;

    @Override
    protected int getContentResId() {
        return R.layout.activity_login;
    }

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(LoginsActivity.this);
        // Set up the login form.

        imeiNoStr = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        SavedData.saveIMEI_NUMBER(imeiNoStr);
        mEmailView = findViewById(R.id.email);
        loginSignUpBtn = findViewById(R.id.loginSignUpBtn);
        signUpLinearLayout = findViewById(R.id.signUpLinearLayout);
        loginLinearLayout = findViewById(R.id.loginLinearLayout);
        signUploginginBtn = findViewById(R.id.signUploginginBtn);

//        find ids of signup page

        parentSignUpLayout = findViewById(R.id.parentSignUpLayout);
        etSignUpName = findViewById(R.id.etSignUpName);
        etSignUpEmail = findViewById(R.id.etSignUpEmail);
        etSignUpPhone = findViewById(R.id.etSignUpPhone);
        collegeNameSp = findViewById(R.id.collegeNameSp);
        maleRB = findViewById(R.id.maleRB);
        femaleRB = findViewById(R.id.femaleRB);
        signupScrollView = findViewById(R.id.signupScrollView);
        FirebaseApp.initializeApp(activity);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        S.E("getInstanceId failed" + task.getException());
                        return;
                    }

                    String refreshedToken = task.getResult();
                    SavedData.saveFcmTokenID(refreshedToken);

                });

        populateAutoComplete();


        pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
      /*  SharedPremEmailSignInButtonferences.Editor editor = pref.edit();
        editor.putString("regId", token);*/
      /*  if (UserDataHelper.getInstance().getList().size() > 0) {
            S.I_clear(LoginsActivity.this, UpdateProfileActivity.class, null);
        }*/
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserAccount.isEmpty(etSignUpName, etSignUpEmail, etSignUpPhone)) {

                    if (UserAccount.isEmailValid(etSignUpEmail)) {
                        if (UserAccount.isPhoneNumberLength(etSignUpPhone)) {
                            userSignUp();
                        } else {
                            S.T(LoginsActivity.this, "Invalid Phone No");
                        }
                    } else {
                        S.T(LoginsActivity.this, "Invalid Email");
                    }
                } else {
                    S.T(LoginsActivity.this, "Field Can't Be empty");
                }
            }
        });

        loginSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginLinearLayout.clearAnimation();
                signUpLinearLayout.setVisibility(View.VISIBLE);
                signupScrollView.setVisibility(View.VISIBLE);
                loginLinearLayout.setVisibility(View.GONE);
                Animation moveUp = AnimationUtils.loadAnimation(LoginsActivity.this, R.anim.move_above);
                signUpLinearLayout.startAnimation(moveUp);

                //  S.I(LoginsActivity.this, SignUpActivity.class, null);
            }
        });
        signUploginginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpLinearLayout.clearAnimation();
                loginLinearLayout.setVisibility(View.VISIBLE);
                signUpLinearLayout.setVisibility(View.GONE);
                signupScrollView.setVisibility(View.GONE);
                Animation moveUp = AnimationUtils.loadAnimation(LoginsActivity.this, R.anim.move_above);
                loginLinearLayout.startAnimation(moveUp);
            }
        });

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mEmailSignInButton = (Button) findViewById(R.id.loginginBtn);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        //  mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void userSignUp() {
        new JSONParser(LoginsActivity.this).parseVollyStringRequest(Const.URL.SIGNUP_URL, 1, getAddTeacher(), new Helper() {
            @Override
            public void backResponse(String response) {
                final Dialog dialog = S.initProgressDialog(LoginsActivity.this);
                S.E("get SignUP Param" + getAddTeacher());
                S.E("comes into try block" + response);
                try {
                    dialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.getInt("status") == 200) {
                        Intent intent = new Intent(LoginsActivity.this, OtpVerificationActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("mobile_num", etSignUpPhone.getText().toString());
                        bundle.putString("imei_num", imeiNoStr);
  			bundle.putString("data",jsonObject.getString("data"));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        //   S.I(SignUpActivity.this, OtpVerificationActivity.class, null);
                        // SavedData.saveProfileUpdate("updatenow");

                    } else {
                        S.T(LoginsActivity.this, jsonObject.getString("message"));
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
        params.put("user_imei_number", imeiNoStr);
        return params;
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
        if (requestCode == REQUEST_READ_PHONE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
        if (requestCode == REQUEST_READ_PHONE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mEmailView.getText().toString().equals("")) {
            mEmailView.setError("Field can't be empty");
        } else {
            loginTask();
        }
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(LoginsActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginsActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(LoginsActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(LoginsActivity.this, new String[]{permission}, requestCode);
            }
        } else {
        }
    }

    public void loginTask() {
        new JSONParser(LoginsActivity.this).parseVollyStringRequest(Const.URL.LOGIN_URL, 1, getLoginParam(), new Helper() {
            @Override
            public void backResponse(String response) {
                final Dialog dialog = S.initProgressDialog(LoginsActivity.this);
                S.E("checkLoginUserParams::" + getLoginParam());
                try {
                    dialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        S.E("checkLoginUserData::" + response);
                        JSONObject dataObjects = jsonObject.getJSONObject("data");
                        loginuserID = dataObjects.getString("user_id");
                        name = dataObjects.getString("userName");
                        email = dataObjects.getString("userEmail");
                        userPhone = dataObjects.getString("userPhone");
                        userCollegeName = dataObjects.getString("userCollegeName");
                        username = dataObjects.getString("userGender");
                        userDOB = dataObjects.getString("userDOB");
                        lang = dataObjects.getString("userDOJ");
                   /*     if (dataObjects.getString("userProfilePic").equals("")||(dataObjects.getString("userProfilePic")==null)) {
                            UserModel userModel = new UserModel();
                            userModel.setUserId(loginuserID);
                            userModel.setUserName(name);
                            userModel.setUserEmail(email);
                            userModel.setUserPhone(userPhone);
                            userModel.setUserCollegeName(userCollegeName);
                            userModel.setUserGender(username);
                            userModel.setUserDob(userDOB);
                            userModel.setUserDoj(lang);
                            userModel.setUserImeiNo(imeiNoStr);
                            userModel.setUserAddress("");
                            userModel.setCity("");
                            UserDataHelper.getInstance().insertData(userModel);
                            S.I_clear(LoginsActivity.this, UpdateProfileActivity.class, null);
                        } else*/
                        if (userDOB.equals("") || userDOB == null) {
                            UserModel userModel = new UserModel();
                            userModel.setUserId(loginuserID);
                            userModel.setUserName(name);
                            userModel.setUserEmail(email);
                            userModel.setUserPhone(userPhone);
                            userModel.setUserCollegeName(userCollegeName);
                            userModel.setUserGender(username);
                            userModel.setUserDob(userDOB);
                            userModel.setUserDoj(lang);
                            userModel.setUserImeiNo(imeiNoStr);
                            userModel.setUserAddress(dataObjects.getString("userAddress"));
                            userModel.setCity(dataObjects.getString("userCity"));
                            userModel.setProfile_pic(Const.URL.IMAGE_URL + dataObjects.getString("userProfilePic"));
                            UserDataHelper.getInstance().insertData(userModel);
                            S.I_clear(LoginsActivity.this, UpdateProfileActivity.class, null);
                        } else if (userCollegeName.equals("") || userCollegeName == null) {
                            UserModel userModel = new UserModel();
                            userModel.setUserId(loginuserID);
                            userModel.setUserName(name);
                            userModel.setUserEmail(email);
                            userModel.setUserPhone(userPhone);
                            userModel.setUserCollegeName(userCollegeName);
                            userModel.setUserGender(username);
                            userModel.setUserDob(userDOB);
                            userModel.setUserDoj(lang);
                            userModel.setUserImeiNo(imeiNoStr);
                            userModel.setUserAddress(dataObjects.getString("userAddress"));
                            userModel.setCity(dataObjects.getString("userCity"));
                            userModel.setProfile_pic(Const.URL.IMAGE_URL + dataObjects.getString("userProfilePic"));
                            UserDataHelper.getInstance().insertData(userModel);
                            S.I_clear(LoginsActivity.this, UpdateProfileActivity.class, null);
                        } else {
                            UserModel userModel = new UserModel();
                            userModel.setUserId(loginuserID);
                            userModel.setUserName(name);
                            userModel.setUserEmail(email);
                            userModel.setUserPhone(userPhone);
                            userModel.setUserCollegeName(userCollegeName);
                            userModel.setUserGender(username);
                            userModel.setUserDob(userDOB);
                            userModel.setUserDoj(lang);
                            userModel.setUserImeiNo(imeiNoStr);
                            userModel.setUserAddress(dataObjects.getString("userAddress"));
                            userModel.setCity(dataObjects.getString("userCity"));
                            userModel.setProfile_pic(Const.URL.IMAGE_URL + dataObjects.getString("userProfilePic"));
                            UserDataHelper.getInstance().insertData(userModel);
                            S.I_clear(LoginsActivity.this, QuizMainActivity.class, null);
                        }
                    } else {
                        S.T(LoginsActivity.this, "" + jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Map<String, String> getLoginParam() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("id", mEmailView.getText().toString());
        stringStringHashMap.put("user_imei_number", imeiNoStr);
        stringStringHashMap.put("fcm_id", SavedData.getFcmTokenID());
        S.E("prams::" + stringStringHashMap);
        return stringStringHashMap;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

           /* mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });*/
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        try {
            List<String> emails = new ArrayList<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                emails.add(cursor.getString(ProfileQuery.ADDRESS));
                cursor.moveToNext();
            }

            addEmailsToAutoComplete(emails);
        } catch (Exception e) {
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginsActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {

        mAppUpdateManager = AppUpdateManagerFactory.create(this);

        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.IMMEDIATE, LoginsActivity.this, RC_APP_UPDATE);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                popupSnackbarForCompleteUpdate();
            } else {
                Log.e("TAG", "checkForAppUpdateAvailability: something else");
            }
        });
        super.onStart();
    }

    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.llMainLayoutId),
                        "New app is ready!",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Install", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAppUpdateManager != null) {
                    mAppUpdateManager.completeUpdate();
                }
            }
        });


        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAppUpdateManager != null) {
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e("TAG", "onActivityResult: app download failed");
                mAppUpdateManager = AppUpdateManagerFactory.create(this);

                mAppUpdateManager.registerListener(installStateUpdatedListener);

                mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                        try {
                            mAppUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo, AppUpdateType.IMMEDIATE, LoginsActivity.this, RC_APP_UPDATE);

                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }

                    } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                        //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                        popupSnackbarForCompleteUpdate();
                    } else {
                        Log.e("TAG", "checkForAppUpdateAvailability: something else");
                    }
                });
            }
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(LoginsActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Process");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {
            String imageURL = URL[0];
            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("JSONException =", " doInBackground  " + e);
            }
            return bitmap;
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Bitmap result) {
            try {
                mProgressDialog.dismiss();
                UserModel userModel = new UserModel();
                userModel.setUserId(loginuserID);
                userModel.setUserName(name);
                userModel.setUserEmail(email);
                userModel.setUserPhone(userPhone);
                userModel.setUserCollegeName(userCollegeName);
                userModel.setUserGender(username);
                userModel.setUserDob(photo);
                userModel.setUserDoj(lang);
                userModel.setUserImeiNo(imeiNoStr);
                userModel.setUserAddress("");
                userModel.setCity("");
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    result.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] arr = baos.toByteArray();
                    userModel.setProfile_pic(Base64.encodeToString(arr, Base64.DEFAULT));
                    UserDataHelper.getInstance().insertData(userModel);
                    S.I_clear(LoginsActivity.this, QuizMainActivity.class, null);
                } catch (Exception e) {
                }

            } catch (Exception e) {
            }
        }
    }

}
