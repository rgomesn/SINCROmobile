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
import android.widget.Toast;

import com.isel.sincroapp.R;

public class CcNumberInputFragment extends Fragment {
    CcNumberInputListener listener;

    public CcNumberInputFragment(CcNumberInputListener listener) {
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
        return inflater.inflate(R.layout.fragment_cc_number_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText ccNumberInput = (EditText)view.findViewById(R.id.cc_number_input);
        Button btn = (Button)view.findViewById(R.id.cc_number_input_submit);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ccNumberInput.getText().length() == 9) {
                    String txt = ccNumberInput.getText().toString();
                    listener.beginRegistration(txt);
                }
                else {
                    Toast.makeText(getContext(), R.string.wrong_cc_number_input, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public interface CcNumberInputListener {
        void beginRegistration(String cc_number);
    }
}