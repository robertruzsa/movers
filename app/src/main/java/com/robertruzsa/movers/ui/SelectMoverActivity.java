package com.robertruzsa.movers.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.robertruzsa.movers.R;
import com.robertruzsa.movers.adapter.MoverAdapter;
import com.robertruzsa.movers.adapter.VehicleTypeAdapter;
import com.robertruzsa.movers.model.MoverItem;
import com.robertruzsa.movers.model.VehicleItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectMoverActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<MoverItem> moverList;
    private ArrayList<VehicleItem> vehicleItems;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mover);

        setToolbarTitle(getString(R.string.mover_selection));
        setHeaderTextView(getString(R.string.step_nine));
        setBodyTextView(getString(R.string.instruction_select_your_mover));
        //setPageIndicatorViewProgress();

        moverList = new ArrayList<>();
       /* moverList.add(new MoverItem(R.drawable.ic_user, "Kovács Péter", "5000 Ft", 4));
        moverList.add(new MoverItem(R.drawable.ic_user, "Nagy Béla", "3750 Ft", 3.5f));
        moverList.add(new MoverItem(R.drawable.ic_user, "Kiss Géza", "4250 Ft", 4.5f));
        moverList.add(new MoverItem(R.drawable.ic_user, "Horváth Csaba", "5500 Ft", 3));
        moverList.add(new MoverItem(R.drawable.ic_user, "Török Gábor", "5750 Ft", 2.5f));
        moverList.add(new MoverItem(R.drawable.ic_user, "Molnár Levente", "4000 Ft", 0));*/

        recyclerView = findViewById(R.id.moverRecycleView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new MoverAdapter(moverList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        getParseVehicles();

        sharedPreferences = this.getSharedPreferences("com.robertruzsa.movers", Context.MODE_PRIVATE);

        getNextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelectedMover();
            }
        });
    }

    private void getParseVehicles() {

        ParseQuery<ParseObject> carQuery = ParseQuery.getQuery("Vehicle");
        ParseQuery<ParseObject> vanQuery = ParseQuery.getQuery("Vehicle");
        ParseQuery<ParseObject> truckQuery = ParseQuery.getQuery("Vehicle");

        List<ParseQuery<ParseObject>> queries = new ArrayList<>();

        if (getIntent().getBooleanExtra("car", true)) {
            carQuery.whereEqualTo("vehicleType", "Személyautó");
            queries.add(carQuery);
        }
        if (getIntent().getBooleanExtra("van", true)) {
            vanQuery.whereEqualTo("vehicleType", "Kisteherautó");
            queries.add(vanQuery);
        }
        if (getIntent().getBooleanExtra("truck", true)) {
            truckQuery.whereEqualTo("vehicleType", "Kamion");
            queries.add(truckQuery);
        }

        if (queries.isEmpty())
            queries.add(carQuery);

        ParseQuery<ParseObject> query = ParseQuery.or(queries);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject object : objects) {
                    String userId = String.valueOf(object.get("userId"));

                    Log.i("number of results", String.valueOf(objects.size()));

                    ParseUser mover = getUser(userId);

                    String moverName = (String) mover.get("name");


                    float distance = sharedPreferences.getFloat("distance", 0);
                    String vehicleType = String.valueOf(object.get("vehicleType"));
                    String kilometreCharge = String.valueOf(object.get("kilometreCharge"));
                    String initialCharge = String.valueOf(object.get("initialCharge"));
                    String requiredHours = String.valueOf(object.get("requiredHours"));
                    String hourlyRate = String.valueOf(object.get("hourlyRate"));

                    /*Log.i("dist", String.valueOf(distance));
                    Log.i("kmCharge", kilometreCharge);
                    Log.i("initCharge", initialCharge);
                    Log.i("reqHours", requiredHours);
                    Log.i("hourlyRate", hourlyRate);*/

                    int calculatedPrice = getPrice(distance, kilometreCharge, initialCharge, requiredHours, hourlyRate);

                    /*NumberFormat format = NumberFormat.getInstance(Locale.forLanguageTag("HU"));
                    String price = format.format(calculatedPrice);*/

                    float rating = getRating(userId);
                    int ratingCount = getRatingCount(userId);

                    moverList.add(new MoverItem(R.drawable.ic_user, userId, moverName, calculatedPrice, rating, ratingCount));
                    adapter.notifyDataSetChanged();

                   /* VehicleItem vehicleItem = new VehicleItem(vehicleType, kilometreCharge,initialCharge,requiredHours,hourlyRate);
                    vehicleItems.add(vehicleItem);*/
                }
            }
        });
    }

    private String getMoverName(String userId) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", userId);
        try {
            return (String) query.find().get(0).get("name");
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private int getPrice(float distance, String kilometreCharge, String initialCharge, String requiredHours, String hourlyRate) {
        return (int) (distance * getInt(kilometreCharge) + getInt(initialCharge) + getInt(requiredHours) * getInt(hourlyRate));
    }

    private int getInt(String string) {
        return Integer.parseInt(string);
    }

    private ParseUser getUser(String userId) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        try {
            return query.get(userId);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private float getRating(String userId) {
        List<ParseObject> ratings = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rating");
        query.whereEqualTo("userId", userId);

        try {
            ratings = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int sum = 0;
        for (ParseObject rating : ratings) {
            sum += rating.getInt("rating");
        }
        return (float) sum / ratings.size();
    }

    private int getRatingCount(String userId) {
        List<ParseObject> ratings = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rating");
        query.whereEqualTo("userId", userId);

        try {
            ratings = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ratings.size();
    }

    private void saveSelectedMover() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Request");
        //query.whereEqualTo("clientName", ParseUser.getCurrentUser().get("name"));
        query.whereEqualTo("clientId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null)
                    Log.i("Error", e.toString());
                else {
                    ParseObject request = objects.get(0);
                    request.put("moverId", getSelectedMoverId());
                    request.put("calculatedPrice", moverList.get(getSelectedMoverIndex()).getPrice());
                    request.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null)
                                Log.i("Error", e.toString());
                            else
                                Log.i("Request", "Request sent");
                        }
                    });
                }
            }
        });
    }

    private String getSelectedMoverId() {
        int i = 0;
        while (!moverList.get(i).isSelected())
            i++;
        return moverList.get(i).getId();
    }

    private int getSelectedMoverIndex() {
        int i = 0;
        while (!moverList.get(i).isSelected())
            i++;
        return i;
    }

}
