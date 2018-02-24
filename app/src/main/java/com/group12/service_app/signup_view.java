package com.group12.service_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class signup_view extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_view);
    }

    public void signup_user(View view){
        Intent signup_new_user = new Intent(this, login_view.class);
        startActivity(signup_new_user);
    }
}
