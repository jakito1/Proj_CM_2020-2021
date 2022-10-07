package com.cmteam4.throughoutportugal.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.cmteam4.throughoutportugal.R;
import com.cmteam4.throughoutportugal.authentication.Login;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import static com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN;

public class SettingsFragment extends Fragment {

    private FirebaseAuth fAuth;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        fAuth = FirebaseAuth.getInstance();

        SharedPreferences settings = requireActivity().getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();

        Button logoutButton = root.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logout());

        Switch nightMode = root.findViewById(R.id.dmSwitch);
        Switch fpAuth = root.findViewById(R.id.fpSwitch);

        if (settings.getBoolean("darkMode", true))
            nightMode.setChecked(true);

        if (settings.getBoolean("fpAuth", true))
            fpAuth.setChecked(true);

        nightMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("darkMode", true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("darkMode", false);
            }
            editor.apply();
        });

        fpAuth.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("fpAuth", isChecked);
            editor.commit();
        });
        return root;
    }

    private void logout() {
        fAuth.signOut();
        GoogleSignIn.getClient(requireActivity(), new GoogleSignInOptions.Builder(DEFAULT_SIGN_IN).build())
                .signOut().addOnSuccessListener(unused -> clearCredentials());
    }

    private void clearCredentials() {
        startActivity(new Intent(requireActivity().getApplicationContext(), Login.class));
        requireActivity().finish();
    }
}