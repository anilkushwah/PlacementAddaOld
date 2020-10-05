package com.dollop.placementadda.model;

import com.dollop.placementadda.database.model.QuestionaryModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kasim on 22-01-2018.
 */

public class StudyMaterialModel implements Serializable {

    String subjectTheoryId,categoty_id,subjectTheoryHeading,subjectTheoryContant,subjectTheoryCreatedDate;

    public String getSubjectTheoryId() {
        return subjectTheoryId;
    }

    public void setSubjectTheoryId(String subjectTheoryId) {
        this.subjectTheoryId = subjectTheoryId;
    }

    public String getCategoty_id() {
        return categoty_id;
    }

    public void setCategoty_id(String categoty_id) {
        this.categoty_id = categoty_id;
    }

    public String getSubjectTheoryHeading() {
        return subjectTheoryHeading;
    }

    public void setSubjectTheoryHeading(String subjectTheoryHeading) {
        this.subjectTheoryHeading = subjectTheoryHeading;
    }

    public String getSubjectTheoryContant() {
        return subjectTheoryContant;
    }

    public void setSubjectTheoryContant(String subjectTheoryContant) {
        this.subjectTheoryContant = subjectTheoryContant;
    }

    public String getSubjectTheoryCreatedDate() {
        return subjectTheoryCreatedDate;
    }

    public void setSubjectTheoryCreatedDate(String subjectTheoryCreatedDate) {
        this.subjectTheoryCreatedDate = subjectTheoryCreatedDate;
    }

}
