package com.cmteam4.throughoutportugal.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmteam4.throughoutportugal.R;
import com.cmteam4.throughoutportugal.cardViewPointInterest.Adapter;
import com.cmteam4.throughoutportugal.cardViewPointInterest.PointsInterest;
import com.cmteam4.throughoutportugal.model.PointOfInterest;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoritesFragment extends Fragment {


    Adapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);

        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        List<PointOfInterest> favourites = new ArrayList<>();
        TextView zeroFav = root.findViewById(R.id.zeroFav);


        for (PointOfInterest item : PointsInterest.items) {
            if (item.isFavourite(userUID))
                favourites.add(item);
        }

        if (favourites.isEmpty())
            zeroFav.setVisibility(View.VISIBLE);

        adapter = new Adapter(root.getContext(), favourites);
        RecyclerView recyclerView = root.findViewById(R.id.rv_fav);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setAdapter(adapter);

        return root;
    }
}