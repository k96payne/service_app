package com.group12.service_app;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private TextView dueDateLabel;
    private TextView dueTimeLabel;

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
        this.dueTimeLabel = (TextView) findViewById(R.id.dueTimeLabel);
        this.dueDateLabel = (TextView) findViewById(R.id.dueDateLabel);
        this.listingResponseButton = (Button) findViewById(R.id.respondToListingButton);

        Listing listing = (Listing)getIntent().getSerializableExtra("listing");
        String time = getIntent().getStringExtra("time");
        String date = getIntent().getStringExtra("date");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String zipCode = getIntent().getStringExtra("address");
        Double price = getIntent().getDoubleExtra("price", 0);
        final String listingID = getIntent().getStringExtra("lisitngID");
        String listingOwnerID = getIntent().getStringExtra("listingOwnerId");
        String id = getIntent().getStringExtra("id");

        if(listing != null) {
            this.LoadListinDetails(listing);
            this.listingRepository.GetListingImage(listing, this);
        }
        else
        {
            this.dueDateLabel.setText(date);
            this.dueTimeLabel.setText(time);
            this.listingTitleTextView.setText(title);
            this.priceTextView.setText("$" + price.toString());
            this.addressTextView.setText(zipCode);
            this.descriptionTextView.setText(description);
            this.listingRepository.GetListingImage(id, this);
        }


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        Log.d("PRINT_LISTING_OWNERID", "listing Owner ID: " + listingOwnerID);
        Log.d("LISTING_ID", id);
        Log.d("PRINT_USERID", "User ID: " + userID);
        listingOwnerID.equals(userID);
        if(listingOwnerID.equals(userID)){
            listingResponseButton.setText("Delete");
        }
    }

    public void LoadListinDetails(Listing listing) {
        if(listing == null) return;

        this.dueDateLabel.setText(listing.date);
        this.dueTimeLabel.setText(listing.time);
        this.listingTitleTextView.setText(listing.title);
        this.priceTextView.setText("$" + listing.price.toString());
        this.addressTextView.setText(listing.zipCode);
        this.descriptionTextView.setText(listing.description);
    }

    public void respondDeleteButtonClicked(View view){
        if(listingResponseButton.getText().equals("Delete")){
            String theListingId = getIntent().getStringExtra("listingID");
            listingRepository.DeleteListing(theListingId);
        }
        else{
            Log.d("RESPONSE_BUTTON_PRESSED", "The 'Response' button was pressed");
        }
    }

    public void ListingImage(Bitmap bitmap) {
        this.listerImageView.setImageBitmap(bitmap);
    }
}
