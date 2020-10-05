package com.dollop.placementadda.model;

/**
 * Created by Kasim on 22-01-2018.
 */

public class QuizSubCatgoryModel {

    int categoryLogo;
    String categoryId;
    String category_Image="";

    public String getCategory_Image() {
        return category_Image;
    }

    public void setCategory_Image(String category_Image) {
        this.category_Image = category_Image;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryLogo() {
        return categoryLogo;
    }

    public void setCategoryLogo(int categoryLogo) {
        this.categoryLogo = categoryLogo;
    }

    String categoryName,CategoryQuiz,CategoryViews;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryQuiz() {
        return CategoryQuiz;
    }

    public void setCategoryQuiz(String categoryQuiz) {
        CategoryQuiz = categoryQuiz;
    }

    public String getCategoryViews() {
        return CategoryViews;
    }

    public void setCategoryViews(String categoryViews) {
        CategoryViews = categoryViews;
    }


}
