package com.robertruzsa.movers.ui;

import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.robertruzsa.movers.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DateTimeActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextInputLayout dateTextInputLayout, timeTextInputLayout;
    private TextInputEditText dateEditText, timeEditText;
    private DialogFragment datePicker, timePicker;

    private final String LANGUAGE_TAG = "HU";
    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);

        setToolbarTitle(getString(R.string.time));
        setHeaderTextView(getString(R.string.step_six));
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
                    timePicker.show(getSupportFragmentManager(), "requestSubmitDate picker");
            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.show(getSupportFragmentManager(), "requestSubmitDate picker");
            }
        });

        getNextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getApplicationContext(), MovingDetailsActivity.class);
                startActivity(intent);*/
                saveDateTime();
            }
        });

        calendar = Calendar.getInstance();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateEditText.setText(DateFormat.getDateInstance(DateFormat.SHORT, Locale.forLanguageTag(LANGUAGE_TAG)).format(calendar.getTime()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        timeEditText.setText(DateFormat.getTimeInstance(DateFormat.SHORT, Locale.forLanguageTag(LANGUAGE_TAG)).format(calendar.getTime()));
    }

    void saveDateTime() {

        if (!validateDateTime(dateTextInputLayout) | !validateDateTime(timeTextInputLayout))
            return;

        if (calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
            timeTextInputLayout.setError("Érvénytelen időpont");
            return;
        }

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Request");
        //query.whereEqualTo("clientName", ParseUser.getCurrentUser().get("name"));
        query.whereEqualTo("clientId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        ParseObject request = objects.get(0);


                        //request.put("dateTime", dateEditText.getText().toString() + " " + timeEditText.getText().toString());

                        request.put("movingDate", calendar.getTime());
                        request.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null)
                                    Log.i("Error", e.toString());
                                else {
                                    Intent intent = new Intent(getApplicationContext(), MovingDetailsActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public boolean validateDateTime(TextInputLayout dateTimeTextInputLayout) {
        if (!dateTimeTextInputLayout.getEditText().getText().toString().equals("")) {
            dateTimeTextInputLayout.setError(null);
            return true;
        } else {
            dateTimeTextInputLayout.setError(getString(R.string.required));
            return false;
        }
    }
}
