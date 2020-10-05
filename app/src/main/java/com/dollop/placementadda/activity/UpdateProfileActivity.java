package com.dollop.placementadda.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.activity.basic.SignUpActivity;
import com.dollop.placementadda.adapter.PlaceArrayAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.database.model.UserModel;
import com.dollop.placementadda.model.CollageNameModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.UserAccount;
import com.dollop.placementadda.sohel.multipart.AppHelper;
import com.dollop.placementadda.sohel.multipart.VolleyMultipartRequest;
import com.dollop.placementadda.sohel.multipart.VolleySingleton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class UpdateProfileActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    Compressor compressor;
    Button updateBtn;
    EditText etSignUpName, etSignUpEmail, etSignUpPhone, etSignUpDob, etSignUpDoJ, etSignUpPass, etSignUpCity, etSignUpConfPass;
    DatePickerDialog datePickerDialog;
    AutoCompleteTextView etSignUpAddress;
    LinearLayout parentSignUpLayout;
    Animation uptodown, downtoup;
    Spinner collegeNameSp;
    RadioButton maleRB, femaleRB;
    DatePicker picker;
    String Gender;
    RadioGroup radioGroupId;
    CircleImageView userprofile;
    public static final int PERMISSION_REQUEST_CODE = 1111;
    private static final int REQUEST = 1337;
    public static int SELECT_FROM_GALLERY = 2;
    public static int CAMERA_PIC_REQUEST = 0;
    Uri mImageCaptureUri;
    int screenWidth = 500;
    Bitmap productImageBitmap;
    ImageView ivEditProfilePicId;
    EditText etCollegeName;
    private String selectedCollage = "";
    private String selectedGender = "";
    private String hidden_image = "";
    private String[] collegeList = {"Select College", "M.I.T. Indore", "Acropolis Institute Indore", "SDPS College Indore", "IPS Academy Indore", "Chameli Devi College Indore", "Prestige College Indore"};
    private int aPosition = 0;
    String userProfilePic, userAddress, userEmail, user_id = "", userName = "", userPhone = "", userDOB, userDOJ, userGender, userCollegeName, user_is_verified, user_is_active;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    private String[] courseList = {"Select Course", "MCA", "BE", "BCA", "Msc"};
    private String[] subjectList = {"Select Subject", "Computer Science", "It"};

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<CollageNameModel> collageNameModelArrayList = new ArrayList<>();
    private String selectcollsgrId;
    private String Image_in_string, Collage_Name = "";
    private double latitude;
    private double longitude;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private String telephonyManagerStr = "";
    private Spinner spCourseNameId;
    private Spinner spSubjectNameId;
    private String courseName="";
    private String subjectStr="";

    @Override
    protected int getContentResId() {
        return R.layout.activity_update_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(UpdateProfileActivity.this);
        getProfile();
        setToolbarWithBackButton("Update Profile");
        mGoogleApiClient = new GoogleApiClient.Builder(UpdateProfileActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        updateBtn = (Button) findViewById(R.id.updateBtn);
        parentSignUpLayout = (LinearLayout) findViewById(R.id.parentSignUpLayout);
        etSignUpName = (EditText) findViewById(R.id.etSignUpName);
        etCollegeName = (EditText) findViewById(R.id.etCollegeName);
        etSignUpEmail = (EditText) findViewById(R.id.etSignUpEmail);
        etSignUpPhone = (EditText) findViewById(R.id.etSignUpPhone);
        etSignUpDob = (EditText) findViewById(R.id.etSignUpDob);

        spCourseNameId = (Spinner) findViewById(R.id.spCourseNameId);
        spSubjectNameId = (Spinner) findViewById(R.id.spSubjectNameId);

        etSignUpDob = (EditText) findViewById(R.id.etSignUpDob);
        etSignUpDob = (EditText) findViewById(R.id.etSignUpDob);


        etSignUpDoJ = (EditText) findViewById(R.id.etSignUpDoJ);
        etSignUpAddress = (AutoCompleteTextView) findViewById(R.id.etSignUpAddress);
        radioGroupId = (RadioGroup) findViewById(R.id.radioGroupId);
        collegeNameSp = (Spinner) findViewById(R.id.collegeNameSp);
        maleRB = (RadioButton) findViewById(R.id.maleRB);
        femaleRB = (RadioButton) findViewById(R.id.femaleRB);
        userprofile = (CircleImageView) findViewById(R.id.userprofile);
        ivEditProfilePicId = (ImageView) findViewById(R.id.ivEditProfilePicId);
        etSignUpCity = (EditText) findViewById(R.id.etSignUpCity);

        ArrayAdapter<String> stringArrayAdapterCourse = new ArrayAdapter<>(UpdateProfileActivity.this, android.R.layout.simple_dropdown_item_1line, courseList);
        spCourseNameId.setAdapter(stringArrayAdapterCourse);
        spCourseNameId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 courseName=adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> stringArrayAdapterSubject = new ArrayAdapter<>(UpdateProfileActivity.this, android.R.layout.simple_dropdown_item_1line, subjectList);
        spSubjectNameId.setAdapter(stringArrayAdapterSubject);
        spSubjectNameId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 subjectStr=adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        askForPermission(Manifest.permission.READ_PHONE_STATE, 101);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        S.E("check runtime" + telephonyManager.getDeviceId());

        telephonyManagerStr = telephonyManager.getDeviceId();
        try {
            etSignUpAddress.setThreshold(3);
            etSignUpAddress.setOnItemClickListener(mAutocompleteClickListener);
            mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                    BOUNDS_MOUNTAIN_VIEW, null);
            etSignUpAddress.setAdapter(mPlaceArrayAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Get the bundle

        getCollageName();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                S.E("etSignUpName::" + etSignUpName + "etSignUpName::" + etSignUpName);
                if (UserAccount.isEmpty(etSignUpName, etSignUpEmail, etSignUpPhone, etSignUpDob, etSignUpCity, etSignUpAddress)) {
                    S.E("update btn k andr aa rha h");
                    if (selectcollsgrId.equals("")) {
                        S.T(UpdateProfileActivity.this, "Please Select Collage Name");
                    } else {
                        updateProfile();
                    }

                    //  S.T(UpdateProfileActivity.this,"If me aa rha h");


                } else {
                    UserAccount.EditTextPointer.setError("Field Can't be empty");
                    UserAccount.EditTextPointer.setFocusable(true);
                    S.E("update btn k andr aa rha h");

                    // S.T(UpdateProfileActivity.this,"else me aa rha h");
                }
            }
        });
//        usign datepicker dialog box for date of birth
        radioGroupId.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                selectedGender = radioButton.getText().toString();

            }
        });
        etSignUpDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(UpdateProfileActivity.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                etSignUpDob.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

//        using datepicker dialog boz for date of joining
        etSignUpDoJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog


                datePickerDialog = new DatePickerDialog(UpdateProfileActivity.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                etSignUpDoJ.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        ivEditProfilePicId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("userProfilePic", userProfilePic);
                S.I(UpdateProfileActivity.this, ImagePreviewActivity.class, bundle);

            }
        });
        if (UserDataHelper.getInstance().getList().size() > 0) {
            //getProfile();
            etSignUpName.setText(UserDataHelper.getInstance().getList().get(0).getUserName());
            etSignUpPhone.setText(UserDataHelper.getInstance().getList().get(0).getUserPhone());
            etSignUpDob.setText(UserDataHelper.getInstance().getList().get(0).getUserDob());
            etSignUpDoJ.setText(UserDataHelper.getInstance().getList().get(0).getUserDoj());
            etSignUpEmail.setText(UserDataHelper.getInstance().getList().get(0).getUserEmail());
            etSignUpAddress.setText(UserDataHelper.getInstance().getList().get(0).getUserAddress());
            etSignUpCity.setText(UserDataHelper.getInstance().getList().get(0).getCity());

            if (UserDataHelper.getInstance().getList().get(0).getUserGender().equals("Male")) {
                maleRB.setChecked(true);
            } else if (UserDataHelper.getInstance().getList().get(0).getUserGender().equals("Female")) {
                femaleRB.setChecked(true);
            }
            try {
                Image_in_string = UserDataHelper.getInstance().getList().get(0).getProfile_pic();
                S.E("image of updte prof" + Image_in_string);

              /*  Picasso.with(UpdateProfileActivity.this).load(Image_in_string)
                        .error(R.drawable.ic_user).into(userprofile);*/

                Picasso.with(UpdateProfileActivity.this).load(Const.URL.IMAGE_URL + Image_in_string).error(R.drawable.ic_user).into(userprofile);

            } catch (Exception e) {
                e.getMessage();
            }
        }
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(UpdateProfileActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));



    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(UpdateProfileActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateProfileActivity.this, permission)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(UpdateProfileActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(UpdateProfileActivity.this, new String[]{permission}, requestCode);
            }
        } else {
/*
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
*/
        }
    }

    private void getProfile() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.getProfile, 1, getParams12(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    S.E("checkProfileData:1:"+response);
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
                    Picasso.with(UpdateProfileActivity.this).load(Const.URL.IMAGE_URL + userProfilePic).error(R.drawable.ic_user).into(userprofile);

                    String course= jsonObject11.getString("course");
                    String branch= jsonObject11.getString("branch");
                 /* etUserName.setText(userName);
                    etUserEmail.setText(userEmail);
                    etUserNum.setText(userPhone);*/
                 /*   if (jsonObject11.getString("userDOJ").equals("")) {
                        S.I_clear(QuizMainActivity.this, UpdateProfileActivity.class, null);
                    }*/
                    for(int i=0;i<courseList.length;i++){
                        S.E("checkCourse::"+subjectList[0]+"courseList::"+course);
                        if(courseList[i].equals(course)){
                            spCourseNameId.setSelection(i);

                        }
                    }

                    for(int i=0;i<subjectList.length;i++){
                        S.E("checkSubjectList::"+subjectList[0]+"checkSubjectJson::"+branch);
                        if(subjectList[i].equals(branch)){
                            spSubjectNameId.setSelection(i);

                        }
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
                    userModel.setBranch(branch);
                    userModel.setCourse(course);
                    userModel.setUserImeiNo(UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
                    UserDataHelper.getInstance().insertData(userModel);

                } catch (Exception e) {
                    e.printStackTrace();
                    S.E("checkErrorHere Error:1:"+e);
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


    private void getProfile2() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.getProfile, 1, getParams12(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    S.E("checkResponse Here:2:"+response);
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


                    String branch = jsonObject11.getString("branch");
                    String course = jsonObject11.getString("course");



                    Picasso.with(UpdateProfileActivity.this).load(Const.URL.IMAGE_URL + userProfilePic).error(R.drawable.ic_user).into(userprofile);

                 /* etUserName.setText(userName);
                    etUserEmail.setText(userEmail);
                    etUserNum.setText(userPhone);*/
                 /*   if (jsonObject11.getString("userDOJ").equals("")) {
                        S.I_clear(QuizMainActivity.this, UpdateProfileActivity.class, null);
                    }*/
                    for(int i=0;i<courseList.length;i++){
                        S.E("checkCourse::"+subjectList[0]+"courseList::"+course);
                        if(courseList[i].equals(course)){
                            spCourseNameId.setSelection(i);

                        }
                    }

                    for(int i=0;i<subjectList.length;i++){
                        S.E("checkSubjectList::"+subjectList[0]+"checkSubjectJson::"+branch);
                        if(subjectList[i].equals(branch)){
                            spSubjectNameId.setSelection(i);

                        }
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
                    userModel.setCity(etSignUpCity.getText().toString());
                    userModel.setProfile_pic(userProfilePic);
                    userModel.setUserImeiNo(UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
                    userModel.setCourse(course);
                    userModel.setBranch(branch);
                    UserDataHelper.getInstance().insertData(userModel);
                    S.I_clear(UpdateProfileActivity.this, QuizMainActivity.class, null);

                } catch (Exception e) {
                    S.E("checkErrorHere Error:2:"+e);
                    e.printStackTrace();
                }
            }
        });
    }


    //    for google places api
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            S.E("place" + place);
            S.E("attributions" + attributions);

        }
    };

    private void selectImage() {
        final CharSequence[] options = {"From Camera", "From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please choose an Image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("From Camera")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkCameraPermission())
                            cameraIntent();
                        else
                            requestPermission();
                    } else
                        cameraIntent();
                } else if (options[item].equals("From Gallery")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkGalleryPermission())
                            galleryIntent();
                        else
                            requestGalleryPermission();
                    } else
                        galleryIntent();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.create().show();
    }

    private void galleryIntent() {
        Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FROM_GALLERY);
    }

    private void cameraIntent() {

        S.E("OnlyCheck");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(UpdateProfileActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(UpdateProfileActivity.this, new String[]{READ_EXTERNAL_STORAGE}, REQUEST);
    }

    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(UpdateProfileActivity.this, Manifest.permission.CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGalleryPermission() {
        int result2 = ContextCompat.checkSelfPermission(UpdateProfileActivity.this, READ_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK && null != data) {
            Uri cameraURI = data.getData();
            productImageBitmap = (Bitmap) data.getExtras().get("data");
            /*compressor = new Compressor(this).compressToFile(productImageBitmap);*/
            userprofile.setImageBitmap(productImageBitmap);
        } else if (requestCode == SELECT_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
            Uri galleryURI = data.getData();
            userprofile.setImageURI(galleryURI);
            Image_in_string = galleryURI.toString();
            //String image = UserDataHelper.getInstance().getList().get(0).getProfile_pic();
            Picasso.with(UpdateProfileActivity.this).load(Image_in_string)
                    .error(R.drawable.ic_user).into(userprofile);
        }
    }

    public void updateProfile() {
        final Dialog dialog = S.initProgressDialog(UpdateProfileActivity.this);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Const.URL.UPDATE_PROFILE, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                S.E("checkTesting" + resultResponse);
                try {
                    dialog.dismiss();
//                    getClientProfile();
                    JSONObject jsonObject1 = new JSONObject(resultResponse);
                    if (jsonObject1.getString("status").equals("200")) {
                        S.T(UpdateProfileActivity.this,"Successfully Updated");
                       /* UserModel userModel = new UserModel();
                        userModel.setUserId(UserDataHelper.getInstance().getList().get(0).getUserId());
                        userModel.setUserName(etSignUpName.getText().toString());
                        userModel.setUserEmail(etSignUpEmail.getText().toString());
                        userModel.setUserPhone(etSignUpPhone.getText().toString());
                        userModel.setUserCollegeName(Collage_Name);
                        userModel.setUserGender(selectedGender);
                        userModel.setUserDob(etSignUpDob.getText().toString());
                        userModel.setUserDoj(etSignUpDoJ.getText().toString());
                        userModel.setUserAddress(etSignUpAddress.getText().toString());
                        userModel.setCity(etSignUpCity.getText().toString());
                        userModel.setProfile_pic(Image_in_string);
                        userModel.setUserImeiNo(UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
                        UserDataHelper.getInstance().insertData(userModel);*/

                        getProfile2();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                dialog.dismiss();
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
                params.put("userName", etSignUpName.getText().toString());
                params.put("userEmail", etSignUpEmail.getText().toString());
                params.put("userCollegeName", etCollegeName.getText().toString());
                params.put("userCollegeId", selectcollsgrId);
                params.put("userGender", selectedGender);
                params.put("userDOB", etSignUpDob.getText().toString());
                params.put("userDOJ", etSignUpDoJ.getText().toString());
                params.put("userCity", etSignUpCity.getText().toString());
                params.put("userAddress", etSignUpAddress.getText().toString());
                params.put("course",courseName);
                params.put("branch", subjectStr);
                params.put("hidden_image", hidden_image);
                params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());
                S.E("params" + params);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    Bitmap bitmap = ((BitmapDrawable) userprofile.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);


                    // Image_in_string= Base64.encodeToString(arr, Base64.DEFAULT);

                    Log.e("Image_post ", "Image_post" + bitmap);
                    if (bitmap != null)
                        params.put("userProfilePic", new DataPart(System.currentTimeMillis() + "Image_event.png", AppHelper.getFileDataFromDrawable(bitmap), "image/png"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);

        RetryPolicy policy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
       /* Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();*/
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        etSignUpDob.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));

    }


    private void getCollageName() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.getCollegeList, 1, getParamscollage(), new Helper() {

            @Override
            public void backResponse(String response) {
                S.E("Check By Ani collagename " + response);
                S.E("Check By Ani" + getParamscollage());
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    JSONArray jsonObject11 = jsonObject1.getJSONArray("data");
                    collageNameModelArrayList.clear();
                    arrayList.clear();
                    for (int i = 0; i < jsonObject11.length(); i++) {
                        CollageNameModel collageNameModel = new CollageNameModel();
                        JSONObject jsonObject = jsonObject11.getJSONObject(i);
                        collageNameModel.setCollege_id(jsonObject.getString("college_id"));
                        collageNameModel.setCollegeName(jsonObject.getString("collegeName"));
                        collageNameModel.setIsActive(jsonObject.getString("isActive"));
                        collageNameModel.setIsRequest(jsonObject.getString("isRequest"));
                        collageNameModel.setIsDelete(jsonObject.getString("isDelete"));
                        collageNameModel.setCollegeCreatedDate(jsonObject.getString("CollegeCreatedDate"));
                        collageNameModelArrayList.add(collageNameModel);
                        arrayList.add(jsonObject.getString("collegeName"));
                    }
                    CollageNameModel collageNameModel = new CollageNameModel();
                    collageNameModel.setCollege_id("-01");
                    collageNameModel.setCollegeName("");
                    collageNameModel.setIsActive("");
                    collageNameModel.setIsRequest("");
                    collageNameModel.setIsDelete("");
                    collageNameModel.setCollegeCreatedDate("");
                    collageNameModelArrayList.add(collageNameModel);
                    arrayList.add("Other");
                    S.E("arrayList" + arrayList.size());
                    S.E("collageNameModelArrayList" + collageNameModelArrayList.size());
                    ArrayAdapter<String> collegeAdapter = new ArrayAdapter<String>(UpdateProfileActivity.this, android.R.layout.simple_spinner_item, arrayList);
                    collegeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    collegeNameSp.setAdapter(collegeAdapter);
                    collegeNameSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View parent, int i, long l) {
                            if (arrayList.get(i).equals("Other")) {
                                etCollegeName.setVisibility(View.VISIBLE);
                                Collage_Name = etCollegeName.getText().toString();
                                S.E("selectcollsgrId" + arrayList.get(i));
                            } else {
                                etCollegeName.setVisibility(View.GONE);
                                selectcollsgrId = collageNameModelArrayList.get(i).getCollege_id();
                                S.E("selectcollsgrId" + selectcollsgrId);
                                Collage_Name = collageNameModelArrayList.get(i).getCollegeName();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParamscollage() {
        HashMap<String, String> params = new HashMap<>();
        try {
            params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
            params.put("user_imei_number", UserDataHelper.getInstance().getList().get(0).getUserImeiNo());

        } catch (Exception e) {
            e.printStackTrace();
        }
        S.E("param of college list" + params);
        return params;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

      /*  Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();*/
    }
}