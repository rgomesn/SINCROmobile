package com.isel.sincroserver.interfaces.external;

import com.isel.sincroserver.entities.Citizen;

public interface ExternalCitizenDataProvider {
    Citizen obtainCitizenInformation(String cc_number);
}