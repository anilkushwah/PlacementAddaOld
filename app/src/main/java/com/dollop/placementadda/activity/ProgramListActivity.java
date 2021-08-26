package com.dollop.placementadda.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.ProgramListAdapter;
import com.dollop.placementadda.model.ProgramListModel;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class ProgramListActivity extends BaseActivity {
    ProgramListAdapter programListAdapter;
    RecyclerView rvProgramListId;
    List<ProgramListModel> programListModels =new ArrayList<>();

    @Override
    protected int getContentResId() {
        return R.layout.activity_program_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(ProgramListActivity.this);
        setToolbarWithBackButton("Coding Problem");
        rvProgramListId = (RecyclerView) findViewById(R.id.rvProgramListId);
        rvProgramListId.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        getProgramList();

    }

    private void getProgramList() {
        new JSONParser(ProgramListActivity.this).parseVollyStringRequest(Const.URL.imageProgramList, 1, getParms(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("checkHere ProgramList response::"+response);
                try {
                    S.E("checkHere Response::" + response);
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            ProgramListModel programListModel =new ProgramListModel();
                            programListModel.CodeImage=jsonObject1.getString("image");
                            programListModel.CodeTitle=jsonObject1.getString("title");
                            programListModel.CodeId=jsonObject1.getString("id");
                            programListModel.CodeTime=jsonObject1.getString("created_date");
                            programListModels.add(programListModel);
                        }
                        programListAdapter= new ProgramListAdapter(ProgramListActivity.this,programListModels);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(programListAdapter);
                        rvProgramListId.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
                    }

                }catch (Exception e) {
                    e.printStackTrace();
                    S.E("checkLiveQuiz::"+e);
                }
            }
        });
    }

    private Map<String, String> getParms() {

        HashMap<String, String> params = new HashMap<>();


        return null;
    }
}
