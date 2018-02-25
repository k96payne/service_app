package com.group12.service_app.core.repositories.interfaces;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by james on 2/25/18.
 */

public interface ILogin {
    void onLoginSuccessful(FirebaseUser user);
    void onLoginFailed(String error);
}
