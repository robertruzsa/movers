package com.robertruzsa.movers.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.robertruzsa.movers.R;
import com.robertruzsa.movers.model.ReviewItem;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ExampleViewHolder> {
    private ArrayList<ReviewItem> reviewItems;

    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.forLanguageTag("HU"));

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView clientName, reviewCount, reviewDate, comment;
        public RatingBar ratingBar;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.reviewImageView);
            clientName = itemView.findViewById(R.id.reviewClientNameTextView);
            ratingBar = itemView.findViewById(R.id.reviewRatingBar);
            reviewCount = itemView.findViewById(R.id.reviewCountTextView);
            reviewDate = itemView.findViewById(R.id.reviewDateTextView);
            comment = itemView.findViewById(R.id.commentTextView);
        }
    }

    public ReviewAdapter(ArrayList<ReviewItem> reviewItems) {
        this.reviewItems = reviewItems;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        ExampleViewHolder exampleViewHolder = new ExampleViewHolder(view);
        return exampleViewHolder;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ReviewItem currentItem = reviewItems.get(position);
        holder.imageView.setImageResource(currentItem.getImageResource());
        holder.clientName.setText(currentItem.getClientName());
        holder.ratingBar.setRating(currentItem.getRating());
        holder.reviewCount.setText(currentItem.getReviewCount() + " értékelés");
        holder.reviewDate.setText(dateFormat.format(currentItem.getReviewDate()));
        holder.comment.setText(currentItem.getComment());
    }

    @Override
    public int getItemCount() {
        return reviewItems.size();
    }
}
