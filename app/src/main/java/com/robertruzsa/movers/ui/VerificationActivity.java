package com.robertruzsa.movers.ui;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.robertruzsa.movers.auth.Authentication;
import com.robertruzsa.movers.R;

public class VerificationActivity extends BaseActivity {

    private TextInputLayout verificationCodeTextInputLayout;
    private TextInputEditText verificationCodeEditText;
    private String phoneNumber;

    private static final int VERIFICATION_CODE_LENGTH = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        setToolbarTitle(getString(R.string.verification));
        setHeaderTextView(getString(R.string.step_two));
        setBodyTextView(getString(R.string.instruction_verification_code));
        setPageIndicatorViewProgress();

        phoneNumber = getIntent().getStringExtra("phoneNumber");

        verificationCodeTextInputLayout = findViewById(R.id.verificationCodeTextInputLayout);
        verificationCodeEditText = (TextInputEditText) verificationCodeTextInputLayout.getEditText();
        verificationCodeEditText.setText("111111"); // TODO: remove this line

        getNextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode();
            }
        });

    }

    public void verifyCode() {
        String verificationCode = verificationCodeEditText.getText().toString().trim();
        if (verificationCode.length() != VERIFICATION_CODE_LENGTH)
            verificationCodeTextInputLayout.setError(getString(R.string.invalid_verification_code));
        else {
            //Authentication.Get(getApplicationContext()).verifyEnteredCode(verificationCode, phoneNumber, verificationCodeTextInputLayout);
            Intent intent = new Intent(this, SignUpActivity.class);
            intent.putExtra("phoneNumber", phoneNumber);
            startActivity(intent);
        }
    }

    public void resendCode(View view) {
        Authentication.Get(getApplicationContext()).requestPhoneVerification(phoneNumber, null);
    }
}
