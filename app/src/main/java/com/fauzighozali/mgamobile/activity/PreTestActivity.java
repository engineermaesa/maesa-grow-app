package com.fauzighozali.mgamobile.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.api.ApiService;
import com.fauzighozali.mgamobile.api.RetrofitBuilder;
import com.fauzighozali.mgamobile.jwt.TokenManager;
import com.fauzighozali.mgamobile.model.Course;
import com.fauzighozali.mgamobile.model.GetResponseDetailCourse;
import com.fauzighozali.mgamobile.model.GetResponseMessage;
import com.fauzighozali.mgamobile.model.PreTest;
import com.fauzighozali.mgamobile.model.Test;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreTestActivity extends AppCompatActivity {

    private static final String TAG = "PreTestActivity";

    private Toolbar mToolbar;
    private TextView mTitleToolbar, tvIndicator;
    private TextView tvSoal;
    private LinearLayout optionsContainer;
    private Button btnNext;
    private int count = 0;
    private List<PreTest> list;
    private int position = 0;
    private int score = 0;

    private ApiService service;
    private TokenManager tokenManager;
    private Call<GetResponseDetailCourse> call;
    private Call<GetResponseMessage> callSubmitQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_test);

        mToolbar = findViewById(R.id.toolbar);
        mTitleToolbar = mToolbar.findViewById(R.id.toolbar_title);
        mTitleToolbar.setText("Your Pre Test");

        tvSoal = findViewById(R.id.text_view_soal);
        tvIndicator = findViewById(R.id.text_view_indicator);
        optionsContainer = findViewById(R.id.linear_layout_options_container);
        btnNext = findViewById(R.id.button_next);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", Context.MODE_PRIVATE));
        if (tokenManager.getToken() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        displayDataQuestions();
    }

    private void displayDataQuestions() {
        Intent intent = getIntent();
        call = service.getCourseDetail(intent.getIntExtra("id", 0));
        call.enqueue(new Callback<GetResponseDetailCourse>() {
            @Override
            public void onResponse(Call<GetResponseDetailCourse> call, Response<GetResponseDetailCourse> response) {
                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    list = response.body().getData().getPreTest();
                    Course course = response.body().getData();

                    for (int i = 0;i < 4;i++) {
                        optionsContainer.getChildAt(i).setOnClickListener(v -> checkAnswer((Button) v));
                    }

                    playAnim(tvSoal,0, list.get(position).getQuestion());

                    btnNext.setOnClickListener(v -> {
                        btnNext.setEnabled(false);
                        btnNext.setAlpha(0.5f);
                        enableOptions(true);
                        position++;
                        if (position == list.size()) {
                            callSubmitQuestion = service.sumbitAnswer(intent.getIntExtra("id", 0), score,1);
                            callSubmitQuestion.enqueue(new Callback<GetResponseMessage>() {
                                @Override
                                public void onResponse(Call<GetResponseMessage> call, Response<GetResponseMessage> response) {
                                    Log.w(TAG, "onResponse: " + response);
                                    if (response.isSuccessful()) {
                                        Intent intentToMateri = new Intent(PreTestActivity.this, MateriActivity.class);
                                        intentToMateri.putExtra("id",intent.getIntExtra("id",0));
                                        intentToMateri.putExtra("file",course.getFile());
                                        intentToMateri.putExtra("pre_test_score",score);
                                        startActivity(intentToMateri);
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<GetResponseMessage> call, Throwable t) {
                                    Log.w(TAG, "onFailure: " + t.getMessage());
                                }
                            });
                            return;
                        }
                        count = 0;
                        playAnim(tvSoal, 0, list.get(position).getQuestion());
                    });
                }
            }

            @Override
            public void onFailure(Call<GetResponseDetailCourse> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void playAnim(View view, int value, String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (value == 0 && count < 4) {
                    String option = "";
                    if (count == 0) {
                        option = list.get(position).getNameA();
                    }else if (count == 1) {
                        option = list.get(position).getNameB();
                    }else if (count == 2) {
                        option = list.get(position).getNameC();
                    }else if (count == 3) {
                        option = list.get(position).getNameD();
                    }
                    playAnim(optionsContainer.getChildAt(count), 0, option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (value == 0) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            ((TextView) view).setText(Html.fromHtml(data, Html.FROM_HTML_MODE_LEGACY));
                        } else
                            ((TextView) view).setText(Html.fromHtml(data));
                        tvIndicator.setText(position+1+"/"+list.size());
                    }catch (ClassCastException ex) {
                        ((Button) view).setText(data);
                    }
                    view.setTag(data);
                    playAnim(view,1,data);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void checkAnswer(Button selectOption) {
        enableOptions(false);
        btnNext.setEnabled(true);
        btnNext.setAlpha(1);
        if (selectOption.getText().toString().equals(list.get(position).getAnswer_true())) {
            score+=20;
            selectOption.setBackground(getResources().getDrawable(R.drawable.btn_gradient_green));
        }else {
            selectOption.setBackground(getResources().getDrawable(R.drawable.btn_gradient_red));
//            Button correctoption = optionsContainer.findViewWithTag(list.get(position).getAnswer_true());
//            correctoption.setBackground(getResources().getDrawable(R.drawable.btn_gradient_green));
        }
    }

    private void enableOptions(boolean enable) {
        for (int i = 0;i < 4;i++) {
            optionsContainer.getChildAt(i).setEnabled(enable);
            if (enable) {
                optionsContainer.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.background_container_soal));
            }
        }
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