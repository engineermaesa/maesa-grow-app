package com.fauzighozali.mgamobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetResponseLeaderboard {

    @SerializedName("data")
    @Expose
    private List<Leaderboard> data = null;

    public List<Leaderboard> getData() {
        return data;
    }
}
