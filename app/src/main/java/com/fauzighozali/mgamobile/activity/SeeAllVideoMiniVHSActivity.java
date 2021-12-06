package com.fauzighozali.mgamobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.adapter.BookAdapter;
import com.fauzighozali.mgamobile.adapter.VideoMiniVHSAdapter;
import com.fauzighozali.mgamobile.api.ApiService;
import com.fauzighozali.mgamobile.api.RetrofitBuilder;
import com.fauzighozali.mgamobile.jwt.TokenManager;
import com.fauzighozali.mgamobile.model.GetResponseBook;
import com.fauzighozali.mgamobile.model.GetResponseVideo;
import com.fauzighozali.mgamobile.model.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeAllVideoMiniVHSActivity extends AppCompatActivity {

    private static final String TAG = "SeeAllVideoMiniVHS";

    private Toolbar mToolbar;
    private TextView mTitleToolbar;

    private VideoMiniVHSAdapter videoMiniVHSAdapter;
    private RecyclerView recyclerViewMiniVHS;
    private RecyclerView.LayoutManager mLayoutManager;

    private ApiService service;
    private TokenManager tokenManager;
    private Call<GetResponseVideo> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_video_mini_vhsactivity);

        mToolbar = findViewById(R.id.toolbar);
        mTitleToolbar = mToolbar.findViewById(R.id.toolbar_title);
        mTitleToolbar.setText("1 Visi Hati Semangat");

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        recyclerViewMiniVHS = findViewById(R.id.recycler_view_see_all_videos);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerViewMiniVHS.setLayoutManager(mLayoutManager);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", Context.MODE_PRIVATE));
        if (tokenManager.getToken() == null) {
            startActivity(new Intent(SeeAllVideoMiniVHSActivity.this, LoginActivity.class));
            finish();
        }
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        getDataVideo();
    }

    private void getDataVideo() {
        call = service.getVideoDashboard();
        call.enqueue(new Callback<GetResponseVideo>() {
            @Override
            public void onResponse(Call<GetResponseVideo> call, Response<GetResponseVideo> response) {
                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    List<Video> videoList = response.body().getData();
                    videoMiniVHSAdapter = new VideoMiniVHSAdapter(videoList, SeeAllVideoMiniVHSActivity.this);
                    recyclerViewMiniVHS.setAdapter(videoMiniVHSAdapter);
                }
            }

            @Override
            public void onFailure(Call<GetResponseVideo> call, Throwable t) {
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