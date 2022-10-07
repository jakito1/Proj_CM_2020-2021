package com.cmteam4.throughoutportugal.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cmteam4.throughoutportugal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    private ImageView btnChange;
    private String email;

    private TextView profileEmail, profileUsername;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        email = Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getEmail());


        profileEmail = root.findViewById(R.id.profileEmail);
        profileUsername = root.findViewById(R.id.profileUsername);

        updateProfile();

        Button resetPassword = root.findViewById(R.id.profileResetPasswordButton);
        resetPassword.setOnClickListener(v -> {
            FirebaseAuth.getInstance()
                    .sendPasswordResetEmail(email);
            Toast.makeText(getActivity(), R.string.resetLinkSent, Toast.LENGTH_LONG).show();
        });

        btnChange = root.findViewById(R.id.btn_change);

        btnChange.setOnClickListener(v ->
                startActivity(new Intent(requireActivity().getApplicationContext(), EditAvatar.class)));

        return root;
    }

    private void updateProfile() {
        String userUID;

        userUID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference().child("profilePictures")
                .child(userUID)
                .child("profile.jpg");

        DocumentReference documentReference = fStore.collection("users")
                .document(userUID);
        documentReference.addSnapshotListener(requireActivity(), (documentSnapshot, e) -> {
            try {
                storageReference.getDownloadUrl().addOnSuccessListener(uri ->
                        Picasso.get().load(uri).into(btnChange));

                profileEmail.setText(email);
                profileUsername.setText(Objects
                        .requireNonNull(documentSnapshot).getString("userName"));
            } catch (NullPointerException ignore) {
            }
        });


    }
}
