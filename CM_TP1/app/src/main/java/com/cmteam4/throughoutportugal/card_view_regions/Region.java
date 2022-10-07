package com.cmteam4.throughoutportugal.card_view_regions;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmteam4.throughoutportugal.R;

import java.util.ArrayList;

public class Region extends AppCompatActivity {

    public ArrayList<String> items;
    AdapterRegion adapter;
    private RecyclerView rvRegions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        rvRegions = findViewById(R.id.rvRegions);

        Bundle variables = getIntent().getExtras();
        if (variables != null) {
            if (variables.getString("REGION").equals("NORTE")) {
                items = new ArrayList<>();
                items.add("Braga");
                items.add("Vila Real");
                items.add("Viana do Castelo");
                items.add("Bragança");
                items.add("Porto");
                items.add("Aveiro");
            } else if (variables.getString("REGION").equals("CENTRO")) {
                items = new ArrayList<>();
                items.add("Viseu");
                items.add("Guarda");
                items.add("Coimbra");
                items.add("Leiria");
                items.add("Castelo Branco");
                items.add("Santarém");
            } else {
                items = new ArrayList<>();
                items.add("Lisboa");
                items.add("Portalegre");
                items.add("Évora");
                items.add("Setúbal");
                items.add("Beja");
                items.add("Faro");
            }
        }

        rvRegions.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterRegion(this, items);
        rvRegions.setAdapter(adapter);

    }
}