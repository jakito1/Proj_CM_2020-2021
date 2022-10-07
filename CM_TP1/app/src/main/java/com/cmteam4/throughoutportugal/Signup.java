package com.cmteam4.throughoutportugal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cmteam4.throughoutportugal.card_view_point_interest.PointsInterest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.cmteam4.throughoutportugal.R.string.emailInvError;
import static com.cmteam4.throughoutportugal.R.string.emptyFieldError;
import static com.cmteam4.throughoutportugal.R.string.passwordsNotMatchError;
import static com.cmteam4.throughoutportugal.R.string.signupError;
import static com.cmteam4.throughoutportugal.R.string.smallPasswordError;

public class Signup extends AppCompatActivity {

    private Button signupButton;
    private EditText userName, email, password, repeatPassword;
    private ProgressBar progressBar;

    private String userUID;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        TextView backToLogin = findViewById(R.id.backToLogin);
        backToLogin.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        });

        userName = findViewById(R.id.signupUserName);
        email = findViewById(R.id.signupEmail);
        password = findViewById(R.id.signupPassword);
        repeatPassword = findViewById(R.id.signupRepeatPassword);

        progressBar = findViewById(R.id.signupProgressBar);

        signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(v -> registerUser());

    }

    private void registerUser() {
        String userName = this.userName.getText().toString().trim();
        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        String repeatPassword = this.repeatPassword.getText().toString().trim();

        if (userName.isEmpty()) {
            this.userName.setError(getString(emptyFieldError));
            this.userName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            this.email.setError(getString(emptyFieldError));
            this.email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email.setError(getString(emailInvError));
            this.email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            this.password.setError(getString(emptyFieldError));
            this.password.requestFocus();
            return;
        }

        if (password.length() < 6) {
            this.password.setError(getString(smallPasswordError));
            this.password.requestFocus();
            return;
        }

        if (repeatPassword.isEmpty()) {
            this.repeatPassword.setError(getString(emptyFieldError));
            this.repeatPassword.requestFocus();
            return;
        }

        if (!repeatPassword.equals(password)) {
            this.repeatPassword.setError(getString(passwordsNotMatchError));
            this.repeatPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {

                    userUID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                    DocumentReference documentReference = fStore.collection("users")
                            .document(userUID);

                    Map<String, Object> user = new HashMap<>();
                    user.put("userName", userName);

                    documentReference.set(user).addOnSuccessListener(unused ->
                            Log.d("TAG", "onSuccess: created user with UID: " + userUID))
                            .addOnFailureListener(e ->
                                    Log.d("TAG", "onFailure: " + e.toString()));

                    PointsInterest pointsInterest = new PointsInterest();
                    pointsInterest.loadItems();

                    startActivity(new Intent(getApplicationContext(), MainPage.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(Signup.this, signupError, Toast.LENGTH_SHORT).show());
        progressBar.setVisibility(View.GONE);

    }
}