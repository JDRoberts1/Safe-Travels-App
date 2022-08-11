package com.fullsail.android.safetravels.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fullsail.android.safetravels.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListViewAdapter extends BaseAdapter {

    private static final long BASE_ID = 0x1011;
    private static final String TAG = "ListViewAdapter";
    private final Context mContext;
    private final ArrayList<String> mResults;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser cUser = mAuth.getCurrentUser();

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
        newFavorite.put("location", r);

        vh._favBttn.setOnClickListener(v -> {

            vh._favBttn.setColorFilter(R.color.orange_700);

            if (r != null) {
                db.collection("users")
                        .document(cUser.getUid())
                        .collection("favorites")
                        .document(r)
                        .set(newFavorite)
                        .addOnSuccessListener(unused -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                        .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
            }
        });

        return convertView;
    }

    static class ViewHolder{
        TextView _resultLabel;
        ImageButton _favBttn;

        public ViewHolder(View layout){
            _resultLabel = layout.findViewById(R.id.userName_LV_Label);
            _favBttn = layout.findViewById(R.id.favoriteBttn);
        }
    }
}
