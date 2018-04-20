package com.group12.service_app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.group12.service_app.core.repositories.ConversationRepository;
import com.group12.service_app.core.repositories.interfaces.IConversationListener;
import com.group12.service_app.core.repositories.interfaces.IConversationMessageListener;
import com.group12.service_app.data.models.Conversation;
import com.group12.service_app.data.models.Listing;
import com.group12.service_app.data.models.Message;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class conversation_view extends AppCompatActivity implements IConversationMessageListener {

    private ConversationRepository conversationRepository = new ConversationRepository();
    private FirebaseListAdapter<Message> adapter;
    private ArrayList<Message> messages = new ArrayList<>();
    private Conversation conversation;
    private Listing listing;
    private String recipient;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_view);

//        INFO USER FOR INITIALMESSAGE SENDING THROUGH PRESS OF RESPONSE BUTTON IN DETAILS VIEW
        String startConversation = getIntent().getStringExtra("conversationStart");
        listing = (Listing) getIntent().getSerializableExtra("listing");
        recipient = getIntent().getStringExtra("recipientID");
        if(startConversation.equals("startConversation")){
//            CURRENT ERROR IF IT ISN'T USER'S FIRST CONVERSATION
//            MESSAGE ISN'T SENT/CONVERSATION ISN'T CREATED
            String initialMessage = "Hi, I am interested in your listing";
            sendInitialMessage(initialMessage, recipient, listing);
        }

//        INFO USED IF CONVESATION IS OPENED FROM CONVERSATIONS FRAGMENT
        conversation = (Conversation) getIntent().getSerializableExtra("conversation");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

//        TRYING TO DISPLY MESSAGES BETWEEN USER AND SELECTED OTHER USER
//        STILL NEED TO IMPLEMENT MORE LOGIC FOR THIS
//        PLAN TO CREATE ARRAYLIST OF MESSAGES AND DISPLAY SIMILAR TO CONVERSATIONS
//        CANNOT USER GETCONVERSATIONS MESSAGES IF IT IS A NEW CONVERSATION BECAUSE NO CONVERSATION TO PASS IN
//        conversationRepository.GetConversationMessages(this, conversation);

//        LOGIC FOR SENDING MESSAGE
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.input);
                String messageRecipient;
                if (conversation != null) {
                    if (currentUser.getUid().equals(conversation.recipient1)) {
                        messageRecipient = conversation.recipient2;
                        conversationRepository.SendMessage(input.getText().toString(), messageRecipient, listing);
                    } else {
                        messageRecipient = conversation.recipient1;
                        conversationRepository.SendMessage(input.getText().toString(), messageRecipient, listing);
                    }
                    //clear the input
                    input.setText("");
                }
                else{
                    messageRecipient = recipient;
                    conversationRepository.SendMessage(input.getText().toString(), messageRecipient, listing);
                    input.setText("");
                }
            }
        });
    }


    //TODO Display the chat messages
//    DIFFERENT ATTEMPT AT DISPLAYING MESSAGES, DON'T THINK IT WILL WORK FOR THIS APP THOUGH
    public void displayMessages(){
        ListView listofMessages = (ListView)findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.messages, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageSender = (TextView)v.findViewById(R.id.message_sender);
            }
        };

        listofMessages.setAdapter(adapter);
    }

//    SENDING INITIAL MESSAGE IF RESPOND BUTTON CLICKED
    public void sendInitialMessage(String initialMessage, String recipient, Listing listing)
    {
        conversationRepository.SendMessage(initialMessage, recipient, listing);
    }

    public void onNewMessage(Message message){
        messages.add(message);
    }

}
