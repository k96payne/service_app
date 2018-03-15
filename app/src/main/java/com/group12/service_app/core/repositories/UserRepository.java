package com.group12.service_app.core.repositories;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group12.service_app.core.repositories.interfaces.ILogin;
import com.group12.service_app.core.repositories.interfaces.ISignup;
import com.group12.service_app.data.models.Conversation;
import com.group12.service_app.data.models.UserPreferences;

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
    }

    public void AddConversationToUser(String userId, String conversationKey) {

        this.usersReference.child(userId).child("messages").push().setValue(conversationKey);

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
