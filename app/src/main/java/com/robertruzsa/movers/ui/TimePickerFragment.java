package com.robertruzsa.movers.ui;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(
                getActivity(),
                (TimePickerDialog.OnTimeSetListener) getActivity(),
                hour,
                minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}
