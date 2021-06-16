package com.fauzighozali.mgamobile.activity;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.widget.TextView;

import com.fauzighozali.mgamobile.R;

public class PendahuluanActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTitleToolbar, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendahuluan);

        mToolbar = findViewById(R.id.toolbar);
        tvDescription = findViewById(R.id.text_view_description);
        mTitleToolbar = mToolbar.findViewById(R.id.toolbar_title);
        mTitleToolbar.setText("Pendahuluan");

        Intent intent = getIntent();
        tvDescription.setText(intent.getStringExtra("description"));

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
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