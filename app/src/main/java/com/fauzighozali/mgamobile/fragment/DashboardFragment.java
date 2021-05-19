package com.fauzighozali.mgamobile.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.activity.CertificationActivity;
import com.fauzighozali.mgamobile.activity.DetailBookActivity;
import com.fauzighozali.mgamobile.activity.HardSkillActivity;
import com.fauzighozali.mgamobile.activity.LevelActivity;
import com.fauzighozali.mgamobile.activity.LoginActivity;
import com.fauzighozali.mgamobile.activity.OrientedActivity;
import com.fauzighozali.mgamobile.activity.SeeAllBookActivity;
import com.fauzighozali.mgamobile.activity.VHSActivity;
import com.fauzighozali.mgamobile.adapter.AllBookAdapter;
import com.fauzighozali.mgamobile.adapter.BookAdapter;
import com.fauzighozali.mgamobile.api.ApiService;
import com.fauzighozali.mgamobile.api.RetrofitBuilder;
import com.fauzighozali.mgamobile.jwt.TokenManager;
import com.fauzighozali.mgamobile.model.Book;
import com.fauzighozali.mgamobile.model.GetResponseBook;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";

    private LinearLayout llHardSkill, llSoftSkill, llOurCompany, llCorporateValue, llSeeAll;
    private ImageView ivIdCard;

    private AllBookAdapter bookAdapter;
    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;
    private ApiService service;
    private TokenManager tokenManager;
    private Call<GetResponseBook> call;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Dashboard");

        llHardSkill = view.findViewById(R.id.linear_layout_hard_skill);
        llSoftSkill = view.findViewById(R.id.linear_layout_soft_skill);
        llOurCompany = view.findViewById(R.id.linear_layout_our_company);
        llCorporateValue = view.findViewById(R.id.linear_layout_corporate_value);
        llSeeAll = view.findViewById(R.id.linear_layout_seeall);
        ivIdCard = view.findViewById(R.id.image_view_id_card);

        recyclerView = view.findViewById(R.id.recycler_view_book);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE));
        if (tokenManager.getToken() == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }
        service = RetrofitBuilder.createService(ApiService.class);

        Picasso.with(getContext()).load("http://api-kms.maesagroup.co.id/files/" + tokenManager.getToken().getFile()).into(ivIdCard);
        getDataBook();

        llHardSkill.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), HardSkillActivity.class);
            startActivity(intent);
        });

        llSoftSkill.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CertificationActivity.class);
            startActivity(intent);
        });

        llOurCompany.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), OrientedActivity.class);
            startActivity(intent);
        });

        llCorporateValue.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), VHSActivity.class);
            startActivity(intent);
        });

        llSeeAll.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SeeAllBookActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
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
                    bookAdapter = new AllBookAdapter(getContext(), bookList);
                    recyclerView.setAdapter(bookAdapter);
                }
            }

            @Override
            public void onFailure(Call<GetResponseBook> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}