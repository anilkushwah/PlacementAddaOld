package com.dollop.placementadda.model;

import com.dollop.placementadda.database.model.QuestionaryModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kasim on 22-01-2018.
 */

public class SelectedListModel implements Serializable {

    String quiz_id;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    String topicId;
    ArrayList<QuestionaryModel>questionaryModels;

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public ArrayList<QuestionaryModel> getQuestionaryModels() {
        return questionaryModels;
    }

    public void setQuestionaryModels(ArrayList<QuestionaryModel> questionaryModels) {
        this.questionaryModels = questionaryModels;
    }
}
