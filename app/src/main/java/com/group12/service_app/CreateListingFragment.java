package com.group12.service_app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.group12.service_app.core.repositories.ListingRepository;
import com.group12.service_app.core.repositories.UserRepository;
import com.group12.service_app.core.repositories.interfaces.IListingReader;
import com.group12.service_app.data.models.Listing;

import java.io.File;
import java.util.UUID;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link CreateListingFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link CreateListingFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class CreateListingFragment extends Fragment {

    private ListingRepository listingRepository;
    private UserRepository userRepository = new UserRepository();
    private int RESULT_LOAD_IMAGE = 1;
    private Bitmap listingImage;

    public EditText listing_title;
    public EditText listing_price;
    public EditText listing_category;
    public EditText listing_location;
    public EditText listing_description;
    public EditText listing_contact;
    public EditText listing_time;
    public EditText listing_date;
    public ImageView listing_image_view;
    public Button select_listing_image;
    public Button create_listing;

    public CreateListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        this.listingRepository = new ListingRepository();

        if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 0);
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_create_listing, container, false);

        listing_title = root.findViewById(R.id.listing_title);
        listing_price = root.findViewById(R.id.listing_price);
        listing_category = root.findViewById(R.id.listing_category);
        listing_location = root.findViewById(R.id.listing_location);
        listing_description = root.findViewById(R.id.listing_description);
        listing_contact = root.findViewById(R.id.listing_contact);
        listing_time = root.findViewById(R.id.listing_time);
        listing_date = root.findViewById(R.id.listing_date);
        listing_image_view = root.findViewById(R.id.listing_image_view);

        select_listing_image = root.findViewById(R.id.select_image_button);

        create_listing = root.findViewById(R.id.create_listing);

        select_listing_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePicker();
            }
        });

        create_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Create the listing
                Listing listing = GetNewListing();
                listingRepository.CreateListing(listing);

                //Save any image they selected.
                if(listingImage != null) {
                    listingRepository.SaveListingImage(listing, listingImage);
                }

                //Reset the form
                ResetForm();

                Toast.makeText(getActivity(), "Listing created successfully.", Toast.LENGTH_LONG).show();

                Activity activity = getActivity();
                Intent intent = new Intent(activity, listing_details_view.class);
                intent.putExtra("listing", listing);
                intent.putExtra("id", listing.id);
                intent.putExtra("listingOwnerId", listing.ownerId);

                //Navigate to the details activity but dismiss this one.
                startActivity(intent);
            }
        });

        return root;
    }

    public void showImagePicker() {

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {

            Uri selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            // String picturePath contains the path of selected Image
            this.listingImage = BitmapFactory.decodeFile(picturePath);

            if(this.listingImage != null) {
                this.listing_image_view.setImageBitmap(this.listingImage);
            }
        }
    }

    private Listing GetNewListing() {
        Listing listing = new Listing();
        FirebaseUser currentUser = this.userRepository.GetCurrentUser();
        String price  = listing_price.getText().toString();

        listing.title  = listing_title.getText().toString();
        listing.description = listing_description.getText().toString();
        listing.price = Double.parseDouble(price);
        listing.zipCode = listing_location.getText().toString();
        listing.id = UUID.randomUUID().toString();
        listing.ownerId = currentUser.getUid();
        listing.date = listing_date.getText().toString();
        listing.time = listing_time.getText().toString();

        return listing;
    }

    private void ResetForm() {
        listing_title.setText("");
        listing_price.setText("");
        listing_category.setText("");
        listing_location.setText("");
        listing_description.setText("");
        listing_contact.setText("");
        listing_time.setText("");
        listing_date.setText("");
        listing_image_view.setImageResource(android.R.drawable.ic_menu_camera);
    }
}
