package com.example.manchanda.smarthome;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ResultListener {


    EditText txtUsername;
    EditText txtPassword;

    SharedPreferences sharedpreferences;

    static String IP_Address=null;

    Button btnLogin;

    ImageView img;
    ProgressBar spinner;

    LinearLayout linearLayout;

    Boolean login_btn_clicked=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUsername=(EditText)findViewById(R.id.etUsername);
        txtPassword=(EditText)findViewById(R.id.etPassword);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        img=(ImageView)findViewById(R.id.logologin);
        linearLayout=(LinearLayout)findViewById(R.id.linear_layout_username_password);
        spinner=(ProgressBar)findViewById(R.id.progressBar);


        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        //int width = metrics.widthPixels;
        int height_screen = metrics.heightPixels;

        int width = FrameLayout.LayoutParams.MATCH_PARENT;
        int height = height_screen;
        linearLayout.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        linearLayout.animate().alpha(0.0f).setDuration(0);
        final float translate_img=-height/4;

        /**
         * Toolbar
         */

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String login_check = preferences.getString("login","");

        /**
         * JUST TO GO TO CONTROLLER CLASS FOR TESTING
         */
        /*if(!login_check.equalsIgnoreCase("1"))
        {
            Intent i = new Intent(getApplicationContext(),Controller.class);
            startActivity(i);
            this.finish();
        }*/

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
         * Get the IP
         */

        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promtView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promtView);

        final EditText et_ipadress = (EditText) promtView.findViewById(R.id.input_dialog_et_ip);


        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        IP_Address=et_ipadress.getText().toString();
                        Toast.makeText(getApplicationContext(),et_ipadress.getText(),Toast.LENGTH_LONG).show();
                        transition(img, translate_img);
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getApplicationContext(),"Bye bye",Toast.LENGTH_LONG).show();
                                dialog.cancel();
                                System.exit(0);
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (login_btn_clicked == false) {
                        login_btn_clicked = true;
                        String username = txtUsername.getText().toString();
                        String password = txtPassword.getText().toString();

                        if (username.trim().length() > 0 && password.trim().length() > 0) {

                            LoginRequest obj_loginrequest = new LoginRequest(MainActivity.this, username, password, spinner, MainActivity.this);

                            /**
                             * Starting to make http request
                             */

                            try {
                                spinner.setVisibility(View.VISIBLE);
                                obj_loginrequest.execute();
                            } catch (Exception e) {
                                e.printStackTrace();
                                spinner.setVisibility(View.INVISIBLE);
                                login_btn_clicked = false;
                            }

                        } else
                            Toast.makeText(MainActivity.this, "Enter correct username and password", Toast.LENGTH_LONG).show();
                        login_btn_clicked = false;
                    } else {
                        Log.i("login button clicked once : ", "waiting for login check");
                    }
                }
        });
    }



    /**
     * Transition
     */

    void transition(ImageView img, float transition_length)
    {
        img.animate().translationY(transition_length).setDuration(3000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                linearLayout.animate().alpha(1.0f).setDuration(1000);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
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

    @Override
    public void onResult(int code, int check, String data, String endpoint) {
        if(code == CommonUtilities.Code_LoginRequest)
        {
            if(check == 0)
            {
                Log.v("Login","Ending the login activity");
                Toast.makeText(getApplicationContext(),"Successfully Login",Toast.LENGTH_LONG).show();



                DataRequest obj_datarequest = new DataRequest(MainActivity.this, spinner, MainActivity.this);

                /**
                 * Starting to make http request
                 */

                try {
                    spinner.setVisibility(View.VISIBLE);
                    obj_datarequest.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    spinner.setVisibility(View.INVISIBLE);
                }





            }
            else
                Toast.makeText(getApplicationContext(),"UNSuccessfully Login",Toast.LENGTH_LONG).show();

        }
        else if (code == CommonUtilities.Code_Data)
        {
            /**
             * Send Data with it
             */
            try {
                Intent i = new Intent(getApplicationContext(),Controller.class);
                JSONObject jsonObject = new JSONObject(data);
                i.putExtra("lights", jsonObject.getString("lights"));
                i.putExtra("security", jsonObject.getString("security"));
                i.putExtra("fan", jsonObject.getString("fan"));
                i.putExtra("automatic", jsonObject.getString("automatic"));
                i.putExtra("curtains", jsonObject.getString("curtains"));
                startActivity(i);
                this.finish();
                Log.d("lights",jsonObject.getString("lights"));
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error try again",Toast.LENGTH_LONG).show();
            }

        }
    }
}
