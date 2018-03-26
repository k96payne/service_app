package com.group12.service_app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;
import android.app.Activity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group12.service_app.core.repositories.*;
import com.group12.service_app.core.repositories.interfaces.*;
import com.group12.service_app.data.models.Listing;

import java.util.ArrayList;
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

        Spinner SortingSpinner = getView().findViewById(R.id.SortingSpinnerId);

        ArrayAdapter<CharSequence>sortingType_ArrayAdapter =  ArrayAdapter.createFromResource(getActivity(), R.array.sorting_categories,android.R.layout.simple_spinner_dropdown_item);

        sortingType_ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SortingSpinner.setAdapter(sortingType_ArrayAdapter);
        //SortingSpinner.setOnItemSelectedListener(this);



//        String[] Listings = {"Listing0","Listing1","Listing2","Listing3","Listing4","Listing5","Listing6","Listing7",
//                "Listing0","Listing1","Listing2","Listing3","Listing4","Listing5","Listing6","Listing7",
//                "Listing0","Listing1","Listing2","Listing3","Listing4","Listing5","Listing6","Listing7"};final ArrayList<String> list = new ArrayList<>();

        // Added by Kyle
        final ArrayList<String> Listings = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("listings");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Listing listing = postSnapshot.getValue(Listing.class);
                        Listings.add(listing.title);
                        System.out.println(listing.title);
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // handle
            }
        });


        ListAdapter ad = new CustomAdapter(getActivity(),Listings);

        ListView Listings_ListView = (ListView) getView().findViewById(R.id.myListView);
        Listings_ListView.setAdapter(ad);
        Listings_ListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        String listing = String.valueOf(adapterView.getItemAtPosition(i));
                        Toast.makeText(getActivity(),listing, Toast.LENGTH_LONG).show();

                    }
                }
        );
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
