package com.robertruzsa.movers.view;

import android.content.Intent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.robertruzsa.movers.R;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;

    TextView lastNameTextView;
    TextView firstNameTextView;
    TextView emailTextView;
    TextView phoneNumberEditText;

    EditText lastNameEditText;
    EditText firstNameEditText;
    EditText emailEditText;

    Button saveUserDataButton;

    String phoneNumber;

    private TextWatcher textWatcher;

    Pattern pattern = Pattern.compile("^$|^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");

    ConstraintLayout signUpConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpConstraintLayout = findViewById(R.id.signUpConstraintLayout);
        signUpConstraintLayout.setOnClickListener(this);

        saveUserDataButton = findViewById(R.id.saveUserDataButton);
        //saveUserDataButton.setEnabled(false);

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
        titleTextView.setText(getString(R.string.regisztracio));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        lastNameTextView = findViewById(R.id.lastNameTextView);
        lastNameEditText = findViewById(R.id.lastNameEditText);

        firstNameTextView = findViewById(R.id.firstNameTextView);
        firstNameEditText = findViewById(R.id.firstNameEditText);

        emailTextView = findViewById(R.id.emailTextView);
        emailEditText = findViewById(R.id.emailEditText);

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        phoneNumberEditText.setText(phoneNumber);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastNameTextView.setVisibility(View.VISIBLE);
                firstNameTextView.setVisibility(View.VISIBLE);
                emailTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (lastNameEditText.getText().toString().equals(""))
                    lastNameTextView.setVisibility(View.GONE);

                if (firstNameEditText.getText().toString().equals(""))
                    firstNameTextView.setVisibility(View.GONE);

                if (emailEditText.getText().toString().equals(""))
                    emailTextView.setVisibility(View.GONE);

                if (!lastNameEditText.getText().toString().equals("") && !firstNameEditText.getText().toString().equals(""))
                    saveUserDataButton.setEnabled(true);
                else
                    saveUserDataButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        lastNameEditText.addTextChangedListener(textWatcher);
        firstNameEditText.addTextChangedListener(textWatcher);
        emailEditText.addTextChangedListener(textWatcher);

    }

    public void saveUserData(View view) {
        /*if (!isEmailValid()) {
            emailEditText.setError(getString(R.string.ervenytelen_email_cim));
            return;
        } else {
            ParseUser.getCurrentUser().put("lastName", lastNameEditText.getText().toString());
            ParseUser.getCurrentUser().put("firstName", firstNameEditText.getText().toString());
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
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
    }

    public boolean isEmailValid() {
        return pattern.matcher(emailEditText.getText().toString()).matches();
    }

    @Override
    public void onClick(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}
