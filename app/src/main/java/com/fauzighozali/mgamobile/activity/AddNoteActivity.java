package com.fauzighozali.mgamobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.fragment.CalendarFragment;
import com.fauzighozali.mgamobile.model.MyEventDay;

public class AddNoteActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTitleToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note_activity);

        CalendarView datePicker = findViewById(R.id.datePicker);
        Button button = findViewById(R.id.addNoteButton);
        EditText noteEditText = findViewById(R.id.noteEditText);
        mToolbar = findViewById(R.id.toolbar);
        mTitleToolbar = mToolbar.findViewById(R.id.toolbar_title);
        mTitleToolbar.setText("Event Note");

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        button.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            MyEventDay myEventDay = new MyEventDay(datePicker.getSelectedDate(), R.drawable.indicator_default, noteEditText.getText().toString());
            returnIntent.putExtra(CalendarFragment.RESULT, myEventDay);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
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
