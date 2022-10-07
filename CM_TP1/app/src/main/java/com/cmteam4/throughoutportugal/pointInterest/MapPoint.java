package com.cmteam4.throughoutportugal.pointInterest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.cmteam4.throughoutportugal.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MapPoint extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private double lastCurrentLatitude = Double.MAX_VALUE;
    private double lastCurrentLongitude = Double.MAX_VALUE;
    private LatLng currentUserPosition;
    private Marker markerCurrLoc;
    private Marker destMarker;

    private GoogleMap mMap;
    private TextView txtKM;

    private ImageView photoPoint;

    private Bundle variables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_point);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        txtKM = findViewById(R.id.txtKM);

        variables = getIntent().getExtras(); //vai buscar as variaveis que foram passadas da class Adapter neste caso

        TextView txtNamePoint = findViewById(R.id.txtNamePoint);
        txtNamePoint.setText(variables.getString("NAME_POINT"));

        photoPoint = findViewById(R.id.imageViewPoint);

        String photoID = getIntent().getStringExtra("PHOTO");

        if (photoID != null) {

            StorageReference storageReference = FirebaseStorage.getInstance()
                    .getReference().child("posts")
                    .child(photoID)
                    .child("photo.jpg");

            storageReference.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(photoPoint));
        } else {
            photoPoint.setImageResource(getIntent().getIntExtra("PHOTO", R.drawable.paisagem));
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        Log.e("LONGITUDE", String.valueOf(location.getLongitude()));
        Log.e("LATITUDE", String.valueOf(location.getLongitude()));

        LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());

        if (lastCurrentLatitude != location.getLatitude() || lastCurrentLongitude != location.getLongitude()) {
            lastCurrentLatitude = location.getLatitude();
            lastCurrentLongitude = location.getLongitude();
            currentUserPosition = currentLoc;
            if (markerCurrLoc != null) {
                markerCurrLoc.remove();
            }
            markerCurrLoc = mMap.addMarker(new MarkerOptions().position(currentLoc).title("IM HERE"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
        }

        destMarker.getPosition();
        if (currentUserPosition != null) {
            double distance = SphericalUtil.computeDistanceBetween(destMarker.getPosition(), currentUserPosition);
            txtKM.setText(String.format("%.2f", distance / 1000) + "Km");
        }
    }

   /* @Override
    public boolean onMarkerClick(Marker marker) {
       if (marker.getPosition() != null && currentUserPosition != null) {
            double distance = SphericalUtil.computeDistanceBetween(marker.getPosition(), currentUserPosition);
            txtKM.setText(String.format("%.2f", distance / 1000) + "Km");
        }

        return false;
    }*/


    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {
        mMap = googleMap;
        if (variables != null) {
            createNewMarker(new LatLng(variables.getDouble("LATITUDE"), variables.getDouble("LONGITUDE")), variables.getString("NAME_POINT"));
        }
    }

    public void createNewMarker(LatLng latLng, String namePoint) {

        Log.e("lat", "lat" + latLng.latitude);
        Log.e("lon", "lon" + latLng.longitude);

        MarkerOptions newMarker = new MarkerOptions();
        newMarker.position(latLng);
        newMarker.title(namePoint);

        //mMap.clear();

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomIn());

        destMarker = mMap.addMarker(newMarker);
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