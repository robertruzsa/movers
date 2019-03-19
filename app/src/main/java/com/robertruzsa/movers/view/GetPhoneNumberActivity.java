package com.robertruzsa.movers.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.robertruzsa.movers.auth.Authentication;
import com.robertruzsa.movers.R;
import com.robertruzsa.movers.helper.Instruction;
import com.santalu.maskedittext.MaskEditText;

import java.util.regex.Pattern;

public class GetPhoneNumberActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MaskEditText phoneNumberEditText;

    Pattern pattern = Pattern.compile("^$|[0-9 ]+");

    static final String COUNTRY_CALLING_CODE = "+36";
    static final int VALID_PHONE_NUMBER_LENGTH = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_phone_number);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView titleTextView = (TextView) toolbar.getChildAt(0);
        titleTextView.setText(getString(R.string.telefonszam));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView instructionTextView = findViewById(R.id.instructionPhoneNumberTextView);
        Instruction.show(instructionTextView, R.string.instruction_phone_number);

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        phoneNumberEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(phoneNumberEditText, 0);
            }
        }, 1000);

        phoneNumberEditText.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.i("char seq", String.valueOf(s));
                if (!pattern.matcher(s).matches())
                    phoneNumberEditText.setError(getString(R.string.ervenytelen_telefonszam));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        phoneNumberEditText.setOnKeyListener(new View.OnKeyListener() {
            Pattern pattern = Pattern.compile("^$|[0-9 ]+");
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (!isValidPhoneNumber(phoneNumberEditText.getRawText()))
                        phoneNumberEditText.setError(getString(R.string.ervenytelen_telefonszam));
                }
                return false;
            }
        });
    }

    public void next(View view) {
        if (isValidPhoneNumber(phoneNumberEditText.getRawText())) {
            String phoneNumber = COUNTRY_CALLING_CODE + phoneNumberEditText.getRawText();
            ProgressDialog progressDialog = ProgressDialog.show(this, "", "Kód elküldése folyamatban...", true);
            Authentication.Get(getApplicationContext()).requestPhoneVerification(phoneNumber, progressDialog);
        } else {
            phoneNumberEditText.setError(getString(R.string.ervenytelen_telefonszam));
        }
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        Log.i("phone number length", String.valueOf(phoneNumber.length()));
        return pattern.matcher(phoneNumber).matches() && phoneNumber.length() == VALID_PHONE_NUMBER_LENGTH;
    }

}
