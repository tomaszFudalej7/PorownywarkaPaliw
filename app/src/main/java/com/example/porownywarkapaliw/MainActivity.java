package com.example.porownywarkapaliw;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.porownywarkapaliw.SQLDataBase.DBAdapter;
import com.example.porownywarkapaliw.UsersPart.Login;
import com.example.porownywarkapaliw.UsersPart.Registration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openAlarmDB();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    //tworzy nowy fragment
    private void loadFragment(android.app.Fragment fragment) {
        android.app.FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    public void bNHMLogin_ClickListener(View view){
        loadFragment(new Login());
         DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         drawer.closeDrawers();
    }

    public void bNHMRegistration_ClickListener(View view){
        loadFragment(new Registration());
         DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         drawer.closeDrawers();
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.AMD_addGasStation :{
                AlertDialog_AddGasStationMethod();
            }
            break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String gasStationName,gasStationLocation;
    private int gasPrice,petrolPrice,servicePoints;
    private DBAdapter dbAdapter;
    private void AlertDialog_AddGasStationMethod(){
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.alert_dialog_view_add_new_gas_station,null);
        final EditText etGasPrice,etPetrolPrice,etServicePoints,etGasStationName,etGasStationLocation;

        etGasPrice = (EditText) linearLayout.findViewById(R.id.etGasPrice);
        etPetrolPrice = (EditText) linearLayout.findViewById(R.id.etPetrolPrice);
        etServicePoints = (EditText) linearLayout.findViewById(R.id.etServicePoints);
        etGasStationName = (EditText) linearLayout.findViewById(R.id.etGasStationName);
        etGasStationLocation = (EditText) linearLayout.findViewById(R.id.etGasStationLocation);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add new gas station")
                .setMessage("Fill in all fields to add new gas station")
                .setView(linearLayout)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            if(!(etGasPrice == null || etPetrolPrice == null || etGasStationLocation ==null ||
                                    etGasStationName ==null || etServicePoints == null)){

                                gasStationName = etGasStationName.getText().toString();
                                gasStationLocation = etGasStationLocation.getText().toString();
                                gasPrice = Integer.parseInt(etGasPrice.getText().toString());
                                petrolPrice = Integer.parseInt(etPetrolPrice.getText().toString());
                                servicePoints = Integer.parseInt(etServicePoints.getText().toString());

                                dbAdapter.GetDataToSaveInDB(gasStationName,gasPrice,petrolPrice,servicePoints,gasStationLocation);
                                dbAdapter.InsertRowAlarmDB();
                            }
                        else {
                                Toast.makeText(MainActivity.this,"Fill in all fields to add new gas station",Toast.LENGTH_LONG).show();
                            }
                    }
                })
                .setNegativeButton("back",null).create().show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            about();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void openAlarmDB(){
        dbAdapter = new DBAdapter(MainActivity.this);
        dbAdapter.OpenAlarmDB();
    }

    private void about() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("About");
        alertDialog.setMessage("Comparision Fuel Prices version 1.0 created by Prysiażny, Duszka, Fudalej, Klank, Starski\nCopyright \u00A9 2017 - Cracow University of Technology");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}
