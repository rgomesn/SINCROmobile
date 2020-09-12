package com.isel.sincroapp.data.model.infraction_detail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.isel.sincroapp.R;
import com.isel.sincroapp.services.SINCROServerService;
import com.isel.sincroapp.util.ServerResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfractionDetailViewModel extends ViewModel {
    private SINCROServerService sincroServerService;
    private MutableLiveData<ServerResult<?>> radarServerResult = new MutableLiveData<>();

    public MutableLiveData<ServerResult<?>> getRadarServerResult() {
        return radarServerResult;
    }

    InfractionDetailViewModel(SINCROServerService sincroServerService) {
        this.sincroServerService = sincroServerService;
    }

    public void getRadar(long radar_id) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("radar_id", new JsonParser().parse(new GsonBuilder().disableHtmlEscaping().create().toJson(radar_id)));
        sincroServerService.getRadar(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    radarServerResult.postValue(new ServerResult<JsonObject>(200, response.body()));
                }
                else {
                    radarServerResult.postValue(new ServerResult<Object>(response.code(), response.message()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                radarServerResult.postValue(new ServerResult<Object>(500, R.string.network_error));
            }
        });
    }
}
