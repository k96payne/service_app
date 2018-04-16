package com.group12.service_app.core.repositories;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group12.service_app.core.repositories.interfaces.ILogin;
import com.group12.service_app.core.repositories.interfaces.ISignup;
import com.group12.service_app.data.models.Conversation;
import com.group12.service_app.data.models.UserPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by james on 2/19/18.
 */

public class UserRepository {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersReference = database.getReference("users");

    public ISignup SignupDelegate;
    public ILogin LoginDelegate;

    public UserRepository()
    {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void CreateUser(String email, String password, final String displayName) {
        final UserRepository self = this;
        this.firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            self.CreateUserPreferences(user, displayName);
                            self.SignupDelegate.onSignupSuccessful(user);

                        } else {
                            self.SignupDelegate.onSignupFailed(task.getException().getMessage());
                        }

                    }
                });
    }

    public void LoginUser(String email, String password) {
        try {
            final UserRepository self = this;
            this.firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                self.LoginDelegate.onLoginSuccessful(user);
                            } else {
                                self.LoginDelegate.onLoginFailed(task.getException().getMessage());
                            }

                        }
                    });
        } catch(Exception ex) {
            System.out.print(ex.getMessage());
        }
    }

    public void AddConversationToUser(final String userId, final String conversationKey) {

        this.usersReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserPreferences preferences = dataSnapshot.getValue(UserPreferences.class);

                if(preferences.messages == null) {
                    preferences.messages = new ArrayList<String>();
                }

                if(!preferences.messages.contains(conversationKey)) {
                    preferences.messages.add(conversationKey);
                }

                Map<String, Object> updates = new HashMap<>();

                updates.put("/users/" + userId, preferences);

                database.getReference().updateChildren(updates);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //this.usersReference.child(userId).child("messages").push().setValue(conversationKey);

        //Boolean isUserConversation1 = conversation.recipient1.equals(currentUser.getUid()) && conversation.recipient2.equals(recipient);
        //Boolean isUserConversation2 = conversation.recipient2.equals(currentUser.getUid()) && conversation.recipient1.equals(recipient);

        ////If we have a conversation between the two people specified, this is where we need to append our message
        //if (isUserConversation1 || isUserConversation2) {
        //    ArrayList<Message> messages = new ArrayList<Message>(conversation.messages);
        //    Map<String, Object> updates = new HashMap<>();

        //    messages.add(conversationMessage);

        //    updates.put("/conversations/" + conversationId + "/messages", (Message[])messages.toArray());

        //    database.getReference().updateChildren(updates);
        //}

    }

    public void GetUserPreferences(String userId, ValueEventListener listener) {

        this.usersReference.child(userId).addListenerForSingleValueEvent(listener);

    }

    public FirebaseUser GetCurrentUser() {
        return this.firebaseAuth.getCurrentUser();
    }


    private void CreateUserPreferences(FirebaseUser user, String displayName) {

        if(user == null) { return; }

        UserPreferences userPreferences = new UserPreferences();

        userPreferences.displayName = displayName;

        this.usersReference.child(user.getUid()).setValue(userPreferences);
    }
}
