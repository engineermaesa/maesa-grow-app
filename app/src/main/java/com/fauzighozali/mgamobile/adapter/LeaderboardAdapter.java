package com.fauzighozali.mgamobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.model.Leaderboard;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private List<Leaderboard> dataModelArrayList;
    private Context context;

    public LeaderboardAdapter(Context context, List<Leaderboard> dataModelArrayList) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.activity_leaderboard_adapter, viewGroup, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder leaderboardViewHolder, int i) {
        Leaderboard leaderboard = dataModelArrayList.get(i);

        Glide.with(context)
                .load(leaderboard.getUiAvatar())
                .into(leaderboardViewHolder.imgLead);

        leaderboardViewHolder.tvRank.setText(String.valueOf(i+1));
        leaderboardViewHolder.tvName.setText(leaderboard.getName());
        leaderboardViewHolder.tvPoint.setText(leaderboard.getTotalScore());

        if (Integer.parseInt(leaderboard.getTotalScore()) > 1000) {
            leaderboardViewHolder.tvLevel.setText("Advanced");
            leaderboardViewHolder.rlBackgroundLevel.setBackgroundResource(R.drawable.background_level_advanced);
        } else if (Integer.parseInt(leaderboard.getTotalScore()) < 1000 && Integer.parseInt(leaderboard.getTotalScore()) > 500) {
            leaderboardViewHolder.tvLevel.setText("Intermediate");
            leaderboardViewHolder.rlBackgroundLevel.setBackgroundResource(R.drawable.background_level_intermediate);
        }else {
            leaderboardViewHolder.tvLevel.setText("Beginner");
            leaderboardViewHolder.rlBackgroundLevel.setBackgroundResource(R.drawable.background_level_beginner);
        }

    }

    @Override
    public int getItemCount() {
        return (dataModelArrayList != null) ? dataModelArrayList.size() : 0;
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgLead;
        private TextView tvRank, tvName, tvPoint, tvLevel;
        private RelativeLayout rlBackgroundLevel;

        public LeaderboardViewHolder(View itemView) {
            super(itemView);

            imgLead = itemView.findViewById(R.id.image_view_person);
            tvRank = itemView.findViewById(R.id.text_view_ranking);
            tvName = itemView.findViewById(R.id.text_view_name);
            tvPoint = itemView.findViewById(R.id.text_view_points);
            tvLevel = itemView.findViewById(R.id.text_view_level);
            rlBackgroundLevel = itemView.findViewById(R.id.linear_layout_background_level);
        }
    }
}