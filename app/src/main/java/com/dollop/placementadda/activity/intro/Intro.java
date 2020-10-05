package com.dollop.placementadda.activity.intro;

/**
 * Created by Sohel on 02/03/2018.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.dollop.placementadda.R;

import com.dollop.placementadda.activity.basic.SplashActivity;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.sohel.S;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public final class Intro extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        } else {
        }
        if (UserDataHelper.getInstance().getList().size() > 0) {
            S.I_clear(this, SplashActivity.class, null);

        } else {
            addSlide(AppIntroFragment.newInstance("", "", R.drawable.slider_one, Color.parseColor("#000000")));
        addSlide(AppIntroFragment.newInstance(" ", "", R.drawable.slieder_two, Color.parseColor("#F47D34")));
        addSlide(AppIntroFragment.newInstance("  ", "", R.drawable.slider_three, Color.parseColor("#F9F9F9")));

            addSlide(AppIntroFragment.newInstance("  ", "", R.drawable.slider_four, Color.parseColor("#2C2C2C")));
            }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void loadMainActivity() {
        S.I(Intro.this, SplashActivity.class, null);
        this.finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        loadMainActivity();
    }

    @Override
    public void onSlideChanged(Fragment oldFragment, Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        loadMainActivity();
    }
}