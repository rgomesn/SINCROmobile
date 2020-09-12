package com.isel.sincroapp.ui.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.isel.sincroapp.R;
import com.isel.sincroapp.data.entities.Citizen;

public class VerificationCodeFragment extends Fragment {
    VerificationCodeListener listener;

    public VerificationCodeFragment(VerificationCodeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verification_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        TextView cc_number_field = (TextView)view.findViewById(R.id.cc_number_field);
        TextView email_field = (TextView)view.findViewById(R.id.email_field);
        final Citizen citizen = (Citizen)args.get("citizen");
        cc_number_field.setText(citizen.getCc_number());
        String email = citizen.getEmail();
        email_field.setText(email.substring(0, 3) + "*****" + email.substring(email.indexOf('@')));
        Button submit = (Button)view.findViewById(R.id.button);
        final EditText verification_code_input = view.findViewById(R.id.verification_code_input);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verification_code_input.getText().length() == 9) {
                    String txt = verification_code_input.getText().toString();
                    listener.verifyRegistrationCode(citizen, txt);
                }
                else {
                    Toast.makeText(getContext(), R.string.wrong_cc_number_input, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public interface VerificationCodeListener {
        void verifyRegistrationCode(Citizen citizen, String inputCode);
    }
}