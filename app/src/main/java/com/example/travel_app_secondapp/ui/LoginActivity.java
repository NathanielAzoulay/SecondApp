package com.example.travel_app_secondapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travel_app_secondapp.R;
import com.example.travel_app_secondapp.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private final String TAG = "LoginActivity";
    private LoginViewModel loginViewModel;
    private TextView errorText;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        //setContentView(R.layout.activity_login);
        binding.setLoginViewModel(loginViewModel);
        errorText = (TextView) findViewById(R.id.IdErrorText);
        sharedPreferences = getSharedPreferences("USER",MODE_PRIVATE);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        loadPreferences();
    }


    private void sendEmailVerification() {
        // Disable Verify Email button
        findViewById(R.id.btn_email_verify).setEnabled(false);

        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Re-enable Verify Email button
                        findViewById(R.id.btn_email_verify).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            errorText.setText("We sent to your email ("+ loginViewModel.form.getEmail() +") a verification message request. verify for login.");
                        } else {
                            Log.e(TAG, "sendEmailVerification failed!", task.getException());
                            Toast.makeText(getApplicationContext(), "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                            errorText.setText("Failed to send verification email to "+ loginViewModel.form.getEmail()+".");
                        }
                    }
                });
    }


    private boolean validateForm() {
        return loginViewModel.form.getEmail() != null && loginViewModel.form.getPassword() != null
                && !loginViewModel.form.getEmail().equals("") && !loginViewModel.form.getPassword().equals("");
    }


    public void signInOnClick(View view) {
        String userEmail = loginViewModel.form.getEmail();
        if (!validateForm()){
            errorText.setText("one or two of the fields are empty");
            return;
        }
        mAuth.signInWithEmailAndPassword(loginViewModel.form.getEmail(), loginViewModel.form.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "signIn: Success!");
                            // update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (!user.isEmailVerified())
                            {
                                errorText.setText("You need to verify your email ("+ userEmail +") for log in.");
                                return;
                            }
                            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                            savePreferences();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("myEmail",user.getEmail());
                            startActivity(intent);
                        } else {
                            Log.e(TAG, "signIn: Fail!", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed!", Toast.LENGTH_SHORT).show();
                            errorText.setText("Email or password are incorrect");
                        }
                    }
                });
    }

    public void signUpOnClick(View view) {
        mAuth.createUserWithEmailAndPassword(loginViewModel.form.getEmail(), loginViewModel.form.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "createAccount: Success!");
                            sendEmailVerification();
                            }
                        else {
                            Log.e(TAG, "createAccount: Fail!", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed!", Toast.LENGTH_SHORT).show();
                            errorText.setText("This email is not valid, or already exists");
                        }
                    }
                });
    }

    public void verifyEmailOnClick(View view) {
        sendEmailVerification();
    }

    public void resetPasswordOnClick(View view) {
        findViewById(R.id.btn_password_reset).setEnabled(false);
        mAuth.sendPasswordResetEmail(loginViewModel.form.getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Re-enable Verify Email button
                        findViewById(R.id.btn_password_reset).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Verification email sent to " + loginViewModel.form.getEmail(), Toast.LENGTH_SHORT).show();
                            errorText.setText("We sent to your email ("+ loginViewModel.form.getEmail() +") a verification message request. verify for reset password.");
                        } else {
                            Log.e(TAG, "sendEmailVerification failed!", task.getException());
                            Toast.makeText(getApplicationContext(), "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                            errorText.setText("Failed to send verification email to "+ loginViewModel.form.getEmail()+".");
                        }
                    }
                });
    }

    private void savePreferences(){
        String emailData = loginViewModel.form.getEmail().trim();
        String passwordData = loginViewModel.form.getPassword().trim();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Email",emailData );
        editor.putString("Pss",passwordData);
        editor.commit();
    }

    private void loadPreferences(){
        loginViewModel.form.setEmail(sharedPreferences.getString("Email",""));
        loginViewModel.form.setPassword(sharedPreferences.getString("Pss",""));
    }

}