package com.group12.service_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.group12.service_app.core.repositories.ListingRepository;
import com.group12.service_app.core.repositories.interfaces.IListingReader;
import com.group12.service_app.data.models.Listing;

public class listings_view extends AppCompatActivity implements IListingReader {

    public ListingRepository ListingRepository = new ListingRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.ListingRepository.GetAllListings(this);
    }

    public void onNewListing(Listing listing) {
        String test = "";

        listing.title = test;
    }

    public void onListingModified(Listing listing) {
        String test = "";
    }

    public void onListingRemoved(Listing listing) {
        String test = "";
    }

    public void onListingMoved(Listing listing) {
        String test = "";
    }
}
