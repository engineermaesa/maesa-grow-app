package com.fauzighozali.mgamobile.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.applandeo.materialcalendarview.EventDay;
import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.fragment.CalendarFragment;
import com.fauzighozali.mgamobile.model.MyEventDay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotePreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_preview_activity);

        Intent intent = getIntent();

        TextView note = (TextView) findViewById(R.id.note);
        TextView date = (TextView) findViewById(R.id.date_note);

        if (intent != null) {
            Object event = intent.getParcelableExtra(CalendarFragment.EVENT);

            if(event instanceof MyEventDay){
                MyEventDay myEventDay = (MyEventDay)event;
                date.setText(getFormattedDate(myEventDay.getCalendar().getTime()));
                note.setText(myEventDay.getNote());
                return;
            }

            if(event instanceof EventDay){
                EventDay eventDay = (EventDay)event;
                date.setText(getFormattedDate(eventDay.getCalendar().getTime()));
            }
        }
    }

    public static String getFormattedDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return simpleDateFormat.format(date);
    }
}
