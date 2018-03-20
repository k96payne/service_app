package com.group12.service_app.core.repositories;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group12.service_app.core.repositories.interfaces.IListingReader;
import com.group12.service_app.data.models.Listing;

import java.util.Iterator;

/**
 * Created by james on 2/25/18.
 */

public class ListingRepository {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference listingsReference = database.getReference("listings");
    ValueEventListener listingEventListener;

    private IListingReader listingReaderDelegate;

    public void CreateListing(Listing listing) {
        Log.d("listing created", "onNewListing: listing created");
        this.listingsReference.push().setValue(listing);
    }

    public void GetAllListings(final IListingReader listingReader) {

        //If they give us a null listing reader, escape.
        if(listingReader == null) { return; }

        this.listingEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(listingReader != null) {

                    Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();

                    for (Iterator<DataSnapshot> i = snapshots.iterator(); i.hasNext();) {

                        DataSnapshot snapshot = i.next();

                        Listing listing = snapshot.getValue(Listing.class);

                        listingReader.onListingModified(listing);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        };

        this.listingsReference.addValueEventListener(this.listingEventListener);

    }

    /*public void SetListingListenerDelegate(IListingReader listingReader) {
        this.listingReaderDelegate = listingReader;
        this.createListingListener();
    }

    private void createListingListener() {

        //If we've already created our listener, return here. Only need to define it once.
        if(this.listingListener != null) { return; }

        //We have to make sure we can access 'this' from within the scope of the ChildEventListener.
        //Define 'self' so we can do so since 'this' is relative to the scope inside of the ChildEventListener.
        final ListingRepository self = this;

        this.listingListener = this.listingsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(self.listingReaderDelegate != null) {

                    Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();

                    for (Iterator<DataSnapshot> i = snapshots.iterator(); i.hasNext();) {

                        DataSnapshot snapshot = i.next();

                        Listing listing = snapshot.getValue(Listing.class);

                        self.listingReaderDelegate.onListingModified(listing);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }*/

}
