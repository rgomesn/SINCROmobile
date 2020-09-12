package com.isel.sincroapp.data.model.payment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Infraction;
import com.isel.sincroapp.services.SINCROServerService;
import com.isel.sincroapp.util.ServerResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentViewModel extends ViewModel {
    private SINCROServerService sincroServerService;
    private MutableLiveData<ServerResult<?>> paymentServerResult = new MutableLiveData<>();

    public MutableLiveData<ServerResult<?>> getPaymentServerResult() {
        return paymentServerResult;
    }

    PaymentViewModel(SINCROServerService sincroServerService) {
        this.sincroServerService = sincroServerService;
    }

    public void payInfraction(long infraction_id, String cardNumber, String expirationDate, String cvv) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("infraction_id", new JsonParser().parse(new GsonBuilder().disableHtmlEscaping().create().toJson(infraction_id)));
        jsonObject.add("cardNumber", new JsonParser().parse(new GsonBuilder().disableHtmlEscaping().create().toJson(cardNumber)));
        jsonObject.add("expirationDate", new JsonParser().parse(new GsonBuilder().disableHtmlEscaping().create().toJson(expirationDate)));
        jsonObject.add("cvv", new JsonParser().parse(new GsonBuilder().disableHtmlEscaping().create().toJson(cvv)));
        sincroServerService.payInfraction(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    paymentServerResult.postValue(new ServerResult<JsonObject>(200, response.body()));
                }
                else {
                    paymentServerResult.postValue(new ServerResult<Object>(response.code(), response.message()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                paymentServerResult.postValue(new ServerResult<Object>(500, R.string.network_error));
            }
        });
    }
}
