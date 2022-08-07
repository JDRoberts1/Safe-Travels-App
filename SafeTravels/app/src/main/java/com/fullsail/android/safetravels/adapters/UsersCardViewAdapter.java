package com.fullsail.android.safetravels.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fullsail.android.safetravels.R;
import com.fullsail.android.safetravels.objects.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersCardViewAdapter extends RecyclerView.Adapter<UsersCardViewAdapter.ViewHolder> {

    List<User> userList;
    String username;
    Context mContext;

    FirebaseFirestore db;
    CollectionReference cR;

    public UsersCardViewAdapter(List<User> userList, String username, Context mContext) {
        this.userList = userList;
        this.username = username;
        this.mContext = mContext;

        db = FirebaseFirestore.getInstance();
        cR = db.collection("userList");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User u = userList.get(position);
        holder.userTV.setText(u.getUsername());
        if (u.getUri() != null){
            Uri imgUri = Uri.parse(u.getUri());
            holder.userIV.setImageURI(imgUri);
        }
        else{
            holder.userIV.setImageResource(R.drawable.default_img);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Transition to the single chat fragment
                // Send username to chat activity
            }
        });
    }

    @Override
    public int getItemCount() {
        if (userList!= null){
            return userList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView userTV;
        private final TextView userMessage;
        private final CircleImageView userIV;
        private final CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userTV = itemView.findViewById(R.id.usernameCV);
            userMessage = itemView.findViewById(R.id.messageCV);
            userIV = itemView.findViewById(R.id.userImageView);
            cardView = itemView.findViewById(R.id.userView);
        }
    }
}
