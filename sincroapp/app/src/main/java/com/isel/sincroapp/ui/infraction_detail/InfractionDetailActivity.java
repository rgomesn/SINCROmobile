package com.isel.sincroapp.ui.infraction_detail;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Citizen;
import com.isel.sincroapp.data.entities.DistanceInfraction;
import com.isel.sincroapp.data.entities.Infraction;
import com.isel.sincroapp.data.entities.Radar;
import com.isel.sincroapp.data.entities.RedLightInfraction;
import com.isel.sincroapp.data.entities.SpeedInfraction;
import com.isel.sincroapp.data.entities.User;
import com.isel.sincroapp.data.model.infraction_detail.InfractionDetailViewModel;
import com.isel.sincroapp.data.model.infraction_detail.InfractionDetailViewModelFactory;
import com.isel.sincroapp.data.model.main.MainViewModel;
import com.isel.sincroapp.data.model.main.MainViewModelFactory;
import com.isel.sincroapp.ui.main.MainActivity;
import com.isel.sincroapp.ui.map.MapActivity;
import com.isel.sincroapp.ui.payment.PaymentActivity;
import com.isel.sincroapp.ui.register.VerificationCodeFragment;
import com.isel.sincroapp.util.ServerResult;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InfractionDetailActivity extends AppCompatActivity {
    private Citizen citizen;
    private User user;
    private String token;
    private Radar radar;
    private Infraction infraction;
    private Toolbar toolbar;
    private InfractionDetailViewModel infractionDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infraction_detail);

        citizen = (Citizen) getIntent().getSerializableExtra("citizen");
        user = (User) getIntent().getSerializableExtra("user");
        token = getIntent().getStringExtra("token");
        infraction = (Infraction) getIntent().getSerializableExtra("infraction");

        TextView licence_plate_text = findViewById(R.id.detail_licence_plate);
        final TextView radar_text = findViewById(R.id.detail_radar);
        TextView date_time_text = findViewById(R.id.detail_date_time);
        TextView price_text = findViewById(R.id.detail_price);
        TextView payment_date_time_text = findViewById(R.id.detail_payment_date_time);
        final Button pay_button = findViewById(R.id.pay_button);

        licence_plate_text.setText(infraction.getLicence_plate());
        date_time_text.setText(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(infraction.getInfraction_date_time()));
        price_text.setText(String.format("%.2f", infraction.getPrice()) + " euros");
        payment_date_time_text.setText(infraction.isPaid() ? new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(infraction.getPayment_date_time()) : "Por pagar");

        infractionDetailViewModel =
                new ViewModelProvider(this, new InfractionDetailViewModelFactory(getApplicationContext(), token)).get(InfractionDetailViewModel.class);

        infractionDetailViewModel.getRadarServerResult().observe(this, new Observer<ServerResult<?>>() {
            @Override
            public void onChanged(ServerResult<?> serverResult) {
                JsonObject obj = (JsonObject) serverResult.getResult();
                Gson gson = new Gson();
                radar = gson.fromJson(obj.get("radar"), Radar.class);

                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                radar_text.setText(radar.getAddress() + " - " + radar.getDirection() + " - Km " + radar.getKilometer());

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                if (infraction instanceof SpeedInfraction) {
                    toolbar.setTitle(R.string.speed_infraction_title);
                    SpeedInfraction si = (SpeedInfraction)infraction;
                    SpeedInfractionFragment frag = new SpeedInfractionFragment(si);
                    Bundle args = new Bundle();
                    args.putSerializable("infraction", si);
                    frag.setArguments(args);
                    fragmentTransaction.replace(R.id.infraction_frag, frag);
                } else if (infraction instanceof RedLightInfraction) {
                    toolbar.setTitle(R.string.red_light_infraction_title);
                    RedLightInfraction rli = (RedLightInfraction)infraction;
                    RedLightInfractionFragment frag = new RedLightInfractionFragment(rli);
                    Bundle args = new Bundle();
                    args.putSerializable("infraction", rli);
                    frag.setArguments(args);
                    fragmentTransaction.replace(R.id.infraction_frag, frag);
                } else {
                    toolbar.setTitle(R.string.distance_infraction_title);
                    DistanceInfraction di = (DistanceInfraction)infraction;
                    DistanceInfractionFragment frag = new DistanceInfractionFragment(di);
                    Bundle args = new Bundle();
                    args.putSerializable("infraction", di);
                    frag.setArguments(args);
                    fragmentTransaction.replace(R.id.infraction_frag, frag);
                }

                fragmentTransaction.commit();

                if (infraction.isPaid()) {
                    pay_button.setVisibility(View.INVISIBLE);
                } else {
                    pay_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(InfractionDetailActivity.this, PaymentActivity.class);

                            intent.putExtra("citizen", citizen);
                            intent.putExtra("user", user);
                            intent.putExtra("token", token);
                            intent.putExtra("infraction", infraction);

                            startActivity(intent);
                        }
                    });
                }
            }
        });

        infractionDetailViewModel.getRadar(infraction.getRadar_id());
    }
}