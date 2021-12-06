package com.fauzighozali.mgamobile.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.activity.DetailCertificationActivity;
import com.fauzighozali.mgamobile.activity.LoginActivity;
import com.fauzighozali.mgamobile.activity.VideoActivity;
import com.fauzighozali.mgamobile.api.ApiService;
import com.fauzighozali.mgamobile.api.RetrofitBuilder;
import com.fauzighozali.mgamobile.jwt.TokenManager;
import com.fauzighozali.mgamobile.model.GetResponseMessage;
import com.fauzighozali.mgamobile.model.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CorporateValueAdapter extends RecyclerView.Adapter<CorporateValueAdapter.CorporateValueViewHolder> {

    private static final String TAG = "CorporateValueAdapter";
    private List<Video> mYoutubeVideos;
    private Context context;
    private ApiService service;
    private TokenManager tokenManager;
    private Call<GetResponseMessage> call;

    public CorporateValueAdapter(List<Video> youtubeVideos, Context context) {
        this.mYoutubeVideos = youtubeVideos;
        this.context = context;
    }

    @NonNull
    @Override
    public CorporateValueViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.activity_corporate_valude_adapter, viewGroup, false);
        return new CorporateValueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CorporateValueViewHolder holder, int position) {
        Video mYoutubeVideo = mYoutubeVideos.get(position);
        holder.tvTitle.setText(mYoutubeVideo.getTitle());
        holder.tvDesc.setText(mYoutubeVideo.getDescription());
        holder.tvStatus.setText("New Course");

        Glide.with(context).load(RetrofitBuilder.BASE_URL_FILE + mYoutubeVideo.getThumbnail()).into(holder.ivThumbnail);
        holder.itemView.setOnClickListener(view -> {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
            View mView = ((Activity)view.getContext()).getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
            mBuilder.setView(mView);
            AlertDialog mDialog = mBuilder.create();
            mDialog.show();

            RelativeLayout relativeLayoutNo = mView.findViewById(R.id.relative_layout_no);
            RelativeLayout relativeLayoutYes = mView.findViewById(R.id.relative_layout_yes);
            View mProgressBar = mView.findViewById(R.id.progress_bar_login);
            ProgressBar mCycleProgressBar = mProgressBar.findViewById(R.id.progress_bar_cycle);

            relativeLayoutYes.setOnClickListener(vYes -> {
                mProgressBar.setVisibility(View.VISIBLE);
                mCycleProgressBar.setVisibility(View.VISIBLE);

                tokenManager = TokenManager.getInstance(context.getSharedPreferences("prefs", Context.MODE_PRIVATE));
                if (tokenManager.getToken() == null) {
                    context.startActivity(new Intent(context, LoginActivity.class));
                    ((Activity)context).finish();
                }
                service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

                call = service.acceptCourse(mYoutubeVideo.getId());
                call.enqueue(new Callback<GetResponseMessage>() {
                    @Override
                    public void onResponse(Call<GetResponseMessage> call, Response<GetResponseMessage> response) {
                        Log.w(TAG, "onResponse: " + response);
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(view.getContext(), VideoActivity.class);
                            intent.putExtra("id",mYoutubeVideo.getId());
                            intent.putExtra("desc",mYoutubeVideo.getDescription());
                            intent.putExtra("title",mYoutubeVideo.getTitle());
                            intent.putExtra("uri",mYoutubeVideo.getVideo());
                            view.getContext().startActivity(intent);
                            mDialog.dismiss();
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
            });
            mDialog.show();
            relativeLayoutNo.setOnClickListener(vNo -> mDialog.dismiss());
        });
    }

    @Override
    public int getItemCount() {
        return (mYoutubeVideos != null) ? mYoutubeVideos.size() : 0;
    }


    public class CorporateValueViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDesc, tvStatus;
        private ImageView ivThumbnail;

        public CorporateValueViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.textViewTitle);
            tvDesc = itemView.findViewById(R.id.textViewDescription);
            tvStatus = itemView.findViewById(R.id.text_view_status_course);
            ivThumbnail = itemView.findViewById(R.id.imageViewItem);
        }
    }
}