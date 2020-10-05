package com.dollop.placementadda.model;

/**
 * Created by Kasim on 22-01-2018.
 */

public class QuizListModel {
    String quizTime;
    String quizService_paid;
    String quizType;
    String quiz_id;
    String quiz_option_type;
    String quizTotalQuestion;
    String quizCategory_id;
    String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    String quizName;

    public String getQuizTime() {
        return quizTime;
    }

    public void setQuizTime(String quizTime) {
        this.quizTime = quizTime;
    }

    public String getQuizService_paid() {
        return quizService_paid;
    }

    public void setQuizService_paid(String quizService_paid) {
        this.quizService_paid = quizService_paid;
    }

    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getQuiz_option_type() {
        return quiz_option_type;
    }

    public void setQuiz_option_type(String quiz_option_type) {
        this.quiz_option_type = quiz_option_type;
    }

    public String getQuizTotalQuestion() {
        return quizTotalQuestion;
    }

    public void setQuizTotalQuestion(String quizTotalQuestion) {
        this.quizTotalQuestion = quizTotalQuestion;
    }

    public String getQuizCategory_id() {
        return quizCategory_id;
    }

    public void setQuizCategory_id(String quizCategory_id) {
        this.quizCategory_id = quizCategory_id;
    }
}
