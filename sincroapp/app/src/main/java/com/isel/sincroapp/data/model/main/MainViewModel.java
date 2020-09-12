package com.isel.sincroapp.data.model.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Citizen;
import com.isel.sincroapp.data.entities.Infraction;
import com.isel.sincroapp.data.entities.Radar;
import com.isel.sincroapp.services.SINCROServerService;
import com.isel.sincroapp.util.ServerResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private SINCROServerService sincroServerService;
    /*private MutableLiveData<ServerResult<?>> pendingInfractionsServerResult = new MutableLiveData<>();
    private MutableLiveData<ServerResult<?>> pastInfractionsServerResult = new MutableLiveData<>();*/
    private MutableLiveData<ServerResult<?>> infractionsServerResult = new MutableLiveData<>();
    private MutableLiveData<ServerResult<?>> radarInfractionsServerResult = new MutableLiveData<>();

    /*public MutableLiveData<ServerResult<?>> getPendingInfractionsServerResult() {
        return pendingInfractionsServerResult;
    }

    public MutableLiveData<ServerResult<?>> getPastingInfractionsServerResult() {
        return pastInfractionsServerResult;
    }*/

    public MutableLiveData<ServerResult<?>> getInfractionsServerResult() {
        return infractionsServerResult;
    }

    public MutableLiveData<ServerResult<?>> getRadarInfractionsServerResult() {
        return radarInfractionsServerResult;
    }

    MainViewModel(SINCROServerService sincroServerService) {
        this.sincroServerService = sincroServerService;
    }

    public void getInfractions(Citizen citizen) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("citizen", new JsonParser().parse(new GsonBuilder().disableHtmlEscaping().create().toJson(citizen)));
        sincroServerService.getInfractions(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    infractionsServerResult.postValue(new ServerResult<JsonObject>(200, response.body()));
                }
                else {
                    infractionsServerResult.postValue(new ServerResult<Object>(response.code(), response.message()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                infractionsServerResult.postValue(new ServerResult<Object>(500, R.string.network_error));
            }

            /*@Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject obj = response.body();
                    Gson gson = new Gson();
                    List<Infraction> infractionList = new ArrayList<Infraction>();
                    infractionList.addAll(gson.fromJson(obj.get("speedInfractions"), List.class));
                    infractionList.addAll(gson.fromJson(obj.get("redLightInfractions"), List.class));
                    infractionList.addAll(gson.fromJson(obj.get("distanceInfractions"), List.class));

                    pendingInfractionsServerResult.postValue(new ServerResult<List<Infraction>>(200, infractionList
                            .stream()
                            .filter(i -> !i.isPaid())
                            .collect(Collectors.toList())));

                    pastInfractionsServerResult.postValue(new ServerResult<List<Infraction>>(200, infractionList
                            .stream()
                            .filter(i -> i.isPaid())
                            .collect(Collectors.toList())));
                }
                else {
                    pendingInfractionsServerResult.postValue(new ServerResult<Object>(response.code(), response.message()));
                    pastInfractionsServerResult.postValue(new ServerResult<Object>(response.code(), response.message()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                pendingInfractionsServerResult.postValue(new ServerResult<Object>(500, R.string.network_error));
                pastInfractionsServerResult.postValue(new ServerResult<Object>(500, R.string.network_error));
            }*/
        });
    }

    public void getRadarInfractions(Citizen citizen, Radar radar) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("citizen", new JsonParser().parse(new GsonBuilder().disableHtmlEscaping().create().toJson(citizen)));
        jsonObject.add("radar", new JsonParser().parse(new GsonBuilder().disableHtmlEscaping().create().toJson(radar)));
        sincroServerService.getRadarInfractions(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    radarInfractionsServerResult.postValue(new ServerResult<JsonObject>(200, response.body()));
                }
                else {
                    radarInfractionsServerResult.postValue(new ServerResult<Object>(response.code(), response.message()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                radarInfractionsServerResult.postValue(new ServerResult<Object>(500, R.string.network_error));
            }
        });
    }
}
