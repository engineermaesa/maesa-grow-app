package com.fauzighozali.mgamobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetResponseMessage {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
