package com.cmteam4.throughoutportugal.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cmteam4.throughoutportugal.R;


public class HomeFragment extends Fragment {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private ImageView imageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        imageView = root.findViewById(R.id.imageViewPerson);
        Button photoButton = root.findViewById(R.id.btnPhoto);


        photoButton.setOnClickListener(v -> {

            if (requireActivity().getApplicationContext()
                    .getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                if (ContextCompat.checkSelfPermission(requireActivity().getApplicationContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            } else {
                Toast toast = Toast.makeText(requireActivity().getApplicationContext(),
                        "Cant take photo, your device don't have camera", Toast.LENGTH_SHORT);
                toast.show();
            }

        });

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireActivity()
                                .getApplicationContext(),
                        "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(requireActivity()
                                .getApplicationContext(),
                        "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }
}