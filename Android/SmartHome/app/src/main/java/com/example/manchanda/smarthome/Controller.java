package com.example.manchanda.smarthome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by manchanda on 21/1/16.
 */


public class Controller extends AppCompatActivity implements ResultListener {

    ImageButton bulb, curtains;
    ProgressBar spinner;

    int bulb_status;
    int curtain_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller);

        /**
         *Get the params from data and acc to it set the image
         */

        bulb=(ImageButton)findViewById(R.id.image_bulb);
        curtains=(ImageButton)findViewById(R.id.image_curtains);
        spinner=(ProgressBar)findViewById(R.id.progressBar_controller);

        final String bulb_data = getIntent().getExtras().getString("lights");
        String curtain_data = getIntent().getExtras().getString("curtains");
        Log.d(bulb_data,curtain_data);

        if(bulb_data.equalsIgnoreCase("1"))
        {
            bulb.setImageResource(R.drawable.ic_menu_send);
            bulb_status=1;
        }
        else {
            bulb.setImageResource(R.drawable.ic_menu_share);
            bulb_status=0;
        }
        if(curtain_data.equalsIgnoreCase("1"))
        {
            curtain_status=1;
            curtains.setImageResource(R.drawable.ic_menu_send);
        }
        else {
            curtain_status=0;
            curtains.setImageResource(R.drawable.ic_menu_share);
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
                    bulb.setImageResource(R.drawable.ic_menu_send);
                    bulb_status=1;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult","COntroller");
                }
                else if(data.equalsIgnoreCase("0\n")) {
                    bulb.setImageResource(R.drawable.ic_menu_share);
                    bulb_status=0;
                    spinner.setVisibility(View.INVISIBLE);
                    Log.d("Inside onResult","COntroller");
                }

            }
        }
    }
}
