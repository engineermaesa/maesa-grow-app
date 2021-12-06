package com.fauzighozali.mgamobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.api.RetrofitBuilder;

public class MateriActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTitleToolbar;
    private WebView webView;
    private LinearLayout llNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi);

        llNext = findViewById(R.id.linear_layout_next);
        webView = findViewById(R.id.webview);
        mToolbar = findViewById(R.id.toolbar);
        mTitleToolbar = mToolbar.findViewById(R.id.toolbar_title);
        mTitleToolbar.setText("Your Subject Matter");

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        Intent intent = getIntent();
        String uri = intent.getStringExtra("file");
        String file = RetrofitBuilder.BASE_URL_FILE + uri;

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyBrowser());
        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + file);

        llNext.setOnClickListener(v -> {
            Intent intentToPostTest = new Intent(MateriActivity.this, PostTestActivity.class);
            intentToPostTest.putExtra("id",intent.getIntExtra("id",0));
            intentToPostTest.putExtra("pre_test_score", intent.getIntExtra("pre_test_score",0));
            startActivity(intentToPostTest);
        });
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}