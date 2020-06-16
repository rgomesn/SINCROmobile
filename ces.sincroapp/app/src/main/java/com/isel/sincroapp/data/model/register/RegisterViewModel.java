package com.isel.sincroapp.data.model.register;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Citizen;
import com.isel.sincroapp.data.entities.User;
import com.isel.sincroapp.services.SINCROServerService;
import com.isel.sincroapp.util.ServerResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel implements Callback<JsonObject> {
    SINCROServerService sincroServerService;

    public MutableLiveData<ServerResult<?>> getServerResult() {
        return serverResult;
    }

    private MutableLiveData<ServerResult<?>> serverResult = new MutableLiveData<>();

    RegisterViewModel(SINCROServerService sincroServerService) {
        this.sincroServerService = sincroServerService;
    }

    public void beginRegistration(String cc_number) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("cc_number", cc_number);
        sincroServerService.beginRegistration(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject object = response.body();
                    Citizen citizen = new Gson().fromJson(object, Citizen.class);
                    serverResult.postValue(new ServerResult<Citizen>(200, citizen));
                }
                else {
                    serverResult.postValue(new ServerResult<Object>(response.code(), response.message()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("SINCRO_APP", "I got an error", t);
                serverResult.postValue(new ServerResult<Object>(0, R.string.network_error));
            }
        });
    }

    public void registerWithEmail(Citizen citizen) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("citizen", new JsonParser().parse(new GsonBuilder().disableHtmlEscaping().create().toJson(citizen)));
        sincroServerService.registerWithEmail(jsonObject).enqueue(this);
    }

    public void verifyRegistrationCode(Citizen citizen, String inputCode) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("citizen", new JsonParser().parse(new GsonBuilder().disableHtmlEscaping().create().toJson(citizen)));
        jsonObject.addProperty("inputCode", inputCode);
        sincroServerService.verifyRegistrationCode(jsonObject).enqueue(this);
    }

    public void registerLoginAndPassword(User user) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("user", new JsonParser().parse(new GsonBuilder().disableHtmlEscaping().create().toJson(user)));
        sincroServerService.registerLoginAndPassword(jsonObject).enqueue(this);
    }

    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        if (response.isSuccessful()) {
            serverResult.postValue(new ServerResult<Object>(200, new Object()));
        }
        else {
            serverResult.postValue(new ServerResult<Object>(response.code(), response.message()));
        }
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        serverResult.postValue(new ServerResult<Object>(500, R.string.network_error));
    }
}
