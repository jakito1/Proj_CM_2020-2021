package com.cmteam4.throughoutportugal.ui.dashboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.cmteam4.throughoutportugal.R;
import com.cmteam4.throughoutportugal.pointInterest.AddPointInterest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private double lastCurrentLatitude = Double.MAX_VALUE;
    private double lastCurrentLongitude = Double.MAX_VALUE;
    private Marker markerCurrLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(latLng -> {
            MarkerOptions choseMarker = new MarkerOptions();
            choseMarker.position(latLng);
            choseMarker.title(latLng.latitude + " : " + latLng.longitude);
            mMap.clear();
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


            mMap.addMarker(choseMarker);

            Intent intent = new Intent(getApplicationContext(), AddPointInterest.class);
            intent.putExtra("CHOOSE_LATITUDE", latLng.latitude);
            intent.putExtra("CHOOSE_LONGITUDE", latLng.longitude);
            startActivity(intent);
        });

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        Log.e("LONGITUDE", String.valueOf(location.getLongitude()));
        Log.e("LATITUDE", String.valueOf(location.getLongitude()));

        LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());

        if (lastCurrentLatitude != location.getLatitude() || lastCurrentLongitude != location.getLongitude()) {

            lastCurrentLatitude = location.getLatitude();
            lastCurrentLongitude = location.getLongitude();

            if (markerCurrLoc != null) {
                markerCurrLoc.remove();
            }
            markerCurrLoc = mMap.addMarker(new MarkerOptions().position(currentLoc).title("IM HERE"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
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