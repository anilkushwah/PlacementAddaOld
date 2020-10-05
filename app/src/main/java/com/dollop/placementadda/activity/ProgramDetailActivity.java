package com.dollop.placementadda.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.ProgramListAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.ProgramListModel;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.multipart.VolleyMultipartRequest;
import com.dollop.placementadda.sohel.multipart.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class ProgramDetailActivity extends BaseActivity {
    ImageView ivProgramDetailImageId;
    EditText etCodeHereId;
    TextView tvFileNameId, tvCodeTitleId;
    Button btnUplaodId, btnSubmitId;
    Spinner prgramLanguageSpinnerId;
    String SelectedLanguageName;
    Bundle bundle;
    String CodeImage, CodeTitle, EditTextCode = "", CodeId;


    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PICK_FILE_REQUEST = 1;
    String mimeType = "";
    byte[] inputData;
    private String selectedFilePath;


    String[] LanguageName = {"Select Language", "C", "C++", "Java", "Python", "PyPy", "C#", "PAS Fpc", "PAS Gpc", "RUBY", "PHP", "GO",
            "NODEJS", "HASK", "Rust", "SCALA", "Swift", "JS", "D", "PERL", "R", "Kotlin", "SQL", "Cobol"};

    @Override
    protected int getContentResId() {
        return R.layout.activity_program_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(ProgramDetailActivity.this);
        setToolbarWithBackButton("Coding Problem");


       /* Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);*/


        bundle = getIntent().getExtras();
        CodeId = bundle.getString("CodeId");
        CodeImage = bundle.getString("CodeImage");
        CodeTitle = bundle.getString("CodeTitle");
        S.E("CodeImage::" + CodeImage);
        S.E("CodeId::" + CodeId);
        S.E("CodeTitle::" + CodeTitle);

        prgramLanguageSpinnerId = findViewById(R.id.prgramLanguageSpinnerId);
        ivProgramDetailImageId = findViewById(R.id.ivProgramDetailImageId);
        etCodeHereId = findViewById(R.id.etCodeHereId);
        tvFileNameId = findViewById(R.id.tvFileNameId);
        btnUplaodId = findViewById(R.id.btnUplaodId);
        btnSubmitId = findViewById(R.id.btnSubmitId);
        tvCodeTitleId = findViewById(R.id.tvCodeTitleId);


        Picasso.with(this).load(CodeImage).into(ivProgramDetailImageId);
        tvCodeTitleId.setText(CodeTitle);


        btnUplaodId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();

            }
        });

        btnSubmitId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitData();
            }
        });

        ArrayAdapter<String> Language = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, LanguageName);
        Language.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prgramLanguageSpinnerId.setAdapter(Language);

        prgramLanguageSpinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedLanguageName = adapterView.getSelectedItem().toString();

               // S.T(ProgramDetailActivity.this, "SelectedLanguageName::" + SelectedLanguageName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    S.T(ProgramDetailActivity.this, "Permission Granted. Now you can write data.");
                } else {
                    S.T(ProgramDetailActivity.this, "Permission Denied. You cannot write data.");
                    closeNow();
                }
                break;
        }
    }

    private void closeNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    return;
                }

                Uri selectedFileUri = data.getData();
                mimeType = getContentResolver().getType(selectedFileUri);

                selectedFilePath = selectedFileUri.getPath();
                tvFileNameId.setText("UploadFile :" + selectedFilePath);
                try {
                    InputStream iStream = getContentResolver().openInputStream(selectedFileUri);
                    inputData = getBytes(iStream);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    // url = file path or whatever suitable URL you want.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void SubmitData() {
        if(prgramLanguageSpinnerId.getSelectedItem().toString().equals("Select Language"));
        {
            ((TextView)prgramLanguageSpinnerId.getSelectedView()).setError("Please select the language first");
            S.T(ProgramDetailActivity.this,"Please select the language first");
        }
        EditTextCode = etCodeHereId.getText().toString();
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Const.URL.imageProgramSolution, new Response.Listener<NetworkResponse>() {

            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);

                S.E("checkResponse:Here::" + resultResponse);
                try {
                    JSONObject jsonObject = new JSONObject(resultResponse);
                    int status = jsonObject.getInt("status");
                    String message = jsonObject.getString("message");
                    if (status == 200 && message.equals("success")) {
                        S.I(ProgramDetailActivity.this, CodeResultActivity.class, null);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                error.printStackTrace();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() {
               /* user_id:16
                solution_language:php
                solution_text:solution_text*/
                Map<String, String> param = new HashMap<>();
                param.put("problem_id", CodeId);
                param.put("solution_text", EditTextCode);
                param.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
                param.put("solution_language", SelectedLanguageName);
                S.E("paramTestHere::" + param);

                return param;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                /*   if (!mimeType.equals("")) {*/

                String[] mimeTypeArray = selectedFilePath.split("/");
                String name = mimeTypeArray[mimeTypeArray.length - 1];

                String[] mimeTypeTest = name.split("\\.");
                Log.e("getByteData", "getByteData: " + mimeTypeTest.length);
                String type = mimeTypeTest[mimeTypeTest.length - 1];

                params.put("solution_file", new DataPart("test." + mimeTypeArray[mimeTypeArray.length - 1], inputData, type));
                // }


                //params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));
                Log.e("getByteData", "getByteData: " + params);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(multipartRequest);
        RetryPolicy policy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);
    }


}
