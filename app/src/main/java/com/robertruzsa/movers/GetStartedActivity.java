package com.robertruzsa.movers;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class GetStartedActivity extends AppCompatActivity {

    TextView clientTextView, moverTextView;
    Switch mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        /*Toast toast = Toast.makeText(this, R.string.insturction, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();*/

        clientTextView = findViewById(R.id.clientTextView);
        moverTextView = findViewById(R.id.moverTextView);
        mSwitch = findViewById(R.id.mSwitch);

        final Typeface font = ResourcesCompat.getFont(this, R.font.open_sans);
        final int colorGray = ContextCompat.getColor(this, R.color.colorBlueInactive);
        final int colorBlueDark = ContextCompat.getColor(this, R.color.colorBlueDark);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        ImageView tvLogo = findViewById(R.id.logoImageView);
        tvLogo.setVisibility(View.INVISIBLE);
        super.onBackPressed();
    }
}
