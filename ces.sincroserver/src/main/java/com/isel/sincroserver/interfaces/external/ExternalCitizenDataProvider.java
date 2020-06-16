package com.isel.sincroserver.interfaces.external;

import com.isel.sincroserver.entities.Citizen;
import com.isel.sincroserver.entities.Vehicle;

import java.util.List;

public interface ExternalCitizenDataProvider {
    Citizen obtainCitizenInformation(String cc_number);

    List<Vehicle> obtainCitizenVehicles(String cc_number);
}