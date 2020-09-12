package com.isel.sincroapp.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.braintreepayments.cardform.view.CardForm;
import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Citizen;
import com.isel.sincroapp.data.entities.Infraction;
import com.isel.sincroapp.data.entities.Radar;
import com.isel.sincroapp.data.entities.User;
import com.isel.sincroapp.data.model.infraction_detail.InfractionDetailViewModel;
import com.isel.sincroapp.data.model.infraction_detail.InfractionDetailViewModelFactory;
import com.isel.sincroapp.data.model.payment.PaymentViewModel;
import com.isel.sincroapp.data.model.payment.PaymentViewModelFactory;
import com.isel.sincroapp.ui.main.MainActivity;
import com.isel.sincroapp.ui.map.MapActivity;
import com.isel.sincroapp.util.ServerResult;

public class PaymentActivity extends AppCompatActivity {
    private Citizen citizen;
    private User user;
    private String token;
    private Radar radar;
    private Infraction infraction;
    private PaymentViewModel paymentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        citizen = (Citizen) getIntent().getSerializableExtra("citizen");
        user = (User) getIntent().getSerializableExtra("user");
        token = getIntent().getStringExtra("token");
        infraction = (Infraction) getIntent().getSerializableExtra("infraction");

        paymentViewModel =
                new ViewModelProvider(this, new PaymentViewModelFactory(getApplicationContext(), token)).get(PaymentViewModel.class);

        final CardForm cardForm = findViewById(R.id.card_form);
        Button buy = findViewById(R.id.btnBuy);

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .setup(PaymentActivity.this);

        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()) {
                    String cardNumber = cardForm.getCardNumber();
                    String expirationDate = cardForm.getExpirationMonth() + "/" + cardForm.getExpirationYear();
                    String cvv = cardForm.getCvv();

                    paymentViewModel.getPaymentServerResult().observe(PaymentActivity.this, new Observer<ServerResult<?>>() {
                        @Override
                        public void onChanged(ServerResult<?> serverResult) {
                            if (serverResult.isOk()) {
                                Toast.makeText(PaymentActivity.this, "Pagamento efetuado com sucesso", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);

                                intent.putExtra("citizen", citizen);
                                intent.putExtra("user", user);
                                intent.putExtra("token", token);

                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(intent);
                            }
                        }
                    });

                    paymentViewModel.payInfraction(infraction.getInfraction_id(), cardNumber, expirationDate, cvv);
                } else {
                    Toast.makeText(PaymentActivity.this, "Por favor complete o formulário", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}