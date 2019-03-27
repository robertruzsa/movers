package com.robertruzsa.movers.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.rd.PageIndicatorView;
import com.robertruzsa.movers.R;

public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView headerTextView, bodyTextView;
    private MaterialButton backButton, nextButton;
    private PageIndicatorView pageIndicatorView;
    private FrameLayout activityContainer;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(final int layoutResId) {
        constraintLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        activityContainer = constraintLayout.findViewById(R.id.activityContainer);
        AppBarLayout appBarLayout = constraintLayout.findViewById(R.id.appBarLayout);
        toolbar = (androidx.appcompat.widget.Toolbar) appBarLayout.getChildAt(0);
        headerTextView = constraintLayout.findViewById(R.id.headerTextView);
        bodyTextView = constraintLayout.findViewById(R.id.bodyTextView);
        ConstraintLayout bottomNavigationBar = constraintLayout.findViewById(R.id.bottomNavigationBar);
        backButton = (MaterialButton) bottomNavigationBar.getChildAt(0);
        pageIndicatorView = (PageIndicatorView) bottomNavigationBar.getChildAt(1);
        nextButton = (MaterialButton) bottomNavigationBar.getChildAt(2);

        getLayoutInflater().inflate(layoutResId, activityContainer, true);
        super.setContentView(constraintLayout);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        backButton.setOnClickListener(onBackPressedListener);
        toolbar.setNavigationOnClickListener(onBackPressedListener);
    }

    View.OnClickListener onBackPressedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    protected void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    protected void setHeaderTextView(String title) {
        headerTextView.setText(title);
    }

    protected void setBodyTextView(String title) {
        bodyTextView.setText(title);
    }

    protected void setPageIndicatorViewProgress() {
        int progress = Integer.parseInt(headerTextView.getText().toString().substring(0, 1));
        pageIndicatorView.setProgress(progress - 1, 1);
    }

    protected MaterialButton getBackButton() {
        return backButton;
    }

    protected MaterialButton getNextButton() {
        return nextButton;
    }

    protected TextView getHeaderTextView() {
        return headerTextView;
    }

    protected TextView getBodyTextView() {
        return bodyTextView;
    }

    protected void setActivityContainerTopMargin(int margin) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) activityContainer.getLayoutParams();
        params.setMargins(0, margin, 0, 0);
        activityContainer.setLayoutParams(params);
    }

}
