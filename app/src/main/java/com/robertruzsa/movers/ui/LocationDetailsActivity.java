package com.robertruzsa.movers.ui;

import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.robertruzsa.movers.R;

public class LocationDetailsActivity extends BaseActivity implements NumberPicker.OnValueChangeListener {

    private DialogFragment numberPicker;
    private TextInputLayout floorNumberTextInputLayout;
    private TextInputEditText floorNumberEditText;
    private MaterialCheckBox elevatorCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);

        setToolbarTitle(getString(R.string.locations));
        getHeaderTextView().setVisibility(View.GONE);
        getBodyTextView().setVisibility(View.GONE);

        floorNumberTextInputLayout = findViewById(R.id.floorNumberTextInputLayout);
        floorNumberEditText = (TextInputEditText) floorNumberTextInputLayout.getEditText();

        elevatorCheckBox = findViewById(R.id.elevatorCheckBox);
        elevatorCheckBox.setEnabled(false);

        numberPicker = new NumberPickerFragment();
        ((NumberPickerFragment) numberPicker).setValueChangeListener(this);

        floorNumberEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    floorNumberEditText.clearFocus();
                    numberPicker.show(getSupportFragmentManager(), "number picker");
                }
            }
        });

        floorNumberEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floorNumberEditText.clearFocus();
                numberPicker.show(getSupportFragmentManager(), "number picker");
            }
        });

        getNextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DateTimeActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (newVal != 0) {
            floorNumberEditText.setText(String.valueOf(newVal));
            elevatorCheckBox.setEnabled(true);
        } else {
            floorNumberEditText.setText("Földszint");
            elevatorCheckBox.setEnabled(false);
        }
    }

}
