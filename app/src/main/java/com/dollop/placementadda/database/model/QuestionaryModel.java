package com.dollop.placementadda.database.model;

import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

/**
 * Created by CRUD-PC on 10/5/2016.
 */
public class QuestionaryModel implements Serializable {
    public static final String TABLE_NAME = "QuestionaryModel";
    public static final String KEY_ID= "key_id";
    public static final String QuizQuestion_Id = "quizQuestion_id";
    public static final String QuizQuestion = "quizQuestion";
    public static final String QuestionOptions= "questionOptions";
    public static final String Ans= "ans";
    public static final String MyAns= "myans";
    public static final String option_A= "option_A";
    public static final String option_B= "option_B";
    public static final String option_C= "option_C";
    public static final String option_D= "option_D";
    public static final String option_E= "option_E";




    public static void creteTable(SQLiteDatabase db) {
        String CREATE_CLIENTTABLE = "create table " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                QuizQuestion_Id + " text, " +
                QuizQuestion + " text, " +
                QuestionOptions+ " text, " +
                Ans+ " text, " +
                MyAns + " text, " +
                option_A + " text, " +
                option_B + " text, " +
                option_C + " text, " +
                option_D + " text ," +
                option_E + " text " +
                ")";
        db.execSQL(CREATE_CLIENTTABLE);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }


    String myAns, ans,questionOptions,quiz_id, quizQuestion_id,questionstr,option1,option2,option3,option4,option5;


    public String getAns() {
        return ans;
    }

    public String getMyAns() {
        return myAns;
    }

    public void setMyAns(String myAns) {
        this.myAns = myAns;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(String questionOptions) {
        this.questionOptions = questionOptions;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getQuizQuestion_id() {
        return quizQuestion_id;
    }

    public void setQuizQuestion_id(String quizQuestion_id) {
        this.quizQuestion_id = quizQuestion_id;
    }


    public String getQuestionstr() {
        return questionstr;
    }

    public void setQuestionstr(String questionstr) {
        this.questionstr = questionstr;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getOption5() {
        return option5;
    }

    public void setOption5(String option5) {
        this.option5 = option5;
    }
}
