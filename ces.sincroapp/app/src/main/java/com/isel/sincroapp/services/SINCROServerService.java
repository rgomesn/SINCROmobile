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
    //@GET("sincroapp/getInfractions")
    Call<JsonObject> getInfractions(@Body JsonObject jsonObject);
}
