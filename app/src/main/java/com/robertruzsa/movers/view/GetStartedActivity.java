package com.robertruzsa.movers.view;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.robertruzsa.movers.auth.Authentication;
import com.robertruzsa.movers.R;
import com.robertruzsa.movers.helper.Instruction;

public class GetStartedActivity extends AppCompatActivity {

    TextView clientTextView, moverTextView;
    Switch userTypeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        //ParseUser.logOut();

        Authentication.Get(this).anonymousLogin();

        clientTextView = findViewById(R.id.clientTextView);
        moverTextView = findViewById(R.id.moverTextView);
        userTypeSwitch = findViewById(R.id.mSwitch);

        TextView instructionTextView = findViewById(R.id.instructionGetStartedTextView);
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
                Authentication.Get(getApplicationContext()).redirectActivity();
            }
        });
        Log.i("Info", "Redirecting as " + userType);
    }
}
