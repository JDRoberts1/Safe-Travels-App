package com.fullsail.android.safetravels;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fullsail.android.safetravels.fragments.HomeFragment;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    ListView searchResultsView;
    ListViewAdapter adpt;
    ArrayList<String> searchResults = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Bundle extras = getIntent().getExtras();
        searchResults = extras.getStringArrayList(HomeFragment.TAG);


        searchResultsView = findViewById(R.id.listview);
        adpt = new ListViewAdapter(this, searchResults);
        searchResultsView.setAdapter(adpt);


    }
}
