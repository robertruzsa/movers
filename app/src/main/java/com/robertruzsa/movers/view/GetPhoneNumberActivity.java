package com.robertruzsa.movers.view;

import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.chabbal.slidingdotsplash.SlidingSplashView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.robertruzsa.movers.auth.Authentication;
import com.robertruzsa.movers.R;

import java.util.regex.Pattern;

public class GetPhoneNumberActivity extends AppCompatActivity implements View.OnClickListener {

    Pattern pattern = Pattern.compile("^[+]?[0-9]{10,13}$");

    private TextInputLayout phoneNumberTextInputLayout;
    private TextInputEditText phoneNumberEditText;
    private ConstraintLayout getPhoneNumberConstraintLayout;
    private SlidingSplashView slidingSplashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_phone_number);

        phoneNumberTextInputLayout = findViewById(R.id.phoneNumberTextInputLayout);

        phoneNumberEditText = (TextInputEditText) phoneNumberTextInputLayout.getEditText();
        phoneNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher("HU"));
        phoneNumberEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(phoneNumberEditText, 0);
            }
        }, 1000);

        getPhoneNumberConstraintLayout = findViewById(R.id.getPhoneNumberConstraintLayout);
        getPhoneNumberConstraintLayout.setOnClickListener(this);

        phoneNumberEditText.setText("+36201234567"); // TODO: Remove this line
        slidingSplashView = findViewById(R.id.slidingSplashView);
        slidingSplashView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

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

    public void requestCode(View view) {
        String phoneNumber = phoneNumberEditText.getText().toString().replace(" ","");
        if (validatePhoneNumber(phoneNumber)) {
            ProgressDialog progressDialog = ProgressDialog.show(this, "", "Kód elküldése folyamatban...", true);
            Authentication.Get(getApplicationContext()).requestPhoneVerification(phoneNumber, progressDialog);
        }
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}
