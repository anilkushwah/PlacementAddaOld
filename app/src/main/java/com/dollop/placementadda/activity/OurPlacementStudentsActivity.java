package com.dollop.placementadda.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.StudentListAdapter;
import com.dollop.placementadda.model.PlacedStudentModel;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OurPlacementStudentsActivity extends BaseActivity {
    ArrayList<PlacedStudentModel> companyModels = new ArrayList<PlacedStudentModel>();

    @Override
    protected int getContentResId() {
        return R.layout.activity_our_placement;
    }

    RecyclerView rvCompanyNameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String companyId = getIntent().getExtras().getString("companyId");
        String companyName = getIntent().getExtras().getString("companyName");

        setToolbarWithBackButton(companyName);
        rvCompanyNameId = (RecyclerView) findViewById(R.id.rvCompanyNameId);
        rvCompanyNameId.setLayoutManager(new GridLayoutManager(this, 2));
        getStudentsNames(companyId);

    }

    private void getStudentsNames(String companyId) {
        new JSONParser(OurPlacementStudentsActivity.this).parseVollyStringRequest(Const.URL.studentList, 1, getParam(companyId), new Helper() {

            @Override
            public void backResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 200) {
                        S.E("statusStudentList::" + response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            PlacedStudentModel companyModel = new PlacedStudentModel();

                            companyModel.packg = jsonObject1.getString("package");
                            companyModel.year = jsonObject1.getString("year");
                            companyModel.date = jsonObject1.getString("date");
                            companyModel.full_name = jsonObject1.getString("full_name");
                            companyModel.mobile = jsonObject1.getString("mobile");
                            companyModel.profile_pic = jsonObject1.getString("profile_pic");

                            companyModels.add(companyModel);

                       /*    "data": [
                           {
                               "id": "1",
                                   "package": "3",
                                   "year": "2018",
                                   "date": "2018-09-10",
                                   "full_name": "Simran Patidar",
                                   "mobile": "9174530192",
                                   "profile_pic": "http://placementsadda.com/FeesMangment/uploads/profile/3aadbf225226f7d7bc9eb21c1d50d965_6.png"
                           },*/
                        }

                        StudentListAdapter companyLIstAdapter = new StudentListAdapter(OurPlacementStudentsActivity.this, companyModels);
                        rvCompanyNameId.setAdapter(companyLIstAdapter);
                    }


                } catch (Exception e) {
                }


            }
        });
    }

    public Map<String, String> getParam(String companyId) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("company_id", companyId);
        return stringStringHashMap;
    }

}
