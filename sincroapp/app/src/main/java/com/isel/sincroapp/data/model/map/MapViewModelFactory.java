package com.isel.sincroapp.data.model.map;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.isel.sincroapp.services.SINCROServerService;
import com.isel.sincroapp.util.Utils;

import retrofit2.Retrofit;

/**
 * ViewModel provider factory to instantiate MapViewModel.
 * Required given MapViewModel has a non-empty constructor
 */
public class MapViewModelFactory implements ViewModelProvider.Factory {
    Context context;
    String token;

    public MapViewModelFactory(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            Retrofit retrofit = Utils.getRetrofit(context, token);

            SINCROServerService apiInterface = retrofit.create(SINCROServerService.class);

            return (T) new MapViewModel(apiInterface);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}