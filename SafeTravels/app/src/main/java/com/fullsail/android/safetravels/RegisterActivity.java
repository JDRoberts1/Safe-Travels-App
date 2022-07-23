package com.fullsail.android.safetravels;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    EditText fName;
    EditText lName;
    EditText uName;
    EditText emailETV;
    EditText passwordETV;
    Button signUpBttn;
    TextView signInTV;
    TextView uploadTV;
    ImageView profileImage;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fName = findViewById(R.id.first_name_etv);
        lName = findViewById(R.id.last_name_etv);
        uName = findViewById(R.id.email_register_etv);
        emailETV = findViewById(R.id.username_etv);
        passwordETV = findViewById(R.id.password_login_etv);
        signUpBttn = findViewById(R.id.sign_up_bttn);
        signUpBttn.setOnClickListener(registerClick);
        signInTV = findViewById(R.id.signIn_TV);
        signInTV.setClickable(true);
        signInTV.setOnClickListener(signInClick);
        uploadTV = findViewById(R.id.profile_img_label);
        uploadTV.setClickable(true);
        uploadTV.setOnClickListener(uploadClick);
        profileImage = findViewById(R.id.profile_iv);
    }

    View.OnClickListener registerClick = new View.OnClickListener() {

        final String email = emailETV.getText().toString();
        final String password = emailETV.getText().toString();


        @Override
        public void onClick(View v) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                if (user != null) {
                                    updateUI(user);
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    private void updateUI(FirebaseUser user) {

        String firstName = fName.getText().toString();
        String lastName = lName.getText().toString();
        String userName = uName.getText().toString();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                // get photo uri
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });


    }

    View.OnClickListener signInClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // TODO: Create Intent to take user to sign in screen
        }
    };

    View.OnClickListener uploadClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // TODO: SET UP CAMERA code


        }
    };
}
