package com.group12.service_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.group12.service_app.core.repositories.UserRepository;
import com.group12.service_app.core.repositories.interfaces.ILogin;

public class login_view extends AppCompatActivity implements ILogin {

    private UserRepository userRepository = new UserRepository();

    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);

        //Set up our repositories.
        this.userRepository.LoginDelegate = this;

        this.emailField = (EditText)findViewById(R.id.emailField);
        this.passwordField = (EditText)findViewById(R.id.passwordField);
    }

    public void login(View view) {

        String email = this.emailField.getText().toString();
        String password = this.passwordField.getText().toString();

        this.userRepository.LoginUser(email, password);
    }

    public void move_to_sign_up(View view){
        Intent move_user_to_sign_up = new Intent(this, signup_view.class);
        startActivity(move_user_to_sign_up);
    }

    public void onLoginSuccessful(FirebaseUser user) {

        Toast.makeText(this, "Logged in successfully!", Toast.LENGTH_LONG).show();

        Intent user_login = new Intent(this, MainActivity.class);
        startActivity(user_login);
    }

    public void onLoginFailed(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}
