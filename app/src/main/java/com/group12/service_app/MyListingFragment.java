package com.group12.service_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.SearchView;
import android.app.Activity;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.group12.service_app.core.repositories.*;
import com.group12.service_app.core.repositories.interfaces.*;
import com.group12.service_app.data.models.Listing;
import com.group12.service_app.data.models.listings;
//import com.group12.service_app.data.models.firebasesearch;


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
public class MyListingFragment extends Fragment implements IListingReader {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

//    private OnFragmentInteractionListener mListener;

   // private SearchView search_text;
    // private Button mSearchButton;
    private  RecyclerView Listings_result;
    private DatabaseReference mListingDatabase ;
    public ListingRepository ListingRepository = new ListingRepository();

    public MyListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();


        mListingDatabase = FirebaseDatabase.getInstance().getReference("listings");

        Listings_result = (RecyclerView) getView().findViewById(R.id.my_Listings_result);
        Listings_result.setHasFixedSize(true);
        Listings_result.setLayoutManager(new LinearLayoutManager(getActivity()));





        firebaseListingSearch();
//          mSearchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String input_text = search_text.getQuery().toString();
//                firebaseListingSearch(input_text);
//
//            }
//        }

        //);

    }

    private void firebaseListingSearch(){


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        Query firebaseSearchQuery  = mListingDatabase.orderByChild("ownerId").equalTo(user.getUid().toString());

        FirebaseRecyclerAdapter< listings, listingsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<listings, listingsViewHolder>(listings.class, R.layout.custom_row_mylisting, listingsViewHolder.class , firebaseSearchQuery) {
            @Override
            protected void populateViewHolder(final listingsViewHolder viewHolder, final listings list, final int position) {

                Log.e("Listing", list.title);
                viewHolder.setListings(list.id, list.description, list.title, list.price, list.zipCode);
                viewHolder.goButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent moveToDetails = new Intent(getActivity(), listing_details_view.class);
                        moveToDetails.putExtra("title", list.getTitle());
                        moveToDetails.putExtra("description", list.getDescription());
                        moveToDetails.putExtra("price", list.getPrice());
                        moveToDetails.putExtra("address", list.getZipCode());
                        startActivity(moveToDetails);
                    }
                });


                viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String id = list.getId();
                        Query toDelete  = mListingDatabase.orderByChild("id").equalTo(id);


                        toDelete.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ListingSnapshot: dataSnapshot.getChildren()) {
                                    ListingSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                       // Toast.makeText(getActivity(),id, Toast.LENGTH_LONG).show();

                        firebaseListingSearch();

                    }
                });


            }
        };



        Listings_result.setAdapter(firebaseRecyclerAdapter);

        Log.e("RecyclerView", "Adapter attached");
    }

    public  static class  listingsViewHolder extends RecyclerView.ViewHolder {
        View myView;
        Button goButton;
        Button deleteButton;
        public listingsViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
            goButton = (Button) itemView.findViewById(R.id.My_button3);
            deleteButton = (Button) itemView.findViewById(R.id.My_deleteButton);

        }

        public  void setListings(String ListingProviderName, String ListingDescription, String ListingTitle, double ListingPrice, String ListingZipCode){


            TextView listing_descirption = (TextView) myView.findViewById(R.id.My_ListingDescription);
            TextView Listing_Title = (TextView) myView.findViewById(R.id.My_ListingTitle);
            TextView Listing_Price = (TextView) myView.findViewById(R.id.My_ListingPrice);
            TextView Listing_zipCode = (TextView) myView.findViewById(R.id.My_ListingZip);


            listing_descirption.setText(ListingDescription);
            Listing_Title.setText(ListingTitle);
            Listing_Price.setText("$ " + String.format ("%.0f", ListingPrice));

            Listing_zipCode.setText(ListingZipCode);


        }
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
        Log.e("OnCreateView", "onCreateView: MyListingFragment");
        return inflater.inflate(R.layout.fragment_my_listing, container, false);
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
