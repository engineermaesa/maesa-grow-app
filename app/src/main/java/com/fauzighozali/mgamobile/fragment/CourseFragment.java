package com.fauzighozali.mgamobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.activity.LoginActivity;
import com.fauzighozali.mgamobile.adapter.CourseAdapter;
import com.fauzighozali.mgamobile.adapter.LeaderboardAdapter;
import com.fauzighozali.mgamobile.api.ApiService;
import com.fauzighozali.mgamobile.api.RetrofitBuilder;
import com.fauzighozali.mgamobile.jwt.TokenManager;
import com.fauzighozali.mgamobile.model.Course;
import com.fauzighozali.mgamobile.model.GetResponseCourse;
import com.fauzighozali.mgamobile.model.GetResponseLeaderboard;
import com.fauzighozali.mgamobile.model.Leaderboard;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseFragment extends Fragment {

    private static final String TAG = "CourseFragment";

    private LinearLayout llEmptyCourse;
    private SwipeRefreshLayout swipeContainer;

    private CourseAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private ApiService service;
    private TokenManager tokenManager;
    private Call<GetResponseCourse> call;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Course");

        llEmptyCourse = view.findViewById(R.id.linear_layout_empty_course);
        swipeContainer = view.findViewById(R.id.swipe_refresh_layout);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE));
        if (tokenManager.getToken() == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        recyclerView = view.findViewById(R.id.recycler_view_course);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        swipeContainer.setOnRefreshListener(() -> {
            getDataHistoryCourse();
            new Handler().postDelayed(() -> swipeContainer.setRefreshing(false), 2500);
        });
        swipeContainer.setColorSchemeResources(
                R.color.gradient_start_color
        );

        getDataHistoryCourse();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDataHistoryCourse();
    }

    private void getDataHistoryCourse() {
        call = service.getHistoryCourse();
        call.enqueue(new Callback<GetResponseCourse>() {
            @Override
            public void onResponse(Call<GetResponseCourse> call, Response<GetResponseCourse> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    List<Course> courseList = response.body().getData();
                    if (courseList.size() > 0) {
                        adapter = new CourseAdapter(courseList, getContext());
                        recyclerView.setAdapter(adapter);
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
}