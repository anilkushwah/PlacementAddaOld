package com.dollop.placementadda;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.IntDef;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dollop.placementadda.database.datahelper.QuestionaryListHelper;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.google.android.gms.ads.MobileAds;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by androidsys1 on 3/31/2017.
 */


public class AppController extends MultiDexApplication {

    private static AppController mInstance;
    public static int invoiceId = 0;
    private RequestQueue mRequestQueue;
    public static final String TAG = AppController.class.getSimpleName();

    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;
    static Context context;
    public static synchronized AppController getInstance() {
        return mInstance;
    }
    public static AppController getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(
            {GRANTED, DENIED, BLOCKED})
    public @interface PermissionStatus {
    }

    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int BLOCKED = 2;

    @PermissionStatus
    public static int getPermissionStatus(Activity activity, String androidPermissionName) {
        if (ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)) {
                return BLOCKED;
            }
            return DENIED;
        }
        return GRANTED;
    }

    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;

        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


    @Override
    public void onCreate() {
//        MultiDex.install(this);
        super.onCreate();
        mInstance = this;
        new UserDataHelper(this);
        new QuestionaryListHelper(this);


    }

    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace();
    }

}

