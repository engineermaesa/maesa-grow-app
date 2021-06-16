package com.fauzighozali.mgamobile.activity;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.api.ApiService;
import com.fauzighozali.mgamobile.api.RetrofitBuilder;
import com.fauzighozali.mgamobile.jwt.TokenManager;
import com.fauzighozali.mgamobile.model.GetResponseMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String TAG = "ChangePasswordActivity";

    private Toolbar mToolbar;
    private TextView mTitleToolbar;
    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private Button btnChangePassword;
    private View mProgressBar;
    private ProgressBar mCycleProgressBar;

    private ApiService service;
    private TokenManager tokenManager;
    private Call<GetResponseMessage> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mToolbar = findViewById(R.id.toolbar);
        mProgressBar = findViewById(R.id.progress_bar);
        mCycleProgressBar = mProgressBar.findViewById(R.id.progress_bar_cycle);
        etOldPassword = findViewById(R.id.edit_text_old_password);
        etNewPassword = findViewById(R.id.edit_text_new_password);
        etConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        mTitleToolbar = mToolbar.findViewById(R.id.toolbar_title);
        btnChangePassword = findViewById(R.id.button_change_password);
        mTitleToolbar.setText("Change Password");

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        if (tokenManager.getToken() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        oldNewConfirmTextChanged();
        btnChangePassword.setOnClickListener(v -> changePassword());
    }

    public void oldNewConfirmTextChanged() {
        etOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etOldPassword.getText().toString().length() > 0) {
                    btnChangePassword.setTextColor(Color.parseColor("#ffffff"));
                    btnChangePassword.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_gradient_style_enable));
                    btnChangePassword.setEnabled(true);
                }
                else {
                    btnChangePassword.setTextColor(Color.parseColor("#eaf3f2"));
                    btnChangePassword.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_gradient_style_disable));
                    btnChangePassword.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etNewPassword.getText().toString().length() > 0) {
                    btnChangePassword.setTextColor(Color.parseColor("#ffffff"));
                    btnChangePassword.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_gradient_style_enable));
                    btnChangePassword.setEnabled(true);
                }
                else {
                    btnChangePassword.setTextColor(Color.parseColor("#eaf3f2"));
                    btnChangePassword.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_gradient_style_disable));
                    btnChangePassword.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etConfirmPassword.getText().toString().length() > 0) {
                    btnChangePassword.setTextColor(Color.parseColor("#ffffff"));
                    btnChangePassword.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_gradient_style_enable));
                    btnChangePassword.setEnabled(true);
                }
                else {
                    btnChangePassword.setTextColor(Color.parseColor("#eaf3f2"));
                    btnChangePassword.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_gradient_style_disable));
                    btnChangePassword.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void changePassword() {
        String old_password = etOldPassword.getText().toString();
        String new_password = etNewPassword.getText().toString();
        String confirm_password = etConfirmPassword.getText().toString();

        mProgressBar.setVisibility(View.VISIBLE);
        mCycleProgressBar.setVisibility(View.VISIBLE);

        call = service.changePassword(old_password, new_password, confirm_password);
        call.enqueue(new Callback<GetResponseMessage>() {
            @Override
            public void onResponse(Call<GetResponseMessage> call, Response<GetResponseMessage> response) {
                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    GetResponseMessage message = response.body();
                    if (message.getStatus() == 200){
                        Toast.makeText(ChangePasswordActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    Toast.makeText(ChangePasswordActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    GetResponseMessage message = response.body();
                    Toast.makeText(ChangePasswordActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
                }
                mProgressBar.setVisibility(View.GONE);
                mCycleProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GetResponseMessage> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
                mProgressBar.setVisibility(View.GONE);
                mCycleProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}