package com.robertruzsa.movers.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.robertruzsa.movers.R;
import com.robertruzsa.movers.model.VehicleTypeItem;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class VehicleTypeAdapter extends RecyclerView.Adapter<VehicleTypeAdapter.ExampleViewHolder> {
    private ArrayList<VehicleTypeItem> vehicleTypeItems;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title;
        public TextView description;
        public CheckBox checkBox;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.clientNameTextView);
            description = itemView.findViewById(R.id.descriptionTextView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    public VehicleTypeAdapter(ArrayList<VehicleTypeItem> vehicleTypeItems) {
        this.vehicleTypeItems = vehicleTypeItems;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_type_item, parent, false);
        ExampleViewHolder exampleViewHolder = new ExampleViewHolder(view);
        return exampleViewHolder;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        VehicleTypeItem currentItem = vehicleTypeItems.get(position);
        holder.imageView.setImageResource(currentItem.getImageResource());
        holder.title.setText(currentItem.getTitle());
        holder.description.setText(currentItem.getDescription());
        holder.checkBox.setChecked(currentItem.isSelected());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem.setSelected(!currentItem.isSelected());
                holder.checkBox.setChecked(currentItem.isSelected());
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicleTypeItems.size();
    }
}
