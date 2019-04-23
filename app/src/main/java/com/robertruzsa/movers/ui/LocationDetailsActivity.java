package com.robertruzsa.movers.ui;

import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.robertruzsa.movers.R;

import java.util.List;

public class LocationDetailsActivity extends BaseActivity {

    private DialogFragment numberPicker;
    private TextInputLayout pickupLocFloorNumberTextInputLayout, dropoffLocFloorNumberTextInputLayout, pickupLocDetailsTextInputLayout, dropoffLocDetailsTextInputLayout;
    private TextInputEditText pickupLocFloorNumberEditText, dropoffLocFloorNumberEditText, pickupLocDetailsEditText, dropoffLocDetailsEditText;
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
                //saveLocationDetails();
            }
        });

        pickupLocDetailsTextInputLayout = findViewById(R.id.pickupLocDetailsTextInputLayout);
        pickupLocDetailsEditText = (TextInputEditText) pickupLocDetailsTextInputLayout.getEditText();

        dropoffLocDetailsTextInputLayout = findViewById(R.id.movingDetailsTextInputLayout);
        dropoffLocDetailsEditText = (TextInputEditText) dropoffLocDetailsTextInputLayout.getEditText();

        // TODO: metódusba szervezés
        TextView pickupLocationTextView = findViewById(R.id.pickupLocationTextView);
        TextView dropoffLocationTextView = findViewById(R.id.dropoffLocationTextView);
        String pickupLocation = getIntent().getStringExtra("pickupLocation");
        String dropoffLocation = getIntent().getStringExtra("dropoffLocation");
        pickupLocationTextView.setText(pickupLocation);
        dropoffLocationTextView.setText(dropoffLocation);
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

    void saveLocationDetails() {

        if (!validateFloorNumber(pickupLocFloorNumberTextInputLayout) | !validateFloorNumber(dropoffLocFloorNumberTextInputLayout))
            return;

        ParseObject pickupLocationDetails = new ParseObject("LocationDetails");
        pickupLocationDetails.put("floorNumber", pickupLocFloorNumberEditText.getText().toString());
        pickupLocationDetails.put("elevator", pickupLocElevatorCheckBox.isChecked());
        pickupLocationDetails.put("parkingInfo", pickupLocDetailsEditText.getText().toString());

        ParseObject dropoffLocationDetails = new ParseObject("LocationDetails");
        dropoffLocationDetails.put("floorNumber", dropoffLocFloorNumberEditText.getText().toString());
        dropoffLocationDetails.put("elevator", dropoffLocElevatorCheckBox.isChecked());
        dropoffLocationDetails.put("parkingInfo", dropoffLocDetailsEditText.getText().toString());

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Request");
        query.whereEqualTo("clientName", ParseUser.getCurrentUser().get("name"));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        ParseObject request = objects.get(0);
                        request.put("pickupLocationDetails", pickupLocationDetails);
                        request.put("dropoffLocationDetails", dropoffLocationDetails);
                        request.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null)
                                    Log.i("Error", e.toString());
                                else {
                                    Intent intent = new Intent(getApplicationContext(), DateTimeActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public boolean validateFloorNumber(TextInputLayout floorNumberTextInputLayout) {
        if (!floorNumberTextInputLayout.getEditText().getText().toString().equals("")) {
            floorNumberTextInputLayout.setError(null);
            return true;
        } else {
            floorNumberTextInputLayout.setError(getString(R.string.required));
            return false;
        }
    }
}
