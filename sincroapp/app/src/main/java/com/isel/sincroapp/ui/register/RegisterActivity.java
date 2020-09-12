package com.isel.sincroapp.ui.register;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Citizen;
import com.isel.sincroapp.data.entities.User;
import com.isel.sincroapp.data.model.register.RegisterViewModel;
import com.isel.sincroapp.data.model.register.RegisterViewModelFactory;
import com.isel.sincroapp.ui.login.LoginActivity;
import com.isel.sincroapp.util.ServerResult;

public class RegisterActivity extends FragmentActivity implements CcNumberInputFragment.CcNumberInputListener,
        VerificationCodeFragment.VerificationCodeListener,
        NewUserFragment.NewUserListener {
    RegisterViewModel registerViewModel;
    Citizen citizenObj;
    String inputCode;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory(getApplicationContext())).get(RegisterViewModel.class);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CcNumberInputFragment ccNumberInputFragment = new CcNumberInputFragment(this);
        fragmentTransaction.add(R.id.register_const_layout, ccNumberInputFragment);
        fragmentTransaction.commit();
    }

    public void beginRegistration(String cc_number) {
        registerViewModel.getServerResult().removeObservers(this);
        registerViewModel.getServerResult().observe(this, new Observer<ServerResult<?>>() {
            @Override
            public void onChanged(ServerResult<?> serverResult) {
                if (serverResult.isOk()) {
                    citizenObj = (Citizen)serverResult.getResult();
                    registerWithEmail(citizenObj);
                }
                else {
                    if (serverResult.getError() != null) {
                        Toast.makeText(getApplicationContext(), serverResult.getStringCode(), Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Toast.makeText(getApplicationContext(), serverResult.getStringCode(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        registerViewModel.beginRegistration(cc_number);
    }

    public void registerWithEmail(final Citizen citizen) {
        registerViewModel.getServerResult().removeObservers(this);
        final RegisterActivity act = this;
        registerViewModel.getServerResult().observe(this, new Observer<ServerResult<?>>() {
            @Override
            public void onChanged(ServerResult<?> serverResult) {
                if (serverResult.isOk()) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    VerificationCodeFragment verificationCodeFragment = new VerificationCodeFragment(act);
                    Bundle args = new Bundle();
                    args.putSerializable("citizen", citizenObj);
                    verificationCodeFragment.setArguments(args);
                    fragmentTransaction.replace(R.id.register_const_layout, verificationCodeFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        registerViewModel.registerWithEmail(citizen);
    }

    public void verifyRegistrationCode(Citizen citizen, String inputCode) {
        registerViewModel.getServerResult().removeObservers(this);
        final RegisterActivity act = this;
        registerViewModel.getServerResult().observe(this, new Observer<ServerResult<?>>() {
            @Override
            public void onChanged(ServerResult<?> serverResult) {
                if (serverResult.isOk()) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    NewUserFragment newUserFragment = new NewUserFragment(act);
                    Bundle args = new Bundle();
                    args.putSerializable("citizen", citizenObj);
                    newUserFragment.setArguments(args);
                    fragmentTransaction.replace(R.id.register_const_layout, newUserFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        registerViewModel.verifyRegistrationCode(citizen, inputCode);
    }

    public void registerLoginAndPassword(User user) {
        registerViewModel.getServerResult().removeObservers(this);
        registerViewModel.getServerResult().observe(this, new Observer<ServerResult<?>>() {
            @Override
            public void onChanged(ServerResult<?> serverResult) {
                if (serverResult.isOk()) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        registerViewModel.registerLoginAndPassword(user);
    }
}