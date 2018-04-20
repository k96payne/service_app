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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.group12.service_app.core.repositories.ConversationRepository;
import com.group12.service_app.core.repositories.interfaces.IConversationListener;
import com.group12.service_app.data.models.Conversation;

import java.io.Serializable;
import java.util.ArrayList;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link CommunicationFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link CommunicationFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class CommunicationFragment extends Fragment implements IConversationListener {

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

    private ConversationRepository conversationRepository = new ConversationRepository();
    private ArrayList<Conversation> conversations = new ArrayList<>();
    private ListView listView;

    public CommunicationFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment CommunicationFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static CommunicationFragment newInstance(String param1, String param2) {
//        CommunicationFragment fragment = new CommunicationFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                                    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communication, container, false);
        listView = (ListView) view.findViewById(R.id.conversationMenu);
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

//        FILL THE CONVERSATIONS ARRAYLIST WITH CONVERSATIONS (NOT WORKING CURRENTLY CONVERSATIONS IS STILL EMPTY)
        conversationRepository.GetMyConversations(this);

//       TESTING IF CONVERSATIONS CAN BE DISPLAYED
//        Conversation testConvo = new Conversation();
//        testConvo.recipient1 = "recipient 1";
//        testConvo.recipient2 = "recipient 2";
//        testConvo.conversationId = "Test convo";
//        testConvo.listingId = "Test Convo listingId";
//        conversations.add(testConvo);

//        FILL THE LISTVIEW WITH THE CONVERSATIONS ARRAYLIST
        ArrayAdapter<Conversation> listViewAdapter = new ArrayAdapter<Conversation>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                conversations
        );

        listView.setAdapter(listViewAdapter);
        listView.setClickable(true);

//        MOVES TO CONVERSATION BETWEEN USER AND SOMEONE ELSE ON CLICK OF ITEM IN LISTVIEW
//        (crashes but haven't tested with real listing yet)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Object object = listView.getItemAtPosition(position);
                    Conversation conversation = (Conversation)object;
                    Intent moveToConversation = new Intent(getActivity(), conversation_view.class);
                    //in order to putExtra of a conversation you must be able to get the conversation held in the listView at that position
                    moveToConversation.putExtra("conversation", (Serializable)conversation);
                    startActivity(moveToConversation);
                }
        });

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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


    public void onNewConversation(Conversation conversation){
        conversations.add(conversation);
    }

}
