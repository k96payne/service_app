package com.group12.service_app.core.repositories;

import android.renderscript.Sampler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group12.service_app.data.models.Conversation;
import com.group12.service_app.data.models.Listing;
import com.group12.service_app.data.models.Message;

/**
 * Created by james on 3/10/18.
 */

public class ConversationRepository {

    UserRepository userRepository = new UserRepository();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference conversationsReference = database.getReference("conversations");

    public void SendMessage(final String message, final String recipient, final Listing listing) {

        final FirebaseUser currentUser = this.userRepository.GetCurrentUser();
        final String key = Conversation.CreateConversationKey(currentUser.getUid(), recipient);
        final Message conversationMessage = new Message(currentUser.getUid(), message);
        final ConversationRepository self = this;

        this.conversationsReference.child(key).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    self.conversationsReference.child(key).child("messages").push().setValue(conversationMessage);

                } else {

                    Conversation conversation = new Conversation();

                    conversation.listingId = listing.id;
                    conversation.recipient1 = currentUser.getUid();
                    conversation.recipient2 = recipient;
                    conversation.messages = new Message[] { conversationMessage };

                    //Add our conversation.
                    self.conversationsReference.child(key).setValue(conversation);

                    //Add a reference to the conversation to each user's preferences so we can find them later.
                    self.userRepository.AddConversationToUser(currentUser.getUid(), key);
                    self.userRepository.AddConversationToUser(recipient, key);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void GetConversationForListing(FirebaseUser user, Listing listing) {

        //this.userRepository.GetUserPreferences(user.getUid(), );

    }

}
