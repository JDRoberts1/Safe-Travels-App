package com.fullsail.android.safetravels;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";
    boolean updatedImg = false;
    Bitmap imageBitmap = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;
    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);

    TextView errorLabel;
    EditText uName;
    EditText emailETV;
    Button passwordReset;
    Button deleteAcctBttn;
    Button updateProfileBttn;
    Button uploadBttn;
    ImageView profileImage;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        uName = findViewById(R.id.username_edit);
        emailETV = findViewById(R.id.email_register_edit);
        errorLabel = findViewById(R.id.error_label_edit);

        deleteAcctBttn = findViewById(R.id.delete_acct_bttn);
        deleteAcctBttn.setOnClickListener(deleteClick);

        updateProfileBttn = findViewById(R.id.update_bttn);
        updateProfileBttn.setOnClickListener(updateClick);

        passwordReset = findViewById(R.id.reset_pw_bttn);
        passwordReset.setOnClickListener(resetClick);

        uploadBttn = findViewById(R.id.upload_bttn_edit);
        uploadBttn.setOnClickListener(uploadClick);

        profileImage = findViewById(R.id.profile_picture_iv_edit);

        displayInfo();
    }

    private void displayInfo(){

        emailETV.setText(user.getEmail());

        if (user.getDisplayName() != null) {
            uName.setText(user.getDisplayName());
        }

        if (user.getPhotoUrl() != null) {
            profileImage.setImageURI(user.getPhotoUrl());
        }

    }

    private void logOutIntent(){
        FirebaseAuth.getInstance().signOut();
        Intent logInIntent = new Intent(EditProfileActivity.this, LoginActivity.class);
        startActivity(logInIntent);
    }
    

    private boolean nullCheck(String email, String username){

        if (email.isEmpty() || email.trim().isEmpty()){
            return false;
        }
        else return !username.isEmpty() && !username.trim().isEmpty();
    }

    // Activity Contracts

    // Contract to Request Permission if not granted
    public ActivityResultLauncher<String> requestPerms = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> Log.i(TAG, "onActivityResult: " + result));


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
        }
        else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK){

            if (data != null) {
                try {
                    imageBitmap = (Bitmap) MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        updatedImg = true;
        profileImage.setImageBitmap(imageBitmap);
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

    public Uri getImgUri(Context c, Bitmap bitmapImg, String uuid){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmapImg.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(c.getContentResolver(), bitmapImg, uuid, null);
        return Uri.parse(path);
    }

    View.OnClickListener updateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String newEmail = emailETV.getText().toString();
            String newUserName = uName.getText().toString();

            // Check for empty fields
            if (nullCheck(newEmail, newUserName)){

                // If the user email has been changed update the email
                if (!user.getEmail().equals(newEmail)){

                    updateEmail(newEmail);
                    logOutIntent();
                }

                // If username is different, Update the username
                if (!user.getDisplayName().equals(newUserName)){
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(newUserName)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            errorLabel.setText(e.getLocalizedMessage());
                        }
                    });
                }

                if (updatedImg){
                    Uri imgUri = getImgUri(EditProfileActivity.this, imageBitmap, user.getUid());

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(imgUri)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    errorLabel.setText(e.getLocalizedMessage());
                                }
                            });
                }

                errorLabel.setText(R.string.prompt_profile_updated);

            }
            else {
                errorLabel.setText(R.string.warning_empty_field);
            }

        }
    };

    private void updateEmail(String newEmail) {
        user.updateEmail(newEmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User email address updated.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        errorLabel.setText(e.getLocalizedMessage());
                    }
                });
    }

    View.OnClickListener resetClick = v -> {
        Intent resetIntent = new Intent(EditProfileActivity.this, ResetPasswordActivity.class);
        startActivity(resetIntent);
    };

    View.OnClickListener deleteClick = v -> {
        deleteUser();
    };

    private void deleteUser() {
        user.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User account deleted.");
                    }
                });

        logOutIntent();
    }

    View.OnClickListener uploadClick = v -> {

        // TODO: Request Permission
        if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPerms.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        // TODO: SET UP CAMERA code
        final CharSequence[] items = {"Take A Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle(R.string.prompt_photo);
        builder.setItems(items, (dialog, which) -> {
            if (items[which].equals("Take A Photo")){

                dispatchTakePictureIntent(cameraIntent);
            }
            else if (items[which].equals("Choose from Gallery")){
                galleryIntent.setType("image/*");
                dispatchGetPictureIntent(galleryIntent);
            }
        });

        builder.show();
    };
}
