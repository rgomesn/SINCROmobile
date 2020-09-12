package com.isel.sincroapp.ui.vehicles;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Citizen;
import com.isel.sincroapp.data.entities.User;
import com.isel.sincroapp.data.entities.Vehicle;
import com.isel.sincroapp.data.entities.VehicleAuthorization;
import com.isel.sincroapp.data.model.infraction_detail.InfractionDetailViewModel;
import com.isel.sincroapp.data.model.infraction_detail.InfractionDetailViewModelFactory;
import com.isel.sincroapp.data.model.vehicles.VehiclesViewModel;
import com.isel.sincroapp.data.model.vehicles.VehiclesViewModelFactory;
import com.isel.sincroapp.ui.infraction_detail.InfractionDetailActivity;
import com.isel.sincroapp.ui.payment.PaymentActivity;
import com.isel.sincroapp.util.ServerResult;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class VehicleDetailActivity extends AppCompatActivity {
    private Citizen citizen;
    private User user;
    private String token;
    private Vehicle vehicle;
    private boolean isOwned;
    private VehiclesViewModel vehiclesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);

        citizen = (Citizen) getIntent().getSerializableExtra("citizen");
        user = (User) getIntent().getSerializableExtra("user");
        token = getIntent().getStringExtra("token");
        vehicle = (Vehicle) getIntent().getSerializableExtra("vehicle");
        isOwned = getIntent().getBooleanExtra("isOwned", false);

        TextView licence_plate_text = findViewById(R.id.detail_licence_plate);
        final TextView make_text = findViewById(R.id.detail_make);
        TextView model_text = findViewById(R.id.detail_model);
        TextView vehicle_date_text = findViewById(R.id.detail_vehicle_date);
        TextView vehicle_category_text = findViewById(R.id.detail_vehicle_category);
        final Button autorize_button = findViewById(R.id.autorize_button);

        licence_plate_text.setText(vehicle.getLicence_plate());
        make_text.setText(vehicle.getMake());
        model_text.setText(vehicle.getModel());
        vehicle_date_text.setText(new SimpleDateFormat("dd/MM/yyyy").format(vehicle.getVehicle_date()));
        vehicle_category_text.setText(vehicle.getCategory() == 'L' ? "Ligeiros" : (CharSequence) (vehicle.getCategory() == 'P' ? "Pesados" : vehicle.getCategory()));

        vehiclesViewModel =
                new ViewModelProvider(this, new VehiclesViewModelFactory(getApplicationContext(), token)).get(VehiclesViewModel.class);

        if (!isOwned) autorize_button.setVisibility(View.INVISIBLE);

        autorize_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VehicleDetailActivity.this);
                builder.setTitle("Insira o número do CC que pretende autorizar");

                final EditText input = new EditText(VehicleDetailActivity.this);

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newUser = input.getText().toString();

                        VehicleAuthorization vehicleAuthorization = new VehicleAuthorization(0,
                            citizen.getCc_number(),
                            newUser,
                            vehicle.getLicence_plate(),
                            true,
                            Date.from(Instant.now()),
                            Date.from(Instant.now()));

                        vehiclesViewModel.getVehicleAuthorizationServerResult().observe(VehicleDetailActivity.this, new Observer<ServerResult<?>>() {
                            @Override
                            public void onChanged(ServerResult<?> serverResult) {
                                if (!serverResult.isOk()) {
                                    Toast.makeText(VehicleDetailActivity.this, "Não foi possível autorizar o condutor", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(VehicleDetailActivity.this, "Condutor autorizado", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        vehiclesViewModel.giveVehicleAuthorization(vehicleAuthorization);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }
}