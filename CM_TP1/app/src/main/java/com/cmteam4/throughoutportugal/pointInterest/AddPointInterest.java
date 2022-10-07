package com.cmteam4.throughoutportugal.pointInterest;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cmteam4.throughoutportugal.R;
import com.cmteam4.throughoutportugal.cardViewPointInterest.PointsInterest;
import com.cmteam4.throughoutportugal.model.PointOfInterest;
import com.cmteam4.throughoutportugal.ui.dashboard.MapsActivity;
import com.cmteam4.throughoutportugal.ui.profile.EditAvatar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class AddPointInterest extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_CODE = 300;
    private double lastCurrentLatitude = Double.MAX_VALUE;
    private double lastCurrentLongitude = Double.MAX_VALUE;
    private boolean manualLoc;

    private EditText editTextTitle;

    private Bundle var;

    private GoogleMap mMap;

    private TextView txtLatitude;
    private TextView txtLongitude;

    private String photoUID;


    // public static double customLatitude;
    // public static double customLongitude;

    // private String currentRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point_interest);

        Button btnCurrLoc = findViewById(R.id.btnCurrLoc);
        Button btnAddPoint = findViewById(R.id.btnAddPoint);
        Button btnTakePhoto = findViewById(R.id.btnTakePhoto);

        ImageView imgBack = findViewById(R.id.imgBack);

        editTextTitle = findViewById(R.id.editTextTitle);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        txtLatitude = findViewById(R.id.txtLatitute);
        txtLongitude = findViewById(R.id.txtLongitute);

        Bundle variables = getIntent().getExtras();
        var = variables;
        if (variables.getDouble("CHOOSE_LATITUDE") != 0.0 || variables.getDouble("CHOOSE_LONGITUDE") != 0.0) {
            manualLoc = true;
            txtLatitude.setText(String.valueOf(variables.getDouble("CHOOSE_LATITUDE")));
            txtLongitude.setText(String.valueOf(variables.getDouble("CHOOSE_LONGITUDE")));
        }


        btnCurrLoc.setOnClickListener(v -> {

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }

            mMap.clear();
            txtLatitude.setText(String.valueOf(lastCurrentLatitude));
            txtLongitude.setText(String.valueOf(lastCurrentLongitude));

            LatLng currLoc = new LatLng(lastCurrentLatitude, lastCurrentLongitude);

            mMap.addMarker(new MarkerOptions().position(currLoc).title("IM HERE"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        });

        btnTakePhoto.setOnClickListener(v -> {
            if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
                } else {
                    Intent cameraInt = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraInt, CAMERA_REQUEST);
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Cant take photo, your device don't have camera", Toast.LENGTH_LONG);
                toast.show();
            }
            // Toast toast = Toast.makeText(getApplicationContext(), "Photo taken!", Toast.LENGTH_SHORT);
            //  toast.show();
        });

        btnAddPoint.setOnClickListener(v -> {
            //PointsInterest.load(v.getContext());
            AlertDialog.Builder builder = new AlertDialog.Builder(AddPointInterest.this);
            builder.setMessage("Are you sure to add this point ?");
            builder.setTitle("Confirmation");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                //  ImageView photo = new ImageView(getApplicationContext());

                Bundle variable = getIntent().getExtras();
                if (variable != null) {

                    PointOfInterest pointOfInterest = new PointOfInterest(String.
                            valueOf(editTextTitle.getText()),
                            photoUID, Double.parseDouble(String.valueOf(txtLatitude.getText())),
                            Double.parseDouble(String.valueOf(txtLongitude.getText())),
                            PointsInterest.currentRegion);


                    PointsInterest.items.add(pointOfInterest);

                    PointsInterest pointsInterest = new PointsInterest();
                    pointsInterest.savePointOfInterestToDatabase(pointOfInterest);


                    Intent intent = new Intent(getApplicationContext(), PointsInterest.class);
                    intent.putExtra("REGION_POINT", pointOfInterest.getRegionOfPoint());
                    startActivity(intent);
                }

                //startActivity(new Intent(getApplicationContext(), PointsInterest.class));

                Toast toast = Toast.makeText(getApplicationContext(), "Point Successfully added!", Toast.LENGTH_SHORT);
                toast.show();
            }).setNegativeButton("No", (dialog, which) -> dialog.cancel());

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });

        imgBack.setOnClickListener(v -> finish());

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        // LatLng currentLoc = new LatLng(location.getLatitude(),location.getLongitude());
        if (lastCurrentLatitude != location.getLatitude() || lastCurrentLongitude != location.getLongitude()) {

            lastCurrentLatitude = location.getLatitude();
            lastCurrentLongitude = location.getLongitude();

          /*  currentUserPosition = currentLoc;
            //mMap.clear();
            mMap.addMarker(new MarkerOptions().position(currentLoc).title("IM HERE MOTHER FUCKER"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc)); */
        }

    }

    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {

        mMap = googleMap;

        if (manualLoc) {

            LatLng currLoc = new LatLng(var.getDouble("CHOOSE_LATITUDE"), var.getDouble("CHOOSE_LONGITUDE"));

            mMap.addMarker(new MarkerOptions().position(currLoc).title("IM HERE"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }

        mMap.setOnMapClickListener(latLng -> startActivity(new Intent(getApplicationContext(), MapsActivity.class)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getApplicationContext(), "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photoTaken = (Bitmap) data.getExtras().get("data");

            photoUID = UUID.randomUUID().toString();

            StorageReference storageReference = FirebaseStorage.getInstance()
                    .getReference().child("posts")
                    .child(photoUID)
                    .child("photo.jpg");

            EditAvatar.storeImg(EditAvatar.getImageUri(getApplicationContext(), photoTaken), storageReference);
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast toast = Toast.makeText(getApplicationContext(), "Please enable GPS!", Toast.LENGTH_LONG);
        toast.show();
    }
}