package com.fauzighozali.mgamobile.jwt;

import android.content.SharedPreferences;

import com.fauzighozali.mgamobile.model.GetResponseToken;

import java.util.Objects;

public class TokenManager {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static TokenManager INSTANCE = null;

    private TokenManager(SharedPreferences prefs) {
        this.prefs = prefs;
        this.editor = prefs.edit();
    }

    public static synchronized TokenManager getInstance(SharedPreferences prefs) {
        if (INSTANCE == null) {
            INSTANCE = new TokenManager(prefs);
        }
        return INSTANCE;
    }

    public void saveToken(GetResponseToken token) {
        editor.putString("API_TOKEN", token.getAccessToken()).commit();
        editor.putString("FILE", token.getFile()).commit();
        editor.putInt("COMPANY_ID", token.getCompanyId()).commit();
    }

    public void deleteToken() {
        editor.remove("API_TOKEN").commit();
        editor.remove("FILE").commit();
        editor.remove("COMPANY_ID").commit();
    }

    public GetResponseToken getToken() {
        GetResponseToken token = new GetResponseToken();
        token.setAccessToken(prefs.getString("API_TOKEN", null));
        token.setFile(prefs.getString("FILE", null));
        token.setCompanyId(prefs.getInt("COMPANY_ID", 0));
        return token;
    }
}
