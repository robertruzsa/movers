package com.robertruzsa.movers.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;

import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.robertruzsa.movers.R;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;

    private static final float DEFAULT_ZOOM = 15;
    private static final float TEXT_SIZE = 18f;
    private static final String LOC_A_TAG = "locA";
    private static final String LOC_B_TAG = "locB";

    private AutocompleteSupportFragment locAAutocompleteFragment, locBAutocompleteFragment;
    private GeoApiContext geoApiContext = null;
    private LatLng userLocation, pickupLocation, dropoffLocation;
    private ImageView locAImageView, locBImageView;
    private EditText locAEditText, locBEditText;

    private List<Marker> markers = new ArrayList<>();
    private List<Polyline> polylines = new ArrayList<>();

    private static final int MAP_LAYOUT_STATE_CONTRACTED = 0;
    private static final int MAP_LAYOUT_STATE_EXPANDED = 1;
    private int mMapLayoutState = 0;
    private ConstraintLayout constraintLayout;

    private int mapSizeExpanded;
    private int mapSizeCollapsed;
    private SupportMapFragment mMapFragment;
    private ViewTreeObserver viewTreeObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setToolbarTitle(getString(R.string.locations));
        setHeaderTextView(getString(R.string.step_four));
        setBodyTextView(getString(R.string.instruction_locations));
        setPageIndicatorViewProgress();

        initPlacesApi();

        locAAutocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.locAAutocompleteFragment);
        locBAutocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.locBAutocompleteFragment);

        locAImageView = (ImageView) ((LinearLayout) locAAutocompleteFragment.getView()).getChildAt(0);
        locBImageView = (ImageView) ((LinearLayout) locBAutocompleteFragment.getView()).getChildAt(0);
        locAEditText = (EditText) ((LinearLayout) locAAutocompleteFragment.getView()).getChildAt(1);
        locBEditText = (EditText) ((LinearLayout) locBAutocompleteFragment.getView()).getChildAt(1);

        showDialog();

        getNextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), SelectVehicleActivity.class);
                Intent intent = new Intent(getApplicationContext(), LocationDetailsActivity.class);
                startActivity(intent);
            }
        });

        constraintLayout = findViewById(R.id.constraintLayout);
        mMapFragment = (SupportMapFragment) (getSupportFragmentManager().findFragmentById(R.id.map));

        initMapSize();
        setMapSize(false);

    }

    private void initPlacesApi() {
        if (geoApiContext == null) {
            geoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_maps_key))
                    .build();
        }
        Places.initialize(this, getString(R.string.google_maps_key));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }

        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            moveCamera(userLocation, DEFAULT_ZOOM, "My Location", 0);
            initSearch();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void initSearch() {
        locAAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        locBAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        setAutocompleteFragmentUI();

        locAAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                resetMap(LOC_A_TAG);
                pickupLocation = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                moveCamera(pickupLocation, DEFAULT_ZOOM, place.getName(), R.drawable.ic_loc_a_map);
                if (!locBEditText.getText().toString().equals(""))
                    calculateDirections(dropoffLocation);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("Tag", "An error occurred: " + status);
            }
        });

        locBAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                resetMap(LOC_B_TAG);
                dropoffLocation = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                moveCamera(dropoffLocation, DEFAULT_ZOOM, place.getName(), R.drawable.ic_loc_b_map);
                if (!locAEditText.getText().toString().equals(""))
                    calculateDirections(dropoffLocation);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("Tag", "An error occurred: " + status);
            }
        });
    }


    private void moveCamera(LatLng latLng, float zoom, String title, int drawableId) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        if (!title.equals("My Location")) {
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(drawableId);
            Drawable circleDrawable = getResources().getDrawable(drawableId);
            BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title).icon(markerIcon);

            Marker marker = mMap.addMarker(markerOptions);

            if (drawableId == R.drawable.ic_loc_a_map)
                marker.setTag("locA");
            else
                marker.setTag("locB");

            markers.add(marker);
        }
    }

    private void setAutocompleteFragmentUI() {
        locAImageView.setImageDrawable(getDrawable(R.drawable.ic_loc_a_search_bar));
        locBImageView.setImageDrawable(getDrawable(R.drawable.ic_loc_b_search_bar));
        locAEditText.setTextSize(TEXT_SIZE);
        locBEditText.setTextSize(TEXT_SIZE);
        locAAutocompleteFragment.setHint(getString(R.string.pickup_location));
        locBAutocompleteFragment.setHint(getString(R.string.dropoff_location));
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void calculateDirections(LatLng place) {
        Log.d("Tag", "calculateDirections: calculating directions.");
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                place.latitude,
                place.longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(geoApiContext);

        directions.origin(
                new com.google.maps.model.LatLng(
                        pickupLocation.latitude,
                        pickupLocation.longitude
                )
        );

        Log.d("Tag", "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.i("onFailure", "error: " + e.getMessage());
            }
        });
    }

    private void addPolylinesToMap(final DirectionsResult result) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.i("run", "run: result routes: " + result.routes.length);
                for (DirectionsRoute route : result.routes) {
                    Log.i("run", "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());
                    List<LatLng> newDecodedPath = new ArrayList<>();
                    for (com.google.maps.model.LatLng latLng : decodedPath) {
                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polylines.add(polyline);
                    zoomRoute(polyline.getPoints());
                }
            }
        });
    }

    public void zoomRoute(List<LatLng> lstLatLngRoute) {
        if (mMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 120;
        LatLngBounds latLngBounds = boundsBuilder.build();

        mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                600,
                null
        );
    }

    private void resetMap(String tag) {
        if (markers.size() > 0)
            for (Marker marker : markers)
                if (marker.getTag() != null && marker.getTag().toString().equals(tag))
                    marker.remove();

        if (markers.size() > 0)
            for (Polyline polyline : polylines)
                if (polyline != null)
                    polyline.remove();
    }

    private void showDialog() {
        new MaterialAlertDialogBuilder(MapsActivity.this)
                .setMessage(getString(R.string.use_current_location_as_pickup_location))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pickupLocation = userLocation;
                        String address = getAddress(userLocation.latitude, userLocation.longitude);
                        if (address != null)
                            locAEditText.setText(address);
                        moveCamera(pickupLocation, DEFAULT_ZOOM, address, R.drawable.ic_loc_a_map);
                    }
                })
                .setNegativeButton(getString(R.string.no), null)
                .setCancelable(false)
                .show();
    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            return obj.getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public void resizeMap(View v) {
        if (mMapLayoutState == MAP_LAYOUT_STATE_CONTRACTED) {
            mMapLayoutState = MAP_LAYOUT_STATE_EXPANDED;
            expandMap();
            ((ImageButton) v).setImageDrawable(getDrawable(R.drawable.ic_fullscreen_exit));
        } else if (mMapLayoutState == MAP_LAYOUT_STATE_EXPANDED) {
            mMapLayoutState = MAP_LAYOUT_STATE_CONTRACTED;
            collapseMap();
            ((ImageButton) v).setImageDrawable(getDrawable(R.drawable.ic_fullscreen));
        }
    }

    private void expandMap() {
        getHeaderTextView().setVisibility(View.GONE);
        getBodyTextView().setVisibility(View.GONE);
        setMapSize(true);
    }

    private void collapseMap() {
        getHeaderTextView().setVisibility(View.VISIBLE);
        getBodyTextView().setVisibility(View.VISIBLE);
        setMapSize(false);
    }

    private void setMapSize(boolean isExpanded) {
        viewTreeObserver = constraintLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                constraintLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewGroup.LayoutParams params = mMapFragment.getView().getLayoutParams();
                if (!isExpanded)
                    params.height = mapSizeCollapsed;
                else
                    params.height = mapSizeExpanded;
                mMapFragment.getView().setLayoutParams(params);
            }
        });
    }

    private void initMapSize() {
        viewTreeObserver = constraintLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                constraintLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int screenHeight = getResources().getDisplayMetrics().heightPixels;
                Rect rectangle = new Rect();
                Window window = getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
                int statusBarHeight = rectangle.top;
                int toolBarHeight = getToolbar().getMeasuredHeight();
                int headerHeight = getHeaderTextView().getMeasuredHeight();
                int bodyHeight = getBodyTextView().getMeasuredHeight();
                int locAEditTextHeight = locAEditText.getMeasuredHeight();
                int locBEditTextHeight = locBEditText.getMeasuredHeight();
                int bottomNavBarHeight = getBottomNavigationBar().getMeasuredHeight();
                int expandedHeight = screenHeight - statusBarHeight - toolBarHeight - locAEditTextHeight - locBEditTextHeight - bottomNavBarHeight;
                mapSizeExpanded = expandedHeight;
                mapSizeCollapsed = expandedHeight - headerHeight - bodyHeight;
            }
        });
    }

    /*private void saveRequest() {
        // Is there an active request?
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Request");
        query.whereEqualTo("clientName", ParseUser.getCurrentUser().get("name"));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                    }
                }

            }
        });

        ParseObject request = new ParseObject("Request");
        request.put("clientName", ParseUser.getCurrentUser().get("name"));
        //request.put("phoneNumber", ParseUser.getCurrentUser().get("phoneNumber"));
        request.put("pickupLocation", new ParseGeoPoint(pickupLocation.latitude, pickupLocation.longitude));
        request.put("dropoffLocation", dropoffLocation.toString().replace("lat/lng: ", ""));
        request.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                    Log.i("Error", e.toString());
                else {
                    Intent intent = new Intent(getApplicationContext(), SelectVehicleActivity.class);
                    startActivity(intent);
                }

            }
        });
    }*/
}
