package com.robertruzsa.movers.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.robertruzsa.movers.R;

public class MovingDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_details);

        setToolbarTitle(getString(R.string.extra_information));
        setHeaderTextView(getString(R.string.step_seven));
        setBodyTextView(getString(R.string.instruction_moving_details));
        setPageIndicatorViewProgress();

        getNextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectVehicleActivity.class);
                startActivity(intent);
            }
        });
    }
}
