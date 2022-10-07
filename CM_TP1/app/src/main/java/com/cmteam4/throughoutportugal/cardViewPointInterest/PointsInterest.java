package com.cmteam4.throughoutportugal.cardViewPointInterest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmteam4.throughoutportugal.R;
import com.cmteam4.throughoutportugal.model.PointOfInterest;
import com.cmteam4.throughoutportugal.pointInterest.AddPointInterest;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PointsInterest extends AppCompatActivity {

    public static Set<PointOfInterest> items = new HashSet<>();
    public static String currentRegion;
    Adapter adapter;

    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[]) obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>) obj);
        }
        return list;
    }

    public void savePointOfInterestToDatabase(PointOfInterest pointOfInterest) {

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("posts")
                .document(pointOfInterest.getUniqueID());

        documentReference.set(pointOfInterest, SetOptions.merge());
    }

    @SuppressWarnings("unchecked")
    public void loadItems() {

        FirebaseFirestore.getInstance().collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        items.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> temp = document.getData();


                            items.add(new PointOfInterest((String) temp.get("txtTitle"),
                                    (String) temp.get("imgPoint"),
                                    Double.parseDouble(String.valueOf(temp.get("latitude"))),
                                    Double.parseDouble(String.valueOf(temp.get("longitude"))),
                                    (String) temp.get("regionOfPoint"), (String) temp.get("uniqueID"),
                                    new ArrayList<>((Collection<? extends String>) convertObjectToList(Objects.requireNonNull(temp.get("likeList")))),
                                    new ArrayList<>((Collection<? extends String>) convertObjectToList(Objects.requireNonNull(temp.get("dislikeList")))),
                                    new ArrayList<>((Collection<? extends String>) convertObjectToList(Objects.requireNonNull(temp.get("favouriteList"))))));
                            Log.d("TAG", document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });
    }

    @SuppressWarnings("unchecked")
    public void listenToDiffs() {
        FirebaseFirestore.getInstance().collection("posts")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w("TAG", "listen:error", e);
                        return;
                    }

                    for (DocumentChange dc : Objects.requireNonNull(snapshots).getDocumentChanges()) {
                        Map<String, Object> temp = dc.getDocument().getData();
                        switch (dc.getType()) {
                            case ADDED:
                                items.add(new PointOfInterest((String) temp.get("txtTitle"),
                                        (String) temp.get("imgPoint"),
                                        Double.parseDouble(String.valueOf(temp.get("latitude"))),
                                        Double.parseDouble(String.valueOf(temp.get("longitude"))),
                                        (String) temp.get("regionOfPoint"), (String) temp.get("uniqueID"),
                                        new ArrayList<>((Collection<? extends String>) convertObjectToList(Objects.requireNonNull(temp.get("likeList")))),
                                        new ArrayList<>((Collection<? extends String>) convertObjectToList(Objects.requireNonNull(temp.get("dislikeList")))),
                                        new ArrayList<>((Collection<? extends String>) convertObjectToList(Objects.requireNonNull(temp.get("favouriteList"))))));

                                break;
                            case MODIFIED:
                                removePointOfInterest((String) temp.get("uniqueID"));
                                items.add(new PointOfInterest((String) temp.get("txtTitle"),
                                        (String) temp.get("imgPoint"),
                                        Double.parseDouble(String.valueOf(temp.get("latitude"))),
                                        Double.parseDouble(String.valueOf(temp.get("longitude"))),
                                        (String) temp.get("regionOfPoint"), (String) temp.get("uniqueID"),
                                        new ArrayList<>((Collection<? extends String>) convertObjectToList(Objects.requireNonNull(temp.get("likeList")))),
                                        new ArrayList<>((Collection<? extends String>) convertObjectToList(Objects.requireNonNull(temp.get("dislikeList")))),
                                        new ArrayList<>((Collection<? extends String>) convertObjectToList(Objects.requireNonNull(temp.get("favouriteList"))))));

                                break;
                        }
                    }

                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_interest);

        ImageView btnAdd = findViewById(R.id.imgAdd);
        ImageView btnBack = findViewById(R.id.imgBack);
        RecyclerView rv = findViewById(R.id.rv);

        rv.setLayoutManager(new LinearLayoutManager(this));

        Bundle variables = getIntent().getExtras();


        ArrayList<PointOfInterest> tempPointsInterest = new ArrayList<>();
        for (PointOfInterest item : items) {
            if (item.getRegionOfPoint().equals(variables.getString("REGION_POINT"))) {
                tempPointsInterest.add(item);
            }
        }


        adapter = new Adapter(this, tempPointsInterest);
        rv.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), AddPointInterest.class);
            Bundle variable = getIntent().getExtras();
            currentRegion = variable.getString("REGION_POINT");
            intent.putExtra("REGION_NAME", variable.getString("REGION_POINT"));
            startActivity(intent);

        });

        btnBack.setOnClickListener(v -> finish());

    }

    private void removePointOfInterest(String uniqueID) {

        items.removeIf(pointOfInterest -> pointOfInterest.getUniqueID().equals(uniqueID));

    }
}