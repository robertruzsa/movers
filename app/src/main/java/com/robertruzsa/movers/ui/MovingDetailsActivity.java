package com.robertruzsa.movers.ui;

import afu.org.checkerframework.checker.nullness.qual.NonNull;
import afu.org.checkerframework.checker.nullness.qual.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.robertruzsa.movers.R;
import com.robertruzsa.movers.adapter.ImageAdapter;
import com.robertruzsa.movers.model.ImageItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovingDetailsActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ImageItem> images;

    private TextInputLayout movingDetailsTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_details);

        setToolbarTitle(getString(R.string.extra_information));
        setHeaderTextView(getString(R.string.step_seven));
        setBodyTextView(getString(R.string.instruction_moving_details));
        setPageIndicatorViewProgress();

        movingDetailsTextInputLayout = findViewById(R.id.movingDetailsTextInputLayout);

        images = new ArrayList<>();

        recyclerView = findViewById(R.id.imageRecycleView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ImageAdapter(images);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onButtonClick(int position) {
                images.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        getNextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImages();
                /*Intent intent = new Intent(getApplicationContext(), SelectVehicleActivity.class);
                startActivity(intent);*/
            }
        });
    }

    public void getPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getPhotoFromGallery();
    }

    public void addPhoto(View view) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        else {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Kép hozzáadása")
                    .setPositiveButton("Kamera", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            takePhoto();
                        }
                    })
                    .setNegativeButton("Galéria", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getPhotoFromGallery();
                        }
                    })
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                images.add(new ImageItem(bitmap));
                adapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            images.add(new ImageItem(bitmap));
            adapter.notifyDataSetChanged();
        }
    }

    private void saveImages() {
        String movingDetails = movingDetailsTextInputLayout.getEditText().getText().toString();

        if (!movingDetails.equals("") || !images.isEmpty()) {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Request");
            //query.whereEqualTo("clientName", ParseUser.getCurrentUser().get("name"));
            query.whereEqualTo("clientId", ParseUser.getCurrentUser().getObjectId());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {

                            ParseObject request = objects.get(0);
                            String requestId = request.getObjectId();

                            if (!movingDetails.equals("")) {
                                request.put("movingDetails", movingDetails);
                                request.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e != null)
                                            Log.i("Error", e.toString());
                                    }
                                });
                            }

                            if (!images.isEmpty()) {
                                List<ParseObject> parseImages = new ArrayList<>();
                                for (ImageItem imageItem : images) {
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    imageItem.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    byte[] byteArray = stream.toByteArray();
                                    ParseFile file = new ParseFile("image.png", byteArray);
                                    ParseObject image = new ParseObject("Image");
                                    image.put("requestId", requestId);
                                    image.put("image", file);
                                    parseImages.add(image);
                                }

                                ParseObject.saveAllInBackground(parseImages, new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e != null)
                                            Log.i("Error", e.toString());
                                        else {
                                            startNextActivity();
                                        }
                                    }
                                });
                            } else
                                startNextActivity();
                        }
                    }

            });
        } else
            startNextActivity();
    }

    private void startNextActivity() {
        Intent intent = new Intent(getApplicationContext(), SelectVehicleActivity.class);
        startActivity(intent);
    }
}
