package com.fauzighozali.mgamobile.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.fauzighozali.mgamobile.R;

public class DetailBookActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTitleToolbar;
    private TextView mTitleBook, mDescBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);

        mToolbar = findViewById(R.id.toolbar);
        mTitleBook = findViewById(R.id.text_view_detail_title_book);
        mDescBook = findViewById(R.id.text_view_detail_desc_book);
        mDescBook.setMovementMethod(new ScrollingMovementMethod());
        mTitleToolbar = mToolbar.findViewById(R.id.toolbar_title);
        mTitleToolbar.setText("Detail Book");

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        getDisplay();
    }

    public void getDisplay() {
        Intent intent = getIntent();
        mTitleBook.setText(intent.getStringExtra("title"));
        mDescBook.setText(intent.getStringExtra("long_desc"));
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