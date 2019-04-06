package com.robertruzsa.movers.ui;

import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.robertruzsa.movers.R;

public class LocationDetailsActivity extends BaseActivity {

    private DialogFragment numberPicker;
    private TextInputLayout pickupLocFloorNumberTextInputLayout, dropoffLocFloorNumberTextInputLayout;
    private TextInputEditText pickupLocFloorNumberEditText, dropoffLocFloorNumberEditText;
    private MaterialCheckBox pickupLocElevatorCheckBox, dropoffLocElevatorCheckBox;

    private boolean pickupLocClicked = false;
    private boolean dropoffLocClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);

        setToolbarTitle(getString(R.string.locations));
        setHeaderTextView(getString(R.string.step_five));
        setBodyTextView(getString(R.string.instruction_more_locaction_information));
        setPageIndicatorViewProgress();

        getCoordinatorLayout().setFocusable(true);
        getCoordinatorLayout().setFocusableInTouchMode(true);

        pickupLocFloorNumberTextInputLayout = findViewById(R.id.pickupLocFloorNumberTextInputLayout);
        pickupLocFloorNumberEditText = (TextInputEditText) pickupLocFloorNumberTextInputLayout.getEditText();

        dropoffLocFloorNumberTextInputLayout = findViewById(R.id.dropoffLocFloorNumberTextInputLayout);
        dropoffLocFloorNumberEditText = (TextInputEditText) dropoffLocFloorNumberTextInputLayout.getEditText();

        pickupLocElevatorCheckBox = findViewById(R.id.pickupLocElevatorCheckBox);
        dropoffLocElevatorCheckBox = findViewById(R.id.dropoffLocElevatorCheckBox);

        numberPicker = new NumberPickerFragment();
        ((NumberPickerFragment) numberPicker).setValueChangeListener(onValueChangeListener);

        pickupLocFloorNumberEditText.setOnFocusChangeListener(onFocusChangeListener);
        pickupLocFloorNumberEditText.setOnClickListener(onClickListener);

        dropoffLocFloorNumberEditText.setOnFocusChangeListener(onFocusChangeListener);
        dropoffLocFloorNumberEditText.setOnClickListener(onClickListener);

        getNextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DateTimeActivity.class);
                startActivity(intent);
            }
        });
    }

    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                if (v == pickupLocFloorNumberEditText)
                    pickupLocClicked = true;
                else if (v == dropoffLocFloorNumberEditText)
                    dropoffLocClicked = true;
                numberPicker.show(getSupportFragmentManager(), "number picker");
            }
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == pickupLocFloorNumberEditText)
                pickupLocClicked = true;
            else if (v == dropoffLocFloorNumberEditText)
                dropoffLocClicked = true;
            numberPicker.show(getSupportFragmentManager(), "number picker");
        }
    };

    NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            if (pickupLocClicked)
                setFloorNumberEditText(pickupLocFloorNumberEditText, pickupLocElevatorCheckBox, newVal);
            else if (dropoffLocClicked)
                setFloorNumberEditText(dropoffLocFloorNumberEditText, dropoffLocElevatorCheckBox, newVal);
            pickupLocClicked = false;
            dropoffLocClicked = false;
        }
    };

    void setFloorNumberEditText(TextInputEditText editText, MaterialCheckBox checkBox, int newVal) {
        if (newVal != 0) {
            editText.setText(String.valueOf(newVal));
            checkBox.setEnabled(true);
        } else {
            editText.setText("Földszint");
            checkBox.setEnabled(false);
        }
    }
}
