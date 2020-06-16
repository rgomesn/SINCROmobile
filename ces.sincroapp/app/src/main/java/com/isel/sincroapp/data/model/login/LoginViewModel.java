package com.isel.sincroapp.data.model.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.isel.sincroapp.R;
import com.isel.sincroapp.services.SINCROServerService;
import com.isel.sincroapp.util.ServerResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    SINCROServerService sincroServerService;

    public MutableLiveData<ServerResult<?>> getServerResult() {
        return serverResult;
    }

    private MutableLiveData<ServerResult<?>> serverResult = new MutableLiveData<>();

    LoginViewModel(SINCROServerService sincroServerService) {
        this.sincroServerService = sincroServerService;
    }

    public void login(String username, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);
        sincroServerService.login(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    serverResult.postValue(new ServerResult<JsonObject>(200, response.body()));
                }
                else {
                    serverResult.postValue(new ServerResult<Object>(response.code(), response.message()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                serverResult.postValue(new ServerResult<Object>(500, R.string.network_error));
            }
        });
    }
}