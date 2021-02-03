package com.fauzighozali.mgamobile.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.activity.DetailCertificationActivity;
import com.fauzighozali.mgamobile.activity.LoginActivity;
import com.fauzighozali.mgamobile.api.ApiService;
import com.fauzighozali.mgamobile.api.RetrofitBuilder;
import com.fauzighozali.mgamobile.jwt.TokenManager;
import com.fauzighozali.mgamobile.model.Course;
import com.fauzighozali.mgamobile.model.GetResponseDetailUser;
import com.fauzighozali.mgamobile.model.GetResponseMessage;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CertificationAdapter extends RecyclerView.Adapter<CertificationAdapter.CertificationViewHolder> {

    private static final String TAG = "CertificationAdapter";
    private List<Course> dataModelArrayList;
    private Context context;
    private ApiService service;
    private TokenManager tokenManager;
    private Call<GetResponseMessage> call;

    public CertificationAdapter(List<Course> dataModelArrayList, Context context) {
        this.dataModelArrayList = dataModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CertificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.activity_certification_adapter, viewGroup, false);
        return new CertificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificationViewHolder certificationViewHolder, int i) {
        Course course = dataModelArrayList.get(i);

        Glide.with(context)
                .load("http://api-kms.maesagroup.co.id/files/" + course.getImage())
                .into(certificationViewHolder.imgCertification);

        certificationViewHolder.tvTitle.setText(course.getTitle());
        certificationViewHolder.tvDescription.setText(course.getDescription());
        certificationViewHolder.rlBackgroundStatus.setBackgroundResource(R.drawable.btn_gradient_style_enable);
        certificationViewHolder.tvStatus.setText("New Course");

        certificationViewHolder.itemView.setOnClickListener(v -> {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
            View mView = ((Activity)v.getContext()).getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
            mBuilder.setView(mView);
            AlertDialog mDialog = mBuilder.create();
            mDialog.show();

            RelativeLayout relativeLayoutNo = mView.findViewById(R.id.relative_layout_no);
            RelativeLayout relativeLayoutYes = mView.findViewById(R.id.relative_layout_yes);

            relativeLayoutYes.setOnClickListener(vYes -> {
                tokenManager = TokenManager.getInstance(context.getSharedPreferences("prefs", Context.MODE_PRIVATE));
                if (tokenManager.getToken() == null) {
                    context.startActivity(new Intent(context, LoginActivity.class));
                    ((Activity)context).finish();
                }
                service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
                call = service.acceptCourse(course.getId());
                call.enqueue(new Callback<GetResponseMessage>() {
                    @Override
                    public void onResponse(Call<GetResponseMessage> call, Response<GetResponseMessage> response) {
                        Log.w(TAG, "onResponse: " + response);
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(v.getContext(), DetailCertificationActivity.class);
                            intent.putExtra("course_id", course.getId());
                            v.getContext().startActivity(intent);
                            mDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetResponseMessage> call, Throwable t) {
                        Log.w(TAG, "onFailure: " + t.getMessage());
                    }
                });
            });
            mDialog.show();
            relativeLayoutNo.setOnClickListener(vNo -> mDialog.dismiss());
        });
    }

    @Override
    public int getItemCount() {
        return (dataModelArrayList != null) ? dataModelArrayList.size() : 0;
    }

    public class CertificationViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCertification;
        private TextView tvTitle, tvDescription, tvStatus;
        private RelativeLayout rlBackgroundStatus;

        public CertificationViewHolder(View itemView) {
            super(itemView);
            imgCertification = itemView.findViewById(R.id.image_view_certification);
            tvTitle = itemView.findViewById(R.id.text_view_title_certification);
            tvDescription = itemView.findViewById(R.id.text_view_description_certification);
            tvStatus = itemView.findViewById(R.id.text_view_status_course);
            rlBackgroundStatus = itemView.findViewById(R.id.relative_layout_background_status_course);
        }
    }
}