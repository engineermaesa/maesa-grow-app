package com.fauzighozali.mgamobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.activity.LoginActivity;
import com.fauzighozali.mgamobile.adapter.InboxAdapter;
import com.fauzighozali.mgamobile.api.ApiService;
import com.fauzighozali.mgamobile.api.RetrofitBuilder;
import com.fauzighozali.mgamobile.jwt.TokenManager;
import com.fauzighozali.mgamobile.model.GetResponseInbox;
import com.fauzighozali.mgamobile.model.Inbox;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InboxFragment extends Fragment {

    private static final String TAG = "InboxFragment";

    private LinearLayout llEmptyCourse;
    private SwipeRefreshLayout swipeContainer;

    private InboxAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private ApiService service;
    private TokenManager tokenManager;
    private Call<GetResponseInbox> call;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Inbox");

        llEmptyCourse = view.findViewById(R.id.linear_layout_empty_course);
        swipeContainer = view.findViewById(R.id.swipe_refresh_layout);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE));
        if (tokenManager.getToken() == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        recyclerView = view.findViewById(R.id.recycler_view_inbox);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        swipeContainer.setOnRefreshListener(() -> {
            getDataInbox();
            new Handler().postDelayed(() -> swipeContainer.setRefreshing(false), 2500);
        });
        swipeContainer.setColorSchemeResources(
                R.color.gradient_start_color
        );

        getDataInbox();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDataInbox();
    }

    private void getDataInbox() {
        int companyId = tokenManager.getToken().getCompanyId();

        call = service.getInbox(companyId);
        call.enqueue(new Callback<GetResponseInbox>() {
            @Override
            public void onResponse(Call<GetResponseInbox> call, Response<GetResponseInbox> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    List<Inbox> inboxList = response.body().getData();
                    if (inboxList.size() > 0) {
                        adapter = new InboxAdapter(inboxList, getContext());
                        recyclerView.setAdapter(adapter);
                    }else {
                        llEmptyCourse.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetResponseInbox> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}