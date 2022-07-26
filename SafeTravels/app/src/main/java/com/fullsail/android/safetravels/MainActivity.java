package com.fullsail.android.safetravels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    SearchView searchView;
    TextView welcomeLabel;
    ImageView iv;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser cUser = mAuth.getCurrentUser();
    String apiKey = "ZmNiYzFjZTc5NjA5NGQ5YjliYjAwODQ3ZWY3OGI2YzM6ZjdkNzU0NWEtMmI4NS00MjYwLTk2YzMtYWE5ODk5MTE3N2M1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                connCheck();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        welcomeLabel = findViewById(R.id.welcomeLabel);
        iv = findViewById(R.id.profile_img_main);

        displayInfo();
    }

    // Method to display current users profile information
    private void displayInfo(){

        if (cUser.getDisplayName() != null) {
            String welcomeString = "Welcome, "+ cUser.getDisplayName();
            welcomeLabel.setText(welcomeString);
        }

        if (cUser.getPhotoUrl() != null) {
            iv.setImageURI(cUser.getPhotoUrl());
        }

    }

    // Menu Creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Method for menu selections
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

    // Check network connection
    private boolean connCheck(){
        String status = UtilsHelper.getConnectivityStatusString(MainActivity.this);
        Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
        return status != null;
    }
}