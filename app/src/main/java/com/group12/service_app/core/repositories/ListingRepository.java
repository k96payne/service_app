package com.group12.service_app.core.repositories;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
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

    public void DeleteListing(Listing listing) {

        this.listingsReference.orderByChild("id").equalTo(listing.id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for (Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator(); i.hasNext();) {

                    i.next().getRef().removeValue();

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    public void GetListingsForUser(final IListingReader listingReader, FirebaseUser user) {

        //If they give us a null listing reader or user, escape.
        if(listingReader == null || user == null) { return; }

        this.listingsReference.orderByChild("ownerId").equalTo(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 if(listingReader != null) {

                    Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();

                    for (Iterator<DataSnapshot> i = snapshots.iterator(); i.hasNext();) {

                        DataSnapshot snapshot = i.next();

                        Listing listing = snapshot.getValue(Listing.class);

                        listingReader.onNewListing(listing);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

                        listingReader.onNewListing(listing);

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

}
