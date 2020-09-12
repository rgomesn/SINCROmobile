package com.isel.sincroapp.ui.main;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Infraction;

import java.text.SimpleDateFormat;
import java.util.List;

public class PendingInfractionRecyclerViewAdapter extends RecyclerView.Adapter<PendingInfractionRecyclerViewAdapter.ViewHolder> {
    public List<Infraction> getmValues() {
        return mValues;
    }

    public void setmmValues(List<Infraction> mValues) {
        this.mValues = mValues;
    }

    private List<Infraction> mValues;

    public PendingInfractionRecyclerViewAdapter(List<Infraction> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pending_infraction, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.item_licence_plate.setText(mValues.get(position).getLicence_plate());
        holder.item_date.setText(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(mValues.get(position).getInfraction_date_time()));
        holder.item_price.setText(String.format("%.2f", mValues.get(position).getPrice()) + " euros");
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
        public Infraction mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            item_licence_plate = (TextView) view.findViewById(R.id.pending_item_licence_plate);
            item_date = (TextView) view.findViewById(R.id.pending_item_date);
            item_price = (TextView) view.findViewById(R.id.pending_item_price);
        }
    }
}