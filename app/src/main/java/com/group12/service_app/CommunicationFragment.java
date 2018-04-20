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
import com.group12.service_app.core.repositories.UserRepository;
import com.group12.service_app.core.repositories.interfaces.IConversationListener;
import com.group12.service_app.data.models.Conversation;
import com.group12.service_app.data.models.UserPreferences;

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
    private UserRepository userRepository = new UserRepository();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_communication, container, false);
        this.listView = (ListView) view.findViewById(R.id.conversationMenu);

        conversationRepository.GetMyConversations(this);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        conversations.clear();
        conversationRepository.GetMyConversations(this);
    }

    private void setListViewData() {

//        FILL THE LISTVIEW WITH THE CONVERSATIONS ARRAYLIST
        ArrayAdapter<Conversation> listViewAdapter = new ArrayAdapter<Conversation>( getActivity(), android.R.layout.simple_list_item_1, conversations) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final Conversation conversation =  conversations.get(position);
                final TextView textView = (TextView)getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
                String recipient = conversation.recipient1 == userRepository.GetCurrentUser().getUid() ? conversation.recipient2 : conversation.recipient1;

                textView.setText(recipient);

                userRepository.GetUserPreferences(recipient, new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserPreferences preferences = (UserPreferences) dataSnapshot.getValue(UserPreferences.class);
                        textView.setText(preferences.displayName);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) { }
                });

                return textView;
            }
        };

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
                startActivityForResult(moveToConversation, 1);
            }
        });
    }

    public void onNewConversation(Conversation conversation){
        conversations.add(conversation);
        this.setListViewData();
    }

}
