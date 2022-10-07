package com.cmteam4.throughoutportugal.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cmteam4.throughoutportugal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.UUID;

public class EditAvatar extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private ImageButton photoButton;

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes); // Used for compression rate of the Image : 100 means no compression
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, UUID.randomUUID().toString(), null);
        return Uri.parse(path);
    }

    public static void storeImg(Uri uri, StorageReference storageReference) {
        storageReference.putFile(uri);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_avatar);

        photoButton = findViewById(R.id.btn_change_pic);
        ImageButton btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        photoButton.setOnClickListener(v -> {

            if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Cant take photo, your device don't have camera", Toast.LENGTH_SHORT);
                toast.show();
            }

        });

        /*final TextView textView = root.findViewById(R.id.text_home);
        //homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getApplicationContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            String userUID = Objects.requireNonNull(FirebaseAuth.getInstance()
                    .getCurrentUser()).getUid();

            StorageReference storageReference = FirebaseStorage.getInstance()
                    .getReference().child("profilePictures")
                    .child(userUID)
                    .child("profile.jpg");

            storeImg(getImageUri(getApplicationContext(), photo), storageReference);

            storageReference.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(photoButton));

        }
    }
}