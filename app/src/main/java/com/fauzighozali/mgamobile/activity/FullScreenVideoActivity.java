package com.fauzighozali.mgamobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.util.FullScreenMediaController;

public class FullScreenVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;
    private ProgressBar mCycleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

        videoView = findViewById(R.id.videoView);
        mCycleProgressBar = findViewById(R.id.progress_bar_cycle);

        String fullScreen =  getIntent().getStringExtra("fullScreenInd");
        if("y".equals(fullScreen)){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else {
            Intent intent = new Intent(FullScreenVideoActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        SharedPreferences sharedpreferences = getSharedPreferences("mySharedPreferences", MODE_PRIVATE);
        String value = sharedpreferences.getString("uri", "");
        videoView.setVideoPath("https://api-staging-kms.duatanganindonesia.com/files/" + value);

        videoView.setOnPreparedListener(mp -> mp.setOnInfoListener((mediaPlayer, i, i1) -> {
            if (i == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                mCycleProgressBar.setVisibility(View.VISIBLE);
            if (i == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                mCycleProgressBar.setVisibility(View.GONE);
            return false;
        }));

        videoView.setOnCompletionListener(mediaPlayer -> finish());

        mediaController = new FullScreenMediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
    }
}