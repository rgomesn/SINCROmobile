package com.isel.sincroapp.data.model.infraction_detail;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.isel.sincroapp.services.SINCROServerService;
import com.isel.sincroapp.util.Utils;

import retrofit2.Retrofit;

public class InfractionDetailViewModelFactory implements ViewModelProvider.Factory {
    Context context;
    String token;

    public InfractionDetailViewModelFactory(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(InfractionDetailViewModel.class)) {
            Retrofit retrofit = Utils.getRetrofit(context, token);

            SINCROServerService apiInterface = retrofit.create(SINCROServerService.class);

            return (T) new InfractionDetailViewModel(apiInterface);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
