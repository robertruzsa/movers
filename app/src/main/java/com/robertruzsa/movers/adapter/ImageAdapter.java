package com.robertruzsa.movers.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.robertruzsa.movers.R;
import com.robertruzsa.movers.model.ImageItem;
import com.robertruzsa.movers.model.MoverItem;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ExampleViewHolder> {
    private ArrayList<ImageItem> imageItems;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onButtonClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public MaterialButton removeImageButton;

        public ExampleViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            removeImageButton = itemView.findViewById(R.id.removeImageButton);
            removeImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onButtonClick(position);
                        }
                    }
                }
            });
        }
    }

    public ImageAdapter(ArrayList<ImageItem> imageItems) {
        this.imageItems = imageItems;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        ExampleViewHolder exampleViewHolder = new ExampleViewHolder(view, listener);
        return exampleViewHolder;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ImageItem currentItem = imageItems.get(position);
        holder.imageView.setImageBitmap(currentItem.getBitmap());
    }

    @Override
    public int getItemCount() {
        return imageItems.size();
    }
}
