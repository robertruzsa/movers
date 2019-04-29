package com.robertruzsa.movers.ui;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.robertruzsa.movers.R;
import com.robertruzsa.movers.adapter.ImageAdapter;
import com.robertruzsa.movers.adapter.RequestAdapter;
import com.robertruzsa.movers.model.ImageItem;
import com.robertruzsa.movers.model.LocationDetails;
import com.robertruzsa.movers.model.RequestItem;
import com.robertruzsa.movers.model.VehicleTypeItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RequestsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<RequestItem> requests;

    int rotationAngle = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getRequests();

        recyclerView = getView().findViewById(R.id.requestsRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(recyclerView.getContext());
        adapter = new RequestAdapter(requests);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void getRequests() {

        requests = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
        query.whereEqualTo("moverId", ParseUser.getCurrentUser().getObjectId());

        try {
            List<ParseObject> parseRequests = query.find();
            for (ParseObject object : parseRequests) {

                String id = object.getObjectId();
                String clientName = object.getString("clientName");
                int calculatedPrice = object.getInt("calculatedPrice");
                String pickupLocation = object.getString("pickupLocation");
                String dropoffLocation = object.getString("dropoffLocation");
                String movingDetails = object.getString("movingDetails");
                Date requestSubmitDate = object.getUpdatedAt();
                Date movingDate = object.getDate("movingDate");
                LocationDetails locationDetails = getLocationDetails(object.getString("locationDetails"));

                requests.add(new RequestItem(R.drawable.ic_user, calculatedPrice, clientName,
                        pickupLocation, dropoffLocation, movingDetails, locationDetails,
                        requestSubmitDate, movingDate));

                Log.i("Size", requests.size() + "");


            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private LocationDetails getLocationDetails(String id) {


        ParseQuery<ParseObject> query = ParseQuery.getQuery("LocationDetails");
        query.whereEqualTo("objectId", id);
       /* query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null)
                    Log.i("Error", e.toString());
                else {
                    ParseObject parseLocationDetails = objects.get(0);
                    String pickupLocationFloorNumber = parseLocationDetails.getString("pickupLocationFloorNumber");
                    boolean pickupLocationElevator = parseLocationDetails.getBoolean("pickupLocationElevator");
                    String pickupLocationParkingInfo = parseLocationDetails.getString("pickupLocationParkingInfo");
                    String dropoffLocationFloorNumber = parseLocationDetails.getString("dropoffLocationFloorNumber");
                    boolean dropoffLocationElevator = parseLocationDetails.getBoolean("dropoffLocationElevator");
                    String dropoffLocationParkingInfo = parseLocationDetails.getString("dropoffLocationParkingInfo");

                    locationDetails[0] = new LocationDetails(pickupLocationFloorNumber, dropoffLocationFloorNumber, pickupLocationParkingInfo, dropoffLocationParkingInfo, pickupLocationElevator, dropoffLocationElevator);
                }
            }
        });*/

        try {
            List<ParseObject> locationDetailsList = query.find();

            ParseObject parseLocationDetails = locationDetailsList.get(0);
            String pickupLocationFloorNumber = parseLocationDetails.getString("pickupLocationFloorNumber");
            boolean pickupLocationElevator = parseLocationDetails.getBoolean("pickupLocationElevator");
            String pickupLocationParkingInfo = parseLocationDetails.getString("pickupLocationParkingInfo");
            String dropoffLocationFloorNumber = parseLocationDetails.getString("dropoffLocationFloorNumber");
            boolean dropoffLocationElevator = parseLocationDetails.getBoolean("dropoffLocationElevator");
            String dropoffLocationParkingInfo = parseLocationDetails.getString("dropoffLocationParkingInfo");
            int distanceInKilometers = parseLocationDetails.getInt("distanceInKilometers");

            return new LocationDetails(pickupLocationFloorNumber, dropoffLocationFloorNumber, pickupLocationParkingInfo, dropoffLocationParkingInfo, pickupLocationElevator, dropoffLocationElevator, distanceInKilometers);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }
}
