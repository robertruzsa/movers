package com.robertruzsa.movers.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.robertruzsa.movers.R;

import java.util.regex.Pattern;

public class MoverSignUpActivity extends BaseActivity {

    private TextInputLayout nameTextInputLayout, emailTextInputLayout, passwordTextInputLayout, confirmPasswordTextInputLayout;
    private TextInputEditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;

    Boolean isPasswordVisible = false;
    Boolean isConfirmPasswordVisible = false;

    private MaterialButton nextButton;

    private Pattern emailPattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
    private Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=]).{8,}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mover_sign_up);

        hideViews();

        nameTextInputLayout = findViewById(R.id.signUpMoverNameTextInputLayout);
        nameEditText = (TextInputEditText) nameTextInputLayout.getEditText();

        emailTextInputLayout = findViewById(R.id.signUpMoverEmailTextInputLayout);
        emailEditText = (TextInputEditText) emailTextInputLayout.getEditText();

        passwordTextInputLayout = findViewById(R.id.signUpMoverPasswordTextInputLayout);
        passwordEditText = (TextInputEditText) passwordTextInputLayout.getEditText();

        passwordEditText.setOnTouchListener(onPasswordEditTextTouchListener);

        confirmPasswordTextInputLayout = findViewById(R.id.moverConfirmPasswordTextInputLayout);
        confirmPasswordEditText = (TextInputEditText) confirmPasswordTextInputLayout.getEditText();

        confirmPasswordEditText.setOnTouchListener(onConfirmPasswordEditTextTouchListener);


        nextButton = findViewById(R.id.moverSignUpNextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddVehicleActivity.class);
                startActivity(intent);
                //saveUserData();
            }
        });
    }

    View.OnTouchListener onPasswordEditTextTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    isPasswordVisible = !isPasswordVisible;
                    setVisibility(isPasswordVisible, passwordEditText);
                    return true;
                }
            }
            return false;
        }
    };

    View.OnTouchListener onConfirmPasswordEditTextTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.i("touch", "touch");
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (confirmPasswordEditText.getRight() - confirmPasswordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    isConfirmPasswordVisible = !isConfirmPasswordVisible;
                    setVisibility(isConfirmPasswordVisible, confirmPasswordEditText);
                    return true;
                }
            }
            return false;
        }
    };

    private void setVisibility(Boolean isPasswordVisible, TextInputEditText passwordEditText) {
        if (isPasswordVisible) {
            passwordEditText.setTransformationMethod(null);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_visibility), null);
        } else {
            passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_visibility_off), null);
        }

        passwordEditText.clearFocus();
    }

    public void saveUserData() {
        String emailAddress = emailEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        /*if (validateName(name) & validateEmailAddress(emailAddress)) {
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

        if (validateName(name) & validateEmailAddress(emailAddress) & validatePassword(password) & confirmPassword(password, confirmPassword)) {
            ParseUser user = new ParseUser();
            user.setUsername(emailAddress);
            user.setEmail(emailAddress);
            user.setPassword(password);
            user.put("name", name);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.i("Error", e.toString());
                    } else {
                        Intent intent = new Intent(getApplicationContext(), AddVehicleActivity.class);
                        startActivity(intent);
                    }

                }
            });
        }
    }

    public boolean validateEmailAddress(String emailAddress) {
        if (!emailAddress.equals("") && !emailPattern.matcher(emailAddress).matches()) {
            emailTextInputLayout.setError(getString(R.string.invalid_email));
            return false;
        } else if (emailAddress.equals("")) {
            emailTextInputLayout.setError(getString(R.string.required));
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

    public boolean validatePassword(String password) {
        if (!password.equals("") && !passwordPattern.matcher(password).matches()) {
            passwordTextInputLayout.setError(getString(R.string.password_helper_text));
            return false;
        } else if (password.equals("")) {
            passwordTextInputLayout.setError(getString(R.string.enter_a_password));
            return false;
        } else {
            passwordTextInputLayout.setError(null);
            passwordTextInputLayout.setHelperText(null);
            return true;
        }
    }

    public boolean confirmPassword(String password, String confirmPassword) {
        if (!confirmPassword.equals("") && !confirmPassword.equals(password)) {
            confirmPasswordTextInputLayout.setError(getString(R.string.passwords_did_not_match));
            return false;
        } else if (confirmPassword.equals("") && validatePassword(password)) {
            confirmPasswordTextInputLayout.setError(getString(R.string.confirm_your_password));
            return false;
        } else {
            confirmPasswordTextInputLayout.setError(null);
            return true;
        }
    }

    public void moverSignIn(View view) {
        Intent intent = new Intent(getApplicationContext(), MoverSignInActivity.class);
        startActivity(intent);
    }
}
