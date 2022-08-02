package com.fullsail.android.safetravels;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListViewAdapter extends BaseAdapter {

    private static final long BASE_ID = 0x1011;
    private static final String TAG = "ListViewAdapter";
    private Context mContext = null;
    private ArrayList<String> mResults = null;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser cUser = mAuth.getCurrentUser();
    private Integer index = 0;

    public ListViewAdapter(Context mContext, ArrayList<String> mResults) {
        this.mContext = mContext;
        this.mResults = mResults;
    }

    @Override
    public int getCount() {
        if(mResults != null){
            return mResults.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mResults != null && position >= 0 && position < mResults.size()){
            return mResults.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return BASE_ID + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        String r = (String) getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else{
            vh = (ViewHolder) convertView.getTag();

        }

        if (r != null){
            vh._resultLabel.setText(r);
        }

        Map<String, Object> newFavorite = new HashMap<>();
        index+=1;
        newFavorite.put(index.toString(), r);

        vh._favBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vh._favBttn.setColorFilter(R.color.orange_700);

                if (r != null) {
                    db.collection("users")
                            .document(cUser.getUid())
                            .collection("favorites")
                            .document(r)
                            .set(newFavorite)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                }
            }
        });

        return convertView;
    }

    static class ViewHolder{
        TextView _resultLabel;
        ImageButton _favBttn;

        public ViewHolder(View layout){
            _resultLabel = layout.findViewById(R.id.resultLabel);
            _favBttn = layout.findViewById(R.id.favoriteBttn);
        }
    }
}
