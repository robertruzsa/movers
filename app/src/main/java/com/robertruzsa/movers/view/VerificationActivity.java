package com.robertruzsa.movers.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rd.PageIndicatorView;
import com.robertruzsa.movers.auth.Authentication;
import com.robertruzsa.movers.R;
import com.robertruzsa.movers.helper.Instruction;
import com.santalu.maskedittext.MaskEditText;

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout verificationCodeTextInputLayout;
    private TextInputEditText verificationCodeEditText;
    private String phoneNumber;


    //private ConstraintLayout verificationConstraintLayout;

    private PageIndicatorView pageIndicatorView;

    private static final int VERIFICATION_CODE_LENGTH = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        phoneNumber = getIntent().getStringExtra("phoneNumber");

        verificationCodeTextInputLayout = findViewById(R.id.verificationCodeTextInputLayout);

        verificationCodeEditText = (TextInputEditText) verificationCodeTextInputLayout.getEditText();
        /*verificationCodeEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(verificationCodeEditText, 0);
            }
        }, 1000);

        verificationConstraintLayout = findViewById(R.id.verificationConstraintLayout);
        verificationConstraintLayout.setOnClickListener(this);*/

        verificationCodeEditText.setText("111111"); // TODO: remove this line

        TextView headerTextView = findViewById(R.id.headerTextView);
        int step = Integer.valueOf(headerTextView.getText().toString().substring(0, 1));
        pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setProgress(step - 1, 1);
    }

    public void verifyCode(View view) {
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

    @Override
    public void onClick(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void back(View view) {
        onBackPressed();
    }
}
