package com.group12.service_app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.group12.service_app.data.models.Listing;

import java.util.ArrayList;
import java.util.Random;

class CustomAdapter  extends ArrayAdapter<Listing> {
    private Context mcon;
//    CustomAdapter(@NonNull Context context, String[] Listings) {
    // Added By Kyle
    CustomAdapter(@NonNull Context context, ArrayList<Listing> Listings, View view) {
        super(context, R.layout.custom_row, Listings);
        mcon = context;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater ListingInflater = LayoutInflater.from(getContext());
        View CustomView = ListingInflater.inflate(R.layout.custom_row,parent,false);
        final Listing singleListing = getItem(position);

        TextView ListingTitle = (TextView) CustomView.findViewById(R.id.ListingTitle);
        ListingTitle.setText(singleListing.title);

        TextView ListingDescription = (TextView) CustomView.findViewById(R.id.ListingDescription);
        ListingDescription.setText(singleListing.description);

        TextView ListingZip = (TextView) CustomView.findViewById(R.id.ListingZip);
        ListingZip.setText(singleListing.zipCode);


//        TextView ListingPrice = (TextView) CustomView.findViewById(R.id.ListingPrice;
//        ListingPrice.setText((String)singleListing.price);



        return CustomView;
    }
}
