package com.cmteam4.throughoutportugal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.cmteam4.throughoutportugal.card_view_point_interest.PointsInterest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

import static com.cmteam4.throughoutportugal.R.layout.activity_login;
import static com.cmteam4.throughoutportugal.R.string.bioAuthentication;
import static com.cmteam4.throughoutportugal.R.string.cancel;
import static com.cmteam4.throughoutportugal.R.string.emailPasswordInv;
import static com.cmteam4.throughoutportugal.R.string.emptyFieldError;
import static com.cmteam4.throughoutportugal.R.string.enterEmail;
import static com.cmteam4.throughoutportugal.R.string.loginFinger;
import static com.cmteam4.throughoutportugal.R.string.passResetInfo;
import static com.cmteam4.throughoutportugal.R.string.useAppPass;
import static com.cmteam4.throughoutportugal.R.string.wantToResetPass;
import static com.cmteam4.throughoutportugal.R.string.yes;


public class Login extends AppCompatActivity {

    public static final int GOOGLE_SIGN_IN_CODE = 999;
    private EditText email, password;
    private ProgressBar progressBar;

    private ImageView googleBtn;

    private AlertDialog.Builder resetAlert;
    private LayoutInflater layoutInflater;

    private FirebaseAuth fAuth;

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_ThroughoutPortugal);
        setContentView(activity_login);

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);

        if (settings.getBoolean("darkMode", true))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        fAuth = FirebaseAuth.getInstance();

        resetAlert = new AlertDialog.Builder(this);

        layoutInflater = this.getLayoutInflater();

        TextView signup = findViewById(R.id.register);
        signup.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), Signup.class)));

        TextView forgotPass = findViewById(R.id.forgotPassword);
        forgotPass.setOnClickListener(v -> forgotPassword());

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);

        progressBar = findViewById(R.id.loginProgressBar);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> loginUser());

        googleBtn = findViewById(R.id.signInWithGoogle);

        googleAuthentication();

        biometricAuthentication();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            PointsInterest pointsInterest = new PointsInterest();
            pointsInterest.loadItems();
            SharedPreferences settings = getSharedPreferences("UserInfo", 0);
            if (settings.getBoolean("fpAuth", true))
                biometricPrompt.authenticate(promptInfo);
            else {
                startActivity(new Intent(getApplicationContext(), MainPage.class));
                finish();
            }
        }
    }

    private void loginUser() {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        if (email.isEmpty()) {
            this.email.setError(getString(emptyFieldError));
            this.email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            this.password.setError(getString(emptyFieldError));
            this.password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        fAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    startActivity(new Intent(getApplicationContext(), MainPage.class));
                    finish();
                }).addOnFailureListener(e ->
                Toast.makeText(Login.this, emailPasswordInv, Toast.LENGTH_SHORT).show());
        progressBar.setVisibility((View.GONE));
    }

    private void forgotPassword() {
        View view = layoutInflater.inflate(R.layout.forgot_password_email, null);
        resetAlert.setTitle(wantToResetPass)
                .setMessage(enterEmail)
                .setPositiveButton(yes, (dialog, which) -> {
                    EditText editText = view.findViewById(R.id.resetEmailPopUp);

                    fAuth.sendPasswordResetEmail(editText.getText().toString()).addOnCompleteListener(task -> {
                        Toast.makeText(Login.this, passResetInfo, Toast.LENGTH_SHORT).show();
                        Toast.makeText(Login.this, passResetInfo, Toast.LENGTH_SHORT).show();

                    });

                }).setNegativeButton(cancel, null)
                .setView(view)
                .create().show();


    }

    private void biometricAuthentication() {

        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(Login.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                startActivity(new Intent(getApplicationContext(), MainPage.class));
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(bioAuthentication))
                .setSubtitle(getString(loginFinger))
                .setNegativeButtonText(getString(useAppPass))
                .build();
    }

    private void googleAuthentication() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("479915440296-5aj8vcpplepv0o7getp5m6mfbr56bg58.apps.googleusercontent.com")
                .requestEmail()
                .build();

        GoogleSignInClient signInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        googleBtn.setOnClickListener(v -> {
            Intent sign = signInClient.getSignInIntent();
            startActivityForResult(sign, GOOGLE_SIGN_IN_CODE);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount signInAcc = signInTask.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAcc.getIdToken(), null);

                fAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {

                    String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

                    DocumentReference documentReference = fStore.collection("users")
                            .document(userUID);

                    Map<String, Object> user = new HashMap<>();
                    user.put("userName", Objects.requireNonNull(fAuth.getCurrentUser()).getDisplayName());

                    documentReference.set(user);

                    PointsInterest pointsInterest = new PointsInterest();
                    pointsInterest.loadItems();

                    Toast.makeText(getApplicationContext(), "Your Google Account is Connected to Our Application.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainPage.class));
                });

            } catch (ApiException e) {
                e.printStackTrace();
            }

        }
    }
}