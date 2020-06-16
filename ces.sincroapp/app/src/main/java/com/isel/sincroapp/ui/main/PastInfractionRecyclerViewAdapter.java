package com.isel.sincroapp.ui.main;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isel.sincroapp.R;

import java.util.List;

public class PastInfractionRecyclerViewAdapter extends RecyclerView.Adapter<PastInfractionRecyclerViewAdapter.ViewHolder> {
    public List<ListInfraction> getmValues() {
        return mValues;
    }

    public void setmmValues(List<ListInfraction> mValues) {
        this.mValues = mValues;
    }

    private List<ListInfraction> mValues;

    public PastInfractionRecyclerViewAdapter(List<ListInfraction> items) {
        mValues = items;
    }

    @Override
    public PastInfractionRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_past_infraction, parent, false);
        return new PastInfractionRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.item_licence_plate.setText(mValues.get(position).getLicence_plate());
        holder.item_date.setText(mValues.get(position).getInfraction_date_time().toString());
        holder.item_price.setText(String.format("%f", mValues.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView item_licence_plate;
        public final TextView item_date;
        public final TextView item_price;
        public ListInfraction mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            item_licence_plate = (TextView) view.findViewById(R.id.pending_item_licence_plate);
            item_date = (TextView) view.findViewById(R.id.pending_item_date);
            item_price = (TextView) view.findViewById(R.id.pending_item_price);
        }
    }
}