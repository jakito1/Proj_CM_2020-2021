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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.cmteam4.throughoutportugal.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DashboardFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private double lastCurrentLatitude = Double.MAX_VALUE;
    private double lastCurrentLongitude = Double.MAX_VALUE;
    private Marker marker;
    private Marker currentMarkerPos;
    private LatLng currentUserPosition;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(requireActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity().getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);

        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);


        Button btnMaps = root.findViewById(R.id.btnMaps);
        Button btnTest = root.findViewById(R.id.btnTest);

        btnMaps.setOnClickListener(v -> startActivity(new Intent(getActivity(), MapsActivity.class)));


        btnTest.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_dashboard_to_navigation_test));

        return root;
    }

    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng porto = new LatLng(41.1629618, -8.6616095);
        marker = mMap.addMarker(new MarkerOptions().position(porto).title("Porto"));
        mMap.setOnMarkerClickListener(this);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(this.marker)) {

            marker.getPosition();
            if (currentUserPosition != null) {
                double distance = SphericalUtil.computeDistanceBetween(marker.getPosition(), currentUserPosition);
                Toast toast = Toast.makeText(requireActivity().getApplicationContext(),
                        "Distance between You and the Destination is \n " + String.format("%.2f", distance / 1000) + "km", Toast.LENGTH_SHORT);
                toast.show();
            }

        }

        return false;
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

            if (currentMarkerPos != null) {
                currentMarkerPos.remove();
            }
            currentMarkerPos = mMap.addMarker(new MarkerOptions().position(currentLoc).title("IM HERE MOTHER FUCKER"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast toast = Toast.makeText(getActivity(), "Please enable GPS!", Toast.LENGTH_LONG);
        toast.show();
    }

}