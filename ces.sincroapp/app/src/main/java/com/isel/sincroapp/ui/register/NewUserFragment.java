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
import com.isel.sincroapp.data.entities.User;

public class NewUserFragment extends Fragment {
    NewUserListener listener;

    public NewUserFragment(NewUserListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        TextView full_name_value = view.findViewById(R.id.full_name_value);
        TextView cc_number_value = view.findViewById(R.id.cc_number_value);
        TextView driver_licence_number_value = view.findViewById(R.id.driver_licence_number_value);
        TextView email_value = view.findViewById(R.id.email_value);
        TextView phone_number_value = view.findViewById(R.id.phone_number_value);

        final Citizen citizen = (Citizen)args.get("citizen");
        full_name_value.setText(citizen.getFirst_name() + " " + citizen.getMiddle_name() + " " + citizen.getLast_name());
        cc_number_value.setText(citizen.getCc_number());
        driver_licence_number_value.setText(citizen.getDriver_licence_number());
        email_value.setText(citizen.getEmail());
        phone_number_value.setText(citizen.getPhone_number());

        final EditText username_input = view.findViewById(R.id.username_input);
        final EditText password_input = view.findViewById(R.id.password_input);
        Button submit = view.findViewById(R.id.submit_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(username_input.getText().toString(), password_input.getText().toString(), citizen.getCc_number());
                listener.registerLoginAndPassword(user);
            }
        });
    }

    public interface NewUserListener {
        void registerLoginAndPassword(User user);
    }
}