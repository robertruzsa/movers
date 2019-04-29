package com.robertruzsa.movers.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.robertruzsa.movers.R;
import com.robertruzsa.movers.adapter.VehicleAdapter;
import com.robertruzsa.movers.model.VehicleItem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AddVehicleActivity extends AppCompatActivity {

    private DialogFragment vehicleTypePicker;
    private DialogFragment numberPicker;

    private RecyclerView recyclerView;
    private VehicleAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<VehicleItem> vehicles;
    int vehicleItemPosition;

    private Pattern numberPattern = Pattern.compile("\\d+(?:\\.\\d+)?");

    FloatingActionButton addVehicleFAB;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        toolbar = findViewById(R.id.addVehicleToolbar);
        toolbar.setTitle("Járművek megadása");

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        vehicles = new ArrayList<>();
        vehicles.add(new VehicleItem());

        recyclerView = findViewById(R.id.vehiclesRecycleView);
        recyclerView.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(this);
        adapter = new VehicleAdapter(vehicles);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        addVehicleFAB = findViewById(R.id.moverSignUpAddVehicleFAB);

        addVehicleFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastIndex = vehicles.size() - 1;
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(lastIndex);
                MaterialButton saveButton = ((VehicleAdapter.ExampleViewHolder) viewHolder).saveVehicleButton;

                validateVehicleType(lastIndex);
                validateKilometreCharge(lastIndex);
                validateInitialCharge(lastIndex);
                validateRequiredHours(lastIndex);
                validateHourlyRate(lastIndex);

                if (saveButton.isEnabled()) {
                    saveButton.setError("");
                } else {
                    vehicles.add(new VehicleItem());
                    adapter.notifyItemInserted(vehicles.size() - 1);
                    recyclerView.scrollToPosition(vehicles.size() - 1);
                }


            }
        });


        vehicleTypePicker = new VehicleTypePickerFragment();
        ((VehicleTypePickerFragment) vehicleTypePicker).setValueChangeListener(onVehicleTypeValueChangeListener);

        numberPicker = new NumberPickerFragment();
        ((NumberPickerFragment) numberPicker).setMaxValue(5);
        ((NumberPickerFragment) numberPicker).setValueChangeListener(onRequiredHoursValueChangeListener);

        adapter.setOnItemClickListener(new VehicleAdapter.OnItemClickListener() {
            @Override
            public void onVehicleTypeEditTextClick(int position) {
                if (vehicleTypePicker.isAdded())
                    return;
                vehicleTypePicker.show(getSupportFragmentManager(), "vehicle type picker");
                vehicleItemPosition = position;
            }


            @Override
            public void onRemoveVehicleButtonClick(int position) {
                if (vehicles.size() > 1) {
                    vehicles.remove(position);
                    adapter.notifyItemRemoved(position);
                }
            }

            @Override
            public void onRequiredHoursEditTextClick(int position) {
                if (numberPicker.isAdded())
                    return;
                numberPicker.show(getSupportFragmentManager(), "number picker");
                vehicleItemPosition = position;
            }

            @Override
            public void onSaveButtonClick(int position) {
                validateVehicleType(position);
                validateKilometreCharge(position);
                validateInitialCharge(position);
                validateRequiredHours(position);
                validateHourlyRate(position);
                vehicleItemPosition = position;
                //adapter.notifyDataSetChanged();
            }

        });

        adapter.setOnFocusChangeListener(new VehicleAdapter.OnFocusChangeListener() {
            @Override
            public void onVehicleTypeEditTextFocusChange(int position) {
                if (vehicleTypePicker.isAdded())
                    return;
                vehicleTypePicker.show(getSupportFragmentManager(), "vehicle type picker");
                vehicleItemPosition = position;
            }

            @Override
            public void onRequiredHoursEditTextFocusChange(int position) {
                if (numberPicker.isAdded())
                    return;
                numberPicker.show(getSupportFragmentManager(), "vehicle type picker");
            }
        });


    }


    NumberPicker.OnValueChangeListener onVehicleTypeValueChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            vehicles.get(vehicleItemPosition).setVehicleType(picker.getDisplayedValues()[newVal]);

            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(/*vehicles.size() - 1*/vehicleItemPosition);
            TextInputLayout vehicleTypeTextInputLayout = ((VehicleAdapter.ExampleViewHolder) viewHolder).vehicleType;
            vehicleTypeTextInputLayout.getEditText().setText(picker.getDisplayedValues()[newVal]);
            vehicleTypeTextInputLayout.clearFocus();

            //adapter.notifyDataSetChanged();
            //adapter.notifyItemChanged(vehicleItemPosition);
        }
    };

    NumberPicker.OnValueChangeListener onRequiredHoursValueChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(vehicleItemPosition);
            TextInputLayout requiredHoursTextInputLayout = ((VehicleAdapter.ExampleViewHolder) viewHolder).requiredHours;
            requiredHoursTextInputLayout.getEditText().setText(String.valueOf(newVal));
            requiredHoursTextInputLayout.clearFocus();
        }
    };

    public boolean validateVehicleType(int position) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        TextInputLayout vehicleTypeTextInputLayout = ((VehicleAdapter.ExampleViewHolder) viewHolder).vehicleType;

        return isNotEmpty(vehicleTypeTextInputLayout);
    }

    public boolean validateKilometreCharge(int position) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        TextInputLayout kilometreChargeTextInputLayout = ((VehicleAdapter.ExampleViewHolder) viewHolder).kilometreCharge;

        if (isNotEmpty(kilometreChargeTextInputLayout) && isNumber(kilometreChargeTextInputLayout.getEditText().getText().toString().trim())) {
            vehicles.get(position).setKilometreCharge(kilometreChargeTextInputLayout.getEditText().getText().toString().trim());
            return true;
        } else if (!isNotEmpty(kilometreChargeTextInputLayout)) {
            kilometreChargeTextInputLayout.setError(getString(R.string.required));
            return false;
        } else {
            kilometreChargeTextInputLayout.setError("Csak számok adhatók meg");
            return false;
        }
    }

    public boolean validateInitialCharge(int position) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        TextInputLayout initialChargeTextInputLayout = ((VehicleAdapter.ExampleViewHolder) viewHolder).initialCharge;

        if (isNotEmpty(initialChargeTextInputLayout) && isNumber(initialChargeTextInputLayout.getEditText().getText().toString().trim())) {
            vehicles.get(position).setInitialCharge(initialChargeTextInputLayout.getEditText().getText().toString().trim());
            return true;
        } else if (!isNotEmpty(initialChargeTextInputLayout)) {
            initialChargeTextInputLayout.setError(getString(R.string.required));
            return false;
        } else {
            initialChargeTextInputLayout.setError("Csak számok adhatók meg");
            return false;
        }
    }

    public boolean validateRequiredHours(int position) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        TextInputLayout requiredHoursTextInputLayout = ((VehicleAdapter.ExampleViewHolder) viewHolder).requiredHours;

        if (isNotEmpty(requiredHoursTextInputLayout) && isNumber(requiredHoursTextInputLayout.getEditText().getText().toString().trim())) {
            vehicles.get(position).setRequiredHours(requiredHoursTextInputLayout.getEditText().getText().toString().trim());
            return true;
        } else if (!isNotEmpty(requiredHoursTextInputLayout)) {
            requiredHoursTextInputLayout.setError(null);
            return true;
        } else {
            requiredHoursTextInputLayout.setError("Csak számok adhatók meg");
            return false;
        }
    }

    public boolean validateHourlyRate(int position) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        TextInputLayout hourlyRateTextInputLayout = ((VehicleAdapter.ExampleViewHolder) viewHolder).hourlyRate;

        if (isNotEmpty(hourlyRateTextInputLayout) && isNumber(hourlyRateTextInputLayout.getEditText().getText().toString().trim())) {
            vehicles.get(position).setHourlyRate(hourlyRateTextInputLayout.getEditText().getText().toString().trim());
            return true;
        } else if (!isNotEmpty(hourlyRateTextInputLayout)) {
            hourlyRateTextInputLayout.setError(null);
            return true;
        } else {
            hourlyRateTextInputLayout.setError("Csak számok adhatók meg");
            return false;
        }
    }

    public boolean isNotEmpty(TextInputLayout textInputLayout) {
        String input = textInputLayout.getEditText().getText().toString();
        if (input.equals("")) {
            textInputLayout.setError(getString(R.string.required));
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }

    public boolean isNumber(String string) {
        return numberPattern.matcher(string).matches();
    }

    public void next(View view) {
        int lastIndex = vehicles.size() - 1;
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(lastIndex);
        MaterialButton saveButton = ((VehicleAdapter.ExampleViewHolder) viewHolder).saveVehicleButton;
        validateVehicleType(lastIndex);
        validateKilometreCharge(lastIndex);
        validateInitialCharge(lastIndex);
        validateRequiredHours(lastIndex);
        validateHourlyRate(lastIndex);
        if (saveButton.isEnabled()) {
            saveButton.setError("");
        } else {
            saveVehicles();
        }
    }

    public void saveVehicles() {
        List<ParseObject> parseVehicles = new ArrayList<>();
        for (VehicleItem vehicleItem : vehicles) {
            ParseObject vehicle = new ParseObject("Vehicle");
            vehicle.put("userId", ParseUser.getCurrentUser().getObjectId());
            vehicle.put("vehicleType", vehicleItem.getVehicleType());
            vehicle.put("kilometreCharge", vehicleItem.getKilometreCharge());
            vehicle.put("initialCharge", vehicleItem.getInitialCharge());
            vehicle.put("requiredHours", vehicleItem.getRequiredHours());
            vehicle.put("hourlyRate", vehicleItem.getHourlyRate());
            parseVehicles.add(vehicle);
        }

        ParseObject.saveAllInBackground(parseVehicles, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                    Log.i("Error", e.toString());
                else {
                    Log.i("Success", "next activity");
                }
            }
        });
    }

}