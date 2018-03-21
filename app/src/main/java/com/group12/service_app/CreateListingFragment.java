package com.group12.service_app;

import android.content.Context;
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

import com.group12.service_app.core.repositories.ListingRepository;
import com.group12.service_app.core.repositories.interfaces.IListingReader;
import com.group12.service_app.data.models.Listing;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link CreateListingFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link CreateListingFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class CreateListingFragment extends Fragment implements IListingReader{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;


    public CreateListingFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment CreateListingFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static CreateListingFragment newInstance(String param1, String param2) {
//        CreateListingFragment fragment = new CreateListingFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    public EditText listing_title;
    public EditText listing_price;
    public EditText listing_category;
    public EditText listing_location;
    public EditText listing_description;
    public EditText listing_contact;
    public Button create_listing;
    private ListingRepository listingRepository = new ListingRepository();
    private Listing listing = new Listing();

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
                onNewListing(listing);
            }
        });


        return root;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);


    public void onNewListing(Listing listing) {
        listing.title  = listing_title.getText().toString();
        listing.description = listing_description.getText().toString();
        String price  = listing_price.getText().toString();
        listing.price = Double.parseDouble(price);
        listing.zipCode = listing_location.getText().toString();
        listing.id = listing_category.getText().toString();

        this.listingRepository.CreateListing(listing);

    }

    public void onListingModified(Listing listing) {
        String test = "";
    }

    public void onListingRemoved(Listing listing) {
        String test = "";
    }

    public void onListingMoved(Listing listing) {
        String test = "";
    }
}
