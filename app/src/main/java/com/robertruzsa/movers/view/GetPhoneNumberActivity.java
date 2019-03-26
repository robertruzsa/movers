package com.robertruzsa.movers.view;

import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TableLayout;
import android.widget.TextView;

import com.chabbal.slidingdotsplash.SlidingSplashView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rd.PageIndicatorView;
import com.robertruzsa.movers.auth.Authentication;
import com.robertruzsa.movers.R;

import java.util.regex.Pattern;

public class GetPhoneNumberActivity extends AppCompatActivity {

    Pattern pattern = Pattern.compile("^[+]?[0-9]{10,13}$");

    private TextInputLayout phoneNumberTextInputLayout;
    private TextInputEditText phoneNumberEditText;

    private MaterialButton backButton, nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_phone_number);

        phoneNumberTextInputLayout = findViewById(R.id.phoneNumberTextInputLayout);
        phoneNumberEditText = (TextInputEditText) phoneNumberTextInputLayout.getEditText();
        phoneNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher("HU"));
        phoneNumberEditText.setText("+36201234567"); // TODO: Remove this line
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
        String phoneNumber = phoneNumberEditText.getText().toString().replace(" ", "");
        if (validatePhoneNumber(phoneNumber)) {
            ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.sending_code), true);
            Authentication.Get(getApplicationContext()).requestPhoneVerification(phoneNumber, progressDialog);
        }
    }

    public void back(View view) {
        onBackPressed();
    }
}
