package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.SelectFourPlayerAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuizPlayFourPlayerDetails extends BaseActivity {
    CircleImageView fourPlayerQuizLogoIv, firstPlyrIv, secondPlyrIv, thrdPlayerIv, fourthPlayerIv;
    TextView firstPlyrNameTv, firstPlyrGenderTv, firstPlyrCityTv, secondPlyrNameTv, secondPlyrGenderTv, secondPlyrCityTv, thrdPlayerNameTv, thrdPlayerGenderTv, thrdPlayerCityTv, fourthPlayerNameTv, fourthPlayerGenderTv, fourthPlayerCityTv;
    ArrayList<String> playersIds = new ArrayList<>();
    String secondPlayerId, thirdPlayerId, fourthPlayerId;

    @Override
    protected int getContentResId() {
        return R.layout.activity_quiz_play_four_player_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(QuizPlayFourPlayerDetails.this);
        setToolbarWithBackButton("Four Players");

        fourPlayerQuizLogoIv = findViewById(R.id.fourPlayerQuizLogoIv);
        firstPlyrIv = findViewById(R.id.firstPlyrIv);
        secondPlyrIv = findViewById(R.id.secondPlyrIv);
        thrdPlayerIv = findViewById(R.id.thrdPlayerIv);
        fourthPlayerIv = findViewById(R.id.fourthPlayerIv);
        firstPlyrNameTv = findViewById(R.id.firstPlyrNameTv);
        firstPlyrGenderTv = findViewById(R.id.firstPlyrGenderTv);
        firstPlyrCityTv = findViewById(R.id.firstPlyrCityTv);
        secondPlyrNameTv = findViewById(R.id.secondPlyrNameTv);
        secondPlyrGenderTv = findViewById(R.id.secondPlyrGenderTv);
        secondPlyrCityTv = findViewById(R.id.secondPlyrCityTv);
        thrdPlayerNameTv = findViewById(R.id.thrdPlayerNameTv);
        thrdPlayerGenderTv = findViewById(R.id.thrdPlayerGenderTv);
        thrdPlayerCityTv = findViewById(R.id.thrdPlayerCityTv);
        fourthPlayerNameTv = findViewById(R.id.fourthPlayerNameTv);
        fourthPlayerGenderTv = findViewById(R.id.fourthPlayerGenderTv);
        fourthPlayerCityTv = findViewById(R.id.fourthPlayerCityTv);


        for (int i = 0; i < SelectFourPlayerAdapter.leaderBoardModelList.size(); i++) {
            if (SelectFourPlayerAdapter.leaderBoardModelList.get(i).getSelected()) {
                String toto = (" " + SelectFourPlayerAdapter.leaderBoardModelList.get(i).getUser_id());
                playersIds.add(SelectFourPlayerAdapter.leaderBoardModelList.get(i).getUser_id());
            }
        }
        secondPlayerId = playersIds.get(0);
        thirdPlayerId = playersIds.get(1);
        fourthPlayerId = playersIds.get(2);

        getFirstPlayerDetails();
        getSecondPlayerDetails();
        getThirdPlayerDetails();
        getFourthPlayerDetails();

        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(QuizPlayFourPlayerDetails.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }


    /*First Player Details Json Parsing...*/
    private void getFirstPlayerDetails() {
        new JSONParser(QuizPlayFourPlayerDetails.this).parseVollyStringRequest(Const.URL.GETPLAYERDETAIL, 1, getParam(), new Helper() {

            @Override
            public void backResponse(String response) {
                //Json parser use for get category method is use for gate category accroding to get param from param we pass 0 for get all category
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
                            Picasso.with(QuizPlayFourPlayerDetails.this).
                                    load(Const.URL.IMAGE_URL + jsonObject1.getString("userProfilePic")).into(firstPlyrIv);
                            firstPlyrNameTv.setText(userName);
                            firstPlyrCityTv.setText(otherUserCIty);
                            firstPlyrGenderTv.setText(userGender);
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


    /*Second players Details Json Parsing*/
    private void getSecondPlayerDetails() {

        new JSONParser(QuizPlayFourPlayerDetails.this).parseVollyStringRequest(Const.URL.GETPLAYERDETAIL, 1, getParam1(), new Helper() {

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
                            Picasso.with(QuizPlayFourPlayerDetails.this).
                                    load(Const.URL.IMAGE_URL + jsonObject1.getString("userProfilePic")).into(secondPlyrIv);
                            secondPlyrNameTv.setText(userName);
                            secondPlyrCityTv.setText(otherUserCIty);
                            secondPlyrGenderTv.setText(userGender);
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
        params.put("user_id", secondPlayerId);
        return params;
    }

    /*Third Players Details Json Parsing */
    private void getThirdPlayerDetails() {
        new JSONParser(QuizPlayFourPlayerDetails.this).parseVollyStringRequest(Const.URL.GETPLAYERDETAIL, 1, getParam2(), new Helper() {

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
                            Picasso.with(QuizPlayFourPlayerDetails.this).
                                    load(Const.URL.IMAGE_URL + jsonObject1.getString("userProfilePic")).into(thrdPlayerIv);
                            thrdPlayerNameTv.setText(userName);
                            thrdPlayerCityTv.setText(otherUserCIty);
                            thrdPlayerGenderTv.setText(userGender);
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
        params.put("user_id", thirdPlayerId);
        return params;
    }

    /*Fourth Player Details Jsong Parsing...*/
    private void getFourthPlayerDetails() {
        new JSONParser(QuizPlayFourPlayerDetails.this).parseVollyStringRequest(Const.URL.GETPLAYERDETAIL, 1, getParam3(), new Helper() {

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
                            Picasso.with(QuizPlayFourPlayerDetails.this).
                                    load(Const.URL.IMAGE_URL + jsonObject1.getString("userProfilePic")).into(fourthPlayerIv);
                            fourthPlayerNameTv.setText(userName);
                            fourthPlayerCityTv.setText(otherUserCIty);
                            fourthPlayerGenderTv.setText(userGender);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParam3() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", fourthPlayerId);
        return params;
    }

}
