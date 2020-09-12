package com.isel.sincroapp.ui.infraction_detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.RedLightInfraction;
import com.isel.sincroapp.data.entities.SpeedInfraction;

public class RedLightInfractionFragment extends Fragment {
    private RedLightInfraction redLightInfraction;

    public RedLightInfractionFragment() {
    }

    public RedLightInfractionFragment(RedLightInfraction redLightInfraction) {
        this.redLightInfraction = redLightInfraction;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_red_light_infraction, container, false);

        TextView vehicle_speed = view.findViewById(R.id.detail_vehicle_speed);
        TextView elapsed_time = view.findViewById(R.id.detail_elapsed_time);
        TextView yellow_duration = view.findViewById(R.id.detail_yellow_duration);

        vehicle_speed.setText(redLightInfraction.getVehicle_speed() + " km/h");
        elapsed_time.setText(redLightInfraction.getElapsed_time() + " segundos");
        yellow_duration.setText(redLightInfraction.getYellow_duration() + " segundos");

        return view;
    }
}