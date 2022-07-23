package com.fullsail.android.safetravels;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    EditText fName;
    EditText lName;
    EditText uName;
    EditText emailETV;
    EditText passwordETV;
    Button signUpBttn;
    TextView signInTV;
    Button uploadTV;
    ImageView profileImage;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;
    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_SafeTravels_Fullscreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fName = findViewById(R.id.first_name_etv);
        lName = findViewById(R.id.last_name_etv);
        uName = findViewById(R.id.username_etv);
        emailETV = findViewById(R.id.email_register_etv);
        passwordETV = findViewById(R.id.password_register_etv);
        signUpBttn = findViewById(R.id.sign_up_bttn);
        signUpBttn.setOnClickListener(registerClick);
        signInTV = findViewById(R.id.signIn_TV);
        signInTV.setClickable(true);
        signInTV.setOnClickListener(signInClick);
        uploadTV = findViewById(R.id.upload_bttn);
        uploadTV.setOnClickListener(uploadClick);
        profileImage = findViewById(R.id.profile_picture_iv);
    }

    View.OnClickListener registerClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            final String email = emailETV.getText().toString();
            final String password = emailETV.getText().toString();

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

    // Activity Contracts

    // Contract to Request Permission if not granted
    public ActivityResultLauncher<String> requestPerms = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> Log.i(TAG, "onActivityResult: " + result));


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profileImage.setImageBitmap(imageBitmap);
        }
        else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK){
            Bitmap bm=null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            profileImage.setImageBitmap(bm);
        }

    }


    private void dispatchTakePictureIntent(Intent i) {
        try {
            startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    private void dispatchGetPictureIntent(Intent i) {
        try {
            startActivityForResult(i, REQUEST_IMAGE_GALLERY);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    private void updateUI(FirebaseUser user) {

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
            Intent logInScreenIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(logInScreenIntent);
        }
    };

    View.OnClickListener uploadClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // TODO: Request Permission
            if (ActivityCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPerms.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            // TODO: SET UP CAMERA code
            final CharSequence[] items = {"Take A Photo", "Choose from Gallery", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setTitle(R.string.prompt_photo);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (items[which].equals("Take A Photo")){

                        dispatchTakePictureIntent(cameraIntent);
                    }
                    else if (items[which].equals("Choose from Gallery")){
                        galleryIntent.setType("image/*");
                        dispatchGetPictureIntent(galleryIntent);
                    }
                }
            });

            builder.show();
        }
    };
}
