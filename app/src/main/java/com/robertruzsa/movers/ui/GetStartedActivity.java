package com.robertruzsa.movers.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.robertruzsa.movers.auth.Authentication;
import com.robertruzsa.movers.R;

public class GetStartedActivity extends AppCompatActivity {

    RadioButton clientRadioButton, moverRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_get_started);

        //ParseUser.logOut();
        Authentication.Get(this).anonymousLogin();

        clientRadioButton = findViewById(R.id.clientRadioButton);
        moverRadioButton = findViewById(R.id.moverRadioButton);
    }

    public void getStarted(View view) {
        String userType = "client";
        if (moverRadioButton.isChecked())
            userType = "mover";
        ParseUser.getCurrentUser().put("userType", userType);
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Authentication.Get(getApplicationContext()).redirectActivity();
            }
        });
        Log.i("Info", "Redirecting as " + userType);
    }
}
