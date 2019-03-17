package com.robertruzsa.movers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.santalu.maskedittext.MaskEditText;

public class GetPhoneNumberActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MaskEditText phoneNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_phone_number);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView titleTextView = (TextView) toolbar.getChildAt(0);
        titleTextView.setText("Telefonszám");

        TextView instructionTextView = findViewById(R.id.instructionTextView);

        Instruction.show(instructionTextView, R.string.instruction_phone_number);

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);


    }


    public void next(View view) {
        Authentication.requestPhoneVerification("+36202453956");
    }
}
