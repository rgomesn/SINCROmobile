package com.isel.sincroapp.data.model.map;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Citizen;
import com.isel.sincroapp.data.entities.Radar;
import com.isel.sincroapp.services.SINCROServerService;
import com.isel.sincroapp.util.ServerResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapViewModel extends ViewModel {
    SINCROServerService sincroServerService;
    private MutableLiveData<ServerResult<?>> infractionsServerResult = new MutableLiveData<>();
    private MutableLiveData<ServerResult<?>> radarsServerResult = new MutableLiveData<>();

    public MutableLiveData<ServerResult<?>> getInfractionsServerResult() {
        return infractionsServerResult;
    }

    public MutableLiveData<ServerResult<?>> getRadarsServerResult() {
        return radarsServerResult;
    }

    MapViewModel(SINCROServerService sincroServerService) {
        this.sincroServerService = sincroServerService;
    }

    public void getRadars() {
        JsonObject jsonObject = new JsonObject();
        sincroServerService.getRadars(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    radarsServerResult.postValue(new ServerResult<JsonObject>(200, response.body()));
                }
                else {
                    radarsServerResult.postValue(new ServerResult<Object>(response.code(), response.message()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                radarsServerResult.postValue(new ServerResult<Object>(500, R.string.network_error));
            }
        });
    }
}