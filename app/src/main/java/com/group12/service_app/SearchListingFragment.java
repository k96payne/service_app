package com.group12.service_app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.group12.service_app.core.repositories.*;
import com.group12.service_app.core.repositories.interfaces.*;
import com.group12.service_app.data.models.Listing;

import java.util.UUID;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link SearchListingFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link SearchListingFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class SearchListingFragment extends Fragment implements IListingReader {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    public ListingRepository ListingRepository = new ListingRepository();

    public SearchListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        this.ListingRepository.GetAllListings(this);
    }

    public void onNewListing(Listing listing) {
        printListing(listing);
    }

    public void onListingModified(Listing listing) {
        printListing(listing);
    }

    public void onListingRemoved(Listing listing) {
        printListing(listing);
    }

    public void onListingMoved(Listing listing) {
        printListing(listing);
    }

    private void printListing(Listing listing) {
        if(listing == null) { return; }
        String title = listing.title;
        String description = listing.description;
        System.out.println(title + ": " + description);
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment SearchListingFragment.
//     */
    // TODO: Rename and change types and number of parameters
//    public static SearchListingFragment newInstance(String param1, String param2) {
//        SearchListingFragment fragment = new SearchListingFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_listing, container, false);
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
//
//    @Override
////    public void onDetach() {
////        super.onDetach();
////        mListener = null;
////    }
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
