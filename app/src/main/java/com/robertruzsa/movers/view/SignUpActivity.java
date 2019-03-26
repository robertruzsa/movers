package com.robertruzsa.movers.view;

import android.content.Intent;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.robertruzsa.movers.R;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {


    private TextInputLayout nameTextInputLayout, emailTextInputLayout, phoneTextInputLayout;
    private TextInputEditText nameEditText, emailEditText, phoneNumberEditText;
    ConstraintLayout signUpConstraintLayout;

    private String phoneNumber;

    private Pattern pattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        phoneTextInputLayout = findViewById(R.id.phoneNumberTextInputLayout);

        phoneNumberEditText = (TextInputEditText) phoneTextInputLayout.getEditText();
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        phoneNumberEditText.setText(phoneNumber);
        phoneNumberEditText.setEnabled(false);

        emailEditText = (TextInputEditText) emailTextInputLayout.getEditText();
        nameEditText = (TextInputEditText) nameTextInputLayout.getEditText();

        signUpConstraintLayout = findViewById(R.id.signUpConstraintLayout);
        signUpConstraintLayout.setOnClickListener(this);
    }

    public void saveUserData(View view) {
        String emailAddress = emailEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        /*if (validateEmailAddress(emailAddress)) {
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
        }*/

        if (validateName(name) & validateEmailAddress(emailAddress)) {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        }
    }

    public boolean validateEmailAddress(String emailAddress) {
        if (!emailAddress.equals("") && !pattern.matcher(emailAddress).matches()) {
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

    @Override
    public void onClick(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void back(View view) {
        onBackPressed();
    }
}
