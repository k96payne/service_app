package com.group12.service_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.group12.service_app.core.repositories.ConversationRepository;
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
import com.group12.service_app.core.repositories.interfaces.IListingReader;
import com.group12.service_app.data.models.Conversation;
import com.group12.service_app.data.models.GenericConversationsListener;
import com.group12.service_app.data.models.Listing;
import com.group12.service_app.data.models.UserPreferences;
import com.group12.service_app.data.models.listings;

import java.util.Iterator;
import java.util.List;

public class listing_details_view extends AppCompatActivity implements IListingImageListener {

    private static final String TAG = "listing_details_view";

    private ConversationRepository conversationsRepository = new ConversationRepository();
    private ListingRepository listingRepository = new ListingRepository();
    private FirebaseDatabase database;
    private Listing listing;

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
        this.listing = (Listing) getIntent().getSerializableExtra("listing");

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

//        SET DELETE BUTTON IF LISTING IS USER OWNED
        if(listingOwnerID.equals(userID)){
            listingResponseButton.setText("Delete");
        }
    }

    public void LoadListinDetails(Listing listing) {
        if(listing == null) return;

        this.dueDateLabel.setText(listing.date);
        this.dueTimeLabel.setText(listing.time);
        this.listingTitleTextView.setText(listing.title);
        this.priceTextView.setText("$" + listing.price);
        this.addressTextView.setText(listing.zipCode);
        this.descriptionTextView.setText(listing.description);
    }

    public void respondDeleteButtonClicked(View view){
        if(listingResponseButton.getText().equals("Delete")){
            String theListingId = listing != null ? listing.id : getIntent().getStringExtra("listingID");

            if(theListingId == null || theListingId.isEmpty()) {
                theListingId = getIntent().getStringExtra("id");
            }

            listingRepository.DeleteListing(theListingId);
            finish();
        }
        else{
            //TODO
//            SEND MESSAGE ON RESPONSE TO LISTING
//            DOESN'T WORK IF IT'S NOT THE USER'S FIRST CONVERSATION
            readData(new MyCallBack() {
                @Override
                public void onCallBack(final Listing listing) {

                    String listingsOwnerID = getIntent().getStringExtra("listingOwnerId");
                    Intent startNewConverstation = new Intent(listing_details_view.this, conversation_view.class);
                    startNewConverstation.putExtra("recipientID", listingsOwnerID);
                    startNewConverstation.putExtra("listing", listing);
                    startNewConverstation.putExtra("conversationStart", "startConversation");
                    startActivity(startNewConverstation);
                }
            });
        }
    }

    public void ListingImage(Bitmap bitmap) {
        this.listerImageView.setImageBitmap(bitmap);
    }

//    USED TO RETRIEVE LISTING TO PASS TO CONVERSATION_VIEW AND START NEW CONVERSATION
    public interface MyCallBack {
        void onCallBack(Listing listing);
    }

//    USED TO HELP IMPLEMENT MYCALLBACK
    public void readData(final MyCallBack myCallBack){
        String id = getIntent().getStringExtra("id");
        database = FirebaseDatabase.getInstance();
        DatabaseReference listingsReference = database.getReference("listings");
        listingsReference.orderByChild("id").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Listing listing = dataSnapshot.getValue(Listing.class);
                myCallBack.onCallBack(listing);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
