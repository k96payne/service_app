package com.group12.service_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class listing_details_view extends AppCompatActivity {

    private static final String TAG = "listing_details_view";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_details_view);
        Log.d(TAG, "onCreate: started.");
        ImageView ListerImageView = (ImageView) findViewById(R.id.ListerProfileImage);
        ImageView ratingStar1 = (ImageView) findViewById(R.id.ratingStar1);
    }
}
