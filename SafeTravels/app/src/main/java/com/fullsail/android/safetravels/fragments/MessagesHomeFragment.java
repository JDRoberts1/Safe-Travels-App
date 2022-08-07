package com.fullsail.android.safetravels.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.fullsail.android.safetravels.R;

public class MessagesHomeFragment extends Fragment {

    ImageButton newMessageBttn;

    public MessagesHomeFragment() {
        // Required empty public constructor
    }

    public static MessagesHomeFragment newInstance(String param1, String param2) {
        return new MessagesHomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newMessageBttn = view.findViewById(R.id.new_Message_Bttn);
        newMessageBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new MessagesFragment()).commit();
            }
        });
    }
}