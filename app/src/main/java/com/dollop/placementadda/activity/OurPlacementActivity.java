package com.dollop.placementadda.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.CompanyLIstAdapter;
import com.dollop.placementadda.adapter.QuizesLiveAdapter;
import com.dollop.placementadda.model.CompanyModel;
import com.dollop.placementadda.model.QuizListModel;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class OurPlacementActivity extends BaseActivity {
ArrayList<CompanyModel>companyModels=new ArrayList<>();
    @Override
    protected int getContentResId() {
        return R.layout.activity_our_placement;
    }
RecyclerView rvCompanyNameId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("Our Placements");
        rvCompanyNameId=(RecyclerView)findViewById(R.id.rvCompanyNameId);
        rvCompanyNameId.setLayoutManager(new GridLayoutManager(this,3));
        getCompanyNames();

    }

    private void getCompanyNames() {
        new JSONParser(OurPlacementActivity.this).parseVollyStringRequest(Const.URL.companyList, 1, null, new Helper() {

            @Override
            public void backResponse(String response) {


                try{
                    JSONObject jsonObject=new JSONObject(response);
                 if(jsonObject.getInt("status")==200)   {
                  JSONArray jsonArray=   jsonObject.getJSONArray("data");
                for(int i=0;i<jsonArray.length();i++){
                   JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    CompanyModel companyModel=new CompanyModel();

                    companyModel.strCompanyImage=jsonObject1.getString("company_img");
                    companyModel.strCompanyName=jsonObject1.getString("company_name");
                    companyModel.strCompanyId=jsonObject1.getString("id");

                    companyModels.add(companyModel);
                }

                     CompanyLIstAdapter companyLIstAdapter=new CompanyLIstAdapter(OurPlacementActivity.this,companyModels);
                rvCompanyNameId.setAdapter(companyLIstAdapter);
                 }



                }catch (Exception e){}



            }
        });
    }

}
