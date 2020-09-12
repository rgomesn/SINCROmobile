package com.isel.sincroapp.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Citizen;
import com.isel.sincroapp.data.entities.DistanceInfraction;
import com.isel.sincroapp.data.entities.Infraction;
import com.isel.sincroapp.data.entities.Radar;
import com.isel.sincroapp.data.entities.RedLightInfraction;
import com.isel.sincroapp.data.entities.SpeedInfraction;
import com.isel.sincroapp.data.entities.User;
import com.isel.sincroapp.data.model.main.MainViewModel;
import com.isel.sincroapp.data.model.main.MainViewModelFactory;
import com.isel.sincroapp.services.NotificationService;
import com.isel.sincroapp.ui.login.LoginActivity;
import com.isel.sincroapp.ui.map.MapActivity;
import com.isel.sincroapp.ui.settings.SettingsActivity;
import com.isel.sincroapp.util.ServerResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {
    private MainViewModel mainViewModel;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout dLayout;
    private Citizen citizen;
    private User user;
    private String token;
    private Radar radar;
    private List<Infraction> pendingArray;
    private List<Infraction> pastArray;
    private TabAdapter adapter;
    private PendingInfractionFragment pendingFrag;
    private PastInfractionFragment pastFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            citizen = (Citizen) savedInstanceState.get("citizen");
            user = (User) savedInstanceState.get("user");
            token = (String) savedInstanceState.get("token");
        } else {
            citizen = (Citizen) getIntent().getSerializableExtra("citizen");
            user = (User) getIntent().getSerializableExtra("user");
            token = getIntent().getStringExtra("token");
        }

        Log.e("Token", token);

        Intent serviceIntent = new Intent(this, NotificationService.class);
        serviceIntent.putExtra("citizen", citizen);
        serviceIntent.putExtra("user", user);
        serviceIntent.putExtra("token", token);
        startService(serviceIntent);

        radar = (Radar) getIntent().getSerializableExtra("radar");

        mainViewModel = new ViewModelProvider(this, new MainViewModelFactory(getApplicationContext(), token)).get(MainViewModel.class);

        if (radar == null) {
            mainViewModel.getInfractionsServerResult().observe(this, this.new CustomObserver());
            mainViewModel.getInfractions(citizen);
        } else {
            mainViewModel.getRadarInfractionsServerResult().observe(this, this.new CustomObserver());
            mainViewModel.getRadarInfractions(citizen, radar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.nav_home).setVisible(false);
        return true;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("citizen", citizen);
        outState.putSerializable("user", user);
        outState.putString("token", token);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (dLayout.isDrawerOpen(GravityCompat.START)) {
            dLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_map: {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);

                intent.putExtra("citizen", citizen);
                intent.putExtra("user", user);
                intent.putExtra("token", token);

                startActivity(intent);
            }
            break;
            case R.id.nav_settings: {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);

                intent.putExtra("citizen", citizen);
                intent.putExtra("user", user);
                intent.putExtra("token", token);

                startActivity(intent);
            }
            break;
            case R.id.nav_sort: {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.sort_title);
                final String[] arr = getResources().getStringArray(R.array.sort_options_values);
                builder.setItems(R.array.sort_options_entries, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (arr[which]) {
                            case "date_asc": {
                                Collections.sort(pendingArray, new DateAscComparator());
                                Collections.sort(pastArray, new DateAscComparator());
                            } break;
                            case "date_dsc": {
                                Collections.sort(pendingArray, new DateDscComparator());
                                Collections.sort(pastArray, new DateDscComparator());
                            } break;
                            case "price_asc": {
                                Collections.sort(pendingArray, new PriceAscComparator());
                                Collections.sort(pastArray, new PriceAscComparator());
                            } break;
                            case "price_dsc": {
                                Collections.sort(pendingArray, new PriceDscComparator());
                                Collections.sort(pastArray, new PriceDscComparator());
                            } break;
                            case "type": {
                                Collections.sort(pendingArray, new TypeComparator());
                                Collections.sort(pastArray, new TypeComparator());
                            } break;
                            case "radar": {
                                Collections.sort(pendingArray, new RadarComparator());
                                Collections.sort(pastArray, new RadarComparator());
                            } break;
                        }

                        pendingFrag.update(pendingArray);
                        pastFrag.update(pastArray);
                    }
                });
                builder.show();
            }
            break;
            case R.id.nav_logout: {
                citizen = null;
                user = null;
                token = null;

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                startActivity(intent);
            }
            break;
            default:
                return false;
        }

        return false;
    }

    public class CustomObserver implements Observer<ServerResult<?>> {
        @Override
        public void onChanged(ServerResult<?> serverResult) {
            JsonObject obj = (JsonObject) serverResult.getResult();
            Gson gson = new Gson();
            List<Infraction> infractionList = new ArrayList<Infraction>();
            Type sListType = new TypeToken<ArrayList<SpeedInfraction>>() {}.getType();
            Type rlListType = new TypeToken<ArrayList<RedLightInfraction>>() {}.getType();
            Type dListType = new TypeToken<ArrayList<DistanceInfraction>>() {}.getType();
            if (obj.get("speedInfractions") != null) infractionList.addAll((Collection<SpeedInfraction>) gson.fromJson(obj.get("speedInfractions"), sListType));
            if (obj.get("redLightInfractions") != null) infractionList.addAll((Collection<RedLightInfraction>) gson.fromJson(obj.get("redLightInfractions"), rlListType));
            if (obj.get("distanceInfractions") != null) infractionList.addAll((Collection<DistanceInfraction>) gson.fromJson(obj.get("distanceInfractions"), dListType));

            Toolbar toolbar;
            final ViewPager viewPager;
            TabLayout tabLayout;

            toolbar = (Toolbar) MainActivity.this.findViewById(R.id.toolbar);
            MainActivity.this.setSupportActionBar(toolbar);
            toolbar.setTitle(citizen.getFirst_name() + " " + citizen.getMiddle_name() + " " + citizen.getLast_name());

            dLayout = findViewById(R.id.drawer);
            drawerToggle = new ActionBarDrawerToggle(MainActivity.this, dLayout, toolbar, R.string.open, R.string.close);
            dLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();

            ((NavigationView) findViewById(R.id.navigation_view)).setNavigationItemSelectedListener(MainActivity.this);

            //MainActivity.this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            pendingArray = new ArrayList<>();
            pastArray = new ArrayList<>();

            for (int i = 0; i < infractionList.size(); ++i) {
                Infraction inf = infractionList.get(i);

                if (inf.isPaid()) {
                    pastArray.add(inf);
                } else {
                    pendingArray.add(inf);
                }
            }

            viewPager = (ViewPager) MainActivity.this.findViewById(R.id.main_tab_view_pager);
            adapter = new TabAdapter(MainActivity.this.getSupportFragmentManager());
            pendingFrag = new PendingInfractionFragment(pendingArray, citizen, user, token);
            pastFrag = new PastInfractionFragment(pastArray, citizen, user, token);
            adapter.addFragment(MainActivity.this.getString(R.string.pending_infractions_title), pendingFrag);
            adapter.addFragment(MainActivity.this.getString(R.string.past_infractions_title), pastFrag);
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
    }

    public class DateAscComparator implements Comparator<Infraction> {
        @Override
        public int compare(Infraction o1, Infraction o2) {
            return o1.getInfraction_date_time().compareTo(o2.getInfraction_date_time());
        }
    }

    public class DateDscComparator implements Comparator<Infraction> {
        @Override
        public int compare(Infraction o1, Infraction o2) {
            return o2.getInfraction_date_time().compareTo(o1.getInfraction_date_time());
        }
    }

    public class PriceAscComparator implements Comparator<Infraction> {
        @Override
        public int compare(Infraction o1, Infraction o2) {
            return new Double(o1.getPrice()).compareTo(new Double(o2.getPrice()));
        }
    }

    public class PriceDscComparator implements Comparator<Infraction> {
        @Override
        public int compare(Infraction o1, Infraction o2) {
            return new Double(o2.getPrice()).compareTo(new Double(o1.getPrice()));
        }
    }

    public class TypeComparator implements Comparator<Infraction> {
        @Override
        public int compare(Infraction o1, Infraction o2) {
            return o1.getClass().getName().compareTo(o2.getClass().getName());
        }
    }

    public class RadarComparator implements Comparator<Infraction> {
        @Override
        public int compare(Infraction o1, Infraction o2) {
            return new Long(o2.getRadar_id()).compareTo(new Long(o1.getRadar_id()));
        }
    }
}