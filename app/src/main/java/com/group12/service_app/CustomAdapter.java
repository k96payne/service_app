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

import com.group12.service_app.data.models.Listing;

import java.util.ArrayList;

class CustomAdapter  extends ArrayAdapter<Listing> {
//    CustomAdapter(@NonNull Context context, String[] Listings) {
    // Added By Kyle
    CustomAdapter(@NonNull Context context, ArrayList<Listing> Listings) {
        super(context, R.layout.custom_row, Listings);




    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater ListingInflater = LayoutInflater.from(getContext());
        View CustomView = ListingInflater.inflate(R.layout.custom_row,parent,false);
        Listing singleListing = getItem(position);

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
