package com.robertruzsa.movers;

import android.os.Handler;
import android.widget.TextView;

public class Instruction {
    static void show(final TextView textView, int stringResourceId) {
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