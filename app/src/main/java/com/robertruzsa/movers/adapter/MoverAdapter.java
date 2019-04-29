package com.robertruzsa.movers.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.robertruzsa.movers.R;
import com.robertruzsa.movers.model.MoverItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MoverAdapter extends RecyclerView.Adapter<MoverAdapter.ExampleViewHolder> {
    private ArrayList<MoverItem> moverItems;


    private static final String CURRENCY = " Ft";
    private int selectedPosition = -1;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView name;
        public TextView price;
        public RatingBar ratingBar;
        public TextView rating;
        public TextView ratingCount;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.clientNameTextView);
            price = itemView.findViewById(R.id.priceTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            rating = itemView.findViewById(R.id.movingDateTextView);
            ratingCount = itemView.findViewById(R.id.ratingCountTextView);
        }
    }

    public MoverAdapter(ArrayList<MoverItem> moverItems) {
        this.moverItems = moverItems;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mover_item, parent, false);
        ExampleViewHolder exampleViewHolder = new ExampleViewHolder(view);
        return exampleViewHolder;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        MoverItem currentItem = moverItems.get(position);
        holder.imageView.setImageResource(currentItem.getImageResource());
        holder.name.setText(currentItem.getName());

        NumberFormat format = NumberFormat.getInstance(Locale.forLanguageTag("HU"));
        String price = format.format(currentItem.getPrice()) + CURRENCY;
        holder.price.setText(price);

        holder.ratingBar.setRating(currentItem.getRating());
        holder.rating.setText(String.format("%.1f", currentItem.getRating()));
        holder.ratingCount.setText("(" + String.valueOf(currentItem.getRatingCount()) + ")");

        if (selectedPosition == position) {
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
        });

    }

    @Override
    public int getItemCount() {
        return moverItems.size();
    }
}
