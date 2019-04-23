package com.robertruzsa.movers.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class VehicleTypePickerFragment extends DialogFragment {
    private NumberPicker.OnValueChangeListener valueChangeListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final NumberPicker vehicleTypePicker = new NumberPicker(getActivity());
        final String[] vehicleTypes = new String[]{"Személyautó", "Kisteherautó", "Kamion"};

        vehicleTypePicker.setMinValue(0);
        vehicleTypePicker.setMaxValue(2);
        vehicleTypePicker.setDisplayedValues(vehicleTypes);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                valueChangeListener.onValueChange(vehicleTypePicker, vehicleTypePicker.getValue(), vehicleTypePicker.getValue());
            }
        });

        builder.setView(vehicleTypePicker);
        return builder.create();
    }

    public NumberPicker.OnValueChangeListener getValueChangeListener() {
        return valueChangeListener;
    }

    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }
}
