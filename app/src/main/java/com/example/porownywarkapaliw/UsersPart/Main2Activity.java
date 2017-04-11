package com.example.porownywarkapaliw.UsersPart;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.porownywarkapaliw.R;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        switch (intent.getIntExtra("whichFragment",2)){
            case 1:
                bNHMRegistration_ClickListener();
                break;
            case 2:
                bNHMLogin_ClickListener();
                break;
        }
    }

    private void loadFragment(android.app.Fragment fragment) {
        android.app.FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    private void bNHMLogin_ClickListener(){
        loadFragment(new Login());
       // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       // drawer.closeDrawers();
    }

    private void bNHMRegistration_ClickListener(){
        loadFragment(new Registration());
       // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       // drawer.closeDrawers();
    }
}
