package com.group12.service_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.group12.service_app.core.repositories.ListingRepository;
import com.group12.service_app.core.repositories.UserRepository;
import com.group12.service_app.core.repositories.interfaces.IListingReader;
import com.group12.service_app.data.models.Listing;

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

    public CreateListingFragment() {
        // Required empty public constructor
    }

    public EditText listing_title;
    public EditText listing_price;
    public EditText listing_category;
    public EditText listing_location;
    public EditText listing_description;
    public EditText listing_contact;
    public Button create_listing;
    private ListingRepository listingRepository;
    private UserRepository userRepository = new UserRepository();

    @Override
    public void onStart() {
        super.onStart();
        this.listingRepository = new ListingRepository();
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

        create_listing = root.findViewById(R.id.create_listing);

        create_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Listing listing = GetNewListing();
                listingRepository.CreateListing(listing);

                Toast.makeText(getActivity(), "Listing created successfully.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), listing_details_view.class);
                intent.putExtra("listing", listing);

                getActivity().startActivity(intent);

            }
        });

        return root;
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

        return listing;
    }
}
