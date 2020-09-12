package com.isel.sincroapp.ui.map;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Citizen;
import com.isel.sincroapp.data.entities.Infraction;
import com.isel.sincroapp.data.entities.Radar;
import com.isel.sincroapp.data.entities.User;
import com.isel.sincroapp.data.model.map.MapViewModel;
import com.isel.sincroapp.data.model.map.MapViewModelFactory;
import com.isel.sincroapp.ui.main.MainActivity;
import com.isel.sincroapp.util.ServerResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends FragmentActivity {
    private MapViewModel mapViewModel;
    private GoogleMap mMap;
    private Citizen citizen;
    private User user;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            citizen = (Citizen)savedInstanceState.get("citizen");
            user = (User)savedInstanceState.get("user");
            token = (String)savedInstanceState.get("token");
        }

        else {
            citizen = (Citizen)getIntent().getSerializableExtra("citizen");
            user = (User)getIntent().getSerializableExtra("user");
            token = getIntent().getStringExtra("token");
        }

        mapViewModel = new ViewModelProvider(this, new MapViewModelFactory(getApplicationContext(), token)).get(MapViewModel.class);

        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new CustomMarkerInfoWindowView(getLayoutInflater(), mapViewModel, mMap));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("citizen", citizen);
        outState.putSerializable("user", user);
        outState.putString("token", token);

        super.onSaveInstanceState(outState);
    }

    public class CustomMarkerInfoWindowView implements GoogleMap.InfoWindowAdapter, OnMapReadyCallback {
        private final View markerItemView;
        private MapViewModel mapViewModel;
        private GoogleMap mMap;

        public CustomMarkerInfoWindowView(LayoutInflater layoutInflater, MapViewModel mapViewModel, GoogleMap mMap) {
            markerItemView = layoutInflater.inflate(R.layout.marker_info_window, null);
            this.mapViewModel = mapViewModel;
            this.mMap = mMap;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            final Radar radar = (Radar) marker.getTag();
            TextView itemNameTextView = markerItemView.findViewById(R.id.radar_address);
            Button radarLinkButton = markerItemView.findViewById(R.id.radar_link);
            itemNameTextView.setText(marker.getTitle());
            return markerItemView;
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setInfoWindowAdapter(this);

            mapViewModel.getRadarsServerResult().observe(MapActivity.this, new Observer<ServerResult<?>>() {
                @Override
                public void onChanged(ServerResult<?> serverResult) {
                    JsonObject obj = (JsonObject)serverResult.getResult();
                    Gson gson = new Gson();

                    Type listType = new TypeToken<ArrayList<Radar>>(){}.getType();
                    List<Radar> radars = (List<Radar>) gson.fromJson(obj.get("radars"), listType);

                    for (Radar radar : radars) {
                        LatLng radarLatLng = new LatLng(Double.parseDouble(radar.getLatitude()), Double.parseDouble(radar.getLongitude()));
                        mMap.addMarker(new MarkerOptions()
                                .position(radarLatLng).title(radar.getAddress() + " - " + radar.getDirection() + " - Km " + radar.getKilometer())).setTag(radar);
                    }

                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Intent intent = new Intent(MapActivity.this, MainActivity.class);

                            intent.putExtra("citizen", citizen);
                            intent.putExtra("user", user);
                            intent.putExtra("token", token);
                            intent.putExtra("radar", (Radar)marker.getTag());

                            startActivity(intent);
                        }
                    });
                }
            });

            mapViewModel.getRadars();
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }
}