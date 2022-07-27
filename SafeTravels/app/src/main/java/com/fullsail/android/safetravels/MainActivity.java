package com.fullsail.android.safetravels;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DataTask.OnFinished {

    private static final String TAG = "MainActivity";
    SearchView searchView;
    TextView welcomeLabel;
    ImageView iv;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser cUser = mAuth.getCurrentUser();
    DataTask dt = null;
    public static ArrayList<String> searchResults = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (connCheck()){
                    apiResults();
                }

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

        return UtilsHelper.getConnectivityStatusString(this);
    }

    // Method to parse json
    public static void parseJson(String _jsonDataString){

        // Title & Subtitle
        String _city;
        String _country;
        String _state;

        try {

            JSONArray outerArr = new JSONArray(_jsonDataString);

            for (int i = 0; i < outerArr.length(); i++){
                JSONObject obj = outerArr.getJSONObject(i);
                _city = obj.getString("name");

                JSONObject innerOuterObj = obj.getJSONObject("country");
                _country = innerOuterObj.getString("id");

                JSONObject innerObj = innerOuterObj.getJSONObject("adminDivision1");
                _state = innerObj.getString("name");

                String result = _city + "," + _state + ", " + _country;
                searchResults.add(result);
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        // Check final ArrayList
        Log.i(TAG, "parseJson: " + searchResults.size());

    }

    private void apiResults(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://spott.p.rapidapi.com/places/autocomplete?limit=10&skip=0&country=US%2CCA&q=Sea&type=CITY")
                .get()
                .addHeader("X-RapidAPI-Key", "a37b576032msha06820f5d94f387p144c67jsn8b971038de49")
                .addHeader("X-RapidAPI-Host", "spott.p.rapidapi.com")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.w(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(Response response) throws IOException {

                ResponseBody body = response.body();

                String results = body.string();

                Log.i(TAG, "onResponse: " + results);

            }
        });
    }

    // Post Execute method to parse API results
    @Override
    public void onPost(String result) {
        parseJson(result);
    }
}