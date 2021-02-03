package com.fauzighozali.mgamobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.model.Course;
import com.fauzighozali.mgamobile.model.Inbox;
import com.fauzighozali.mgamobile.model.Leaderboard;

import java.util.ArrayList;
import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {

    private List<Inbox> dataModelArrayList;
    private Context context;

    public InboxAdapter(List<Inbox> dataModelArrayList, Context context) {
        this.dataModelArrayList = dataModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.activity_inbox_adapter, viewGroup, false);
        return new InboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxViewHolder inboxViewHolder, int i) {
        Inbox inbox = dataModelArrayList.get(i);

        inboxViewHolder.tvTitle.setText(inbox.getTitle());
        inboxViewHolder.tvDesc.setText(inbox.getDescription());

        inboxViewHolder.btnToRegis.setOnClickListener(v -> {
            Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(inbox.getLink()));
            context.startActivity(viewIntent);
        });

        inboxViewHolder.btnToReminder.setOnClickListener(v -> {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflaterAlert = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflaterAlert.inflate(R.layout.inbox_reminder, null);

                EditText etTitle = layout.findViewById(R.id.edit_text_title);
                EditText etLocation = layout.findViewById(R.id.edit_text_location);
                EditText etDescription = layout.findViewById(R.id.edit_text_description);
                Button btnSaveReminder = layout.findViewById(R.id.button_submit_reminder);

                btnSaveReminder.setOnClickListener(vRemind -> {
                    if (!etTitle.getText().toString().isEmpty() && !etLocation.getText().toString().isEmpty()
                            && !etDescription.getText().toString().isEmpty()) {

                        Intent intent = new Intent(Intent.ACTION_INSERT);
                        intent.setData(CalendarContract.Events.CONTENT_URI);
                        intent.putExtra(CalendarContract.Events.TITLE, etTitle.getText().toString());
                        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, etLocation.getText().toString());
                        intent.putExtra(CalendarContract.Events.DESCRIPTION, etDescription.getText().toString());

                        if (intent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(intent);
                        }else {
                            Toast.makeText(context, "There is no app that can support this action!", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(context, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setView(layout);
                builder.show();
            }catch (Exception e) {
                e.printStackTrace();
            }
        });

        inboxViewHolder.itemView.setOnClickListener(v -> {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflaterAlert = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflaterAlert.inflate(R.layout.detail_inbox, null);

                TextView detailInbox = layout.findViewById(R.id.text_view_detail_inbox_content);
                Button btnToRegis = layout.findViewById(R.id.button_apply_now);

                detailInbox.setText(inbox.getDescription());
                btnToRegis.setOnClickListener(v1 -> {
                    Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(inbox.getLink()));
                    context.startActivity(viewIntent);
                });

                builder.setView(layout);
                builder.show();
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataModelArrayList != null) ? dataModelArrayList.size() : 0;
    }

    public class InboxViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDesc;
        private Button btnToRegis, btnToReminder;

        public InboxViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.text_view_inbox_name);
            tvDesc = itemView.findViewById(R.id.text_view_inbox_content);
            btnToRegis = itemView.findViewById(R.id.button_apply_now);
            btnToReminder = itemView.findViewById(R.id.button_reminder);
        }
    }
}