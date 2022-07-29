package com.fullsail.android.safetravels;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private static final long BASE_ID = 0x1011;
    private Context mContext = null;
    private ArrayList<String> mResults = null;

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

        vh._favBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable tapped =  AppCompatResources.getDrawable(mContext, R.drawable.ic_baseline_favorite_tapped);
                vh._favBttn.setImageDrawable(tapped);
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
