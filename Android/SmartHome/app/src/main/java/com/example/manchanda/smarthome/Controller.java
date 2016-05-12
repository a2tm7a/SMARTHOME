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
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by manchanda on 21/1/16.
 */


public class Controller extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ResultListener {

    ImageButton bulb1, bulb2, bulb3, curtains, security, away_mode,morning_mode,sleep_mode, theater_mode;
    ProgressBar spinner;
    Switch autoswitch;
    Button automatedFan;
    SeekBar fanSeekBar;
    TextView fanSpeed_tv;



    int bulb_status1;
    int bulb_status2;
    int curtain_status;
    int fan_status;
    int automatic_status;
    int security_status;


    String bulb_data1,bulb_data2;
    String curtain_data;
    String fan_data;
    String security_data;
    String automatic_data;
    String temperature_data;
    String humidity_data;
    String co2emission_data;
    String users_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller);

        fanSpeed_tv=(TextView)findViewById(R.id.fanSpeed);
        bulb1=(ImageButton)findViewById(R.id.image_bulb1);
        bulb2=(ImageButton)findViewById(R.id.image_bulb2);
        automatedFan=(Button)findViewById(R.id.automatedFan);
        curtains=(ImageButton)findViewById(R.id.image_curtains);
        fanSeekBar=(SeekBar)findViewById(R.id.seekBarFan);
        spinner=(ProgressBar)findViewById(R.id.progressBar_controller);
        autoswitch=(Switch)findViewById(R.id.switch_auto_mode);
        security=(ImageButton)findViewById(R.id.image_security);
        away_mode=(ImageButton)findViewById(R.id.away_mode);
        theater_mode=(ImageButton)findViewById(R.id.theater_mode);
        morning_mode=(ImageButton)findViewById(R.id.morning_mode);
        sleep_mode=(ImageButton)findViewById(R.id.sleep_mode);


        bulb_data1 = getIntent().getExtras().getString("lights1");
        bulb_data2 = getIntent().getExtras().getString("lights2");
        curtain_data = getIntent().getExtras().getString("curtains");
        fan_data = getIntent().getExtras().getString("fan");
        automatic_data = getIntent().getExtras().getString("automatic");
        security_data = getIntent().getExtras().getString("security");
        humidity_data = getIntent().getExtras().getString("humidity");
        temperature_data = getIntent().getExtras().getString("temperature");
        co2emission_data = getIntent().getExtras().getString("co2emission");
        users_data = getIntent().getExtras().getString("connected_user");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String endpoint="sos";
                Log.d("inside onclick listener", "floatingactionbutton");
                int status=3;
                double longitude;
                double latitude;
                GPSTracker gps;
                gps = new GPSTracker(Controller.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                    SOSRequest obj_SOSrequest = new SOSRequest(Controller.this, spinner, Controller.this,"sos",latitude,longitude);

                    /**
                     * Starting to make http request
                     */

                    try {
                        spinner.setVisibility(View.VISIBLE);
                        obj_SOSrequest.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                        spinner.setVisibility(View.INVISIBLE);
                    }

                }else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
                Log.d("SOS Clickde", status + "");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        SubMenu topChannelMenu = m.addSubMenu("Sensor Readings");

        topChannelMenu.add("Temperature : "+temperature_data+" C");
        topChannelMenu.add("Humidity : "+humidity_data+"%");
        topChannelMenu.add("CO2 Emission : "+co2emission_data+" ppm");
        topChannelMenu.add("Online Users : " + users_data);


        MenuItem mi = m.getItem(m.size() - 1);
        mi.setTitle(mi.getTitle());
        navigationView.setNavigationItemSelectedListener(this);

        /**
         *Get the params from data and acc to it set the image
         */





        Log.d(bulb_data1,curtain_data);

        if(bulb_data1.equalsIgnoreCase("1"))
        {
            bulb1.setImageResource(R.drawable.bulbandroidlefton);
            bulb_status1=1;
        }
        else {
            bulb1.setImageResource(R.drawable.bulboffandroidleft1);
            bulb_status1=0;
        }



        if(bulb_data2.equalsIgnoreCase("1"))
        {
            bulb2.setImageResource(R.drawable.bulbandroirighton);
            bulb_status2=1;
        }
        else {
            bulb2.setImageResource(R.drawable.bulboffandroidright);
            bulb_status2=0;
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
            fanSeekBar.setProgress(0);
            fanSpeed_tv.setText("Fan Speed: 0");
        }
        else if(fan_data.equalsIgnoreCase("1")){
            fan_status=1;
            fanSeekBar.setProgress(1);
            fanSpeed_tv.setText("Fan Speed: 1");
        }
        else if(fan_data.equalsIgnoreCase("2")){
            fan_status=2;
            fanSeekBar.setProgress(2);
            fanSpeed_tv.setText("Fan Speed: 2");
        }
        else if(fan_data.equalsIgnoreCase("3")){
            fan_status=3;
            fanSeekBar.setProgress(3);
            fanSpeed_tv.setText("Fan Speed: 3");
        }
        else if(fan_data.equalsIgnoreCase("4")){
            fan_status=4;
            fanSeekBar.setProgress(4);
            fanSpeed_tv.setText("Fan Speed: 4");
        }
        else if(fan_data.equalsIgnoreCase("5")){
            fan_status=5;
            fanSeekBar.setProgress(5);
            fanSpeed_tv.setText("Fan Speed: 5");
        }

        automatedFan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endpoint="automatedFan";
                Log.d("inside onclick listener","automatedFan");
                int status=0;   //no use only because there would be no use of other function
                ApplicationRequest obj_applicationrequest = new ApplicationRequest(Controller.this, spinner, Controller.this,endpoint,status+"");
                try {
                    spinner.setVisibility(View.VISIBLE);
                    obj_applicationrequest.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    spinner.setVisibility(View.INVISIBLE);
                }

            }
        });


        bulb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endpoint="lights_control1";
                Log.d("inside onclick listener","bubl1");
                int status=-1;
                if(bulb_status1==1) {
                    status = 0;
                }
                else if(bulb_status1==0)
                    status=1;

                Log.d("bulb1 button clicked",status+"");
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

        bulb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endpoint="lights_control2";
                Log.d("inside onclick listener","bubl2");
                int status=-1;
                if(bulb_status2==1) {
                    status = 0;
                }
                else if(bulb_status2==0)
                    status=1;

                Log.d("bulb2 button clicked",status+"");
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



        away_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endpoint="away_mode_control";
                Log.d("inside onclick listener","away_mode");
                int status=1;

                Log.d("away_mode button clicked",status+"");
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

        morning_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endpoint="morning_mode_control";
                Log.d("inside onclick listener","morning_mode_control");
                int status=1;

                Log.d("morning_mode button clicked",status+"");
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


        theater_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endpoint="theater_mode_control";
                Log.d("inside onclick listener","theater_mode_control");
                int status=1;

                Log.d("theater_mode button clicked",status+"");
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



        sleep_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endpoint="sleep_mode_control";
                Log.d("inside onclick listener","sleep_mode_control");
                int status=1;

                Log.d("Sleep_mode button clicked",status+"");
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

        /*fan.setOnClickListener(new View.OnClickListener() {
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

          /*      try {
                    spinner.setVisibility(View.VISIBLE);
                    obj_applicationrequest.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    spinner.setVisibility(View.INVISIBLE);
                }


            }

        });*/
        fanSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
                fanSpeed_tv.setText("Fan Speed: "+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(Controller.this,"seek bar progress:"+progressChanged,
                  //      Toast.LENGTH_SHORT).show();
                String endpoint="fan_control";
                Log.d("fan button clicked", progressChanged + "");
                ApplicationRequest obj_applicationrequest = new ApplicationRequest(Controller.this, spinner, Controller.this, endpoint, progressChanged + "");

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
                String endpoint = "curtain_control";
                Log.d("inside onclick listener", "curtain");
                int status = -1;
                if (curtain_status == 1) {
                    status = 0;
                } else if (curtain_status == 0)
                    status = 1;

                Log.d("curtain button clicked", status + "");
                ApplicationRequest obj_applicationrequest = new ApplicationRequest(Controller.this, spinner, Controller.this, "curtain_control", status + "");

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
        if(code == CommonUtilities.Code_Application) {
            Log.d("onResult", "outside the endpoint ");

            if (endpoint.equalsIgnoreCase("lights_control1")) {
                Log.d("onResult", "inside the endpoint " + endpoint + data);

                if (data.equalsIgnoreCase("1\n")) {
                    bulb1.setImageResource(R.drawable.bulbandroidlefton);
                    bulb_status1 = 1;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult", "COntroller");
                } else if (data.equalsIgnoreCase("0\n")) {
                    bulb1.setImageResource(R.drawable.bulboffandroidleft1);
                    bulb_status1 = 0;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult", "COntroller");
                }

            } else if (endpoint.equalsIgnoreCase("lights_control2")) {
                Log.d("onResult", "inside the endpoint " + endpoint + data);

                if (data.equalsIgnoreCase("1\n")) {
                    bulb2.setImageResource(R.drawable.bulbandroirighton);
                    bulb_status2 = 1;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult", "COntroller");
                } else if (data.equalsIgnoreCase("0\n")) {
                    bulb2.setImageResource(R.drawable.bulboffandroidright);
                    bulb_status2 = 0;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult", "COntroller");
                }

            } else if (endpoint.equalsIgnoreCase("curtain_control")) {
                Log.d("onResult", "inside the endpoint " + endpoint + data);

                if (data.equalsIgnoreCase("1\n")) {
                    curtains.setImageResource(R.drawable.curtainopenandroid);
                    curtain_status = 1;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult", "COntroller");
                } else if (data.equalsIgnoreCase("0\n")) {
                    curtains.setImageResource(R.drawable.curtains_closedandroid);
                    curtain_status = 0;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult", "COntroller");
                }

            } else if (endpoint.equalsIgnoreCase("security_control")) {
                Log.d("onResult", "inside the endpoint " + endpoint + data);

                if (data.equalsIgnoreCase("1\n")) {
                    security.setImageResource(R.drawable.doorsecurityonandroid);
                    security_status = 1;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult", "COntroller");
                } else if (data.equalsIgnoreCase("0\n")) {
                    security.setImageResource(R.drawable.doorsecurityoffandroid);
                    security_status = 0;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult", "COntroller");
                }

            } else if (endpoint.equalsIgnoreCase("automatic_control")) {
                Log.d("onResult", "inside the endpoint " + endpoint + data);

                if (data.equalsIgnoreCase("1\n")) {
                    autoswitch.setChecked(true);
                    automatic_status = 1;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult", "COntroller");
                } else if (data.equalsIgnoreCase("0\n")) {
                    autoswitch.setChecked(false);
                    automatic_status = 0;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult", "COntroller");
                }

            } else if (endpoint.equalsIgnoreCase("away_mode_control")) {
                Log.d("onResult", "inside the endpoint " + endpoint + data);

                security.setImageResource(R.drawable.doorsecurityonandroid);
                security_status = 1;

                curtains.setImageResource(R.drawable.curtains_closedandroid);
                curtain_status = 0;

                bulb1.setImageResource(R.drawable.bulboffandroidleft1);
                bulb_status1 = 0;
                bulb2.setImageResource(R.drawable.bulboffandroidright);
                bulb_status2 = 0;

                fanSeekBar.setProgress(0);
                fanSpeed_tv.setText("Fan Speed: 0");
                fan_status = 0;

                spinner.setVisibility(View.INVISIBLE);
                Log.d("Inside onResult", "COntroller");

                Toast.makeText(getApplicationContext(), "Away Mode Activaed", Toast.LENGTH_LONG).show();

            } else if (endpoint.equalsIgnoreCase("morning_mode_control")) {
                Log.d("onResult", "inside the endpoint " + endpoint + data);

                security.setImageResource(R.drawable.doorsecurityoffandroid);
                security_status = 0;

                curtains.setImageResource(R.drawable.curtainopenandroid);
                curtain_status = 1;

                bulb1.setImageResource(R.drawable.bulbandroidlefton);
                bulb_status1 = 1;
                bulb2.setImageResource(R.drawable.bulbandroirighton);
                bulb_status2 = 1;

                spinner.setVisibility(View.INVISIBLE);
                Log.d("Inside onResult", "COntroller");

                Toast.makeText(getApplicationContext(), "Morning Mode Activaed", Toast.LENGTH_LONG).show();

            } else if (endpoint.equalsIgnoreCase("sleep_mode_control")) {
                Log.d("onResult", "inside the endpoint " + endpoint + data);

                security.setImageResource(R.drawable.doorsecurityonandroid);
                security_status = 1;

                curtains.setImageResource(R.drawable.curtains_closedandroid);
                curtain_status = 0;

                bulb1.setImageResource(R.drawable.bulboffandroidleft1);
                bulb_status1 = 0;
                bulb2.setImageResource(R.drawable.bulboffandroidright);
                bulb_status2 = 0;

                spinner.setVisibility(View.INVISIBLE);
                Log.d("Inside onResult", "COntroller");

                Toast.makeText(getApplicationContext(), "Sleep Mode Activaed", Toast.LENGTH_LONG).show();

            } else if (endpoint.equalsIgnoreCase("theater_mode_control")) {
                Log.d("onResult", "inside the endpoint " + endpoint + data);

                curtains.setImageResource(R.drawable.curtains_closedandroid);
                curtain_status = 0;

                bulb1.setImageResource(R.drawable.bulboffandroidleft1);
                bulb_status1 = 0;
                bulb2.setImageResource(R.drawable.bulboffandroidright);
                bulb_status2 = 0;

                spinner.setVisibility(View.INVISIBLE);
                Log.d("Inside onResult", "COntroller");

                Toast.makeText(getApplicationContext(), "Theater Mode Activaed", Toast.LENGTH_LONG).show();

            } else if (endpoint.equalsIgnoreCase("fan_control")) {
                Log.d("onResult", "inside the endpoint " + endpoint + data);

                if (data.equalsIgnoreCase("0\n")) {
                    fanSeekBar.setProgress(0);
                    fan_status = 0;
                    fanSpeed_tv.setText("Fan Speed: 0");
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult", "COntroller");
                } else if (data.equalsIgnoreCase("1\n")) {
                    fanSeekBar.setProgress(1);
                    fan_status = 1;
                    fanSpeed_tv.setText("Fan Speed: 1");
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult", "COntroller");
                } else if (data.equalsIgnoreCase("2\n")) {
                    fanSeekBar.setProgress(2);
                    fan_status = 2;
                    fanSpeed_tv.setText("Fan Speed: 2");
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult", "COntroller");
                } else if (data.equalsIgnoreCase("3\n")) {
                    fanSeekBar.setProgress(3);
                    fan_status = 3;
                    fanSpeed_tv.setText("Fan Speed: 3");
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult", "COntroller");
                } else if (data.equalsIgnoreCase("4\n")) {
                    fanSeekBar.setProgress(4);
                    fan_status = 4;
                    spinner.setVisibility(View.INVISIBLE);
                    fanSpeed_tv.setText("Fan Speed: 4");
                    Log.d("Inside onResult", "COntroller");
                } else if (data.equalsIgnoreCase("5\n")) {
                    fanSeekBar.setProgress(5);
                    fan_status = 5;
                    fanSpeed_tv.setText("Fan Speed: 5");
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult", "COntroller");
                }

            }
            else if (endpoint.equalsIgnoreCase("automatedFan")) {
                Log.d("onResult", "inside the endpoint " + endpoint + data);

                if (data.equalsIgnoreCase("0\n")) {
                    fanSeekBar.setProgress(0);
                    fan_status = 0;
                    fanSpeed_tv.setText("Fan Speed: 0");
                    spinner.setVisibility(View.INVISIBLE);

                } else if (data.equalsIgnoreCase("1\n")) {
                    fanSeekBar.setProgress(1);
                    fan_status = 1;
                    fanSpeed_tv.setText("Fan Speed: 1");
                    spinner.setVisibility(View.INVISIBLE);

                } else if (data.equalsIgnoreCase("2\n")) {
                    fanSeekBar.setProgress(2);
                    fan_status = 2;
                    fanSpeed_tv.setText("Fan Speed: 2");
                    spinner.setVisibility(View.INVISIBLE);

                } else if (data.equalsIgnoreCase("3\n")) {
                    fanSeekBar.setProgress(3);
                    fan_status = 3;
                    fanSpeed_tv.setText("Fan Speed: 3");
                    spinner.setVisibility(View.INVISIBLE);

                } else if (data.equalsIgnoreCase("4\n")) {
                    fanSeekBar.setProgress(4);
                    fan_status = 4;
                    fanSpeed_tv.setText("Fan Speed: 4");
                    spinner.setVisibility(View.INVISIBLE);

                } else if (data.equalsIgnoreCase("5\n")) {
                    fanSeekBar.setProgress(5);
                    fan_status = 5;
                    fanSpeed_tv.setText("Fan Speed: 5");
                    spinner.setVisibility(View.INVISIBLE);

                }
                Log.d("Inside onResult", "COntroller");

            }
        }
        if(code==CommonUtilities.Code_SOS)
        {
            Log.d("onResult", "inside the endpoint " + endpoint + data);

            spinner.setVisibility(View.INVISIBLE);
            Log.d("Inside onResult", "COntroller");

            Toast.makeText(getApplicationContext(),"Request for Help send",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

      /*  if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
