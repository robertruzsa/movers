package com.robertruzsa.movers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class GetStartedActivity extends AppCompatActivity {

    TextView clientTextView, moverTextView;
    Switch userTypeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        anonymousLogin();

        clientTextView = findViewById(R.id.clientTextView);
        moverTextView = findViewById(R.id.moverTextView);
        userTypeSwitch = findViewById(R.id.mSwitch);

        TextView instructionTextView = findViewById(R.id.instructionTextView);
        Instruction.show(instructionTextView, R.string.instruction_get_started);

        final Typeface font = ResourcesCompat.getFont(this, R.font.open_sans);
        final int colorGray = ContextCompat.getColor(this, R.color.colorBlueInactive);
        final int colorBlueDark = ContextCompat.getColor(this, R.color.colorBlueDark);

        userTypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    moverTextView.setTypeface(font, Typeface.BOLD);
                    moverTextView.setTextColor(colorBlueDark);
                    clientTextView.setTypeface(font, Typeface.NORMAL);
                    clientTextView.setTextColor(colorGray);
                } else {
                    clientTextView.setTypeface(font, Typeface.BOLD);
                    clientTextView.setTextColor(colorBlueDark);
                    moverTextView.setTypeface(font, Typeface.NORMAL);
                    moverTextView.setTextColor(colorGray);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        ImageView logoImageView = findViewById(R.id.logoImageView);
        logoImageView.setVisibility(View.INVISIBLE);
        super.onBackPressed();
    }

    public void getStarted(View view) {
        String userType = "client";
        if (userTypeSwitch.isChecked())
            userType = "mover";
        ParseUser.getCurrentUser().put("userType", userType);

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                redirectActivity();
            }
        });
        Log.i("Info", "Redirecting as " + userType);
    }

    public void anonymousLogin() {
        if (ParseUser.getCurrentUser() == null) {
            ParseAnonymousUtils.logIn(new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null)
                        Log.i("Info", "Anonymous login successful");
                    else
                        Log.i("Info", "Anonymous login failed");
                }
            });
        } else { // If we have a current user - they already set the user type
            if (ParseUser.getCurrentUser().get("userType") != null) {
                Log.i("Info", "Redirecting as " + ParseUser.getCurrentUser().get("userType"));
                redirectActivity();
            }
        }
    }

    public void redirectActivity() {
        if (ParseUser.getCurrentUser().get("userType").equals("client")) {
            Intent intent = new Intent(getApplicationContext(), GetPhoneNumberActivity.class);
            startActivity(intent);
        } else {
            // Start the MoverActivity
        }
    }

}
