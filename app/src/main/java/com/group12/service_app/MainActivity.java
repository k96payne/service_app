package com.group12.service_app;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {



    //private List<> fragments = new ArrayList<>(4);

    private Fragment fragment;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        //bottomNavigationView.inflateMenu(R.menu.bottom_navigation_menu);

        fragmentManager = getSupportFragmentManager();



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.listing_search:
                        fragment = new SearchListingFragment();
                        Log.d("listing search", "onNavigationItemSelected: listing search");
                        break;
                    case R.id.Create_listing:
                        fragment = new CreateListingFragment();
                        Log.d("create listing", "onNavigationItemSelected: create listing");
                        break;
                    case R.id.my_listing:
                        fragment = new MyListingFragment();
                        Log.d("my listing", "onNavigationItemSelected: my listing");
                        break;
                    case R.id.messages:
                        fragment = new CommunicationFragment();
                        Log.d("messages", "onNavigationItemSelected: messages");
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.bottomBar, fragment).commit();
                return true;
            }
        });

    }



}
