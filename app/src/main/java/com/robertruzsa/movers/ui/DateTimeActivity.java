package com.robertruzsa.movers.ui;

import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.robertruzsa.movers.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateTimeActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextInputLayout dateTextInputLayout, timeTextInputLayout;
    private TextInputEditText dateEditText, timeEditText;
    private DialogFragment datePicker, timePicker;

    private final String LANGUAGE_TAG = "HU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);

        setToolbarTitle(getString(R.string.time));
        setHeaderTextView(getString(R.string.step_seven));
        setBodyTextView(getString(R.string.instruction_time));
        setPageIndicatorViewProgress();

        dateTextInputLayout = findViewById(R.id.dateTextInputLayout);
        timeTextInputLayout = findViewById(R.id.timeTextInputLayout);

        dateEditText = (TextInputEditText) dateTextInputLayout.getEditText();
        timeEditText = (TextInputEditText) timeTextInputLayout.getEditText();

        datePicker = new DatePickerFragment();
        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dateEditText.clearFocus();
                    datePicker.show(getSupportFragmentManager(), "date picker");
                }
            }
        });


        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        timePicker = new TimePickerFragment();
        timeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateEditText.setText(DateFormat.getDateInstance(DateFormat.SHORT, Locale.forLanguageTag(LANGUAGE_TAG)).format(calendar.getTime()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        if (calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
            timeTextInputLayout.setError("Érvénytelen időpont");
        else if (timeTextInputLayout.getError() != null)
            timeTextInputLayout.setError(null);
        timeEditText.setText(DateFormat.getTimeInstance(DateFormat.SHORT, Locale.forLanguageTag(LANGUAGE_TAG)).format(calendar.getTime()));
    }
}
