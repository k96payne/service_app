package com.group12.service_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import com.group12.service_app.core.repositories.*;
import com.group12.service_app.core.repositories.interfaces.ISignup;

public class signup_view extends AppCompatActivity implements ISignup {

    private UserRepository userRepository = new UserRepository();

    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_view);

        //Set up our repositories.
        this.userRepository.SignupDelegate = this;

        this.emailField = (EditText)findViewById(R.id.emailField);
        this.passwordField = (EditText)findViewById(R.id.passwordField);
    }

    public void signup_user(View view){

        String email = this.emailField.getText().toString();
        String password = this.passwordField.getText().toString();

        this.userRepository.CreateUser(email, password);

        Intent login = new Intent(this, login_view.class);
        startActivity(login);
    }

    public void onSignupSuccessful(FirebaseUser user) {
        Toast.makeText(this, "Account successfully created. Please log in.", Toast.LENGTH_LONG).show();
        this.finish();
    }

    public void onSignupFailed(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}