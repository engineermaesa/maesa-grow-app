package com.fauzighozali.mgamobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.activity.AddNoteActivity;
import com.fauzighozali.mgamobile.model.MyEventDay;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CalendarFragment extends Fragment {

    public static final String RESULT = "result";
    public static final String EVENT = "event";
    private static final int ADD_NOTE = 44;

    private CalendarView mCalendarView;
    private FloatingActionButton floatingActionButton;
    private List<EventDay> mEventDays = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Calendar");

        mCalendarView = (CalendarView) view.findViewById(R.id.calendarView);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(v -> addNote());
        mCalendarView.setOnDayClickListener(eventDay -> previewNote(eventDay));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {
            MyEventDay myEventDay = data.getParcelableExtra(RESULT);
            mCalendarView.setDate(myEventDay.getCalendar());
            mEventDays.add(myEventDay);
            mCalendarView.setEvents(mEventDays);
        }
    }

    private void addNote() {
        Intent intent = new Intent(getContext(), AddNoteActivity.class);
        startActivityForResult(intent, ADD_NOTE);
    }

    private void previewNote(EventDay eventDay) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflaterAlert = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflaterAlert.inflate(R.layout.calendar_event, null);

            MyEventDay myEventDay = (MyEventDay)eventDay;
            TextView tvNoteEvent = layout.findViewById(R.id.text_view_note_event);
            tvNoteEvent.setText(myEventDay.getNote());

            builder.setView(layout);
            builder.show();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}