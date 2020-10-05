package com.dollop.placementadda.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.activity.basic.LoginsActivity;
import com.dollop.placementadda.broadcast.MyBroadcastReceiver;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.database.model.UserModel;
import com.dollop.placementadda.fragments.QuizMainFragment;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.services.CheckPlayerStatusService;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QuizMainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    DrawerLayout drawer;
    private ImageView imageView;
    private TextView etUserName, etUserEmail, etUserNum;
    NavigationView navigationView;
    private final int REQUEST_READ_PHONE_STATE = 100;
    LinearLayout mainheader_layout;
    private Button header_back_btn;


    @Override
    protected int getContentResId() {
        return R.layout.activity_quiz_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = new Intent(getApplicationContext(), CheckPlayerStatusService.class);
        startService(intent);
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
        }
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.act_main_content, new QuizMainFragment()).commit();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        imageView = (ImageView) headerview.findViewById(R.id.imageView);
        etUserName = (TextView) headerview.findViewById(R.id.etUserName);
        etUserEmail=(TextView)headerview.findViewById(R.id.etUserEmail);
        header_back_btn=(Button)headerview.findViewById(R.id.header_back_btn);
        mainheader_layout = (LinearLayout) headerview.findViewById(R.id.mainheader_layout);
        navigationView.getBackground().setAlpha(200);

        mainheader_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                S.I(QuizMainActivity.this, UpdateProfileActivity.class, null);
            }
        });
        header_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        hideSoftKeyboard();
        if (UserDataHelper.getInstance().getList().size() > 0) {
            etUserName.setText(UserDataHelper.getInstance().getList().get(0).getUserName());
            etUserEmail.setText(UserDataHelper.getInstance().getList().get(0).getUserEmail());
            try {
                String image = UserDataHelper.getInstance().getList().get(0).getProfile_pic();
                S.E("checkIamgePath");
                Picasso.with(QuizMainActivity.this).load(image).error(R.drawable.ic_user).into(imageView);
            } catch (Exception e) {
                e.getMessage();

            }
        }
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(QuizMainActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
        getProfile();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            S.I(QuizMainActivity.this, NotificationListActivity.class, null);
            return true;
        } else if (id == R.id.action_getcoins) {
            S.I(QuizMainActivity.this, MoreCoinsActivity.class, null);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_history) {
            S.I(QuizMainActivity.this, FilteredHistoryActivity.class, null);
//            S.I(QuizMainActivity.this, HistoryActivity.class, null);
        } else if (id == R.id.nav_home) {
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.act_main_content, new QuizMainFragment()).commit();
        } else if (id == R.id.nav_newbatch) {
            S.E("Training");
            S.I(QuizMainActivity.this, QuizNewBatchActivity.class, null);
        } else if (id == R.id.nav_wallet) {
            S.I(QuizMainActivity.this, QuizWalletActivity.class, null);
        } else if (id == R.id.timeline) {
            S.I(QuizMainActivity.this, TimeLineActivity.class, null);
        } else if (id == R.id.jonAlert) {
            S.I(QuizMainActivity.this, JobAlertActivity.class, null);
        } else if (id == R.id.discussion_form) {
            S.I(QuizMainActivity.this, DiscussionForumActivity.class, null);
        } else if (id == R.id.nav_notification) {
            S.I(QuizMainActivity.this, NotificationListActivity.class, null);
        } else if (id == R.id.nav_buy_coins) {
            S.I(QuizMainActivity.this, MoreCoinsActivity.class, null);
        } else if (id == R.id.transaction) {
            S.I(QuizMainActivity.this, TransactionHistroyActivity.class, null);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void launchFragmentTitle(String fragemntTitle) {
        getSupportActionBar().setTitle(fragemntTitle);
    }

    private void getProfile() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.getProfile, 1, getParams12(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    S.E("checkmainProfile::"+response);
                    JSONObject jsonObject1 = new JSONObject(response);
                    JSONObject jsonObject11 = jsonObject1.getJSONObject("data");
                    String user_id = jsonObject11.getString("user_id");
                    String userName = jsonObject11.getString("userName");
                    String userPhone = jsonObject11.getString("userPhone");
                    String userDOB = jsonObject11.getString("userDOB");
                    String userDOJ = jsonObject11.getString("userDOJ");
                    String userGender = jsonObject11.getString("userGender");
                    String userCollegeName = jsonObject11.getString("userCollegeName");
                    String user_is_verified = jsonObject11.getString("user_is_verified");
                    String user_is_active = jsonObject11.getString("user_is_active");
                    String userEmail = jsonObject11.getString("userEmail");
                    String userAddress = jsonObject11.getString("userAddress");
                    String userProfilePic = jsonObject11.getString("userProfilePic");
                    Picasso.with(QuizMainActivity.this).load(Const.URL.IMAGE_URL + userProfilePic).error(R.drawable.ic_user).into(imageView);
                 /*   etUserName.setText(userName);
                    etUserEmail.setText(userEmail);
                    etUserNum.setText(userPhone);*/
                   /* if (jsonObject11.getString("userDOJ").equals("")) {
                        S.I_clear(QuizMainActivity.this, UpdateProfileActivity.class, null);
                    }*/

                    if(userDOB.equals("")||userDOB==null){

                        S.I_clear(QuizMainActivity.this, UpdateProfileActivity.class, null);
                    }
                    UserModel userModel = new UserModel();
                    userModel.setUserId(user_id);
                    userModel.setUserName(userName);
                    userModel.setUserEmail(userEmail);
                    userModel.setUserPhone(userPhone);
                    userModel.setUserCollegeName(userCollegeName);
                    userModel.setUserGender(userGender);
                    userModel.setUserDob(userDOB);
                    userModel.setUserDoj(userDOJ);
                    userModel.setUserAddress(userAddress);
                    userModel.setCity(UserDataHelper.getInstance().getList().get(0).getCity());
                    userModel.setProfile_pic(userProfilePic);
                    userModel.setUserImeiNo(UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
                    UserDataHelper.getInstance().insertData(userModel);

                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("checkErrorTest::"+e);
                }
            }
        });
    }

    private Map<String, String> getParams12() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
        return params;
    }

}
