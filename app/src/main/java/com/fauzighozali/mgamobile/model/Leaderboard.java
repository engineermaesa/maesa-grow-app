package com.fauzighozali.mgamobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Leaderboard {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("ui_avatar")
    @Expose
    private String uiAvatar;
    @SerializedName("total_score")
    @Expose
    private String totalScore;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public String getUiAvatar() {
        return uiAvatar;
    }

    public void setUiAvatar(String uiAvatar) {
        this.uiAvatar = uiAvatar;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }
}
