package com.group12.service_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.group12.service_app.data.models.Listing;

public class listing_details_view extends AppCompatActivity {

    private static final String TAG = "listing_details_view";

    private ImageView listerImageView;
    private ImageView ratingStar1ImgaeView;
    private TextView listingTitleTextView;
    private TextView priceTextView;
    private TextView addressTextView;
    private TextView descriptionTextView;

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

        Listing listing = (Listing)getIntent().getSerializableExtra("listing");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String zipCode = getIntent().getStringExtra("address");
        Double price = getIntent().getDoubleExtra("price", 0);


        if(listing != null) {
            this.LoadListinDetails(listing);
        }
        else
        {
            this.listingTitleTextView.setText(title);
            this.priceTextView.setText("$" + price);
            this.addressTextView.setText(zipCode);
            this.descriptionTextView.setText(description);
        }
    }

    public void LoadListinDetails(Listing listing) {
        if(listing == null) return;

        this.listingTitleTextView.setText(listing.title);
        this.priceTextView.setText("$" + listing.price.toString());
        this.addressTextView.setText(listing.zipCode);
        this.descriptionTextView.setText(listing.description);
    }
}
