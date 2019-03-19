package com.robertruzsa.movers.view;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.robertruzsa.movers.R;

public class SignUpActivity extends AppCompatActivity {

    private Toolbar toolbar;

    ConstraintLayout lastNameConstraintLayout;
    ConstraintLayout firstNameConstraintLayout;
    ConstraintLayout emailConstraintLayout;
    ConstraintLayout phoneNumberConstraintLayout;

    TextView lastNameTextView;
    TextView firstNameTextView;
    TextView emailTextView;

    EditText lastNameEditText;
    EditText firstNameEditText;
    EditText emailEditText;
    TextView phoneNumberEditText;

    Button saveUserDataButton;

    String phoneNumber;

    private TextWatcher textWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        saveUserDataButton = findViewById(R.id.saveUserDataButton);
        saveUserDataButton.setEnabled(false);

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

        lastNameConstraintLayout = findViewById(R.id.lastNameConstraintLayout);
        firstNameConstraintLayout = findViewById(R.id.firstNameConstraintLayout);
        emailConstraintLayout = findViewById(R.id.emailConstraintLayout);
        phoneNumberConstraintLayout = findViewById(R.id.phoneNumberConstraintLayout);

        lastNameTextView = (TextView) lastNameConstraintLayout.getChildAt(0);
        lastNameEditText = (EditText) lastNameConstraintLayout.getChildAt(1);

        firstNameTextView = (TextView) firstNameConstraintLayout.getChildAt(0);
        firstNameEditText = (EditText) firstNameConstraintLayout.getChildAt(1);

        emailTextView = (TextView) emailConstraintLayout.getChildAt(0);
        emailEditText = (EditText) emailConstraintLayout.getChildAt(1);

        phoneNumberEditText = (TextView) phoneNumberConstraintLayout.getChildAt(1);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        phoneNumberEditText.setText(phoneNumber);
        //phoneNumberEditText.setEnabled(false);

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

                if (!lastNameEditText.getText().toString().equals("") && !firstNameTextView.getText().toString().equals(""))
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
    }
}
