package com.isel.sincroapp.ui.vehicles;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Citizen;
import com.isel.sincroapp.data.entities.DistanceInfraction;
import com.isel.sincroapp.data.entities.Infraction;
import com.isel.sincroapp.data.entities.RedLightInfraction;
import com.isel.sincroapp.data.entities.SpeedInfraction;
import com.isel.sincroapp.data.entities.User;
import com.isel.sincroapp.data.entities.Vehicle;
import com.isel.sincroapp.data.model.main.MainViewModel;
import com.isel.sincroapp.data.model.main.MainViewModelFactory;
import com.isel.sincroapp.data.model.vehicles.VehiclesViewModel;
import com.isel.sincroapp.data.model.vehicles.VehiclesViewModelFactory;
import com.isel.sincroapp.ui.infraction_detail.InfractionDetailActivity;
import com.isel.sincroapp.ui.login.LoginActivity;
import com.isel.sincroapp.ui.main.MainActivity;
import com.isel.sincroapp.ui.main.PendingInfractionFragment;
import com.isel.sincroapp.ui.main.PendingInfractionRecyclerViewAdapter;
import com.isel.sincroapp.ui.main.RecyclerItemClickListener;
import com.isel.sincroapp.ui.map.MapActivity;
import com.isel.sincroapp.ui.settings.SettingsActivity;
import com.isel.sincroapp.util.ServerResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class VehiclesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Citizen citizen;
    private User user;
    private String token;
    private RecyclerView mRecycler;
    private VehiclesViewModel vehiclesViewModel;
    private List<Vehicle> ownedVehicles;
    private List<Vehicle> drivenVehicles;
    private VehicleRecyclerViewAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            citizen = (Citizen) savedInstanceState.get("citizen");
            user = (User) savedInstanceState.get("user");
            token = (String) savedInstanceState.get("token");
        } else {
            citizen = (Citizen) getIntent().getSerializableExtra("citizen");
            user = (User) getIntent().getSerializableExtra("user");
            token = getIntent().getStringExtra("token");
        }

        setContentView(R.layout.activity_vehicles);

        mRecycler = findViewById(R.id.vehiclesRecyclerView);

        vehiclesViewModel = new ViewModelProvider(this, new VehiclesViewModelFactory(getApplicationContext(), token)).get(VehiclesViewModel.class);

        mRecycler.addOnItemTouchListener(
            new RecyclerItemClickListener(this, mRecycler ,new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int itemPosition = mRecycler.getChildLayoutPosition(view);
                    ListVehicle item = adp.getItem(position);

                    Intent intent = new Intent(VehiclesActivity.this, VehicleDetailActivity.class);

                    intent.putExtra("citizen", citizen);
                    intent.putExtra("user", user);
                    intent.putExtra("token", token);
                    intent.putExtra("vehicle", item.getVehicle());
                    intent.putExtra("isOwned", item.isOwned());

                    startActivity(intent);
                }

                @Override
                public void onLongItemClick(View view, int position) {
                    // do whatever
                }
            })
        );

        vehiclesViewModel.getVehiclesServerResult().observe(this, new Observer<ServerResult<?>>() {
            @Override
            public void onChanged(ServerResult<?> serverResult) {
                JsonObject obj = (JsonObject) serverResult.getResult();
                Gson gson = new Gson();
                ownedVehicles = new ArrayList<>();
                drivenVehicles = new ArrayList<>();
                Type ListType = new TypeToken<ArrayList<Vehicle>>() {}.getType();
                ownedVehicles.addAll((Collection<Vehicle>) gson.fromJson(obj.get("ownedVehicles"), ListType));
                drivenVehicles.addAll((Collection<Vehicle>) gson.fromJson(obj.get("drivenVehicles"), ListType));

                List<ListVehicle> values = new ArrayList<>();

                for (Vehicle v : ownedVehicles) values.add(new ListVehicle(v, true));
                for (Vehicle v : drivenVehicles) values.add(new ListVehicle(v, false));

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                toolbar.setTitle("Veículos");

                mRecycler.setLayoutManager(new LinearLayoutManager(VehiclesActivity.this));

                adp = new VehicleRecyclerViewAdapter(values);
                mRecycler.setAdapter(adp);
            }
        });

        vehiclesViewModel.getVehicles(citizen);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("citizen", citizen);
        outState.putSerializable("user", user);
        outState.putString("token", token);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.nav_sort).setVisible(false);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home: {
                Intent intent = new Intent(VehiclesActivity.this, MapActivity.class);

                intent.putExtra("citizen", citizen);
                intent.putExtra("user", user);
                intent.putExtra("token", token);

                startActivity(intent);
            }
            break;
            case R.id.nav_map: {
                Intent intent = new Intent(VehiclesActivity.this, MainActivity.class);

                intent.putExtra("citizen", citizen);
                intent.putExtra("user", user);
                intent.putExtra("token", token);

                startActivity(intent);
            }
            break;
            case R.id.nav_settings: {
                Intent intent = new Intent(VehiclesActivity.this, SettingsActivity.class);

                intent.putExtra("citizen", citizen);
                intent.putExtra("user", user);
                intent.putExtra("token", token);

                startActivity(intent);
            }
            break;
            case R.id.nav_logout: {
                citizen = null;
                user = null;
                token = null;

                Intent intent = new Intent(VehiclesActivity.this, LoginActivity.class);

                startActivity(intent);
            }
            break;
            default:
                return false;
        }

        return false;
    }
}