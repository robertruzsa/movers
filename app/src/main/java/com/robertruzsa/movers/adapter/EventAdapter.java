package com.robertruzsa.movers.adapter;

import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.robertruzsa.movers.R;
import com.robertruzsa.movers.model.RequestItem;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ExampleViewHolder> {
    private ArrayList<RequestItem> requestItems;


    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.forLanguageTag("HU"));


    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView clientName,  movingDate,  pickupLocation, dropoffLocation;


        public ExampleViewHolder(View itemView) {
            super(itemView);
            clientName = itemView.findViewById(R.id.eventClientNameTextView);
            movingDate = itemView.findViewById(R.id.eventMovingDateTextView);
            pickupLocation = itemView.findViewById(R.id.eventPickupLocationTextView);
            dropoffLocation = itemView.findViewById(R.id.eventDropoffLocationTextView);
        }
    }

    public EventAdapter(ArrayList<RequestItem> moverItems) {
        this.requestItems = moverItems;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        ExampleViewHolder exampleViewHolder = new ExampleViewHolder(view);
        return exampleViewHolder;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        RequestItem currentItem = requestItems.get(position);
        holder.clientName.setText(currentItem.getClientName());
        holder.movingDate.setText(dateFormat.format(currentItem.getMovingDate()));
        holder.pickupLocation.setText(currentItem.getPickupLocation());
        holder.dropoffLocation.setText(currentItem.getDropoffLocation());
    }

    @Override
    public int getItemCount() {
        return requestItems.size();
    }
}
