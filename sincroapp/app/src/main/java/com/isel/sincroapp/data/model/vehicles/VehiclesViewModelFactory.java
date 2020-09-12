package com.isel.sincroapp.data.model.vehicles;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.isel.sincroapp.services.SINCROServerService;
import com.isel.sincroapp.util.Utils;

import retrofit2.Retrofit;

public class VehiclesViewModelFactory implements ViewModelProvider.Factory {
    Context context;
    String token;

    public VehiclesViewModelFactory(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(VehiclesViewModel.class)) {
            Retrofit retrofit = Utils.getRetrofit(context, token);

            SINCROServerService apiInterface = retrofit.create(SINCROServerService.class);

            return (T) new VehiclesViewModel(apiInterface);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
