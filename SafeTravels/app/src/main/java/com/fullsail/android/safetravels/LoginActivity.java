package com.fullsail.android.safetravels;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity:";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Button signInBttn;
    TextView forgotPasswordTV;
    TextView createAccountTV;
    TextView errorLabel;
    EditText emailETV;
    EditText passwordETV;


    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_SafeTravels_Fullscreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInBttn = findViewById(R.id.sign_in_bttn);
        signInBttn.setOnClickListener(signInClick);
        errorLabel = findViewById(R.id.sign_in_error_label);
        forgotPasswordTV = findViewById(R.id.forgot_password_bttn);
        forgotPasswordTV.setClickable(true);
        forgotPasswordTV.setOnClickListener(forgotPWClick);
        createAccountTV = findViewById(R.id.register_bttn);
        createAccountTV.setClickable(true);
        createAccountTV.setOnClickListener(createAcctClick);
        emailETV = findViewById(R.id.email_login_etv);
        passwordETV = findViewById(R.id.password_login_etv);
    }

    // Method to log user in
    private void logInUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }

    // Method to update the UI and take the user to the main screen
    private void updateUI(FirebaseUser user) {
        // TODO: Create Intent to take user to the main screen

    }

    View.OnClickListener signInClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    View.OnClickListener forgotPWClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    View.OnClickListener createAcctClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

}

