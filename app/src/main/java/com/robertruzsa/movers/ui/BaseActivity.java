package com.robertruzsa.movers.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rd.PageIndicatorView;
import com.robertruzsa.movers.R;

public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView headerTextView, bodyTextView;
    private MaterialButton backButton, nextButton;
    private PageIndicatorView pageIndicatorView;
    private FrameLayout activityContainer;

    private CoordinatorLayout coordinatorLayout;
    private ConstraintLayout bottomNavigationBar;

    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(final int layoutResId) {
        coordinatorLayout = (CoordinatorLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        activityContainer = coordinatorLayout.findViewById(R.id.activityContainer);
        AppBarLayout appBarLayout = coordinatorLayout.findViewById(R.id.appBarLayout);
        toolbar = (androidx.appcompat.widget.Toolbar) appBarLayout.getChildAt(0);
        headerTextView = coordinatorLayout.findViewById(R.id.headerTextView);
        bodyTextView = coordinatorLayout.findViewById(R.id.bodyTextView);
        bottomNavigationBar = coordinatorLayout.findViewById(R.id.bottomNavigationBar);
        backButton = (MaterialButton) bottomNavigationBar.getChildAt(0);
        pageIndicatorView = (PageIndicatorView) bottomNavigationBar.getChildAt(1);
        nextButton = (MaterialButton) bottomNavigationBar.getChildAt(2);

        constraintLayout = coordinatorLayout.findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });

        getLayoutInflater().inflate(layoutResId, activityContainer, true);
        super.setContentView(coordinatorLayout);

        backButton.setOnClickListener(onBackPressedListener);
        toolbar.setNavigationOnClickListener(onBackPressedListener);
    }

    View.OnClickListener onBackPressedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP)
            hideKeyboard();
        return true;
    }

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

    protected void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    protected void hideViews()
    {
        headerTextView.setVisibility(View.GONE);
        bodyTextView.setVisibility(View.GONE);
        bottomNavigationBar.setVisibility(View.GONE);
    }

    protected Toolbar getToolbar() {
        return this.toolbar;
    }

    public ConstraintLayout getBottomNavigationBar() {
        return bottomNavigationBar;
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }

    public PageIndicatorView getPageIndicatorView() {
        return pageIndicatorView;
    }

}
