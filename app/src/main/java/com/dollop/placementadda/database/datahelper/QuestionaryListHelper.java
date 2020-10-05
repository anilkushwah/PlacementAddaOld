package com.dollop.placementadda.database.datahelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dollop.placementadda.database.DataManager;
import com.dollop.placementadda.database.model.QuestionaryModel;
import com.dollop.placementadda.sohel.S;

import java.util.ArrayList;

/**
 * Created by CRUD-PC on 10/7/2016.
 */
public class QuestionaryListHelper {
    private static QuestionaryListHelper instance;
    private SQLiteDatabase db;
    private DataManager dm;
    Context cx;

    public QuestionaryListHelper(Context cx) {
        instance = this;
        this.cx = cx;
        dm = new DataManager(cx, DataManager.DATABASE_NAME, null, DataManager.DATABASE_VERSION);
    }

    public static QuestionaryListHelper getInstance() {
        return instance;
    }

    public void open() {
        db = dm.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public void read() {
        db = dm.getReadableDatabase();
    }

    public void delete(int companyId) {
        open();
        db.delete(QuestionaryModel.TABLE_NAME, QuestionaryModel.KEY_ID + " = '" + companyId + "'", null);
        close();
    }

    public void deleteAll() {
        open();
        db.delete(QuestionaryModel.TABLE_NAME, null, null);
        close();
    }


    private boolean isExist(QuestionaryModel QuestionaryModel) {
        read();
        Cursor cur = db.rawQuery("select * from " + QuestionaryModel.TABLE_NAME + " where " + QuestionaryModel.QuizQuestion_Id + "='" + QuestionaryModel.getQuizQuestion_id() + "'", null);
        if (cur.moveToFirst()) {
            return true;
        }
        return false;
    }

    public void insertData(QuestionaryModel quiz) {
        open();
        ContentValues values = new ContentValues();
        values.put(QuestionaryModel.QuizQuestion_Id, quiz.getQuizQuestion_id());
        values.put(QuestionaryModel.QuizQuestion, quiz.getQuestionstr());
        values.put(QuestionaryModel.QuestionOptions, quiz.getQuestionOptions());
        values.put(QuestionaryModel.Ans, quiz.getAns());
        values.put(QuestionaryModel.MyAns, quiz.getMyAns());
        values.put(QuestionaryModel.option_A, quiz.getOption1());
        values.put(QuestionaryModel.option_B, quiz.getOption2());
        values.put(QuestionaryModel.option_C, quiz.getOption3());
        values.put(QuestionaryModel.option_D, quiz.getOption4());
        values.put(QuestionaryModel.option_E, quiz.getOption5());


        if (!isExist(quiz)) {
            S.E("insert successfully");
            db.insert(QuestionaryModel.TABLE_NAME, null, values);
        } else {
            S.E("update successfully" + quiz.getQuizQuestion_id());
            db.update(QuestionaryModel.TABLE_NAME, values, QuestionaryModel.QuizQuestion_Id + "=" + quiz.getQuizQuestion_id(), null);
        }
        close();
    }

    public ArrayList<QuestionaryModel> getList() {
        ArrayList<QuestionaryModel> userItem = new ArrayList<QuestionaryModel>();
        read();
        Cursor cursor = db.rawQuery("select * from QuestionaryModel", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            do {
                QuestionaryModel taxiModel = new QuestionaryModel();
                taxiModel.setQuizQuestion_id(cursor.getString(cursor.getColumnIndex(QuestionaryModel.QuizQuestion_Id)));
                taxiModel.setQuestionstr(cursor.getString(cursor.getColumnIndex(QuestionaryModel.QuizQuestion)));
                taxiModel.setQuestionOptions(cursor.getString(cursor.getColumnIndex(QuestionaryModel.QuestionOptions)));
                taxiModel.setAns(cursor.getString(cursor.getColumnIndex(QuestionaryModel.Ans)));
                taxiModel.setMyAns(cursor.getString(cursor.getColumnIndex(QuestionaryModel.MyAns)));
                taxiModel.setOption1(cursor.getString(cursor.getColumnIndex(QuestionaryModel.option_A)));
                taxiModel.setOption2(cursor.getString(cursor.getColumnIndex(QuestionaryModel.option_B)));
                taxiModel.setOption3(cursor.getString(cursor.getColumnIndex(QuestionaryModel.option_C)));
                taxiModel.setOption4(cursor.getString(cursor.getColumnIndex(QuestionaryModel.option_D)));
                taxiModel.setOption5(cursor.getString(cursor.getColumnIndex(QuestionaryModel.option_E)));

                userItem.add(taxiModel);
            } while ((cursor.moveToPrevious()));
            cursor.close();
        }

        close();
        return userItem;
    }
}