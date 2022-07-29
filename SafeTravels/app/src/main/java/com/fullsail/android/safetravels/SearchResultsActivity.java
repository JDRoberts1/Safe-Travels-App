package com.fullsail.android.safetravels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        searchResults = extras.getStringArrayList(MainActivity.TAG);


        searchResultsView = findViewById(R.id.listview);
        adpt = new ListViewAdapter(this, searchResults);
        searchResultsView.setAdapter(adpt);


    }
}
