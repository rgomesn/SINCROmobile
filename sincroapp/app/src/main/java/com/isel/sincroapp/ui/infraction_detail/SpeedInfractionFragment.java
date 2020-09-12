package com.isel.sincroapp.ui.infraction_detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.SpeedInfraction;

public class SpeedInfractionFragment extends Fragment {
    private SpeedInfraction speedInfraction;

    public SpeedInfractionFragment() {
    }

    public SpeedInfractionFragment(SpeedInfraction speedInfraction) {
        this.speedInfraction = speedInfraction;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speed_infraction, container, false);

        TextView vehicle_speed = view.findViewById(R.id.detail_vehicle_speed);
        TextView speed_limit = view.findViewById(R.id.detail_speed_limit);
        TextView direction = view.findViewById(R.id.detail_direction);
        TextView chasing_vehicle_speed = view.findViewById(R.id.detail_chasing_vehicle_speed);

        vehicle_speed.setText(speedInfraction.getVehicle_speed() + " km/h");
        speed_limit.setText(speedInfraction.getSpeed_limit() + " km/h");
        direction.setText(speedInfraction.getDirection().equals("+") ? "Sentido do radar" : "Sentido contrário ao radar");
        chasing_vehicle_speed.setText(speedInfraction.getChasing_vehicle_speed() + " km/h");

        return view;
    }
}