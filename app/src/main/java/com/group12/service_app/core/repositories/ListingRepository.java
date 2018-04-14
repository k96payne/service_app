package com.group12.service_app.core.repositories;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.group12.service_app.core.repositories.interfaces.IListingImageListener;
import com.group12.service_app.core.repositories.interfaces.IListingReader;
import com.group12.service_app.data.models.Listing;

import java.io.ByteArrayOutputStream;
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

    public void DeleteListing(String listingId) {

        this.listingsReference.orderByChild("id").equalTo(listingId).addChildEventListener(new ChildEventListener() {
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

    public void DeleteListing(Listing listing) {
        if(listing != null) {
            DeleteListing(listing.id);
        }
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

    public void SaveListingImage(Listing listing, Bitmap image) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference("images/" + listing.id);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        image.compress(Bitmap.CompressFormat.JPEG, 100, output);

        byte[] data = output.toByteArray();
        UploadTask task = reference.putBytes(data);

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.print(e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.print("Image successfully saved!");
            }
        });
    }

    public void GetListingImage(Listing listing, final IListingImageListener listingListener) {
        GetListingImage(listing.id, listingListener);
    }

    public void GetListingImage(String listingId, final IListingImageListener listingListener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference("images/" + listingId);
        long tenMegabytes = 10 * 1024 * 1024;

        reference.getBytes(tenMegabytes).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                listingListener.ListingImage(bitmap);
            }
        });
    }

}
