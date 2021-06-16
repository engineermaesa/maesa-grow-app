package com.fauzighozali.mgamobile.fragment;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.activity.ChangePasswordActivity;
import com.fauzighozali.mgamobile.activity.GuideActivity;
import com.fauzighozali.mgamobile.activity.LoginActivity;
import com.fauzighozali.mgamobile.activity.PrivacyPolicyActivity;
import com.fauzighozali.mgamobile.api.ApiService;
import com.fauzighozali.mgamobile.api.RetrofitBuilder;
import com.fauzighozali.mgamobile.jwt.TokenManager;
import com.fauzighozali.mgamobile.model.GetResponseToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends Fragment {

    private static final String TAG = "SettingFragment";

    private RelativeLayout relativeLayoutChangePassword, relativeLayoutGuide, relativeLayoutPrivacy, relativeLayoutLogout;
    private ApiService service;
    private TokenManager tokenManager;
    private Call<GetResponseToken> callLogoutUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting_fragment, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Setting");

        relativeLayoutChangePassword = view.findViewById(R.id.relative_layout_change_password);
        relativeLayoutGuide = view.findViewById(R.id.relative_layout_guide);
        relativeLayoutPrivacy = view.findViewById(R.id.relative_layout_privacy);
        relativeLayoutLogout = view.findViewById(R.id.relative_layout_logout);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        if (tokenManager.getToken() == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        relativeLayoutChangePassword.setOnClickListener(v -> startActivity(new Intent(getContext(), ChangePasswordActivity.class)));
        relativeLayoutGuide.setOnClickListener(v -> startActivity(new Intent(getContext(), GuideActivity.class)));
        relativeLayoutPrivacy.setOnClickListener(v -> startActivity(new Intent(getContext(), PrivacyPolicyActivity.class)));
        relativeLayoutLogout.setOnClickListener(v -> logout());

        return view;
    }

    private void logout() {
        callLogoutUser = service.logout(tokenManager.getToken().getAccessToken());
        callLogoutUser.enqueue(new Callback<GetResponseToken>() {
            @Override
            public void onResponse(Call<GetResponseToken> call, Response<GetResponseToken> response) {
                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    TokenManager tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
                    tokenManager.deleteToken();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<GetResponseToken> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}