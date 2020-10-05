package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.SelectThreePlayerAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuizPlayThreePlayerDetail extends BaseActivity {
    CircleImageView quizLogoImageView, firstPlayerIv, secondPlayerIv, thirdPlayerIv;
    TextView firstPlayerNameTv, firstPlayerGenderTv, firstPlayerCityTv, secondPlayerNameTv, secondPlayerGenderTv, secondPlayerCityTv, thirdPlayerNameTv, thirdPlayerGenderTv, thirdPlayerCityTv, idsTV, idsthirdTV;
    ArrayList<String> playersIds = new ArrayList<>();
    String secondid, thirdid;

    @Override
    protected int getContentResId() {
        return R.layout.activity_quiz_play_three_player_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(QuizPlayThreePlayerDetail.this);
        setToolbarWithBackButton("Player Detail");

        quizLogoImageView = findViewById(R.id.quizLogoImageView);
        firstPlayerIv = findViewById(R.id.firstPlayerIv);
        secondPlayerIv = findViewById(R.id.secondPlayerIv);
        thirdPlayerIv = findViewById(R.id.thirdPlayerIv);
        firstPlayerNameTv = findViewById(R.id.firstPlayerNameTv);
        firstPlayerGenderTv = findViewById(R.id.firstPlayerGenderTv);
        firstPlayerCityTv = findViewById(R.id.firstPlayerCityTv);
        secondPlayerNameTv = findViewById(R.id.secondPlayerNameTv);
        secondPlayerGenderTv = findViewById(R.id.secondPlayerGenderTv);
        secondPlayerCityTv = findViewById(R.id.secondPlayerCityTv);
        thirdPlayerNameTv = findViewById(R.id.thirdPlayerNameTv);
        thirdPlayerGenderTv = findViewById(R.id.thirdPlayerGenderTv);
        thirdPlayerCityTv = findViewById(R.id.thirdPlayerCityTv);
        idsTV = findViewById(R.id.idsTV);
        idsthirdTV = findViewById(R.id.idsthirdTV);

        String logoImage = SavedData.getCategoryImage();
        Picasso.with(QuizPlayThreePlayerDetail.this).
                load(SavedData.getCategoryImage())
                .into(quizLogoImageView);

        for (int i = 0; i < SelectThreePlayerAdapter.leaderBoardModelList.size(); i++) {
            if (SelectThreePlayerAdapter.leaderBoardModelList.get(i).getSelected()) {
                playersIds.add(SelectThreePlayerAdapter.leaderBoardModelList.get(i).getUser_id());
            }
        }
        secondid = playersIds.get(0);
        thirdid = playersIds.get(1);
        getFirstPlayerDetails();
        getSecondPlayerDetails();
        getThirdPlayerDetails();
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(QuizPlayThreePlayerDetail.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }


    private void getFirstPlayerDetails() {
        new JSONParser(QuizPlayThreePlayerDetail.this).parseVollyStringRequest(Const.URL.GETPLAYERDETAIL, 1, getParam(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            jsonObject1.getString("user_id");
                            String userName = jsonObject1.getString("userName");
                            String userEmail = jsonObject1.getString("userEmail");
                            jsonObject1.getString("userPhone");
                            jsonObject1.getString("user_imei_number");
                            jsonObject1.getString("userCollegeId");
                            jsonObject1.getString("userCollegeName");
                            String userGender = jsonObject1.getString("userGender");
                            jsonObject1.getString("userDOB");
                            jsonObject1.getString("userDOJ");
                            String otherUserCIty = jsonObject1.getString("userCity");
                            jsonObject1.getString("userAddress");
                            jsonObject1.getString("userProfilePic");
                            jsonObject1.getString("user_is_active");
                            jsonObject1.getString("user_is_verified");
                            jsonObject1.getString("user_change_imei");
                            jsonObject1.getString("rankingPoints");
                            jsonObject1.getString("fcm_id");
                            jsonObject1.getString("userCreatedDate");
                            Picasso.with(QuizPlayThreePlayerDetail.this).
                                    load(Const.URL.IMAGE_URL + jsonObject1.getString("userProfilePic")).into(firstPlayerIv);
                            firstPlayerNameTv.setText(userName);
                            firstPlayerCityTv.setText(otherUserCIty);
                            firstPlayerGenderTv.setText(userGender);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParam() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        return params;
    }

    private void getSecondPlayerDetails() {
        new JSONParser(QuizPlayThreePlayerDetail.this).parseVollyStringRequest(Const.URL.GETPLAYERDETAIL, 1, getParam1(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                      JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            jsonObject1.getString("user_id");
                            String userName = jsonObject1.getString("userName");
                            String userEmail = jsonObject1.getString("userEmail");
                            jsonObject1.getString("userPhone");
                            jsonObject1.getString("user_imei_number");
                            jsonObject1.getString("userCollegeId");
                            jsonObject1.getString("userCollegeName");
                            String userGender = jsonObject1.getString("userGender");
                            jsonObject1.getString("userDOB");
                            jsonObject1.getString("userDOJ");
                            String otherUserCIty = jsonObject1.getString("userCity");
                            jsonObject1.getString("userAddress");
                            jsonObject1.getString("userProfilePic");
                            jsonObject1.getString("user_is_active");
                            jsonObject1.getString("user_is_verified");
                            jsonObject1.getString("user_change_imei");
                            jsonObject1.getString("rankingPoints");
                            jsonObject1.getString("fcm_id");
                            jsonObject1.getString("userCreatedDate");
                            Picasso.with(QuizPlayThreePlayerDetail.this).
                                    load(Const.URL.IMAGE_URL + jsonObject1.getString("userProfilePic")).into(secondPlayerIv);
                            secondPlayerNameTv.setText(userName);
                            secondPlayerCityTv.setText(otherUserCIty);
                            secondPlayerGenderTv.setText(userGender);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParam1() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", secondid);
        return params;
    }

    private void getThirdPlayerDetails() {
        new JSONParser(QuizPlayThreePlayerDetail.this).parseVollyStringRequest(Const.URL.GETPLAYERDETAIL, 1, getParam2(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            jsonObject1.getString("user_id");
                            String userName = jsonObject1.getString("userName");
                            String userEmail = jsonObject1.getString("userEmail");
                            jsonObject1.getString("userPhone");
                            jsonObject1.getString("user_imei_number");
                            jsonObject1.getString("userCollegeId");
                            jsonObject1.getString("userCollegeName");
                            String userGender = jsonObject1.getString("userGender");
                            jsonObject1.getString("userDOB");
                            jsonObject1.getString("userDOJ");
                            String otherUserCIty = jsonObject1.getString("userCity");
                            jsonObject1.getString("userAddress");
                            jsonObject1.getString("userProfilePic");
                            jsonObject1.getString("user_is_active");
                            jsonObject1.getString("user_is_verified");
                            jsonObject1.getString("user_change_imei");
                            jsonObject1.getString("rankingPoints");
                            jsonObject1.getString("fcm_id");
                            jsonObject1.getString("userCreatedDate");
                            Picasso.with(QuizPlayThreePlayerDetail.this).
                                    load(Const.URL.IMAGE_URL + jsonObject1.getString("userProfilePic")).into(thirdPlayerIv);
                            thirdPlayerNameTv.setText(userName);
                            thirdPlayerCityTv.setText(otherUserCIty);
                            thirdPlayerGenderTv.setText(userGender);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParam2() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", thirdid);
        return params;
    }
}
