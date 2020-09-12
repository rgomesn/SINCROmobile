package com.isel.sincroapp.ui.infraction_detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.DistanceInfraction;

public class DistanceInfractionFragment extends Fragment {
    private DistanceInfraction distanceInfraction;

    public DistanceInfractionFragment() {
    }

    public DistanceInfractionFragment(DistanceInfraction distanceInfraction) {
        this.distanceInfraction = distanceInfraction;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_distance_infraction, container, false);

        TextView distance_to_next_vehicle = view.findViewById(R.id.detail_distance_to_next_vehicle);
        TextView distance_limit = view.findViewById(R.id.detail_distance_limit);

        distance_to_next_vehicle.setText(distanceInfraction.getDistance_to_next_vehicle() + " metros");
        distance_limit.setText(distanceInfraction.getDistance_limit() + " metros");

        return view;
    }
}