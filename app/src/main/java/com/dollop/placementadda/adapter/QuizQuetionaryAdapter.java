package com.dollop.placementadda.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.QuizQuestionaryActivity;
import com.dollop.placementadda.database.model.QuestionaryModel;
import com.dollop.placementadda.sohel.S;

import java.util.ArrayList;

/**
 * Created by Kasim on 18-01-2018.
 */

public class QuizQuetionaryAdapter extends PagerAdapter {

    private ArrayList<QuestionaryModel> questionaryModels;
    private LayoutInflater inflater;
    private Context context;

    public QuizQuetionaryAdapter(Context context, ArrayList<QuestionaryModel> questionaryModels) {
        this.context = context;
        this.questionaryModels = questionaryModels;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return questionaryModels.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.item_questionary_view, view, false);
        TextView tvQuetionId = (TextView) myImageLayout.findViewById(R.id.tvQuetionId);
        RadioGroup radioGroup = (RadioGroup) myImageLayout.findViewById(R.id.rgGroupId);

        RadioButton rbOPtion1Id = (RadioButton) myImageLayout.findViewById(R.id.rbOption1Id);
        RadioButton rbOPtion2Id = (RadioButton) myImageLayout.findViewById(R.id.rbOption2Id);
        RadioButton rbOPtion3Id = (RadioButton) myImageLayout.findViewById(R.id.rbOption3Id);
        RadioButton rbOPtion4Id = (RadioButton) myImageLayout.findViewById(R.id.rbOption4Id);
        RadioButton rbOPtion5Id = (RadioButton) myImageLayout.findViewById(R.id.rbOption5Id);
        final TextView tvClearId = (TextView) myImageLayout.findViewById(R.id.tvClearId);


        final ArrayList<Integer> ints = new ArrayList<>();

        ints.add(R.id.rbOption1Id);
        ints.add(R.id.rbOption2Id);
        ints.add(R.id.rbOption3Id);
        ints.add(R.id.rbOption4Id);
        ints.add(R.id.rbOption5Id);

        tvQuetionId.setText(Html.fromHtml(questionaryModels.get(position).getQuestionstr()));
        rbOPtion1Id.setText(questionaryModels.get(position).getOption1());
        rbOPtion2Id.setText(questionaryModels.get(position).getOption2());
        rbOPtion3Id.setText(questionaryModels.get(position).getOption3());
        rbOPtion4Id.setText(questionaryModels.get(position).getOption4());
        rbOPtion5Id.setText(questionaryModels.get(position).getOption5());
S.E("checkHere::"+questionaryModels.get(position).getQuizQuestion_id()+" MRight Ans::"+questionaryModels.get(position).getAns());
        if (questionaryModels.get(position).getQuestionOptions().equals("2")) {

            rbOPtion3Id.setVisibility(View.GONE);
            rbOPtion4Id.setVisibility(View.GONE);
            rbOPtion5Id.setVisibility(View.GONE);

        } else if (questionaryModels.get(position).getQuestionOptions().equals("5")) {
            rbOPtion5Id.setVisibility(View.VISIBLE);
        } else {
            rbOPtion5Id.setVisibility(View.GONE);
        }
        view.addView(myImageLayout, 0);
        S.E("Check My Ans" + questionaryModels.get(position).getMyAns());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup radioGroup, int i) {
                RadioButton radioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                for (int a = 0; a < ints.size(); a++) {
                    if (radioGroup.getCheckedRadioButtonId() == ints.get(a)) {
                        radioButton.setTextColor(Color.parseColor("#1abc9c"));
                    } else {
                        RadioButton radioButton2 = radioGroup.findViewById(ints.get(a));
                        radioButton2.setTextColor(Color.BLACK);
                    }
                }
                String myAnwser = "";
                switch (radioGroup.getCheckedRadioButtonId()) {


                    case R.id.rbOption1Id:
                        myAnwser = "option_a";
                        break;
                    case R.id.rbOption2Id:
                        myAnwser = "option_b";
                        break;
                    case R.id.rbOption3Id:
                        myAnwser = "option_c";
                        break;
                    case R.id.rbOption4Id:
                        myAnwser = "option_d";
                        break;
                    case R.id.rbOption5Id:
                        myAnwser = "option_e";
                        break;


                }
                ((QuizQuestionaryActivity) context).checkedSetValue(position, myAnwser);
                //call method on quiz questionary activity from type cast with context send position and ans given by user
                tvClearId.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        radioGroup.clearCheck();
                        ((QuizQuestionaryActivity) context).checkedSetValue(position, "notAttempt");

                    }
                });


            }
        });

        if (questionaryModels.get(position).getMyAns().equals("option_a")) {
            S.E("Check My Ans1" + questionaryModels.get(position).getMyAns());

            rbOPtion1Id.setChecked(true);
        }
        if (questionaryModels.get(position).getMyAns().equals("option_b")) {
            S.E("Check My Ans2" + questionaryModels.get(position).getMyAns());
            rbOPtion2Id.setChecked(true);
        }
        if (questionaryModels.get(position).getMyAns().equals("option_c")) {
            S.E("Check My Ans3" + questionaryModels.get(position).getMyAns());

            rbOPtion3Id.setChecked(true);
        }
        if (questionaryModels.get(position).getMyAns().equals("option_d")) {
            S.E("Check My Ans4" + questionaryModels.get(position).getMyAns());
            rbOPtion4Id.setChecked(true);

        }
        if (questionaryModels.get(position).getMyAns().equals("option_e")) {
            rbOPtion5Id.setChecked(true);
            S.E("Check My Ans5" + questionaryModels.get(position).getMyAns());
        }

        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}