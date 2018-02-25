package com.group12.service_app.core.repositories;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.group12.service_app.core.repositories.interfaces.ILogin;
import com.group12.service_app.core.repositories.interfaces.ISignup;

/**
 * Created by james on 2/19/18.
 */

public class UserRepository {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public ISignup SignupDelegate;
    public ILogin LoginDelegate;

    public UserRepository()
    {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void CreateUser(String email, String password) {
        final UserRepository self = this;
        this.firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();

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
}
