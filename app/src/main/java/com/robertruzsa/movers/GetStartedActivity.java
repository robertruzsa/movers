package com.robertruzsa.movers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class GetStartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
    }

    @Override
    public void onBackPressed() {
        ImageView tvLogo = findViewById(R.id.logoImageView);
        tvLogo.setVisibility(View.INVISIBLE);
        super.onBackPressed();
    }
}
