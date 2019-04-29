package com.robertruzsa.movers.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.robertruzsa.movers.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    ImageView profilePictureImageView;
    ImageButton changeProfilePictureImageButton;

    TextInputLayout nameTextInputLayout, emailTextInputLayout, phoneNumberTextInputLayout;
    TextInputEditText nameEditText, emailEditText, phoneNumberEditText;

    MaterialButton saveButton, signOutButton;

    Pattern phoneNumberPattern = Pattern.compile("^$|^[+]?[0-9]{10,13}$");

    ParseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        currentUser = ParseUser.getCurrentUser();

        initViews();

        getUserData();

        changeProfilePictureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoto();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateName(nameEditText.getText().toString().trim()) & validatePhoneNumber(phoneNumberEditText.getText().toString().trim())) {
                    saveChanges();
                    setEditTextDisabled(nameEditText);
                    setEditTextDisabled(phoneNumberEditText);
                }
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ParseUser.logOut();
            }
        });

        nameEditText.setOnTouchListener(onNameEditTextTouchListener);
        phoneNumberEditText.setOnTouchListener(onEmailEditTextTouchListener);
    }

    private void initViews() {
        nameTextInputLayout = getView().findViewById(R.id.profileMoverNameTextInputLayout);
        nameEditText = (TextInputEditText) nameTextInputLayout.getEditText();

        emailTextInputLayout = getView().findViewById(R.id.profileMoverEmailTextInputLayout);
        emailEditText = (TextInputEditText) emailTextInputLayout.getEditText();

        phoneNumberTextInputLayout = getView().findViewById(R.id.profilePhoneNumberTextInputLayout);
        phoneNumberEditText = (TextInputEditText) phoneNumberTextInputLayout.getEditText();

        profilePictureImageView = getView().findViewById(R.id.profilePictureImageView);
        changeProfilePictureImageButton = getView().findViewById(R.id.changeProfilePictureImageButton);

        saveButton = getView().findViewById(R.id.profileSaveButton);
        signOutButton = getView().findViewById(R.id.profileSignOutButton);
    }

    public void addPhoto() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        else
            getPhotoFromGallery();
    }

    public void getPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @afu.org.checkerframework.checker.nullness.qual.NonNull String[] permissions, @afu.org.checkerframework.checker.nullness.qual.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getPhotoFromGallery();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                profilePictureImageView.setImageBitmap(bitmap);
                saveProfilePhoto(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveProfilePhoto(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        ParseFile file = new ParseFile("image.png", byteArray);
        currentUser.put("profilePhoto", file);

        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                    Log.i("Error", e.toString());
                else
                    Log.i("Profile photo", "Profile photo saved successfully.");
            }
        });
    }

    private void getProfilePhoto() {
        ParseFile profilePhoto = currentUser.getParseFile("profilePhoto");
        if (profilePhoto!=null) {
            profilePhoto.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null && data != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        profilePictureImageView.setImageBitmap(bitmap);
                    }
                }
            });
        }
    }

    private void getUserData() {

        currentUser.fetchInBackground();

        getProfilePhoto();

        nameEditText.setText(currentUser.getString("name"));
        emailEditText.setText(currentUser.getEmail());
        phoneNumberEditText.setText(currentUser.getString("phoneNumber"));

    }



    public void saveChanges() {
        currentUser.put("name", nameEditText.getText().toString().trim());
        currentUser.put("phoneNumber", phoneNumberEditText.getText().toString().trim());

        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                    Log.i("Error", e.toString());
                else
                    Log.i("Save", "Successful");
            }
        });
    }

    View.OnTouchListener onNameEditTextTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (nameEditText.getRight() - nameEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    setEditTextEnabled(nameEditText);
                    return true;
                }
            }
            return false;
        }
    };

    View.OnTouchListener onEmailEditTextTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (phoneNumberEditText.getRight() - phoneNumberEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    setEditTextEnabled(phoneNumberEditText);
                    return true;
                }
            }
            return false;
        }
    };

    void setEditTextDisabled(EditText editText){
        editText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreyDisabled));
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
    }

    void setEditTextEnabled(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreyPrimary));
        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public boolean validateName(String name) {
        if (!name.equals("")) {
            nameTextInputLayout.setError(null);
            return true;
        } else {
            nameTextInputLayout.setError(getString(R.string.required));
            return false;
        }
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        if (!phoneNumberPattern.matcher(phoneNumber).matches()) {
            phoneNumberTextInputLayout.setError(getString(R.string.invalid_phone_number));
            return false;
        } else {
            phoneNumberTextInputLayout.setError(null);
            return true;
        }
    }
}
