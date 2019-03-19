package com.robertruzsa.movers.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.robertruzsa.movers.auth.Authentication;
import com.robertruzsa.movers.R;
import com.robertruzsa.movers.helper.Instruction;
import com.santalu.maskedittext.MaskEditText;

public class VerificationActivity extends AppCompatActivity implements View.OnKeyListener {

    private Toolbar toolbar;
    private MaskEditText codeEditText;
    private String phoneNumber;

    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

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

        codeEditText = findViewById(R.id.verificationCodeEditText);

        codeEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(codeEditText, 0);
            }
        },1000);
    }

    public void next(View view) {
        String verificationCode = codeEditText.getRawText();
        Authentication.Get(getApplicationContext()).verifyEnteredCode(verificationCode, phoneNumber);
    }

    public void resendCode(View view) {
        Authentication.Get(getApplicationContext()).requestPhoneVerification(phoneNumber, null);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

        }
        return false;
    }
}
