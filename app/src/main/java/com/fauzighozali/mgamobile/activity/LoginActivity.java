package com.fauzighozali.mgamobile.activity;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.api.ApiService;
import com.fauzighozali.mgamobile.api.RetrofitBuilder;
import com.fauzighozali.mgamobile.jwt.TokenManager;
import com.fauzighozali.mgamobile.model.GetResponseToken;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private LoginActivity self;

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private View mProgressBar;
    private ProgressBar mCycleProgressBar;

    private ApiService service;
    private TokenManager tokenManager;
    private Call<GetResponseToken> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        self = this;

        mProgressBar = findViewById(R.id.progress_bar_login);
        mCycleProgressBar = mProgressBar.findViewById(R.id.progress_bar_cycle);
        etUsername = findViewById(R.id.edit_text_username);
        etUsername.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        etPassword = findViewById(R.id.edit_text_password);
        btnLogin = findViewById(R.id.button_login);

        service = RetrofitBuilder.createService(ApiService.class);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        if (tokenManager.getToken().getAccessToken() != null) {
            startActivity(new Intent(self, HomeActivity.class));
            finish();
        }

        emailpassTextChanged();
        btnLogin.setOnClickListener(v -> navigateToHome());
    }

    public void emailpassTextChanged() {
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etUsername.getText().toString().length() > 0) {
                    btnLogin.setTextColor(Color.parseColor("#ffffff"));
                    btnLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_gradient_style_enable));
                    btnLogin.setEnabled(true);
                }
                else {
                    btnLogin.setTextColor(Color.parseColor("#eaf3f2"));
                    btnLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_gradient_style_disable));
                    btnLogin.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etPassword.getText().toString().length() > 0) {
                    btnLogin.setTextColor(Color.parseColor("#ffffff"));
                    btnLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_gradient_style_enable));
                    btnLogin.setEnabled(true);
                }
                else {
                    btnLogin.setTextColor(Color.parseColor("#eaf3f2"));
                    btnLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_gradient_style_disable));
                    btnLogin.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void navigateToHome() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        int isWeb = 0;

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.w(TAG, "Token Firebase: " + token);

        mProgressBar.setVisibility(View.VISIBLE);
        mCycleProgressBar.setVisibility(View.VISIBLE);

        call = service.login(username, password, isWeb, token);
        call.enqueue(new Callback<GetResponseToken>() {
            @Override
            public void onResponse(Call<GetResponseToken> call, Response<GetResponseToken> response) {
                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    tokenManager.saveToken(response.body());
                    Intent intent = new Intent(self, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(self, "Account not exist!", Toast.LENGTH_LONG).show();
                }
                mProgressBar.setVisibility(View.GONE);
                mCycleProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GetResponseToken> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(self, t.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
                mCycleProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
