package com.example.manchanda.smarthome;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;

/**
 * Created by manchanda on 21/1/16.
 */


public class Controller extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ResultListener {

    ImageButton bulb, curtains, fan, security;
    ProgressBar spinner;
    Switch autoswitch;

    int bulb_status;
    int curtain_status;
    int fan_status;
    int automatic_status;
    int security_status;


    String bulb_data;
    String curtain_data;
    String fan_data;
    String security_data;
    String automatic_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller);

        bulb=(ImageButton)findViewById(R.id.image_bulb);
        curtains=(ImageButton)findViewById(R.id.image_curtains);
        fan=(ImageButton)findViewById(R.id.image_fan);
        spinner=(ProgressBar)findViewById(R.id.progressBar_controller);
        autoswitch=(Switch)findViewById(R.id.switch_auto_mode);
        security=(ImageButton)findViewById(R.id.image_security);

        bulb_data = getIntent().getExtras().getString("lights");
        curtain_data = getIntent().getExtras().getString("curtains");
        fan_data = getIntent().getExtras().getString("fan");
        automatic_data = getIntent().getExtras().getString("automatic");
        security_data = getIntent().getExtras().getString("security");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**
         *Get the params from data and acc to it set the image
         */





        Log.d(bulb_data,curtain_data);

        if(bulb_data.equalsIgnoreCase("1"))
        {
            bulb.setImageResource(R.drawable.bulb_on_android);
            bulb_status=1;
        }
        else {
            bulb.setImageResource(R.drawable.bulb_off_android);
            bulb_status=0;
        }


        if(automatic_data.equalsIgnoreCase("1"))
        {
            autoswitch.setChecked(true);
            automatic_status=1;
        }
        else {
            autoswitch.setChecked(false);
            automatic_status=0;
        }

        if(security_data.equalsIgnoreCase("1"))
        {
            security.setImageResource(R.drawable.doorsecurityonandroid);
            security_status=1;
        }
        else {
            security.setImageResource(R.drawable.doorsecurityoffandroid);
            security_status=0;
        }


        if(curtain_data.equalsIgnoreCase("1"))
        {
            curtain_status=1;
            curtains.setImageResource(R.drawable.curtainopenandroid);
        }
        else {
            curtain_status=0;
            curtains.setImageResource(R.drawable.curtains_closedandroid);
        }

        if(fan_data.equalsIgnoreCase("0"))
        {
            fan_status=0;
            fan.setImageResource(R.drawable.fan_speedandroid0);
        }
        else if(fan_data.equalsIgnoreCase("1")){
            fan_status=1;
            fan.setImageResource(R.drawable.fan_speedandroid1);
        }
        else if(fan_data.equalsIgnoreCase("2")){
            fan_status=2;
            fan.setImageResource(R.drawable.fan_speedandroid2);
        }
        else if(fan_data.equalsIgnoreCase("3")){
            fan_status=3;
            fan.setImageResource(R.drawable.fan_speedandroid3);
        }
        else if(fan_data.equalsIgnoreCase("4")){
            fan_status=4;
            fan.setImageResource(R.drawable.fan_speedandroid4);
        }
        else if(fan_data.equalsIgnoreCase("5")){
            fan_status=5;
            fan.setImageResource(R.drawable.fan_speedandroid5);
        }


        bulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endpoint="lights_control";
                Log.d("inside onclick listener","bubl");
                int status=-1;
                if(bulb_status==1) {
                    status = 0;
                }
                else if(bulb_status==0)
                    status=1;

                Log.d("bulb button clicked",status+"");
                ApplicationRequest obj_applicationrequest = new ApplicationRequest(Controller.this, spinner, Controller.this,"lights_control",status+"");

                /**
                 * Starting to make http request
                 */

                try {
                    spinner.setVisibility(View.VISIBLE);
                    obj_applicationrequest.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    spinner.setVisibility(View.INVISIBLE);
                }



            }

        });


        autoswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                int status = -1;
                String endpoint = "automatic_control";
                Log.d("inside onclick listener", "automatic");

                if (isChecked) {
                    status = 1;

                } else {
                    status = 0;

                }

                Log.d("switch button clicked", status + "");
                ApplicationRequest obj_applicationrequest = new ApplicationRequest(Controller.this, spinner, Controller.this, endpoint, status + "");

                /**
                 * Starting to make http request
                 */

                try {
                    spinner.setVisibility(View.VISIBLE);
                    obj_applicationrequest.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    spinner.setVisibility(View.INVISIBLE);
                }


            }
        });

        fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endpoint = "fan_control";
                Log.d("inside onclick listener", "fan");
                int status = -1;
                if (fan_status == 0)
                    status = 1;
                else if (fan_status == 1)
                    status = 2;
                else if (fan_status == 2)
                    status = 3;
                else if (fan_status == 3)
                    status = 4;
                else if (fan_status == 4)
                    status = 5;
                else if (fan_status == 5)
                    status = 0;

                Log.d("fan button clicked", status + "");
                ApplicationRequest obj_applicationrequest = new ApplicationRequest(Controller.this, spinner, Controller.this, endpoint, status + "");

                /**
                 * Starting to make http request
                 */

                try {
                    spinner.setVisibility(View.VISIBLE);
                    obj_applicationrequest.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    spinner.setVisibility(View.INVISIBLE);
                }


            }

        });



        curtains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endpoint="curtain_control";
                Log.d("inside onclick listener","curtain");
                int status=-1;
                if(curtain_status==1) {
                    status = 0;
                }
                else if(curtain_status==0)
                    status=1;

                Log.d("curtain button clicked",status+"");
                ApplicationRequest obj_applicationrequest = new ApplicationRequest(Controller.this, spinner, Controller.this,"curtain_control",status+"");

                /**
                 * Starting to make http request
                 */

                try {
                    spinner.setVisibility(View.VISIBLE);
                    obj_applicationrequest.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    spinner.setVisibility(View.INVISIBLE);
                }



            }

        });

        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endpoint="security_control";
                Log.d("inside onclick listener","curtain");
                int status=-1;
                if(security_status==1) {
                    status = 0;
                }
                else if(security_status==0)
                    status=1;

                Log.d("curtain button clicked",status+"");
                ApplicationRequest obj_applicationrequest = new ApplicationRequest(Controller.this, spinner, Controller.this,endpoint,status+"");

                /**
                 * Starting to make http request
                 */

                try {
                    spinner.setVisibility(View.VISIBLE);
                    obj_applicationrequest.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    spinner.setVisibility(View.INVISIBLE);
                }



            }

        });


    }

    @Override
    public void onResult(int code, int check, String data, String endpoint) {
        if(code == CommonUtilities.Code_Application)
        {
            Log.d("onResult","outside the endpoint ");

            if(endpoint.equalsIgnoreCase("lights_control"))
            {
                Log.d("onResult","inside the endpoint "+endpoint+data);

                if(data.equalsIgnoreCase("1\n")) {
                    bulb.setImageResource(R.drawable.bulb_on_android);
                    bulb_status=1;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult","COntroller");
                }
                else if(data.equalsIgnoreCase("0\n")) {
                    bulb.setImageResource(R.drawable.bulb_off_android);
                    bulb_status=0;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult","COntroller");
                }

            }

            else if(endpoint.equalsIgnoreCase("curtain_control"))
            {
                Log.d("onResult","inside the endpoint "+endpoint+data);

                if(data.equalsIgnoreCase("1\n")) {
                    curtains.setImageResource(R.drawable.curtainopenandroid);
                    curtain_status=1;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult","COntroller");
                }
                else if(data.equalsIgnoreCase("0\n")) {
                    curtains.setImageResource(R.drawable.curtains_closedandroid);
                    curtain_status=0;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult","COntroller");
                }

            }

            else if(endpoint.equalsIgnoreCase("security_control"))
            {
                Log.d("onResult","inside the endpoint "+endpoint+data);

                if(data.equalsIgnoreCase("1\n")) {
                    security.setImageResource(R.drawable.doorsecurityonandroid);
                    security_status=1;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult","COntroller");
                }
                else if(data.equalsIgnoreCase("0\n")) {
                    security.setImageResource(R.drawable.doorsecurityoffandroid);
                    security_status=0;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult","COntroller");
                }

            }


            else if(endpoint.equalsIgnoreCase("automatic_control"))
            {
                Log.d("onResult","inside the endpoint "+endpoint+data);

                if(data.equalsIgnoreCase("1\n")) {
                    autoswitch.setChecked(true);
                    automatic_status=1;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult","COntroller");
                }
                else if(data.equalsIgnoreCase("0\n")) {
                    autoswitch.setChecked(false);
                    automatic_status=0;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult","COntroller");
                }

            }




            else if(endpoint.equalsIgnoreCase("fan_control"))
            {
                Log.d("onResult","inside the endpoint "+endpoint+data);

                if(data.equalsIgnoreCase("0\n")) {
                    fan.setImageResource(R.drawable.fan_speedandroid0);
                    fan_status=0;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult","COntroller");
                }
                else if(data.equalsIgnoreCase("1\n")) {
                    fan.setImageResource(R.drawable.fan_speedandroid1);
                    fan_status=1;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult","COntroller");
                }
                else if(data.equalsIgnoreCase("2\n")) {
                    fan.setImageResource(R.drawable.fan_speedandroid2);
                    fan_status=2;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult","COntroller");
                }
                else if(data.equalsIgnoreCase("3\n")) {
                    fan.setImageResource(R.drawable.fan_speedandroid3);
                    fan_status=3;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult","COntroller");
                }
                else if(data.equalsIgnoreCase("4\n")) {
                    fan.setImageResource(R.drawable.fan_speedandroid4);
                    fan_status=4;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult","COntroller");
                }
                else if(data.equalsIgnoreCase("5\n")) {
                    fan.setImageResource(R.drawable.fan_speedandroid5);
                    fan_status=5;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult","COntroller");
                }

            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
