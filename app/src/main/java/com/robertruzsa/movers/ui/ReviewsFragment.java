package com.robertruzsa.movers.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.robertruzsa.movers.R;
import com.robertruzsa.movers.adapter.RequestAdapter;
import com.robertruzsa.movers.adapter.ReviewAdapter;
import com.robertruzsa.movers.model.RequestItem;
import com.robertruzsa.movers.model.ReviewItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewsFragment extends Fragment {

    private static final String TAG = "ReviewsFragment";
    private RecyclerView recyclerView;
    private ReviewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ReviewItem> reviews;

    private TextView noReviewsTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        noReviewsTextView = getView().findViewById(R.id.noReviewsTextView);

        getReviews();

        /*reviews = new ArrayList<>();
        reviews.add(new ReviewItem(R.drawable.ic_user, "Nagy Péter", 12, 3, Calendar.getInstance().getTime(), getString(R.string.lorem_ipsum)));
        reviews.add(new ReviewItem(R.drawable.ic_user, "Kiss Zoltán", 1, 3.5f, Calendar.getInstance().getTime(), "Never again"));
        reviews.add(new ReviewItem(R.drawable.ic_user, "Kovács Éva", 120, 3, Calendar.getInstance().getTime(), "Jó szar volt. Botrány, baszdmeg."));*/

        recyclerView = getView().findViewById(R.id.requestsRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(recyclerView.getContext());
        adapter = new ReviewAdapter(reviews);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void getReviews() {
        reviews = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");
        query.whereEqualTo("moverId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null)
                    Log.d(TAG, "done: " + e.toString());
                else {
                    if (objects.size() > 0) {

                        for (ParseObject object : objects) {

                            String clientId = object.getString("clientId");
                            String clientName = getClientName(clientId);
                            int reviewCount = getReviewCount(clientId);
                            float rating = object.getInt("rating");
                            Date reviewDate = object.getUpdatedAt();
                            String comment = object.getString("comment");

                            reviews.add(new ReviewItem(R.drawable.ic_user, clientName, reviewCount, rating, reviewDate, comment));

                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noReviewsTextView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    private String getClientName(String clientId) {

        String clientName = "";

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.setLimit(1);
        query.whereEqualTo("objectId", clientId);
        try {
            List<ParseUser> parseUsers = query.find();
            if (parseUsers.size() > 0) {
                ParseUser parseUser = parseUsers.get(0);
                clientName = parseUser.getString("name");
            }
            return clientName;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getReviewCount(String clientId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");
        query.whereEqualTo("clientId", clientId);
        try {
            return query.count();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
