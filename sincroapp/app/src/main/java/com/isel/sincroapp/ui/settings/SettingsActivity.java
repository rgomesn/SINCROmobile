package com.isel.sincroapp.ui.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Citizen;
import com.isel.sincroapp.data.entities.DistanceInfraction;
import com.isel.sincroapp.data.entities.Infraction;
import com.isel.sincroapp.data.entities.RedLightInfraction;
import com.isel.sincroapp.data.entities.SpeedInfraction;
import com.isel.sincroapp.data.entities.User;
import com.isel.sincroapp.services.SINCROServerService;
import com.isel.sincroapp.ui.login.LoginActivity;
import com.isel.sincroapp.ui.main.MainActivity;
import com.isel.sincroapp.ui.map.MapActivity;
import com.isel.sincroapp.ui.vehicles.VehiclesActivity;
import com.isel.sincroapp.util.Utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SettingsActivity extends AppCompatActivity {
    private static Citizen citizen;
    private User user;
    private String token;
    private static SINCROServerService sincroServerService;
    private static Context context;
    private static SettingsActivity sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        sa = this;

        if (savedInstanceState != null) {
            citizen = (Citizen)savedInstanceState.get("citizen");
            user = (User)savedInstanceState.get("user");
            token = (String)savedInstanceState.get("token");
        }

        else {
            citizen = (Citizen)getIntent().getSerializableExtra("citizen");
            user = (User)getIntent().getSerializableExtra("user");
            token = getIntent().getStringExtra("token");
        }

        Retrofit retrofit = Utils.getRetrofit(getApplicationContext(), token);
        sincroServerService = retrofit.create(SINCROServerService.class);

        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        /*Toolbar toolbar = (Toolbar) SettingsActivity.this.findViewById(R.id.toolbar);
        SettingsActivity.this.setSupportActionBar(toolbar);
        toolbar.setTitle("Definições");*/
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            EditTextPreference email = findPreference("email");
            EditTextPreference phone_number = findPreference("phone_number");
            ListPreference sync_time = findPreference("sync_time");
            Preference vehicles = findPreference("vehicles");

            email.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(final Preference preference, Object newValue) {
                    final String oldEmail = citizen.getEmail();

                    citizen.setEmail((String) newValue);

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.add("citizen", new JsonParser().parse(new GsonBuilder().disableHtmlEscaping().create().toJson(citizen)));

                    sincroServerService.updateCitizenEmail(jsonObject).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (!response.isSuccessful()) {
                                SharedPreferences editor = preference.getSharedPreferences();
                                editor.edit().putString(preference.getKey(), oldEmail).apply();
                                citizen.setEmail(oldEmail);
                                Toast.makeText(context, "Não foi possível alterar o email", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(context, "Email alterado com sucesso", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            SharedPreferences editor = preference.getSharedPreferences();
                            editor.edit().putString(preference.getKey(), oldEmail).apply();
                            citizen.setEmail(oldEmail);
                            Toast.makeText(context, "Não foi possível alterar o email", Toast.LENGTH_LONG).show();
                        }
                    });

                    return true;
                }
            });

            phone_number.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(final Preference preference, Object newValue) {
                    final String oldPhone_number = citizen.getPhone_number();

                    citizen.setPhone_number((String) newValue);

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.add("citizen", new JsonParser().parse(new GsonBuilder().disableHtmlEscaping().create().toJson(citizen)));

                    sincroServerService.updateCitizenPhoneNumber(jsonObject).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (!response.isSuccessful()) {
                                SharedPreferences editor = preference.getSharedPreferences();
                                editor.edit().putString(preference.getKey(), oldPhone_number).apply();
                                citizen.setPhone_number(oldPhone_number);
                                Toast.makeText(context, "Não foi possível alterar o número de telemóvel", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(context, "Número de telemóvel alterado com sucesso", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            SharedPreferences editor = preference.getSharedPreferences();
                            editor.edit().putString(preference.getKey(), oldPhone_number).apply();
                            citizen.setPhone_number(oldPhone_number);
                            Toast.makeText(context, "Não foi possível alterar o número de telemóvel", Toast.LENGTH_LONG).show();
                        }
                    });

                    return true;
                }
            });

            vehicles.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(SettingsActivity.sa, VehiclesActivity.class);

                    intent.putExtra("citizen", citizen);
                    intent.putExtra("user", SettingsActivity.sa.user);
                    intent.putExtra("token", SettingsActivity.sa.token);

                    startActivity(intent);

                    return true;
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("citizen", citizen);
        outState.putSerializable("user", user);
        outState.putString("token", token);

        super.onSaveInstanceState(outState);
    }
}