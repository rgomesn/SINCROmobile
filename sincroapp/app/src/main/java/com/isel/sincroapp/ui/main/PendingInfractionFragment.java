package com.isel.sincroapp.ui.main;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Citizen;
import com.isel.sincroapp.data.entities.DistanceInfraction;
import com.isel.sincroapp.data.entities.Infraction;
import com.isel.sincroapp.data.entities.RedLightInfraction;
import com.isel.sincroapp.data.entities.SpeedInfraction;
import com.isel.sincroapp.data.entities.User;
import com.isel.sincroapp.ui.infraction_detail.InfractionDetailActivity;
import com.isel.sincroapp.ui.map.MapActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A fragment representing a list of Items.
 */
public class PendingInfractionFragment extends Fragment {
    private List<Infraction> infractions;
    private PendingInfractionRecyclerViewAdapter adp;
    private View view;
    private RecyclerView mRecycler;
    private Citizen citizen;
    private User user;
    private String token;

    public PendingInfractionFragment() {

    }

    public PendingInfractionFragment(List<Infraction> infractions, Citizen citizen, User user, String token) {
        this.infractions = infractions;
        this.citizen = citizen;
        this.user = user;
        this.token = token;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pending_infraction_list, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext() ,LinearLayoutManager.VERTICAL, false);
        mRecycler = (RecyclerView) view.findViewById(R.id.pendingRecyclerView);

        mRecycler.addOnItemTouchListener(
            new RecyclerItemClickListener(this.getContext(), mRecycler ,new RecyclerItemClickListener.OnItemClickListener() {
                @Override public void onItemClick(View view, int position) {
                    int itemPosition = mRecycler.getChildLayoutPosition(view);
                    Infraction item = infractions.get(itemPosition);

                    Intent intent = new Intent(PendingInfractionFragment.this.getActivity(), InfractionDetailActivity.class);

                    if (item instanceof SpeedInfraction) {
                        intent.putExtra("infraction", (SpeedInfraction)item);
                    } else if (item instanceof RedLightInfraction) {
                        intent.putExtra("infraction", (RedLightInfraction)item);
                    } else {
                        intent.putExtra("infraction", (DistanceInfraction)item);
                    }

                    intent.putExtra("citizen", citizen);
                    intent.putExtra("user", user);
                    intent.putExtra("token", token);

                    startActivity(intent);
                }

                @Override public void onLongItemClick(View view, int position) {
                    // do whatever
                }
            })
        );

        Context context = view.getContext();
        mRecycler.setLayoutManager(new LinearLayoutManager(context));

        adp = new PendingInfractionRecyclerViewAdapter(infractions);
        mRecycler.setAdapter(adp);

        return view;
    }

    public void update(List<Infraction> infractions) {
        this.infractions = infractions;

        adp = new PendingInfractionRecyclerViewAdapter(infractions);
        mRecycler.setAdapter(adp);
    }
}