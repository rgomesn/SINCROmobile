package com.isel.sincroapp.data.model.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.isel.sincroapp.data.model.register.RegisterViewModel;
import com.isel.sincroapp.services.SINCROServerService;
import com.isel.sincroapp.util.Utils;

import retrofit2.Retrofit;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    Context context;
    String token;

    public MainViewModelFactory(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            Retrofit retrofit = Utils.getRetrofit(context, token);

            SINCROServerService apiInterface = retrofit.create(SINCROServerService.class);

            return (T) new MainViewModel(apiInterface);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
