package com.fauzighozali.mgamobile.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.api.RetrofitBuilder;
import com.fauzighozali.mgamobile.model.Video;
import com.fauzighozali.mgamobile.util.FullScreenMediaController;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

import java.util.List;

import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class VideoMiniVHSAdapter extends RecyclerView.Adapter<VideoMiniVHSAdapter.VideoMiniVHSViewHolder> {

    private List<Video> mYoutubeVideos;
    private Context context;

    private MediaController mediaController;

    public VideoMiniVHSAdapter(List<Video> youtubeVideos, Context context) {
        this.mYoutubeVideos = youtubeVideos;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoMiniVHSViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.activity_video_mini_vhsadapter, viewGroup, false);
        return new VideoMiniVHSViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoMiniVHSViewHolder holder, int position) {
        Video mYoutubeVideo = mYoutubeVideos.get(position);
        holder.tvTitle.setText(mYoutubeVideo.getTitle());
        holder.tvDescription.setText(mYoutubeVideo.getDescription());

        SharedPreferences sharedpreferences = context.getSharedPreferences("mySharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor PrefsEditor = sharedpreferences.edit();

        Glide.with(context).load(RetrofitBuilder.BASE_URL_FILE + mYoutubeVideo.getThumbnail()).into(holder.ivThumbnail);

        mediaController = new MediaController(context);
        mediaController.setAnchorView(holder.videoView);

        holder.videoView.setVideoPath(RetrofitBuilder.BASE_URL_FILE + mYoutubeVideo.getVideo());
        holder.videoView.setMediaController(mediaController);
        holder.videoView.setKeepScreenOn(true);

        holder.videoView.setOnPreparedListener(mp -> mp.setOnInfoListener((mediaPlayer, i, i1) -> {
            holder.mCycleProgressBar.setVisibility(View.GONE);
            if (i == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                holder.mCycleProgressBar.setVisibility(View.VISIBLE);
            if (i == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                holder.mCycleProgressBar.setVisibility(View.GONE);
            return false;
        }));

        holder.videoView.setOnCompletionListener(mediaPlayer -> {
            holder.ivThumbnail.setVisibility(View.VISIBLE);
            holder.ivPlay.setVisibility(View.VISIBLE);
        });

        holder.ivPlay.setOnClickListener(view -> {
            PrefsEditor.putString("uri", mYoutubeVideo.getVideo()).apply();
            holder.ivThumbnail.setVisibility(View.GONE);
            holder.ivPlay.setVisibility(View.GONE);
            holder.mCycleProgressBar.setVisibility(View.VISIBLE);
            holder.videoView.start();
        });
    }

    @Override
    public int getItemCount() {
        return (mYoutubeVideos != null) ? mYoutubeVideos.size() : 0;
    }


    public class VideoMiniVHSViewHolder extends RecyclerView.ViewHolder {
        private VideoView videoView;
        private TextView tvTitle, tvDescription;
        private ImageView ivPlay, ivThumbnail;
        private ProgressBar mCycleProgressBar;

        public VideoMiniVHSViewHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.video);
            tvTitle = itemView.findViewById(R.id.textViewTitle);
            tvDescription = itemView.findViewById(R.id.textViewDescription);
            ivPlay = itemView.findViewById(R.id.btnPlay);
            ivThumbnail = itemView.findViewById(R.id.imageViewItem);
            mCycleProgressBar = itemView.findViewById(R.id.progress_bar_cycle);
        }
    }
}