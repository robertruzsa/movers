package com.robertruzsa.movers.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.robertruzsa.movers.R;
import com.robertruzsa.movers.model.VehicleItem;
import com.robertruzsa.movers.ui.VehicleTypePickerFragment;

import java.util.ArrayList;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ExampleViewHolder> {
    private ArrayList<VehicleItem> vehicleItems;

    private OnItemClickListener onItemClickListener;
    private OnFocusChangeListener onFocusChangeListener;

    public interface OnItemClickListener {
        void onVehicleTypeEditTextClick(int position);
        void onRemoveVehicleButtonClick(int position);
        void onRequiredHoursEditTextClick(int position);
    }

    public interface OnFocusChangeListener {
        void onVehicleTypeEditTextFocusChange(int position);
        void onRequiredHoursEditTextFocusChange(int position);
    }

    public void setOnItemClickListener(VehicleAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextInputLayout vehicleType, kilometreCharge, initialCharge, requiredHours, hourlyRate;
        public MaterialButton removeVehicleButton;

        ExampleViewHolder(View itemView, OnItemClickListener onItemClickListener, OnFocusChangeListener onFocusChangeListener) {
            super(itemView);
            vehicleType = itemView.findViewById(R.id.vehicleTypeTextInputLayout);
            kilometreCharge = itemView.findViewById(R.id.kilometreChargeTextInputLayout);
            initialCharge = itemView.findViewById(R.id.initialChargeTextInputLayout);
            requiredHours = itemView.findViewById(R.id.requiredHoursTextInputLayout);
            hourlyRate = itemView.findViewById(R.id.hourlyRateTextInputLayout);
            removeVehicleButton = itemView.findViewById(R.id.removeVehicleButton);

            removeVehicleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onRemoveVehicleButtonClick(position);
                        }
                    }
                }
            });

            vehicleType.getEditText().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onVehicleTypeEditTextClick(position);
                        }
                    }
                }
            });

            vehicleType.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (onFocusChangeListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            if (hasFocus)
                                onFocusChangeListener.onVehicleTypeEditTextFocusChange(position);
                        }
                    }
                }
            });

            requiredHours.getEditText().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onRequiredHoursEditTextClick(position);
                        }
                    }
                }
            });

            requiredHours.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (onFocusChangeListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            if (hasFocus)
                                onFocusChangeListener.onRequiredHoursEditTextFocusChange(position);
                        }
                    }
                }
            });
        }
    }

    public VehicleAdapter(ArrayList<VehicleItem> vehicleItems) {
        this.vehicleItems = vehicleItems;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_vehicle_item, parent, false);
        ExampleViewHolder exampleViewHolder = new ExampleViewHolder(view, onItemClickListener, onFocusChangeListener);
        return exampleViewHolder;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        VehicleItem currentItem = vehicleItems.get(position);
        holder.vehicleType.getEditText().setText(currentItem.getVehicleType());
        holder.kilometreCharge.getEditText().setText(currentItem.getKilometreCharge());
        holder.initialCharge.getEditText().setText(currentItem.getInitialCharge());
        holder.requiredHours.getEditText().setText(currentItem.getRequiredHours());
        holder.hourlyRate.getEditText().setText(currentItem.getHourlyRate());
    }

    @Override
    public int getItemCount() {
        return vehicleItems.size();
    }
}
