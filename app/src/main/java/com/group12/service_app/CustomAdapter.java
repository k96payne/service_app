package com.group12.service_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class CustomAdapter  extends ArrayAdapter<String> {
//    CustomAdapter(@NonNull Context context, String[] Listings) {
    // Added By Kyle
    CustomAdapter(@NonNull Context context, ArrayList<String> Listings) {
        super(context, R.layout.custom_row, Listings);




    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater ListingInflater = LayoutInflater.from(getContext());
        View CustomView = ListingInflater.inflate(R.layout.custom_row,parent,false);
        String singleListing = getItem(position);
        TextView  ListingText = (TextView) CustomView.findViewById(R.id.ListingTitle);
        //ImageView  ListingImage = (ImageView) CustomView.findViewById(R.id.ListingImage);
        ListingText.setText(singleListing);
        // ListingImage.setImageResource(R.mipmap.brokencar);

        return CustomView;
    }
}
