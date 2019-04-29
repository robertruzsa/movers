package com.robertruzsa.movers.ui;

import android.content.Intent;

import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.robertruzsa.movers.R;

import java.util.regex.Pattern;

public class ClientSignUpActivity extends BaseActivity {

    protected TextInputLayout nameTextInputLayout, emailTextInputLayout, phoneTextInputLayout;
    protected TextInputEditText nameEditText, emailEditText, phoneNumberEditText;

    protected String phoneNumber;

    private Pattern emailPattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setToolbarTitle(getString(R.string.registration));
        setHeaderTextView(getString(R.string.step_three));
        setBodyTextView(getString(R.string.instruction_personal_information));
        setPageIndicatorViewProgress();

        nameTextInputLayout = findViewById(R.id.signUpClientNameTextInputLayout);
        emailTextInputLayout = findViewById(R.id.signUpClientEmailTextInputLayout);
        phoneTextInputLayout = findViewById(R.id.signUpClientPhoneNumberTextInputLayout);

        phoneNumberEditText = (TextInputEditText) phoneTextInputLayout.getEditText();
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        phoneNumberEditText.setText(PhoneNumberUtils.formatNumber(phoneNumber, "HU"));
        phoneNumberEditText.setEnabled(false);

        emailEditText = (TextInputEditText) emailTextInputLayout.getEditText();
        nameEditText = (TextInputEditText) nameTextInputLayout.getEditText();

        getNextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });

        nameEditText.setText("Kovács Péter"); // TODO: remove this line
    }

    public void saveUserData() {
        String emailAddress = emailEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        if (validateName(name) & validateEmailAddress(emailAddress)) {
            ParseUser.getCurrentUser().put("name", nameEditText.getText().toString());
            if (!emailEditText.equals(""))
                ParseUser.getCurrentUser().setEmail(emailEditText.getText().toString());

            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Log.i("User data", "Save successful");
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intent);
                }
            });
        }

        /*if (validateName(name) & validateEmailAddress(emailAddress)) {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        }*/
    }

    public boolean validateEmailAddress(String emailAddress) {
        if (!emailAddress.equals("") && !emailPattern.matcher(emailAddress).matches()) {
            emailTextInputLayout.setError(getString(R.string.invalid_email));
            return false;
        } else {
            emailTextInputLayout.setError(null);
            return true;
        }
    }

    public boolean validateName(String name) {
        if (!name.equals("")) {
            nameTextInputLayout.setError(null);
            return true;
        } else {
            nameTextInputLayout.setError(getString(R.string.required));
            return false;
        }
    }
}
