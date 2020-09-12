package com.isel.sincroapp.ui.vehicles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Vehicle;

import java.util.List;

public class VehicleRecyclerViewAdapter extends RecyclerView.Adapter<VehicleRecyclerViewAdapter.VehicleInnerViewHolder> {
    public List<ListVehicle> getmValues() {
        return mValues;
    }

    public void setmmValues(List<ListVehicle> mValues) {
        this.mValues = mValues;
    }

    private List<ListVehicle> mValues;

    public VehicleRecyclerViewAdapter(List<ListVehicle> items) {
        mValues = items;
    }

    @Override
    public VehicleInnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehicle_item, parent, false);
        return new VehicleInnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VehicleInnerViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.vehicle_item_licence_plate.setText(mValues.get(position).getVehicle().getLicence_plate());
    }

    public ListVehicle getItem(int position) {
        return mValues.get(position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class VehicleInnerViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView vehicle_item_licence_plate;
        public ListVehicle mItem;

        public VehicleInnerViewHolder(View view) {
            super(view);
            mView = view;
            vehicle_item_licence_plate = (TextView) view.findViewById(R.id.vehicle_item_licence_plate);
        }
    }
}