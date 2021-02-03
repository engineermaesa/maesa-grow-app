package com.fauzighozali.mgamobile.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.api.ApiService;
import com.fauzighozali.mgamobile.api.RetrofitBuilder;
import com.fauzighozali.mgamobile.fragment.CalendarFragment;
import com.fauzighozali.mgamobile.fragment.CourseFragment;
import com.fauzighozali.mgamobile.fragment.DashboardFragment;
import com.fauzighozali.mgamobile.fragment.InboxFragment;
import com.fauzighozali.mgamobile.fragment.LeaderboardFragment;
import com.fauzighozali.mgamobile.fragment.SettingFragment;
import com.fauzighozali.mgamobile.jwt.TokenManager;
import com.fauzighozali.mgamobile.model.Course;
import com.fauzighozali.mgamobile.model.GetResponseDetailUser;
import com.fauzighozali.mgamobile.model.GetResponseToken;
import com.fauzighozali.mgamobile.model.Organization;
import com.fauzighozali.mgamobile.model.User;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";
    private HomeActivity self;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView tvUsername, tvOrganizations, tvCompany;
    private ImageView ivUser;

    private ApiService service;
    private TokenManager tokenManager;
    private Call<GetResponseDetailUser> callDetailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        self = this;

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        callFragment(new DashboardFragment());
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        tvUsername = headerView.findViewById(R.id.text_view_user_name);
        tvOrganizations = headerView.findViewById(R.id.text_view_user_organization);
        tvCompany = headerView.findViewById(R.id.text_view_user_company);
        ivUser = headerView.findViewById(R.id.image_view_user);
        navigationView.setNavigationItemSelectedListener(this);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", Context.MODE_PRIVATE));
        if (tokenManager.getToken() == null) {
            startActivity(new Intent(self, LoginActivity.class));
            finish();
        }
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        getDetailUser();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                fragment = new DashboardFragment();
                break;
            case R.id.nav_leaderboard:
                fragment = new LeaderboardFragment();
                break;
            case R.id.nav_course:
                fragment = new CourseFragment();
                break;
            case R.id.nav_inbox:
                fragment = new InboxFragment();
                break;
            case R.id.nav_setting:
                fragment = new SettingFragment();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return callFragment(fragment);
    }

    private void getDetailUser() {
        callDetailUser = service.getDetailUser();
        callDetailUser.enqueue(new Callback<GetResponseDetailUser>() {
            @Override
            public void onResponse(Call<GetResponseDetailUser> call, Response<GetResponseDetailUser> response) {
                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    List<User> userList = response.body().getData();
                    for (int i = 0; i < userList.size(); i++) {
                        User user = userList.get(i);
                        Picasso.with(getApplicationContext()).load("http://api-kms.maesagroup.co.id/files/" + user.getImage()).into(ivUser);
                        tvUsername.setText(user.getName());
                        tvOrganizations.setText(user.getOrganization().getName());
                        tvCompany.setText(user.getCompany().getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetResponseDetailUser> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private boolean callFragment(android.support.v4.app.Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}