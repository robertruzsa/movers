package com.robertruzsa.movers.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.robertruzsa.movers.auth.Authentication;
import com.robertruzsa.movers.R;
import com.robertruzsa.movers.helper.Instruction;
import com.santalu.maskedittext.MaskEditText;

public class VerificationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MaskEditText verificationCodeEditText;
    private String phoneNumber;

    private ProgressDialog progressDialog = null;
    private Button verifyCodeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        verifyCodeButton = findViewById(R.id.verifyCodeButton);
        verifyCodeButton.setEnabled(false);

        phoneNumber = getIntent().getStringExtra("phoneNumber");

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
        titleTextView.setText(getString(R.string.verifikacio));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView instructionTextView = findViewById(R.id.instructionVerificationTextView);
        Instruction.show(instructionTextView, R.string.instruction_verification_code);

        verificationCodeEditText = findViewById(R.id.verificationCodeEditText);

        verificationCodeEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(verificationCodeEditText, 0);
            }
        }, 1000);

        verificationCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 6)
                    verifyCodeButton.setEnabled(false);
                else
                    verifyCodeButton.setEnabled(true);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void verifyCode(View view) {
        String verificationCode = verificationCodeEditText.getRawText();
        //Authentication.Get(getApplicationContext()).verifyEnteredCode(verificationCode, phoneNumber, verificationCodeEditText);
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        startActivity(intent);
    }

    public void resendCode(View view) {
        Authentication.Get(getApplicationContext()).requestPhoneVerification(phoneNumber, null);
    }
}
