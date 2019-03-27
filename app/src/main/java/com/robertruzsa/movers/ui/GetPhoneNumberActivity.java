package com.robertruzsa.movers.ui;

import android.app.ProgressDialog;

import android.os.Bundle;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.robertruzsa.movers.auth.Authentication;
import com.robertruzsa.movers.R;

import java.util.regex.Pattern;

import androidx.constraintlayout.widget.ConstraintLayout;

public class GetPhoneNumberActivity extends BaseActivity {

    Pattern pattern = Pattern.compile("^[+]?[0-9]{10,13}$");

    private TextInputLayout phoneNumberTextInputLayout;
    private TextInputEditText phoneNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_phone_number);

        setToolbarTitle(getString(R.string.contact));
        setHeaderTextView(getString(R.string.step_one));
        setBodyTextView(getString(R.string.instruction_phone_number));

        phoneNumberTextInputLayout = findViewById(R.id.phoneNumberTextInputLayout);
        phoneNumberEditText = (TextInputEditText) phoneNumberTextInputLayout.getEditText();
        phoneNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher("HU"));
        phoneNumberEditText.setText("+36201234567"); // TODO: Remove this line

        getNextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCode();
            }
        });
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        if (!pattern.matcher(phoneNumber).matches()) {
            phoneNumberTextInputLayout.setError(getString(R.string.invalid_phone_number));
            return false;
        } else {
            phoneNumberTextInputLayout.setError(null);
            return true;
        }
    }

    public void requestCode() {
        String phoneNumber = phoneNumberEditText.getText().toString().replace(" ", "");
        if (validatePhoneNumber(phoneNumber)) {
            ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.sending_code), true);
            Authentication.Get(getApplicationContext()).requestPhoneVerification(phoneNumber, progressDialog);
        }
    }

}
