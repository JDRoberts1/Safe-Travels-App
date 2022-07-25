package com.fullsail.android.safetravels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView welcomeLabel;
    ImageView iv;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser cUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeLabel = findViewById(R.id.welcomeLabel);
        iv = findViewById(R.id.profile_img_main);

        displayInfo();
    }

    private void displayInfo(){

        if (cUser.getDisplayName() != null) {
            String welcomeString = "Welcome, "+ cUser.getDisplayName();
            welcomeLabel.setText(welcomeString);
        }

        if (cUser.getPhotoUrl() != null) {
            iv.setImageURI(cUser.getPhotoUrl());
        }

    }

    private void displayNewInfo(FirebaseUser u){

        if (u.getDisplayName() != null) {
            String welcomeString = "Welcome, "+ cUser.getDisplayName();
            welcomeLabel.setText(welcomeString);
        }

        if (u.getPhotoUrl() != null) {
            iv.setImageURI(cUser.getPhotoUrl());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_edit_profile){
            Intent editIntent = new Intent(MainActivity.this, EditProfileActivity.class);
            startActivity(editIntent);
        }
        else if (item.getItemId() == R.id.menu_log_out){

            // Log the current user out
            mAuth.signOut();

            // Once user is logged out send user back to the log in screen
            Intent logInIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(logInIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}