package com.group12.service_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.group12.service_app.data.models.Conversation;
import com.group12.service_app.data.models.Listing;
import com.group12.service_app.data.models.Message;
import com.group12.service_app.data.models.UserPreferences;
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
public class SearchListingFragment extends Fragment implements IListingReader {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    private SearchView search_text;
    // private Button mSearchButton;
    private  RecyclerView Listings_result;
    //private DatabaseReference mListingDatabase ;
    public ListingRepository ListingRepository = new ListingRepository();
    public final static String LIST_STATE_KEY = "recycler_list_state";
    protected Parcelable listState;




    public UserRepository UserRepository = new UserRepository();

    public SearchListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("On start", "search listings accessed");

        final ArrayList<Listing> Listings = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("listings");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Listing listing = postSnapshot.getValue(Listing.class);
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


        //mListingDatabase = FirebaseDatabase.getInstance().getReference("listings");
        search_text = (SearchView) getView().findViewById(R.id.search_text);
        Listings_result = (RecyclerView) getView().findViewById(R.id.Listings_result);

        Listings_result.setHasFixedSize(true);
        Listings_result.setLayoutManager(new LinearLayoutManager(getActivity()));

        search_text.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String input_text = search_text.getQuery().toString();
                Log.e("Search query:", input_text);
                firebaseListingSearch(input_text);
                return true;
            }

        });

        firebaseListingSearch("");
    }

    private void firebaseListingSearch(String input_text){
        if(input_text != null){
            Toast.makeText(getActivity(),"Started Search", Toast.LENGTH_SHORT).show();
        }


        Query firebaseSearchQuery  = FirebaseDatabase.getInstance().getReference("listings").orderByChild("title").startAt(input_text).endAt(input_text + "\uf8ff");

        FirebaseRecyclerAdapter< listings, listingsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<listings, listingsViewHolder>(listings.class, R.layout.custom_row, listingsViewHolder.class , firebaseSearchQuery) {
            @Override
            protected void populateViewHolder(final listingsViewHolder viewHolder, final listings list, final int position) {

                Log.e("Listing", list.title);

                viewHolder.setListings("", list.description, list.title, list.price, list.zipCode, list.time, list.date);

                UserRepository.GetUserPreferences(list.ownerId, new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserPreferences preferences = dataSnapshot.getValue(UserPreferences.class);

                        if(preferences == null) { return; }

                        //viewHolder.setListings(preferences.displayName, list.description, list.title, list.price, list.zipCode);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) { }
                });

                viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.print("Clicked!");
                        Intent moveToDetails = new Intent(getActivity(), listing_details_view.class);
                        moveToDetails.putExtra("title", list.getTitle());
                        moveToDetails.putExtra("description", list.getDescription());
                        moveToDetails.putExtra("price", list.getPrice());
                        moveToDetails.putExtra("address", list.getZipCode());
                        moveToDetails.putExtra("id", list.getId());
                        moveToDetails.putExtra("listingID", list.getId());
                        moveToDetails.putExtra("listingOwnerId", list.getOwnerId());
                        moveToDetails.putExtra("time", list.getTime());
                        moveToDetails.putExtra("date", list.getDate());
                        startActivity(moveToDetails);
                    }
                });
                //System.out.println("test");
                // System.out.println(list.title);
            }
        };
        //System.out.println("test");


        Listings_result.setAdapter(firebaseRecyclerAdapter);

        Log.e("RecyclerView", "Adapter attached");
    }

    public  static class  listingsViewHolder extends RecyclerView.ViewHolder {
        View myView;
        Button goButton;
        public listingsViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
            goButton = (Button) itemView.findViewById(R.id.button3);

        }

        public  void setListings(String ListingProviderName, String ListingDescription, String ListingTitle, double ListingPrice, String ListingZipCode, String ListingTime, String ListingDate){

            TextView Listing_provider_name = (TextView) myView.findViewById(R.id.ProviderName);
            TextView listing_descirption = (TextView) myView.findViewById(R.id.ListingDescription);
            TextView Listing_Title = (TextView) myView.findViewById(R.id.ListingTitle);
            TextView Listing_Price = (TextView) myView.findViewById(R.id.ListingPrice);
            TextView Listing_zipCode = (TextView) myView.findViewById(R.id.ListingZip);

            Listing_provider_name.setText(ListingProviderName);
            listing_descirption.setText(ListingDescription);
            Listing_Title.setText(ListingTitle);
            Listing_Price.setText("$ " + String.format ("%.0f", ListingPrice));
            // Listing_Price.setText(String.valueOf(ListingPrice));
            Listing_zipCode.setText(ListingZipCode);
            //  Listing_Price.setText(ListingPrice);

            //   Glide.with(getApplicationContext()).load()

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
        Log.e("OnCreateView", "onCreateView: SearchListingFragment");
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
