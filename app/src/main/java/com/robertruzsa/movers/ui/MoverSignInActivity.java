package com.robertruzsa.movers.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.robertruzsa.movers.R;

public class MoverSignInActivity extends BaseActivity {

    private TextInputLayout emailTextInputLayout, passwordTextInputLayout;
    private TextInputEditText emailEditText, passwordEditText;

    Boolean isPasswordVisible = false;
    Boolean isConfirmPasswordVisible = false;

    private MaterialButton moverSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mover_sign_in);

        hideViews();

        emailTextInputLayout = findViewById(R.id.signInMoverEmailTextInputLayout);
        emailEditText = (TextInputEditText) emailTextInputLayout.getEditText();

        passwordTextInputLayout = findViewById(R.id.signInMoverPasswordTextInputLayout);
        passwordEditText = (TextInputEditText) passwordTextInputLayout.getEditText();

        passwordEditText.setOnTouchListener(onPasswordEditTextTouchListener);

        /*moverSignInButton = findViewById(R.id.moverSignInButton);
        moverSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MoverHomeActivity.class);
                startActivity(intent);
            }
        });*/
    }

    public void createAccount(View view) {
        Intent intent = new Intent(this, MoverSignUpActivity.class);
        startActivity(intent);
    }

    View.OnTouchListener onPasswordEditTextTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    isPasswordVisible = !isPasswordVisible;
                    if (isPasswordVisible) {
                        passwordEditText.setTransformationMethod(null);
                        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_visibility), null);
                    } else {
                        passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
                        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_visibility_off), null);
                    }
                    passwordEditText.clearFocus();
                    return true;
                }
            }
            return false;
        }
    };

    public boolean validateEmailAddress(String emailAddress) {
        if (emailAddress.equals("")) {
            emailTextInputLayout.setError(getString(R.string.enter_an_email));
            return false;
        } else {
            emailTextInputLayout.setError(null);
            return true;
        }
    }

    public boolean validatePassword(String password) {
        if (password.equals("")) {
            passwordTextInputLayout.setError(getString(R.string.enter_a_password));
            return false;
        } else {
            passwordTextInputLayout.setError(null);
            return true;
        }
    }

    public void signIn(View view) {
        /*String emailAddress = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (validateEmailAddress(emailAddress) & validatePassword(password)) {
            ParseUser.logInInBackground(emailAddress, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        Log.i("Sign in", "Successful");
                        Intent intent = new Intent(getApplicationContext(), MoverHomeActivity.class);
                        startActivity(intent);
                    } else {
                        Log.i("Error", e.toString());
                    }
                }
            });
        }*/

        Intent intent = new Intent(getApplicationContext(), MoverHomeActivity.class);
        startActivity(intent);

    }
}
