package com.robertruzsa.movers.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.parse.ParseAnalytics;
import com.robertruzsa.movers.R;

public class SplashActivity extends AppCompatActivity {

    ImageView logoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        logoImageView = findViewById(R.id.logoImageView);

        final Intent intent = new Intent(SplashActivity.this, GetStartedActivity.class);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, new Pair<View, String>(logoImageView, "logoTransition"));
                startActivity(intent, options.toBundle());
                finish();
            }

        }, 2000);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
