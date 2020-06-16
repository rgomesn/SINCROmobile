package com.isel.sincroapp.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Citizen;
import com.isel.sincroapp.data.entities.Infraction;
import com.isel.sincroapp.data.entities.User;
import com.isel.sincroapp.data.model.main.MainViewModel;
import com.isel.sincroapp.data.model.main.MainViewModelFactory;
import com.isel.sincroapp.util.ServerResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;
    private Citizen citizen;
    private User user;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            citizen = (Citizen)savedInstanceState.get("citizen");
            user = (User)savedInstanceState.get("user");
            token = (String)savedInstanceState.get("token");
        }

        else {
            citizen = (Citizen)getIntent().getSerializableExtra("citizen");
            user = (User)getIntent().getSerializableExtra("user");
            token = (String)getIntent().getStringExtra("token");
        }

        mainViewModel = new ViewModelProvider(this, new MainViewModelFactory(getApplicationContext(), token)).get(MainViewModel.class);

        final ProgressBar pb = findViewById(R.id.main_progress_bar);
        pb.setIndeterminate(true);
        pb.setVisibility(View.VISIBLE);

        mainViewModel.getInfractionsServerResult().observe(this, new Observer<ServerResult<?>>() {
            @Override
            public void onChanged(ServerResult<?> serverResult) {
                JsonObject obj = (JsonObject)serverResult.getResult();
                Gson gson = new Gson();
                List<Infraction> infractionList = new ArrayList<Infraction>();
                Type listType = new TypeToken<ArrayList<Infraction>>(){}.getType();
                infractionList.addAll((Collection<? extends Infraction>) gson.fromJson(obj.get("speedInfractions"), listType));
                infractionList.addAll((Collection<? extends Infraction>) gson.fromJson(obj.get("redLightInfractions"), listType));
                infractionList.addAll((Collection<? extends Infraction>) gson.fromJson(obj.get("distanceInfractions"), listType));

                Toolbar toolbar;
                final ViewPager viewPager;
                TabLayout tabLayout;

                /*toolbar = (Toolbar) MainActivity.this.findViewById(R.id.toolbar);
                MainActivity.this.setSupportActionBar(toolbar);
                toolbar.setTitle("Welcome: " + citizen.getFirst_name() + " " + citizen.getMiddle_name() + " " + citizen.getLast_name() + " (" + user.getUsername() + ")");
*/
                List<Infraction> pendingArray = new ArrayList<>();
                List<Infraction> pastArray = new ArrayList<>();

                for (int i = 0; i < infractionList.size(); ++i) {
                    Infraction inf = infractionList.get(i);

                    if (!inf.isPaid()) {
                        pastArray.add(inf);
                    }
                    else {
                        pendingArray.add(inf);
                    }
                }

                viewPager = (ViewPager) MainActivity.this.findViewById(R.id.main_tab_view_pager);
                TabAdapter adapter = new TabAdapter(MainActivity.this.getSupportFragmentManager());
                adapter.addFragment(MainActivity.this.getString(R.string.pending_infractions_title), new PendingInfractionFragment(pendingArray));
                adapter.addFragment(MainActivity.this.getString(R.string.past_infractions_title), new PastInfractionFragment(pastArray));
                viewPager.setAdapter(adapter);

                tabLayout = (TabLayout) MainActivity.this.findViewById(R.id.main_tab_layout);
                tabLayout.setupWithViewPager(viewPager);

                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });
            }
        });
        mainViewModel.getInfractions(citizen);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("citizen", citizen);
        outState.putSerializable("user", user);
        outState.putString("token", token);

        super.onSaveInstanceState(outState);
    }
}