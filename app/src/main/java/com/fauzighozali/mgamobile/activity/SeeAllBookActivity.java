package com.fauzighozali.mgamobile.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.adapter.BookAdapter;
import com.fauzighozali.mgamobile.api.ApiService;
import com.fauzighozali.mgamobile.api.RetrofitBuilder;
import com.fauzighozali.mgamobile.model.Book;
import com.fauzighozali.mgamobile.model.GetResponseBook;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeAllBookActivity extends AppCompatActivity {

    private static final String TAG = "SeeAllBookActivity";

    private Toolbar mToolbar;
    private TextView mTitleToolbar;

    private BookAdapter bookAdapter;
    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;
    private ApiService service;
    private Call<GetResponseBook> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_book);

        mToolbar = findViewById(R.id.toolbar);
        mTitleToolbar = mToolbar.findViewById(R.id.toolbar_title);
        mTitleToolbar.setText("Book");

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        recyclerView = findViewById(R.id.recycler_view_see_all_book);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        service = RetrofitBuilder.createService(ApiService.class);
        getDataBook();
    }

    private void getDataBook() {
        call = service.getBook();
        call.enqueue(new Callback<GetResponseBook>() {
            @Override
            public void onResponse(Call<GetResponseBook> call, Response<GetResponseBook> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    List<Book> bookList = response.body().getData();
                    bookAdapter = new BookAdapter(getApplicationContext(), bookList);
                    recyclerView.setAdapter(bookAdapter);
                }
            }

            @Override
            public void onFailure(Call<GetResponseBook> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
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