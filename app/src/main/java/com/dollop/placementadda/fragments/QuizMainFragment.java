package com.dollop.placementadda.fragments;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cunoraz.gifview.library.GifView;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.dollop.placementadda.activity.ProgramListActivity;
import com.dollop.placementadda.activity.OurPlacementActivity;
import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.CreateGroupActivity;
import com.dollop.placementadda.activity.HomeSubjectActivity;
import com.dollop.placementadda.activity.LeaderBoardActivity;
import com.dollop.placementadda.activity.LiveQuizActivity;
import com.dollop.placementadda.activity.QuizMainActivity;
import com.dollop.placementadda.activity.QuizMultiplierActivity;
import com.dollop.placementadda.activity.RecentQuizActivity;
import com.dollop.placementadda.activity.category.QuizCategoryActivity;
import com.dollop.placementadda.adapter.LeaderBoardAdapter;
import com.dollop.placementadda.adapter.MainFragmentAdapter;
import com.dollop.placementadda.broadcast.MyBroadcastReceiver;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.library.LuckyWheelView;
import com.dollop.placementadda.library.model.LuckyItem;
import com.dollop.placementadda.model.BannerModel;

import com.dollop.placementadda.model.LeaderBoardModel;
import com.dollop.placementadda.model.MainFragModel;
import com.dollop.placementadda.model.TimeLineCommentModel;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import me.relex.circleindicator.CircleIndicator;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.INPUT_METHOD_SERVICE;

public class QuizMainFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    RecyclerView landingRV;
    List<MainFragModel> mainFragModelList = new ArrayList<>();
    ArrayList<BannerModel> sliderArrayList = new ArrayList<BannerModel>();
    private static ViewPager mPager;
    private static int currentPage = 0;
    CardView mainCategoryCv;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private ImageView cdHistoryId;
    private CardView liveQuizeId;
    private CardView recentQuizId;
    private CardView cdSubjectId;
    private CardView createGroupId;
    private CardView multiplierQuizId;
    private CardView cdOurPlacementId;
    private CardView cdCodingProblemId;
    CircleImageView ivFirstRankId;
    TextView tvFirstRankId;
    private CircleImageView ivSecondRankId;
    private TextView tvSecondId;
    private CircleImageView ivThirdRankId;
    private TextView tvThirdId;
    private CircleIndicator indicator;
    List<LuckyItem> data = new ArrayList<>();
    private AdView mAdview;
    private LinearLayout bottom_sheet;

    private InterstitialAd mInterstitialAd;
    SliderLayout sliderLayout;
    HashMap<String, String> Hash_file_maps;
    PagerIndicator pagerIndicator;
    private String xyz;
    int avoidCirculeRepeataion = 0;

    //    For Leaderboard
//Leader Board Activity is use for show user rank place in this app total quiz result
    int mypostion = 0;
    RecyclerView leaderBoardRecyclerView;
    //RecycleView landingRecyclerView is use for show listing
    private ArrayList<LeaderBoardModel> leaderBoardModelArrayList = new ArrayList<>();

    private LinearLayoutManager linearlayoutManage;
    private LeaderBoardAdapter pdfAdapter;
    ImageView bottomSHeetLeaderImageView;
    BottomSheetBehavior sheetBehavior;
    private TextView rankTV;
    private TextView UserName;
    private TextView totalNumberTV;
    CircleImageView ic_user_imageview;


    public QuizMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz_main, container, false);
        AdView mAdView = view.findViewById(R.id.adView);


        MobileAds.initialize(getActivity(), "ca-app-pub-9640495884863409~1242406785");
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("02C53424006E9E58B04EA68DD69C2521").build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("02C53424006E9E58B04EA68DD69C2521").build());

        bottom_sheet = view.findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        getLeaderBoardRanks();
        ((QuizMainActivity) getActivity()).launchFragmentTitle("Placement Adda..");

        leaderBoardRecyclerView = view.findViewById(R.id.leaderBoardRecyclerView);
        mainCategoryCv = (CardView) view.findViewById(R.id.mainCategoryCv);
        liveQuizeId = (CardView) view.findViewById(R.id.liveQuizeId);
        recentQuizId = (CardView) view.findViewById(R.id.recentQuizId);
        cdSubjectId = (CardView) view.findViewById(R.id.cdSubjectId);
        landingRV = (RecyclerView) view.findViewById(R.id.landingRecyclerView);
        cdHistoryId = (ImageView) view.findViewById(R.id.cdLeaderBoardId);
        createGroupId = (CardView) view.findViewById(R.id.createGroupId);
        multiplierQuizId = (CardView) view.findViewById(R.id.multiplierQuizId);
        //anil
        cdOurPlacementId = (CardView) view.findViewById(R.id.cdOurPlacementId);
        cdCodingProblemId = (CardView) view.findViewById(R.id.cdCodingProblemId);

        ivFirstRankId = (CircleImageView) view.findViewById(R.id.ivFirstRankId);
        tvFirstRankId = (TextView) view.findViewById(R.id.tvFirstRankId);
        ivSecondRankId = (CircleImageView) view.findViewById(R.id.ivSecondRankId);
        tvSecondId = (TextView) view.findViewById(R.id.tvSecondId);
        ivThirdRankId = (CircleImageView) view.findViewById(R.id.ivThirdRankId);
        tvThirdId = (TextView) view.findViewById(R.id.tvThirdId);
        bottomSHeetLeaderImageView = view.findViewById(R.id.bottomSHeetLeaderImageView);
//     ids for show current user ranking in leaderboard
        rankTV = view.findViewById(R.id.rankTV);
        UserName = view.findViewById(R.id.UserName);
        totalNumberTV = view.findViewById(R.id.totalNumberTV);
        ic_user_imageview = view.findViewById(R.id.ic_user_imageview);
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREFS", 0);
        int lastDay = sharedPreferences.getInt("day", 1);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        bottomSHeetLeaderImageView.setImageResource(R.drawable.new_leaderboard_above_img);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //btnBottomSheet.setText("Close Sheet");
                        bottomSHeetLeaderImageView.setImageResource(0000);
                        bottomSHeetLeaderImageView.setImageResource(R.drawable.new_leader_below_img);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        // btnBottomSheet.setText("Expand Sheet");
                        bottomSHeetLeaderImageView.setImageResource(R.drawable.new_leaderboard_above_img);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        bottomSHeetLeaderImageView.setImageResource(R.drawable.new_leaderboard_above_img);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        //getUserOnlineStatus();
        //  getCoinsDialogBox();
        if (lastDay != currentDay) {
            getCoinsDialogBox();
            // getDailyCoinsDialogBox();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("day", currentDay);
            editor.commit();
        } else {
            // Toast.makeText(getContext(), "Please try next day", Toast.LENGTH_SHORT).show();
        }
        startAlert();

        multiplierQuizId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                S.I(getActivity(), QuizMultiplierActivity.class, null);
            }
        });
        createGroupId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                S.I(getActivity(), CreateGroupActivity.class, null);
            }
        });
        cdHistoryId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                S.I(getActivity(), LeaderBoardActivity.class, null);
            }
        });

        recentQuizId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "QuizCategery");
                S.I(getActivity(), RecentQuizActivity.class, bundle);
            }
        });

        liveQuizeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "QuizCategery");
                S.I(getActivity(), LiveQuizActivity.class, bundle);
            }
        });

        cdSubjectId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                S.I(getActivity(), HomeSubjectActivity.class, null);
            }
        });
        //anil
        cdOurPlacementId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Coming Soon",Toast.LENGTH_LONG).show();
              //  S.I(getActivity(), OurPlacementActivity.class, null);
            }
        });
        cdCodingProblemId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   S.I(getActivity(), ProgramListActivity.class, null);
                Toast.makeText(getActivity(),"Coming Soon",Toast.LENGTH_LONG).show();
            }
        });


        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        landingRV.setLayoutManager(gaggeredGridLayoutManager);
        landingRV.setHasFixedSize(true);

        mainCategoryCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("ActivityCheck", "QuizCategery");
                S.I(getActivity(), QuizCategoryActivity.class, bundle);
            }
        });

        //getLeaderTopThree();
        MainFragmentAdapter mainFragModel = new MainFragmentAdapter(getActivity(), mainFragModelList);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mainFragModel);
        landingRV.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        sliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        pagerIndicator = (PagerIndicator) view.findViewById(R.id.banner_slider_indicator);
        hideSoftKeyboard();
        getBannerAds();
        S.E("fcm token id" + SavedData.getFcmTokenID());
        return view;
    }

    private void getLeaderBoardRanks() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GetAllUserByRank, 1, getParmsans(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            LeaderBoardModel leaderBoardModel = new LeaderBoardModel();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (i < 10) {
                                leaderBoardModel.setUser_id(jsonObject1.getString("user_id"));
                                leaderBoardModel.setUserProfilePic(jsonObject1.getString("userProfilePic"));
                                leaderBoardModel.setUserName(jsonObject1.getString("userName"));
                                leaderBoardModel.setRankingPoints(jsonObject1.getString("rankingPoints"));
                                leaderBoardModelArrayList.add(leaderBoardModel);
                            }

                         /*   if (jsonObject1.getString("user_id").equals(UserDataHelper.getInstance().getList().get(0).getUserId())) {
                                mypostion = i;
                                final int h1 = leaderBoardRecyclerView.getHeight();
                                final int h2 = leaderBoardRecyclerView.getHeight();
                                leaderBoardRecyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        S.E("now : 2 + " + mypostion);
                                        leaderBoardRecyclerView.smoothScrollToPositionFromTop(mypostion - 3, h1 / 2 - h2 / 2);
                                    }
                                }, 500);
                            }*/

                            if (jsonObject1.getString("user_id").equals(UserDataHelper.getInstance().getList().get(0).getUserId())) {

                                leaderBoardModel.setUserProfilePic(jsonObject1.getString("userProfilePic"));
                                String username = jsonObject1.getString("userName");
                                S.E("username" + username);
                                String ranking = jsonObject1.getString("rankingPoints");
                                S.E("ranking" + ranking);
                                Picasso.with(getActivity()).load(Const.URL.IMAGE_URL +jsonObject1.getString("userProfilePic")).error(R.drawable.ic_user).into(ic_user_imageview);

                                UserName.setText(username);
                                totalNumberTV.setText(ranking);

                                int totalMyRank = i + 1;
                                rankTV.setText("" + totalMyRank);

                            }

                        }
                        pdfAdapter = new LeaderBoardAdapter(getActivity(), leaderBoardModelArrayList, 0);
                        leaderBoardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        leaderBoardRecyclerView.setAdapter(pdfAdapter);
//                        pdfAdapter.notifyDataSetChanged();
                    } else {
                        S.T(getActivity(), "" + message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParmsans() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
        return params;
    }

//    For coins

    private void getCoinsDialogBox() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_get_coins);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView selected_position_text = (TextView) dialog.findViewById(R.id.selected_position_text);
        final Button btnGetCoin = (Button) dialog.findViewById(R.id.btnGetCoin);
        btnGetCoin.setVisibility(View.GONE);
        ImageView cancel_img = (ImageView) dialog.findViewById(R.id.cancel_img);
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        selected_position_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //btnGetCoin.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnGetCoin.setVisibility(View.VISIBLE);
            }
        });

        final LuckyWheelView luckyWheelView = (LuckyWheelView) dialog.findViewById(R.id.luckyWheel);
        luckyWheelView.setRound(1);
        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.text = "10";
        luckyItem1.icon = R.drawable.test1;
        luckyItem1.color = 0xffFFF3E0;
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.text = "20";
        luckyItem2.icon = R.drawable.test2;
        luckyItem2.color = 0xffFFE0B2;
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.text = "30";
        luckyItem3.icon = R.drawable.test3;
        luckyItem3.color = 0xffFFCC80;
        data.add(luckyItem3);

        //////////////////
        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.text = "40";
        luckyItem4.icon = R.drawable.test4;
        luckyItem4.color = 0xffFFF3E0;
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.text = "50";
        luckyItem5.icon = R.drawable.test5;
        luckyItem5.color = 0xffFFE0B2;
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.text = "60";
        luckyItem6.icon = R.drawable.test6;
        luckyItem6.color = 0xffFFCC80;
        data.add(luckyItem6);
        //////////////////

        //////////////////////
        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.text = "70";
        luckyItem7.icon = R.drawable.test7;
        luckyItem7.color = 0xffFFF3E0;
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.text = "80";
        luckyItem8.icon = R.drawable.test8;
        luckyItem8.color = 0xffFFE0B2;
        data.add(luckyItem8);


        LuckyItem luckyItem9 = new LuckyItem();
        luckyItem9.text = "90";
        luckyItem9.icon = R.drawable.test9;
        luckyItem9.color = 0xffFFCC80;
        data.add(luckyItem9);
        ////////////////////////

        luckyWheelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (avoidCirculeRepeataion == 0) {
                    avoidCirculeRepeataion++;
                    int index = getRandomIndex();
                    luckyWheelView.startLuckyWheelWithTargetIndex(index);
                }
            }
        });
        /////////////////////

        luckyWheelView.setData(data);
        luckyWheelView.setRound(getRandomRound());
        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                try {
                    LuckyItem luckyItem = data.get(index - 1);
                    //     Toast.makeText(getActivity(), luckyItem.text, Toast.LENGTH_SHORT).show();
                    TextView selected_position_text = (TextView) dialog.findViewById(R.id.selected_position_text);
                    xyz = luckyItem.text;
                    selected_position_text.setText(xyz);
                    SavedData.saveCoins(xyz);
                    S.E("save amount" + xyz);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnGetCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                AddCoin(xyz, "Get Daily Coins");
            }
        });
        try {
            dialog.show();
        } catch (Exception e) {

        }
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/dir1/dir2");
        dir.mkdirs();
    }

    private void startAlert() {
        Intent intent = new Intent(getContext(), MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getActivity(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1 * 1000 * 60), pendingIntent);
        //Toast.makeText(getActivity(), "Alarm set in seconds",Toast.LENGTH_LONG).show();
    }


    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }


    private void getLeaderTopThree() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GetAllUserByRank, 1, S.getParams(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    int mypostion = 10;

                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");


                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            LeaderBoardModel leaderBoardModel = new LeaderBoardModel();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            leaderBoardModel.setUser_id(jsonObject1.getString("user_id"));
                            leaderBoardModel.setUserProfilePic(jsonObject1.getString("userProfilePic"));
                            leaderBoardModel.setUserName(jsonObject1.getString("userName"));
                            leaderBoardModel.setRankingPoints(jsonObject1.getString("rankingPoints"));

                            if (i == 0) {

                                Picasso.with(getActivity()).load(Const.URL.IMAGE_URL + leaderBoardModel.getUserProfilePic()).error(R.drawable.ic_user).into(ivFirstRankId);
                                tvFirstRankId.setText(jsonObject1.getString("userName"));

                            } else if (i == 1) {
                                Picasso.with(getActivity()).load(Const.URL.IMAGE_URL + leaderBoardModel.getUserProfilePic()).error(R.drawable.ic_user).into(ivSecondRankId);
                                tvSecondId.setText(jsonObject1.getString("userName"));

                            } else if (i == 2) {
                                Picasso.with(getActivity()).load(Const.URL.IMAGE_URL + leaderBoardModel.getUserProfilePic()).error(R.drawable.ic_user).into(ivThirdRankId);
                                tvThirdId.setText(jsonObject1.getString("userName"));

                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
    }


    private void getBannerAds() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.getBannerList, 1, S.getParams(), new Helper() {

            @Override
            public void backResponse(String response) {
                S.E("Check By Ani" + response);
                S.E("Check By Ani" + S.getParams());
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    if (jsonObject1.getString("status").equals("200")) {
                        JSONArray jsonArray = jsonObject1.getJSONArray("data");
                        Hash_file_maps = new HashMap<String, String>();
                        Hash_file_maps.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            BannerModel bannerModel = new BannerModel();
                            JSONObject jsonObject11 = jsonArray.getJSONObject(i);
                            String banner_id = jsonObject11.getString("banner_id");
                            String bannerImage = jsonObject11.getString("bannerImage");
                            String bannerCreatedDate = jsonObject11.getString("bannerCreatedDate");
                            bannerModel.setBanner_id(banner_id);
                            bannerModel.setBannerImage(bannerImage);
                            bannerModel.setBannerCreatedDate(bannerCreatedDate);
                            sliderArrayList.add(bannerModel);
                            Hash_file_maps.put(String.valueOf(i), Const.URL.Banner_Image + jsonObject11.getString("bannerImage"));
                        }
                        S.E("Hash_file_maps" + Hash_file_maps.size());
                        for (String name : Hash_file_maps.keySet()) {

                            DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
                            textSliderView
                                    .description(name)
                                    .image(Hash_file_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop);
                            /*.setOnSliderClickListener(this);*/
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra", name);
                            sliderLayout.addSlider(textSliderView);
                        }

                        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        sliderLayout.setCustomAnimation(new DescriptionAnimation());
                        sliderLayout.setDuration(5000);
                        sliderLayout.setCustomIndicator(pagerIndicator);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private int getRandomIndex() {
        Random rand = new Random();
        return rand.nextInt(data.size() - 1) + 0;
    }

    private int getRandomRound() {
        Random rand = new Random();
        return rand.nextInt(10) + 15;
    }

    @Override
    public void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void AddCoin(String amount, String Status) {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.addWalletAmount, 1, getCommentParams(amount, Status), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("getContest response" + response);
                try {
                    ArrayList<TimeLineCommentModel> timeLineCommentModelslist = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("message").equals("success")) {
                        /* getPostOnDiscussion();*/
                        ConfirmPopup();
                    } else {
                        S.T(getActivity(), jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    S.E("errror" + e);
                }
            }/*167222*/
        });

    }

    protected Map<String, String> getCommentParams(String amount, String Status) {
        Map<String, String> param = new HashMap<>();
        param.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        param.put("amount", amount);
        param.put("for", Status);
        S.E("prams : : " + param);
        return param;
    }

    public void ConfirmPopup() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.paymentsucess_popup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        ImageView cancel_img, full_image;
        Button continue_btn;
        TextView textview;
        GifView gifView1 = (GifView) dialog.findViewById(R.id.gif1);
        cancel_img = (ImageView) dialog.findViewById(R.id.cancel_img);
        continue_btn = (Button) dialog.findViewById(R.id.continue_btn);
        textview = (TextView) dialog.findViewById(R.id.textview);
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}