package com.robertruzsa.movers.adapter;

import android.util.Log;
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

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ExampleViewHolder> {
    private ArrayList<VehicleItem> vehicleItems;

    private OnItemClickListener onItemClickListener;
    private OnFocusChangeListener onFocusChangeListener;

    public interface OnItemClickListener {
        void onVehicleTypeEditTextClick(int position);

        void onRemoveVehicleButtonClick(int position);

        void onRequiredHoursEditTextClick(int position);

        void onSaveButtonClick(int position);
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

    public void setVehicleItems(ArrayList<VehicleItem> vehicleItems) {
        this.vehicleItems = vehicleItems;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextInputLayout vehicleType, kilometreCharge, initialCharge, requiredHours, hourlyRate;
        public MaterialButton removeVehicleButton, saveVehicleButton, editVehicleButton;

        ExampleViewHolder(View itemView, OnItemClickListener onItemClickListener, OnFocusChangeListener onFocusChangeListener) {
            super(itemView);
            vehicleType = itemView.findViewById(R.id.vehicleTypeTextInputLayout);
            kilometreCharge = itemView.findViewById(R.id.kilometreChargeTextInputLayout);
            initialCharge = itemView.findViewById(R.id.initialChargeTextInputLayout);
            requiredHours = itemView.findViewById(R.id.requiredHoursTextInputLayout);
            hourlyRate = itemView.findViewById(R.id.hourlyRateTextInputLayout);
            removeVehicleButton = itemView.findViewById(R.id.removeVehicleButton);
            saveVehicleButton = itemView.findViewById(R.id.saveVehicleButton);
            editVehicleButton = itemView.findViewById(R.id.editVehicleButton);

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

            saveVehicleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onSaveButtonClick(position);
                        }
                    }

                    if (vehicleType.getError() == null && kilometreCharge.getError() == null && initialCharge.getError() == null) {
                        saveVehicleButton.setEnabled(false);
                        editVehicleButton.setEnabled(true);
                        removeVehicleButton.setEnabled(false);
                        saveVehicleButton.setError(null);
                        setCardViewEnabled(false);
                    }

                }
            });

            editVehicleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editVehicleButton.setEnabled(false);
                    saveVehicleButton.setEnabled(true);

                    if (((RecyclerView) itemView.getParent()).getChildCount() > 1)
                        removeVehicleButton.setEnabled(true);
                    else
                        removeVehicleButton.setEnabled(false);

                    setCardViewEnabled(true);
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

        public void setCardViewEnabled(boolean isEnabled) {
            vehicleType.setEnabled(isEnabled);
            kilometreCharge.setEnabled(isEnabled);
            initialCharge.setEnabled(isEnabled);
            requiredHours.setEnabled(isEnabled);
            hourlyRate.setEnabled(isEnabled);
        }

        public void setButtonsDisabled() {
            setCardViewEnabled(false);
            editVehicleButton.setEnabled(true);
            removeVehicleButton.setEnabled(false);
            saveVehicleButton.setEnabled(false);
        }

    }

    public VehicleAdapter(ArrayList<VehicleItem> vehicleItems) {
        this.vehicleItems = vehicleItems;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_vehicle_item, parent, false);
        ExampleViewHolder exampleViewHolder = new ExampleViewHolder(view, onItemClickListener, onFocusChangeListener);
        //exampleViewHolder.setIsRecyclable(false);
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
        holder.removeVehicleButton.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return vehicleItems.size();
    }
}
