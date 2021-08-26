package com.dollop.placementadda.activity.basic;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.QuizMainActivity;
import com.dollop.placementadda.activity.QuizQuestionaryActivity;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;
import com.google.android.gms.common.api.GoogleApiClient;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.dollop.placementadda.activity.UpdateProfileActivity.PERMISSION_REQUEST_CODE;


public class SplashActivity extends BaseActivity {
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 42;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 43;
    final static int REQUEST_LOCATION = 199;
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private GoogleApiClient googleApiClient;
    private int STORAGE_PERMISSION_CODE = 23;
    ImageView splashIconsImageView;

    @Override
    protected int getContentResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashIconsImageView = findViewById(R.id.splashIconsImageView);

//        checkNetworkAvailability(SplashActivity.this);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
        if (checkPermission()) {
            S.E("checkPermission is come");
            checkTimerIntent();
           /* Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animations);
            splashIconsImageView.startAnimation(animation);*/
        } else {
            S.E("requestPermission is come");
            requestPermission();
           /* Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animations);
            splashIconsImageView.startAnimation(animation);*/
        }
    }

    public void checkTimerIntent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UserDataHelper.getInstance().getList().size() > 0) {

                    if (!SavedData.getNotificationStatus().equals("")) {
                        if (SavedData.getNotificationStatus().equals("QuizCategery")) {

                            Bundle bundle = new Bundle();
                            bundle.putString("ActivityCheck", SavedData.getNotificationStatus());
                            bundle.putString("quiz_id", SavedData.getQuiz_id());
                            bundle.putString("time", SavedData.gettime());
                            bundle.putString("topicId", SavedData.gettopicId());
                            S.I_clear(SplashActivity.this, QuizQuestionaryActivity.class, bundle);
                        } else {
                            S.I_clear(SplashActivity.this, QuizMainActivity.class, null);
                        }
                    } else {
                        S.I_clear(SplashActivity.this, QuizMainActivity.class, null);
                    }
                } else {
                    S.I_clear(SplashActivity.this, LoginsActivity.class, null);
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, " "}, PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);


        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ContactAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;


                    if (locationAccepted && cameraAccepted && ContactAccepted) {
                        checkTimerIntent();

                    } else {
                        checkTimerIntent();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{READ_PHONE_STATE, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                                    checkTimerIntent();
                                }
                            }
                        }
                        return;
                    }
                } else {

                }
                break;
        }
    }
}




