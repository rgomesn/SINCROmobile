package com.isel.sincroapp.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Citizen;
import com.isel.sincroapp.data.entities.User;
import com.isel.sincroapp.data.model.login.LoginViewModel;
import com.isel.sincroapp.data.model.login.LoginViewModelFactory;
import com.isel.sincroapp.ui.main.MainActivity;
import com.isel.sincroapp.ui.register.RegisterActivity;
import com.isel.sincroapp.util.ServerResult;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(getApplicationContext())).get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login_button);
        final Button registerButton = findViewById(R.id.register_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginViewModel.getServerResult().observe(this, new Observer<ServerResult<?>>() {
            @Override
            public void onChanged(ServerResult<?> serverResult) {
                if (serverResult.isOk()) {
                    JsonObject obj = (JsonObject)serverResult.getResult();
                    Gson gson = new Gson();
                    Citizen citizen = gson.fromJson(obj.get("citizen"), Citizen.class);
                    User user = gson.fromJson(obj.get("user"), User.class);
                    String token = obj.get("token").getAsString();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    intent.putExtra("citizen", citizen);
                    intent.putExtra("user", user);
                    intent.putExtra("token", token);

                    startActivity(intent);
                }

                else {
                    if (serverResult.getCode() == 400) {
                        Toast.makeText(getApplicationContext(), R.string.login_failed, Toast.LENGTH_SHORT).show();
                    }

                    else {
                        Toast.makeText(getApplicationContext(), serverResult.getError(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}