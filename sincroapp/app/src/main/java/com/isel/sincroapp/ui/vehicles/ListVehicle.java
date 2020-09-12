package com.isel.sincroapp.ui.vehicles;

import com.isel.sincroapp.data.entities.Vehicle;

public class ListVehicle {
    private Vehicle vehicle;
    private boolean owned;

    public ListVehicle(Vehicle vehicle, boolean owned) {
        this.vehicle = vehicle;
        this.owned = owned;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public boolean isOwned() {
        return owned;
    }
}
