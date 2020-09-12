package com.isel.sincroapp.services;

import com.google.gson.JsonObject;
import com.isel.sincroapp.data.entities.Citizen;
import com.isel.sincroapp.data.entities.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.*;

public interface SINCROServerService {
    @Headers("Content-Type: application/json")
    @POST("register/beginRegistration")
    Call<JsonObject> beginRegistration(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("register/registerWithEmail")
    Call<JsonObject> registerWithEmail(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("register/verifyRegistrationCode")
    Call<JsonObject> verifyRegistrationCode(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("register/registerLoginAndPassword")
    Call<JsonObject> registerLoginAndPassword(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("authenticate/login")
    Call<JsonObject> login(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "POST", path = "sincroapp/getInfractions", hasBody = true)
    Call<JsonObject> getInfractions(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "POST", path = "sincroapp/getRadarInfractions", hasBody = true)
    Call<JsonObject> getRadarInfractions(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "POST", path = "sincroapp/getRadars", hasBody = true)
    Call<JsonObject> getRadars(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "POST", path = "sincroapp/getNewInfractions", hasBody = true)
    Call<JsonObject> getNewInfractions(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "POST", path = "sincroapp/getRadar", hasBody = true)
    Call<JsonObject> getRadar(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "POST", path = "sincroapp/payInfraction", hasBody = true)
    Call<JsonObject> payInfraction(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "POST", path = "sincroapp/getCitizenNotifications", hasBody = true)
    Call<JsonObject> getCitizenNotifications(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "POST", path = "sincroapp/updateCitizenEmail", hasBody = true)
    Call<JsonObject> updateCitizenEmail(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "POST", path = "sincroapp/updateCitizenPhoneNumber", hasBody = true)
    Call<JsonObject> updateCitizenPhoneNumber(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "POST", path = "sincroapp/getVehicles", hasBody = true)
    Call<JsonObject> getVehicles(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "POST", path = "sincroapp/giveVehicleAuthorization", hasBody = true)
    Call<JsonObject> giveVehicleAuthorization(@Body JsonObject jsonObject);
}