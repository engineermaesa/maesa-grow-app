package com.fauzighozali.mgamobile.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
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
import com.fauzighozali.mgamobile.activity.HistoryDetailCertificationActivity;
import com.fauzighozali.mgamobile.activity.MateriActivity;
import com.fauzighozali.mgamobile.activity.PostTestActivity;
import com.fauzighozali.mgamobile.activity.PreTestActivity;
import com.fauzighozali.mgamobile.api.RetrofitBuilder;
import com.fauzighozali.mgamobile.model.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> dataModelArrayList;
    private Context context;

    public CourseAdapter(List<Course> dataModelArrayList, Context context) {
        this.dataModelArrayList = dataModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.activity_course_adapter, viewGroup, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder courseViewHolder, int i) {
        Course course = dataModelArrayList.get(i);

        Glide.with(context)
                .load(RetrofitBuilder.BASE_URL_FILE + course.getImage())
                .into(courseViewHolder.imgCourse);

        courseViewHolder.tvTitle.setText(course.getTitle());
        courseViewHolder.tvDesc.setText(course.getDescription());
        courseViewHolder.tvScore.setText(String.valueOf(course.getScore()));
        courseViewHolder.tvScorePreTest.setText(String.valueOf(course.getPreScore()));
        courseViewHolder.tvScorePostTest.setText(String.valueOf(course.getPostScore()));

        if (course.getPreScore() == null && course.getPostScore() == null){
            courseViewHolder.tvScorePreTest.setVisibility(View.GONE);
            courseViewHolder.tvScorePostTest.setVisibility(View.GONE);
            courseViewHolder.rlPre.setVisibility(View.GONE);
            courseViewHolder.rlPost.setVisibility(View.GONE);
            courseViewHolder.view.setVisibility(View.GONE);
        }

        if (course.getStatus().equals("2")) {
            courseViewHolder.rlBackgroundStatus.setBackgroundResource(R.drawable.btn_gradient_style_enable);
            courseViewHolder.tvStatus.setText("Finish");
            courseViewHolder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), HistoryDetailCertificationActivity.class);
                intent.putExtra("course_id", course.getId());
                v.getContext().startActivity(intent);
            });
        }else {
            courseViewHolder.rlBackgroundStatus.setBackgroundResource(R.drawable.background_level_beginner);
            courseViewHolder.tvStatus.setText("On Going");
            courseViewHolder.itemView.setOnClickListener(v -> {
                if (course.getPreScore() == null && course.getPostScore() == null){
                    Intent intent = new Intent(v.getContext(), DetailCertificationActivity.class);
                    intent.putExtra("course_id", course.getId());
                    v.getContext().startActivity(intent);
                }else {
                    if (course.getPreStatus().equals("1")) {
                        Intent intent = new Intent(v.getContext(), PreTestActivity.class);
                        intent.putExtra("id", course.getCourse_id());
                        v.getContext().startActivity(intent);
                    }else {
                        Intent intent = new Intent(v.getContext(), PostTestActivity.class);
                        intent.putExtra("id", course.getCourse_id());
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (dataModelArrayList != null) ? dataModelArrayList.size() : 0;
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCourse;
        private TextView tvTitle, tvDesc, tvScore, tvStatus, tvScorePreTest, tvScorePostTest;
        private RelativeLayout rlBackgroundStatus, rlPre, rlPost;
        private View view;

        public CourseViewHolder(View itemView) {
            super(itemView);

            imgCourse = itemView.findViewById(R.id.image_view_course);
            tvTitle = itemView.findViewById(R.id.text_view_title_course);
            tvDesc = itemView.findViewById(R.id.text_view_description_course);
            tvScore = itemView.findViewById(R.id.text_view_score_course);
            tvStatus = itemView.findViewById(R.id.text_view_status_history_course);
            rlBackgroundStatus = itemView.findViewById(R.id.relative_layout_background_history_status_course);
            tvScorePreTest = itemView.findViewById(R.id.text_view_pre_test_score);
            tvScorePostTest = itemView.findViewById(R.id.text_view_post_test_score);
            view = itemView.findViewById(R.id.view);
            rlPre = itemView.findViewById(R.id.relative_layout_pre);
            rlPost = itemView.findViewById(R.id.relative_layout_post);
        }
    }
}