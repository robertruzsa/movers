package com.robertruzsa.movers.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.provider.Telephony;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.robertruzsa.movers.R;

public class MoverHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mover_home);

        toolbar = findViewById(R.id.moverHomeToolbar);
        toolbar.setTitle("Kérések");
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new RequestsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_requests);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_requests:
                toolbar.setTitle("Kérések");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new RequestsFragment()).commit();
                break;
            case R.id.nav_time_sheet:
                toolbar.setTitle("Ráérés");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new TimeSheetFragment()).commit();
                break;
            case R.id.nav_profile:
                toolbar.setTitle("Profil");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ProfileFragment()).commit();
                break;
            case R.id.nav_vehicles:
                toolbar.setTitle("Járművek");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new VehiclesFragment()).commit();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
