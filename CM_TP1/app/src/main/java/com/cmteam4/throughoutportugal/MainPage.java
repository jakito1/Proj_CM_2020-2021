package com.cmteam4.throughoutportugal;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.cmteam4.throughoutportugal.cardViewPointInterest.PointsInterest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PointsInterest pointsInterest = new PointsInterest();
        pointsInterest.loadItems();
        pointsInterest.listenToDiffs();

        setContentView(R.layout.activity_main_page);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(navView, navController);

    }
}