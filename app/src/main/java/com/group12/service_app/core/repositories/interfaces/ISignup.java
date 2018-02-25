package com.group12.service_app.core.repositories.interfaces;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by james on 2/25/18.
 */

public interface ISignup {
    void onSignupSuccessful(FirebaseUser user);
    void onSignupFailed(String error);
}
