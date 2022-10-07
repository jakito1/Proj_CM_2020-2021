package com.cmteam4.throughoutportugal.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cmteam4.throughoutportugal.R;
import com.cmteam4.throughoutportugal.cardViewPointInterest.PointsInterest;

public class NotificationsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        Button btnPoints = root.findViewById(R.id.btnPontos);

        btnPoints.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), PointsInterest.class)));


        return root;
    }
}