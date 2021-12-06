package com.fauzighozali.mgamobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Test {

    @SerializedName("question_id")
    @Expose
    private Integer questionId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("answer_true")
    @Expose
    private String answer_true;
    @SerializedName("nameA")
    @Expose
    private String nameA;
    @SerializedName("nameB")
    @Expose
    private String nameB;
    @SerializedName("nameC")
    @Expose
    private String nameC;
    @SerializedName("nameD")
    @Expose
    private String nameD;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer_true() {
        return answer_true;
    }

    public void setAnswer_true(String answer_true) {
        this.answer_true = answer_true;
    }

    public String getNameA() {
        return nameA;
    }

    public void setNameA(String nameA) {
        this.nameA = nameA;
    }

    public String getNameB() {
        return nameB;
    }

    public void setNameB(String nameB) {
        this.nameB = nameB;
    }

    public String getNameC() {
        return nameC;
    }

    public void setNameC(String nameC) {
        this.nameC = nameC;
    }

    public String getNameD() {
        return nameD;
    }

    public void setNameD(String nameD) {
        this.nameD = nameD;
    }
}
