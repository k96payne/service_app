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
import com.group12.service_app.data.models.AggregatedConversationListener;
import com.group12.service_app.data.models.Conversation;
import com.group12.service_app.data.models.Listing;
import com.group12.service_app.data.models.Message;
import com.group12.service_app.data.models.UserPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
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

    public void GetConversationWithUser(String recipient, final IConversationListener parentConversationListener) {

        final String currentUserId = this.userRepository.GetCurrentUser().getUid();

        final AggregatedConversationListener listener = new AggregatedConversationListener(2) {
            @Override
            public void finish(ArrayList<Conversation> conversations) {

                boolean found = false;

                for(Conversation conversation : conversations) {

                    if(conversation.UserIsInConversation(currentUserId)) {
                        found = true;
                        parentConversationListener.onNewConversation(conversation);
                    }

                }

                if(!found) {
                    parentConversationListener.onNoConversations();
                }

            }
        };

        this.conversationsReference.orderByChild("recipient1").equalTo(recipient).addListenerForSingleValueEvent(listener);
        this.conversationsReference.orderByChild("recipient2").equalTo(recipient).addListenerForSingleValueEvent(listener);
    }

    public void GetMyConversations(final IConversationListener conversationListener) {

        FirebaseUser user = this.userRepository.GetCurrentUser();
        String userId = user.getUid();

        this.userRepository.GetUserPreferences(userId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserPreferences preferences = dataSnapshot.getValue(UserPreferences.class);

                if(preferences == null) {
                    conversationListener.onNoConversations();
                    return;
                }

                if(preferences.messages == null || preferences.messages.isEmpty()) {
                    conversationListener.onNoConversations();
                    return;
                }

                for(String conversationId : preferences.messages) {

                    conversationsReference.child(conversationId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Conversation conversation = dataSnapshot.getValue(Conversation.class);

                            if(!dataSnapshot.exists() || conversation == null) {
                                conversationListener.onNoConversations();
                                return;
                            }

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
        SendMessage(message, recipient, null);
    }

    public void SendMessage(final String message, final String recipient, final IConversationListener listener) {

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

                    conversationsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot conversationDataSnapshots) {

                            Boolean found = false;

                            for (DataSnapshot conversationDataSnapshot : conversationDataSnapshots.getChildren()) {

                                Conversation conversationTest = conversationDataSnapshots.getValue(Conversation.class);
                                Map<String, Map<String, Object>> conversations = (Map<String, Map<String, Object>>)conversationDataSnapshots.getValue();

                                for(final String conversationId : conversations.keySet()) {

                                    Map<String, Object> conversation = conversations.get(conversationId);

                                    if (conversation == null) { return; }

                                    String recipient1 = (String)conversation.get("recipient1");
                                    String recipient2 = (String)conversation.get("recipient2");
                                    Boolean isUserConversation1 = recipient1.equals(currentUserId) && recipient2.equals(recipient);
                                    Boolean isUserConversation2 = recipient2.equals(currentUserId) && recipient1.equals(recipient);

                                    //If we have a conversation between the two people specified, this is where we need to append our message
                                    if (isUserConversation1 || isUserConversation2) {

                                        conversationsReference.child(conversationId).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot conversationDataSnapshot) {

                                                Map<String, Object> updates = new HashMap<>();
                                                Conversation conversationToUpdate = conversationDataSnapshot.getValue(Conversation.class);

                                                conversationToUpdate.messages.add(conversationMessage);

                                                //updates.put("/conversations/" + conversationId + "/messages", (Message[])messages.toArray());
                                                updates.put("/conversations/" + conversationId, conversationToUpdate);

                                                database.getReference().updateChildren(updates);

                                                if(listener != null) {
                                                    listener.onNewConversation(conversationToUpdate);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) { }
                                        });


                                        found = true;
                                    }
                                }
                            }

                            //If not found, create a new conversation between these two users.
                            if(!found) {
                                CreateNewConversation(recipient, conversationMessage, listener);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {

                    //This person doesn't have any conversations. These two people haven't talked before. We need to create a conversation between them.
                    CreateNewConversation(recipient, conversationMessage, listener);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                System.out.print(databaseError.getMessage());
            }
        });

    }

    private void CreateNewConversation(String recipient, Message message, final IConversationListener listener) {

        final Conversation conversation = new Conversation();
        DatabaseReference reference = conversationsReference.push();
        String conversationKey = reference.getKey();
        String currentUserId = this.userRepository.GetCurrentUser().getUid();

        conversation.conversationId = conversationKey;
        conversation.recipient1 = currentUserId;
        conversation.recipient2 = recipient;
        conversation.messages = new ArrayList<>();

        conversation.messages.add(message);

        try {
            reference.setValue(conversation, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(listener != null) {
                        listener.onNewConversation(conversation);
                    }
                }
            });
        } catch(Exception ex) {
            System.out.print(ex.getMessage());
        }
        userRepository.AddConversationToUser(currentUserId, conversationKey);
        userRepository.AddConversationToUser(recipient, conversationKey);
    }


}
