package com.group12.service_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class login_view extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
    }

    public void login(View view) {
        Intent user_login = new Intent(this, MainActivity.class);
        startActivity(user_login);
    }

    public void move_to_sign_up(View view){
        Intent move_user_to_sign_up = new Intent(this, signup_view.class);
        startActivity(move_user_to_sign_up);
    }
}
