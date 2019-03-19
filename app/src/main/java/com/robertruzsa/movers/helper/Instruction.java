package com.robertruzsa.movers.helper;

import android.os.Handler;
import android.widget.TextView;

public class Instruction {
    public static void show(final TextView textView, int stringResourceId) {
        textView.setText(stringResourceId);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.animate().alpha(1.f).setDuration(1000);
            }
        }, 1000);
    }
}