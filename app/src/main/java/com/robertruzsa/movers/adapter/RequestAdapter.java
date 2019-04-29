package com.robertruzsa.movers.adapter;

import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ExampleViewHolder> {
    private ArrayList<RequestItem> requestItems;


    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.forLanguageTag("HU"));
    private static final String CURRENCY = " Ft";
    private int selectedPosition = -1;


    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView clientName, price, movingDate, requestSubmitDate, pickupLocation, dropoffLocation, pickupFloorNumber, dropoffFloorNumber, distanceTextView;
        public ImageButton expandButton;
        public ConstraintLayout detailsConstraintLayout;

        MaterialCardView cardView;

        private static final int CARD_LAYOUT_STATE_CONTRACTED = 0;
        private static final int CARD_LAYOUT_STATE_EXPANDED = 1;
        private int cardLayoutState = 0;


        public ExampleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            clientName = itemView.findViewById(R.id.clientNameTextView);
            price = itemView.findViewById(R.id.priceTextView);
            movingDate = itemView.findViewById(R.id.movingDateTextView);
            requestSubmitDate = itemView.findViewById(R.id.requestSubmitDateTextView);
            pickupLocation = itemView.findViewById(R.id.pickupLocationTextView);
            dropoffLocation = itemView.findViewById(R.id.dropoffLocationTextView);
            pickupFloorNumber = itemView.findViewById(R.id.pickupFloorNumberTextView);
            dropoffFloorNumber = itemView.findViewById(R.id.dropoffFloorNumberTextView);
            distanceTextView = itemView.findViewById(R.id.distanceTextView);
            expandButton = itemView.findViewById(R.id.expandButton);

            detailsConstraintLayout = itemView.findViewById(R.id.detailsConstraintLayout);
            cardView = itemView.findViewById(R.id.cardView);


            final int[] rotationAngle = {0};

            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ObjectAnimator anim = ObjectAnimator.ofFloat(v, "rotation", rotationAngle[0], rotationAngle[0] + 180);
                    anim.setDuration(500);
                    anim.start();
                    rotationAngle[0] += 180;
                    rotationAngle[0] = rotationAngle[0] %360;

                    if (cardLayoutState == CARD_LAYOUT_STATE_CONTRACTED) {
                        cardLayoutState = CARD_LAYOUT_STATE_EXPANDED;
                        TransitionManager.beginDelayedTransition(cardView);
                        detailsConstraintLayout.setVisibility(View.VISIBLE);

                    } else if (cardLayoutState == CARD_LAYOUT_STATE_EXPANDED) {
                        cardLayoutState = CARD_LAYOUT_STATE_CONTRACTED;
                        TransitionManager.beginDelayedTransition(cardView);
                        detailsConstraintLayout.setVisibility(View.GONE);
                    }



                    //TransitionManager.beginDelayedTransition(cardView);
                    //detailsConstraintLayout.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public RequestAdapter(ArrayList<RequestItem> moverItems) {
        this.requestItems = moverItems;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false);
        ExampleViewHolder exampleViewHolder = new ExampleViewHolder(view);
        return exampleViewHolder;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        RequestItem currentItem = requestItems.get(position);
        holder.imageView.setImageResource(currentItem.getImageResource());
        holder.clientName.setText(currentItem.getClientName());

        NumberFormat format = NumberFormat.getInstance(Locale.forLanguageTag("HU"));
        String price = format.format(currentItem.getPrice()) + CURRENCY;
        holder.price.setText(price);

        holder.movingDate.setText(dateFormat.format(currentItem.getMovingDate()));

        holder.requestSubmitDate.setText(dateFormat.format(currentItem.getRequestSubmitDate()));


        holder.pickupLocation.setText(currentItem.getPickupLocation());
        holder.dropoffLocation.setText(currentItem.getDropoffLocation());


        String pickupLocationFloorNumber = currentItem.getLocationDetails().getPickupLocationFloorNumber();
        if (!pickupLocationFloorNumber.equals("Földszint"))
            holder.pickupFloorNumber.setText(pickupLocationFloorNumber + " .emelet");
        else
            holder.pickupFloorNumber.setText(currentItem.getLocationDetails().getPickupLocationFloorNumber());

        String dropoffLocationFloorNumber = currentItem.getLocationDetails().getDropoffLocationFloorNumber();
        if (!dropoffLocationFloorNumber.equals("Földszint"))
            holder.dropoffFloorNumber.setText(dropoffLocationFloorNumber + " .emelet");
        else
            holder.dropoffFloorNumber.setText(currentItem.getLocationDetails().getDropoffLocationFloorNumber());

        holder.distanceTextView.setText((int) currentItem.getLocationDetails().getDistanceInKilometers() + " km");


        /*if (selectedPosition == position) {
            currentItem.setSelected(true);
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorPrimary));
        } else {
            currentItem.setSelected(false);
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.quantum_white_text));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition >= 0)
                    notifyItemChanged(selectedPosition);
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(selectedPosition);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return requestItems.size();
    }
}
