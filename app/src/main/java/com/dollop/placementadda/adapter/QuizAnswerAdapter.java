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

import java.util.ArrayList;

/**
 * Created by Kasim on 18-01-2018.
 */

public class QuizAnswerAdapter extends PagerAdapter {

    private ArrayList<QuestionaryModel> questionaryModels;
    private LayoutInflater inflater;
    private Context context;

    public QuizAnswerAdapter(Context context, ArrayList<QuestionaryModel> questionaryModels) {
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
        View myImageLayout = inflater.inflate(R.layout.item_answer_view, view, false);
        TextView tvQuetionId = (TextView) myImageLayout.findViewById(R.id.tvQuetionId);
        RadioGroup radioGroup = (RadioGroup) myImageLayout.findViewById(R.id.rgGroupId);

        RadioButton rbOPtion1Id = (RadioButton) myImageLayout.findViewById(R.id.rbOption1Id);
        RadioButton rbOPtion2Id = (RadioButton) myImageLayout.findViewById(R.id.rbOption2Id);
        RadioButton rbOPtion3Id = (RadioButton) myImageLayout.findViewById(R.id.rbOption3Id);
        RadioButton rbOPtion4Id = (RadioButton) myImageLayout.findViewById(R.id.rbOption4Id);
        RadioButton rbOPtion5Id = (RadioButton) myImageLayout.findViewById(R.id.rbOption5Id);
        rbOPtion1Id.setEnabled(false);
        rbOPtion2Id.setEnabled(false);
        rbOPtion3Id.setEnabled(false);
        rbOPtion4Id.setEnabled(false);
        rbOPtion5Id.setEnabled(false);

        TextView textViewAnsId = (TextView) myImageLayout.findViewById(R.id.textViewAnsId);
        radioGroup.setEnabled(false);
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

        if (questionaryModels.get(position).getMyAns().equals(questionaryModels.get(position).getOption1())) {
            rbOPtion1Id.setChecked(true);
        } else if (questionaryModels.get(position).getMyAns().equals(questionaryModels.get(position).getOption2())) {
            rbOPtion2Id.setChecked(true);
        } else if (questionaryModels.get(position).getMyAns().equals(questionaryModels.get(position).getOption3())) {
            rbOPtion3Id.setChecked(true);
        } else if (questionaryModels.get(position).getMyAns().equals(questionaryModels.get(position).getOption4())) {
            rbOPtion4Id.setChecked(true);
        } else if (questionaryModels.get(position).getMyAns().equals(questionaryModels.get(position).getOption5())) {
            rbOPtion5Id.setChecked(true);
        }


        textViewAnsId.setText("Ans:" + questionaryModels.get(position).getAns());
        if (questionaryModels.get(position).getMyAns().equals("option_a")) {

            rbOPtion1Id.setChecked(true);
            textViewAnsId.setText("Option A");
        } else if (questionaryModels.get(position).getMyAns().equals("option_b")) {

            rbOPtion2Id.setChecked(true);
            textViewAnsId.setText("Option B");
        } else if (questionaryModels.get(position).getMyAns().equals("option_c")) {
            rbOPtion3Id.setChecked(true);
            textViewAnsId.setText("Option C");
        } else if (questionaryModels.get(position).getMyAns().equals("option_d")) {
            rbOPtion4Id.setChecked(true);
            textViewAnsId.setText("Option D");
        } else if (questionaryModels.get(position).getMyAns().equals("option_e")) {
            rbOPtion5Id.setChecked(true);
            textViewAnsId.setText("Option E");
        }

        if (questionaryModels.get(position).getQuestionOptions().equals("5")) {
            rbOPtion5Id.setVisibility(View.VISIBLE);
        } else {
            rbOPtion5Id.setVisibility(View.GONE);
        }
        view.addView(myImageLayout, 0);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                for (int a = 0; a < ints.size(); a++) {
                    if (radioGroup.getCheckedRadioButtonId() == ints.get(a)) {
                        radioButton.setTextColor(Color.parseColor("#1abc9c"));
                    } else {
                        RadioButton radioButton2 = radioGroup.findViewById(ints.get(a));
                        radioButton2.setTextColor(Color.BLACK);
                    }
                }


                ((QuizQuestionaryActivity) context).checkedSetValue(position, radioButton.getText().toString());
                //call method on quiz questionary activity from type cast with context send position and ans given by user

            }
        });
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}