package com.group12.service_app;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.group12.service_app.core.repositories.ListingRepository;
import com.group12.service_app.core.repositories.interfaces.IListingImageListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.group12.service_app.core.repositories.ListingRepository;
import com.group12.service_app.core.repositories.UserRepository;
import com.group12.service_app.data.models.Listing;
import com.group12.service_app.data.models.UserPreferences;

import java.util.Iterator;

public class listing_details_view extends AppCompatActivity implements IListingImageListener {

    private static final String TAG = "listing_details_view";

    private ListingRepository listingRepository = new ListingRepository();

    private ImageView listerImageView;
    private ImageView ratingStar1ImgaeView;
    private TextView listingTitleTextView;
    private TextView priceTextView;
    private TextView addressTextView;
    private TextView descriptionTextView;
    private Button listingResponseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_details_view);
        Log.d(TAG, "onCreate: started.");
        this.listerImageView = (ImageView) findViewById(R.id.ListerProfileImage);
        this.ratingStar1ImgaeView = (ImageView) findViewById(R.id.ratingStar1);
        this.listingTitleTextView = (TextView) findViewById(R.id.listingNameLabel);
        this.priceTextView = (TextView) findViewById(R.id.payoffPriceLabel);
        this.addressTextView = (TextView) findViewById(R.id.dueAddressLabel);
        this.descriptionTextView = (TextView) findViewById(R.id.dueDescriptionLabel);
        this.listingResponseButton = (Button) findViewById(R.id.respondToListingButton);

        Listing listing = (Listing)getIntent().getSerializableExtra("listing");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String zipCode = getIntent().getStringExtra("address");
        Double price = getIntent().getDoubleExtra("price", 0);
        final String listingID = getIntent().getStringExtra("lisitngID");

        String id = getIntent().getStringExtra("id");

        if(listing != null) {
            this.LoadListinDetails(listing);
            this.listingRepository.GetListingImage(listing, this);
        }
        else
        {
            this.listingTitleTextView.setText(title);
            this.priceTextView.setText("$" + price.toString());
            this.addressTextView.setText(zipCode);
            this.descriptionTextView.setText(description);
            this.listingRepository.GetListingImage(id, this);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference listingsReference = database.getReference("listings");
        //final Query usersListings = listingsReference.orderByChild("ownerId").equalTo(user.getUid());
        final Query listingsMatchingId = listingsReference.orderByChild("id").equalTo(listingID);
        listingsMatchingId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });


    }

    public void LoadListinDetails(Listing listing) {
        if(listing == null) return;

        this.listingTitleTextView.setText(listing.title);
        this.priceTextView.setText("$" + listing.price.toString());
        this.addressTextView.setText(listing.zipCode);
        this.descriptionTextView.setText(listing.description);
    }

    public void ListingImage(Bitmap bitmap) {
        this.listerImageView.setImageBitmap(bitmap);
    }
}
