package com.isel.sincroapp.data.model.login;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.isel.sincroapp.services.SINCROServerService;
import com.isel.sincroapp.util.Utils;

import retrofit2.Retrofit;

/**
 * ViewModel provider factory to instantiate MapViewModel.
 * Required given MapViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {
    Context context;

    public LoginViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            Retrofit retrofit = Utils.getRetrofit(context);

            SINCROServerService apiInterface = retrofit.create(SINCROServerService.class);

            return (T) new LoginViewModel(apiInterface);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}