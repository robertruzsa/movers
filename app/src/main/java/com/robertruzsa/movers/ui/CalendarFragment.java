package com.robertruzsa.movers.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.robertruzsa.movers.R;
import com.robertruzsa.movers.adapter.EventAdapter;
import com.robertruzsa.movers.adapter.RequestAdapter;
import com.robertruzsa.movers.model.RequestItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarFragment extends Fragment {

    private static final String TAG = "CalendarFragment";

    private CompactCalendarView calendarView;
    private TextView yearMonthTextView, noEventsTextView;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY. MMMM", Locale.forLanguageTag("HU"));

    private ImageButton previousMonthImageButton, nextMonthImageButton;

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<RequestItem> acceptedRequests;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        initViews();

        getAcceptedRequests();

        recyclerView = getView().findViewById(R.id.eventsRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(recyclerView.getContext());
        adapter = new EventAdapter(acceptedRequests);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void initViews() {

        calendarView = getView().findViewById(R.id.compactCalendarView);
        yearMonthTextView = getView().findViewById(R.id.yearMonthTextView);

        previousMonthImageButton = getView().findViewById(R.id.previousMonthImageButton);
        nextMonthImageButton = getView().findViewById(R.id.nextMonthImageButton);

        noEventsTextView = getView().findViewById(R.id.noEventsTextView);

        previousMonthImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.scrollLeft();
            }
        });

        nextMonthImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.scrollRight();
            }
        });

        yearMonthTextView.setText(dateFormat.format(calendarView.getFirstDayOfCurrentMonth()));


        calendarView.setLocale(TimeZone.getDefault(), Locale.forLanguageTag("HU"));
        calendarView.setUseThreeLetterAbbreviation(true);

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = calendarView.getEvents(dateClicked);

                if (events.size() != 0) {

                    //

                    String eventDetails = "";

                    for (Event event : events) {
                        RequestItem requestItem = (RequestItem) event.getData();
                        eventDetails += "Időpont: " + getTime(requestItem.getMovingDate()) +
                                "\nÜgyfél: " + requestItem.getClientName() +
                                "\nFelrakodási cím: " + requestItem.getPickupLocation() +
                                "\nKirakodási cím: " + requestItem.getDropoffLocation();
                        if (event != events.get(events.size() - 1))
                            eventDetails += "\n\n";
                    }

                    new MaterialAlertDialogBuilder(getContext())
                            .setMessage(eventDetails)
                            .show();

                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                yearMonthTextView.setText(dateFormat.format(firstDayOfNewMonth));
            }
        });

    }

    private void getAcceptedRequests() {
        acceptedRequests = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
        query.whereEqualTo("moverId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null)
                    Log.d(TAG, "done: " + e.toString());
                else {
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            String clientName = object.getString("clientName");
                            Date movingDate = object.getDate("movingDate");
                            String pickupLocation = object.getString("pickupLocation");
                            String dropoffLocation = object.getString("dropoffLocation");
                            RequestItem requestItem = new RequestItem(clientName, movingDate, pickupLocation, dropoffLocation);
                            acceptedRequests.add(requestItem);
                            calendarView.addEvent(new Event(Color.parseColor("#E57373"), movingDate.getTime(), requestItem));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noEventsTextView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    private String getTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }
}
