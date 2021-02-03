package com.fauzighozali.mgamobile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.adapter.CertificationAdapter;
import com.fauzighozali.mgamobile.api.ApiService;
import com.fauzighozali.mgamobile.api.RetrofitBuilder;
import com.fauzighozali.mgamobile.jwt.TokenManager;
import com.fauzighozali.mgamobile.model.Course;
import com.fauzighozali.mgamobile.model.GetResponseCourse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrientedActivity extends AppCompatActivity {

    private static final String TAG = "OrientedActivity";

    private Toolbar mToolbar;
    private TextView mTitleToolbar;
    private LinearLayout llEmptyCourse;
    private SwipeRefreshLayout swipeContainer;

    private CertificationAdapter certificationAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private ApiService service;
    private TokenManager tokenManager;
    private Call<GetResponseCourse> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oriented);

        llEmptyCourse = findViewById(R.id.linear_layout_empty_course);
        swipeContainer = findViewById(R.id.swipe_refresh_layout);
        mToolbar = findViewById(R.id.toolbar);
        mTitleToolbar = mToolbar.findViewById(R.id.toolbar_title);
        mTitleToolbar.setText("Company Oriented");

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

        recyclerView = findViewById(R.id.recycler_view_oriented);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        swipeContainer.setOnRefreshListener(() -> {
            getCourseOriented();
            new Handler().postDelayed(() -> swipeContainer.setRefreshing(false), 2500);
        });
        swipeContainer.setColorSchemeResources(
                R.color.gradient_start_color
        );

        getCourseOriented();
    }

    private void getCourseOriented() {
        call = service.getCourseCertificationDashboard(2,1);
        call.enqueue(new Callback<GetResponseCourse>() {
            @Override
            public void onResponse(Call<GetResponseCourse> call, Response<GetResponseCourse> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    List<Course> courseList = response.body().getData();
                    if (courseList.size() > 0) {
                        certificationAdapter = new CertificationAdapter(courseList, getApplicationContext());
                        recyclerView.setAdapter(certificationAdapter);
                    }else {
                        llEmptyCourse.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetResponseCourse> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
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