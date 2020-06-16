package com.isel.sincroapp.data.model.register;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.isel.sincroapp.services.SINCROServerService;
import com.isel.sincroapp.util.Utils;

import retrofit2.Retrofit;

public class RegisterViewModelFactory implements ViewModelProvider.Factory {
    Context context;

    public RegisterViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
            Retrofit retrofit = Utils.getRetrofit(context);

            SINCROServerService apiInterface = retrofit.create(SINCROServerService.class);

            return (T) new RegisterViewModel(apiInterface);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}