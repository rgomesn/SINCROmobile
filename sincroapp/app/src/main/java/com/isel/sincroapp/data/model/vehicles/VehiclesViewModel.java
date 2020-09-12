package com.isel.sincroapp.data.model.vehicles;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Citizen;
import com.isel.sincroapp.data.entities.VehicleAuthorization;
import com.isel.sincroapp.services.SINCROServerService;
import com.isel.sincroapp.util.ServerResult;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehiclesViewModel extends ViewModel {
    private SINCROServerService sincroServerService;
    private MutableLiveData<ServerResult<?>> vehiclesServerResult = new MutableLiveData<>();
    private MutableLiveData<ServerResult<?>> vehicleAuthorizationServerResult = new MutableLiveData<>();

    public MutableLiveData<ServerResult<?>> getVehiclesServerResult() {
        return vehiclesServerResult;
    }

    public MutableLiveData<ServerResult<?>> getVehicleAuthorizationServerResult() {
        return vehicleAuthorizationServerResult;
    }

    VehiclesViewModel(SINCROServerService sincroServerService) {
        this.sincroServerService = sincroServerService;
    }

    public void getVehicles(Citizen citizen) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("citizen", new JsonParser().parse(new GsonBuilder().disableHtmlEscaping().create().toJson(citizen)));
        sincroServerService.getVehicles(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    vehiclesServerResult.postValue(new ServerResult<JsonObject>(200, response.body()));
                }
                else {
                    vehiclesServerResult.postValue(new ServerResult<Object>(response.code(), response.message()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                vehiclesServerResult.postValue(new ServerResult<Object>(500, R.string.network_error));
            }
        });
    }

    public void giveVehicleAuthorization(VehicleAuthorization vehicleAuthorization) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("vehicleAuthorization", new JsonParser().parse(new GsonBuilder().disableHtmlEscaping().setDateFormat("yyyy-MM-dd hh:mm:ss").create().toJson(vehicleAuthorization)));
        sincroServerService.giveVehicleAuthorization(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    vehicleAuthorizationServerResult.postValue(new ServerResult<JsonObject>(200, response.body()));
                }
                else {
                    vehicleAuthorizationServerResult.postValue(new ServerResult<Object>(response.code(), response.message()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                vehicleAuthorizationServerResult.postValue(new ServerResult<Object>(500, R.string.network_error));
            }
        });
    }
}
