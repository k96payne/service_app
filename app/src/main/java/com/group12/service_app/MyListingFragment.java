package com.group12.service_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.group12.service_app.core.repositories.ListingRepository;
import com.group12.service_app.core.repositories.interfaces.IListingReader;
import com.group12.service_app.data.models.Listing;

import java.util.ArrayList;
import java.util.Random;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link MyListingFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link MyListingFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class MyListingFragment extends Fragment implements IListingReader {

    private ListingRepository listingRepository;
    private ArrayList<Listing> listings = new ArrayList<Listing>();

    public MyListingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_listing, container, false);
        Button moveToDetailsBtn = (Button) view.findViewById(R.id.moveToDetailsBtn);

        moveToDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToDetails = new Intent(getActivity(), listing_details_view.class);
                Random random = new Random();
                Integer index = random.nextInt(listings.size());
                Listing listing = listings.get(index);

                moveToDetails.putExtra("listing", listing);

                startActivity(moveToDetails);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.listingRepository = new ListingRepository();
        this.listingRepository.GetAllListings(this);
    }

    public void onNewListing(Listing listing) {
        this.listings.add(listing);
    }

    public void onListingModified(Listing listing) {

    }

    public void onListingRemoved(Listing listing) {

    }

    public void onListingMoved(Listing listing) {

    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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
//
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
//    }
}
