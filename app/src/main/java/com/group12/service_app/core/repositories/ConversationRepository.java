package com.group12.service_app.core.repositories;

import android.renderscript.Sampler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group12.service_app.core.repositories.interfaces.IConversationListener;
import com.group12.service_app.core.repositories.interfaces.IConversationMessageListener;
import com.group12.service_app.data.models.Conversation;
import com.group12.service_app.data.models.Listing;
import com.group12.service_app.data.models.Message;
import com.group12.service_app.data.models.UserPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by james on 3/10/18.
 */

public class ConversationRepository {

    UserRepository userRepository = new UserRepository();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference conversationsReference = database.getReference("conversations");

    public void GetMyConversations(final IConversationListener conversationListener) {

        FirebaseUser user = this.userRepository.GetCurrentUser();
        String userId = user.getUid();

        this.userRepository.GetUserPreferences(userId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserPreferences preferences = dataSnapshot.getValue(UserPreferences.class);

                if(preferences == null) { return; }

                for(String conversationId : preferences.messages) {

                    conversationsReference.child(conversationId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Conversation conversation = dataSnapshot.getValue(Conversation.class);

                            if(conversation == null) { return; }

                            conversationListener.onNewConversation(conversation);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    public void GetConversationMessages(final IConversationMessageListener conversationListener, final Conversation conversation) {

        conversationsReference.child(conversation.conversationId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Conversation liveConversation = dataSnapshot.getValue(Conversation.class);

                for(Message message: liveConversation.messages) {
                    conversationListener.onNewMessage(message);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }


    public void SendMessage(final String message, final String recipient) {

        final String currentUserId = this.userRepository.GetCurrentUser().getUid();
        final Message conversationMessage = new Message(currentUserId, message);
        final ConversationRepository self = this;

        this.userRepository.GetUserPreferences(currentUserId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long count = dataSnapshot.getChildrenCount();

                System.out.print(count);
                String key = dataSnapshot.getKey();
                UserPreferences preferences = dataSnapshot.getValue(UserPreferences.class);

                if(preferences == null) { return; }

                if(preferences.messages != null && preferences.messages.size() > 0) {

                    for (final String conversationId : preferences.messages) {
                        //conversationsReference.child(conversationId).addValueEventListener(new ValueEventListener() {
                        conversationsReference.child(conversationId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot conversationDataSnapshots) {

                                //for (DataSnapshot conversationDataSnapshot : conversationDataSnapshots.getChildren()) {

                                    //Conversation conversation = conversationDataSnapshot.getValue(Conversation.class);
                                    Conversation conversation = conversationDataSnapshots.getValue(Conversation.class);

                                    if (conversation == null) {
                                        return;
                                    }

                                    Boolean isUserConversation1 = conversation.recipient1.equals(currentUserId) && conversation.recipient2.equals(recipient);
                                    Boolean isUserConversation2 = conversation.recipient2.equals(currentUserId) && conversation.recipient1.equals(recipient);

                                    //If we have a conversation between the two people specified, this is where we need to append our message
                                    if (isUserConversation1 || isUserConversation2) {
                                        Map<String, Object> updates = new HashMap<>();

                                        conversation.messages.add(conversationMessage);

                                        //updates.put("/conversations/" + conversationId + "/messages", (Message[])messages.toArray());
                                        updates.put("/conversations/" + conversationId, conversation);

                                        database.getReference().updateChildren(updates);
                                    }

                                //}

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                } else {

                    //This person doesn't have any conversations. These two people haven't talked before. We need to create a conversation between them.
                    Conversation conversation = new Conversation();
                    DatabaseReference reference = conversationsReference.push();
                    String conversationKey = reference.getKey();

                    conversation.conversationId = conversationKey;
                    conversation.recipient1 = currentUserId;
                    conversation.recipient2 = recipient;
                    //conversation.listingId = listing.id;
                    conversation.messages = new ArrayList<>();

                    conversation.messages.add(conversationMessage);

                    try {
                        reference.setValue(conversation);
                    } catch(Exception ex) {
                        System.out.print(ex.getMessage());
                    }
                    userRepository.AddConversationToUser(currentUserId, conversationKey);
                    userRepository.AddConversationToUser(recipient, conversationKey);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                System.out.print(databaseError.getMessage());
            }
        });

    }


}
