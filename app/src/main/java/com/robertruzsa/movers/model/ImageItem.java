package com.robertruzsa.movers.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageItem {
    private int imageResource;

    public Bitmap getBitmap() {
        return bitmap;
    }

    private Bitmap bitmap;

    public ImageItem(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ImageItem(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getImageResource() {
        return imageResource;
    }
}
