package com.fullsail.android.safetravels.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fullsail.android.safetravels.HomeActivity;
import com.fullsail.android.safetravels.R;
import com.fullsail.android.safetravels.objects.User;
import com.fullsail.android.safetravels.adapters.UsersCardViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {

    private static final String TAG = "MessagesFragment";
    RecyclerView rv;
    FirebaseFirestore db;
    CollectionReference cR;
    List<User> users;
    UsersCardViewAdapter uA;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser cUser = mAuth.getCurrentUser();

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get User List collection
        db = FirebaseFirestore.getInstance();
        cR = db.collection("userList");

        // Add users from collection to list
        users = new ArrayList<>();
        getUsers();

        // RecycleView Set up
        rv = view.findViewById(R.id.recycleView);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        uA = new UsersCardViewAdapter(users, cUser.getDisplayName(), view.getContext());
        rv.setAdapter(uA);

    }

    public void getUsers(){
        cR.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }

                for (QueryDocumentSnapshot doc : value) {
                    String id = (String) doc.get("userId");
                    if (id != null && !id.equals(cUser.getUid())) {
                        String username = (String) doc.get("username");
                        String imgUrl = (String) doc.get("img");
                        User u = new User(username, id, imgUrl);
                        users.add(u);
                        uA.notifyDataSetChanged();
                        Log.i(TAG, "Snapshot: " + users.size());
                    }
                }

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}