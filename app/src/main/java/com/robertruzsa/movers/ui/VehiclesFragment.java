package com.robertruzsa.movers.ui;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.robertruzsa.movers.R;
import com.robertruzsa.movers.adapter.VehicleAdapter;
import com.robertruzsa.movers.model.VehicleItem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VehiclesFragment extends Fragment {

    private DialogFragment vehicleTypePicker;
    private DialogFragment numberPicker;

    private RecyclerView recyclerView;

    private VehicleAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<VehicleItem> vehicles;
    int vehicleItemPosition;

    private Pattern numberPattern = Pattern.compile("\\d+(?:\\.\\d+)?");

    FloatingActionButton addVehicleFloatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vehicles, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        vehicles = new ArrayList<>();

        recyclerView = getView().findViewById(R.id.moverVehiclesRecyclerView);

        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new VehicleAdapter(vehicles);

        getVehicles();

        for (int i = 0; i < vehicles.size(); i++)
            setVehicleItemDisabled(new Handler(), recyclerView, i);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        addVehicleFloatingActionButton = getView().findViewById(R.id.addVehicleFloatingActionButton);

        addVehicleFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addVehicle(new Handler(), recyclerView);

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
                vehicleTypePicker.show(getFragmentManager(), "vehicle type picker");
                vehicleItemPosition = position;
            }


            @Override
            public void onRemoveVehicleButtonClick(int position) {
                if (vehicles.size() > 1) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Vehicle");
                    query.whereEqualTo("objectId", vehicles.get(position).getId());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e != null)
                                Log.i("Error", e.toString());
                            else {
                                if (objects.size() > 0) {
                                    objects.get(0).deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e != null) {
                                                Log.i("Error", e.toString());
                                            } else {
                                                vehicles.remove(position);
                                                adapter.notifyItemRemoved(position);
                                                Log.i("Delete", "Successful");
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onRequiredHoursEditTextClick(int position) {
                if (numberPicker.isAdded())
                    return;
                numberPicker.show(getFragmentManager(), "number picker");
                vehicleItemPosition = position;
            }

            @Override
            public void onSaveButtonClick(int position) {
                validateVehicleType(position);
                validateKilometreCharge(position);
                validateInitialCharge(position);
                validateRequiredHours(position);
                validateHourlyRate(position);
                if (validateVehicleType(position) & validateKilometreCharge(position) & validateInitialCharge(position) & validateRequiredHours(position) & validateHourlyRate(position)) {

                    VehicleItem vehicleItem = vehicles.get(position);

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Vehicle");
                    query.whereEqualTo("objectId", vehicleItem.getId());
                    query.setLimit(1);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e != null)
                                Log.i("Error", e.toString());
                            else {
                                ParseObject vehicle;
                                if (objects.size() > 0) {
                                    vehicle = objects.get(0);
                                } else {
                                    vehicle = new ParseObject("Vehicle");
                                }
                                vehicle.put("userId", ParseUser.getCurrentUser().getObjectId());
                                vehicle.put("vehicleType", vehicleItem.getVehicleType());
                                vehicle.put("kilometreCharge", vehicleItem.getKilometreCharge());
                                vehicle.put("initialCharge", vehicleItem.getInitialCharge());
                                vehicle.put("requiredHours", vehicleItem.getRequiredHours());
                                vehicle.put("hourlyRate", vehicleItem.getHourlyRate());
                                vehicle.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e != null)
                                            Log.i("Error", e.toString());
                                        else
                                            Log.i("Save", "Successful");
                                    }
                                });
                            }
                        }
                    });

                }

                vehicleItemPosition = position;
                //adapter.notifyDataSetChanged();
            }

        });

        adapter.setOnFocusChangeListener(new VehicleAdapter.OnFocusChangeListener() {
            @Override
            public void onVehicleTypeEditTextFocusChange(int position) {
                if (vehicleTypePicker.isAdded())
                    return;
                vehicleTypePicker.show(getFragmentManager(), "vehicle type picker");
                vehicleItemPosition = position;
            }

            @Override
            public void onRequiredHoursEditTextFocusChange(int position) {
                if (numberPicker.isAdded())
                    return;
                numberPicker.show(getFragmentManager(), "vehicle type picker");
            }
        });

    }

    public void getVehicles() {


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vehicle");
        query.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
        /*query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null)
                    Log.i("Error", e.toString());
                else if (objects != null) {
                    for (ParseObject object : objects) {
                        String vehicleType = object.getString("vehicleType");
                        String kilometreCharge = object.getString("kilometreCharge");
                        String initialCharge = object.getString("initialCharge");
                        String requiredHours = object.getString("requiredHours");
                        String hourlyRate = object.getString("hourlyRate");
                        vehicles.add(new VehicleItem(vehicleType, kilometreCharge, initialCharge, requiredHours, hourlyRate));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });*/

        try {
            List<ParseObject> objects = query.find();
            for (ParseObject object : objects) {
                String vehicleType = object.getString("vehicleType");
                String kilometreCharge = object.getString("kilometreCharge");
                String initialCharge = object.getString("initialCharge");
                String requiredHours = object.getString("requiredHours");
                String hourlyRate = object.getString("hourlyRate");
                vehicles.add(new VehicleItem(object.getObjectId(), vehicleType, kilometreCharge, initialCharge, requiredHours, hourlyRate));
            }
            adapter.notifyDataSetChanged();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    private void addVehicle(final Handler handler, RecyclerView recyclerView) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int lastIndex = vehicles.size() - 1;
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(lastIndex);

                if (viewHolder != null) {
                    MaterialButton saveButton = ((VehicleAdapter.ExampleViewHolder) viewHolder).saveVehicleButton;
                    //saveButton.setEnabled(true);

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

                } else {
                    handler.postDelayed(this, 50);
                }
            }
        });
    }

    private void setVehicleItemDisabled(final Handler handler, RecyclerView recyclerView, int position) {
        handler.post(new Runnable() {
            @Override
            public void run() {

                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);

                if (viewHolder != null) {
                    ((VehicleAdapter.ExampleViewHolder) viewHolder).setButtonsDisabled();
                } else {
                    handler.postDelayed(this, 50);
                }

            }
        });
    }
}
