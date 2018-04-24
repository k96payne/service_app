package com.group12.service_app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group12.service_app.core.repositories.ConversationRepository;
import com.group12.service_app.core.repositories.UserRepository;
import com.group12.service_app.core.repositories.interfaces.IConversationListener;
import com.group12.service_app.core.repositories.interfaces.IConversationMessageListener;
import com.group12.service_app.data.models.Conversation;
import com.group12.service_app.data.models.Listing;
import com.group12.service_app.data.models.Message;
import com.group12.service_app.data.models.UserPreferences;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class conversation_view extends AppCompatActivity implements IConversationListener, IConversationMessageListener {

    private ConversationRepository conversationRepository = new ConversationRepository();
    private UserRepository userRepository = new UserRepository();
    private FirebaseListAdapter<Message> adapter;
    private Conversation conversation;
    private Listing listing;
    private String recipient;
    private FirebaseUser currentUser;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_view);

        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.listView = (ListView)findViewById(R.id.list_of_messages);
        this.conversation = (Conversation)getIntent().getSerializableExtra("conversation");
        this.recipient = getIntent().getStringExtra("recipientID");

        if(this.conversation == null || this.conversation.messages == null || this.conversation.messages.isEmpty()) {

            final conversation_view parent = this;

            this.conversationRepository.GetConversationWithUser(this.recipient, new IConversationListener() {
                @Override
                public void onNewConversation(Conversation conversation) {
                    parent.conversation = conversation;

                    conversationRepository.GetConversationMessages(parent, conversation, true);

                    setListViewData();
                }

                @Override
                public void onNoConversations() {
                    String initialMessage = "Hi, I am interested in your listing";
                    conversationRepository.SendMessage(initialMessage, recipient, parent);
                }
            });


        } else {

            conversationRepository.GetConversationMessages(this, this.conversation, true);

        }

        setListViewData();

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.input);
                String message = input.getText().toString();
                if (conversation != null) {
                    String messageRecipient = currentUser.getUid().equals(conversation.recipient1) ? conversation.recipient2 : conversation.recipient1;
                    sendMessage(message, messageRecipient);
                } else{
                    sendMessage(message, recipient);
                }
                input.setText("");
            }
        });
    }

    private void setListViewData() {

        //If we don't have a conversation, there's nothing to do here.
        if(this.conversation == null) { return; }

        ArrayAdapter<Message> listViewAdapter = new ArrayAdapter<Message>( this, android.R.layout.simple_list_item_1, this.conversation.messages) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final Message message = conversation.messages.get(position);
                final TextView textView = convertView != null ? (TextView)convertView : new TextView(getContext());

                textView.setText(message.toString());

                userRepository.GetUserPreferences(message.sender, new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserPreferences preferences = (UserPreferences) dataSnapshot.getValue(UserPreferences.class);

                        textView.setText(preferences.displayName + ": " + message.message);

                        scrollToBottom();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                return textView;
            }
        };

        this.listView.setAdapter(listViewAdapter);
        this.listView.setClickable(true);
        this.scrollToBottom();

    }

    private void sendMessage(String message, String recipient) {

        if(this.conversation != null && this.conversation.messages != null) {
            this.conversation.messages.clear();
        }

        this.conversationRepository.SendMessage(message, recipient);
        //this.conversation.messages.add(new Message(currentUser.getUid(), message));
        this.setListViewData();
        this.scrollToBottom();
    }

    private void scrollToBottom() {
        this.listView.setSelection(this.conversation.messages.size() - 1);
    }

    public void onNewConversation(Conversation conversation) {
        this.conversation = conversation;
        conversationRepository.GetConversationMessages(this, conversation);
        this.setListViewData();
    }

    public void onNoConversations() {

    }

    public void onNewMessage(Message message) {
        this.conversation.messages.add(message);
        this.setListViewData();
    }

}
