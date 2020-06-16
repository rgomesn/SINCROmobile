package com.isel.sincroapp.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Infraction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A fragment representing a list of Items.
 */
public class PastInfractionFragment extends Fragment {
    private List<Infraction> infractions;

    public PastInfractionFragment() {
    }

    public PastInfractionFragment(List<Infraction> infractions) {
        this.infractions = infractions;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_infraction_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (infractions.size() <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, infractions.size()));
            }

            List<ListInfraction> list = new ArrayList<>();

            for (int i = 0; i < infractions.size(); ++i) {
                Infraction inf = infractions.get(i);
                list.add(new ListInfraction(inf.getLicence_plate(), inf.getRadar_id(), inf.getInfraction_date_time(), inf.getPrice(), inf.getPayment_date_time()));
            }

            PastInfractionRecyclerViewAdapter adp = new PastInfractionRecyclerViewAdapter(list);
            recyclerView.setAdapter(adp);
        }
        return view;
    }
}