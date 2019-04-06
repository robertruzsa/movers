package com.robertruzsa.movers.ui;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.robertruzsa.movers.R;
import com.robertruzsa.movers.adapter.VehicleTypeAdapter;
import com.robertruzsa.movers.model.VehicleTypeItem;

import java.util.ArrayList;

public class SelectVehicleActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vehicle);

        setToolbarTitle(getString(R.string.vehicle_type));
        setHeaderTextView(getString(R.string.step_six));
        setBodyTextView(getString(R.string.instruction_vehicle_type));
        setPageIndicatorViewProgress();

        ArrayList<VehicleTypeItem> vehicleTypeList = new ArrayList<>();
        vehicleTypeList.add(new VehicleTypeItem(R.drawable.ic_car, getString(R.string.car), getString(R.string.description_car)));
        vehicleTypeList.add(new VehicleTypeItem(R.drawable.ic_van, getString(R.string.van), getString(R.string.description_van)));
        vehicleTypeList.add(new VehicleTypeItem(R.drawable.ic_truck, getString(R.string.truck), getString(R.string.description_truck)));

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new VehicleTypeAdapter(vehicleTypeList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        getNextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DateTimeActivity.class);
                startActivity(intent);
            }
        });
    }
}
