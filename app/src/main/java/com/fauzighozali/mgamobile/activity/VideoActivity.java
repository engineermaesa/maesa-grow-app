package com.fauzighozali.mgamobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.api.RetrofitBuilder;
import com.fauzighozali.mgamobile.util.FullScreenMediaController;

public class VideoActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTitleToolbar, tvTitle, tvDesc;
    private VideoView videoView;
    private MediaController mediaController;
    private LinearLayout llNext;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.video);
        tvDesc = findViewById(R.id.text_view_description_videos);
        tvTitle = findViewById(R.id.text_view_title_videos);
        llNext = findViewById(R.id.linear_layout_next);
        mToolbar = findViewById(R.id.toolbar);
        mTitleToolbar = mToolbar.findViewById(R.id.toolbar_title);
        mTitleToolbar.setText("Video");

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        playVideos();

        Intent intent = getIntent();
        llNext.setOnClickListener(view -> {
            Intent intentToPreTest = new Intent(VideoActivity.this, PreTestActivity.class);
            intentToPreTest.putExtra("id",intent.getIntExtra("id",0));
            startActivity(intentToPreTest);
            finish();
        });
    }

    private void playVideos() {
        pd = new ProgressDialog(this);
        pd.setMessage("Loading video...");
        pd.show();

//        String fullScreen =  getIntent().getStringExtra("fullScreenInd");
//        if("y".equals(fullScreen)){
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            getSupportActionBar().hide();
//        }

        Intent intent = getIntent();
        String path = intent.getStringExtra("uri");

        tvTitle.setText(intent.getStringExtra("title"));
        tvDesc.setText(intent.getStringExtra("desc"));
        videoView.setVideoPath(RetrofitBuilder.BASE_URL_FILE + path);

        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setKeepScreenOn(true);
        videoView.start();

        videoView.setOnPreparedListener(mp -> pd.dismiss());
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