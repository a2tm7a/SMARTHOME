package com.example.manchanda.smarthome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


import static com.example.manchanda.smarthome.CommonUtilities.EXTRA_MESSAGE;

/**
 * Created by amit on 30/3/15.
 */
public class Broadcastreceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
        int check= intent.getIntExtra("check",0);


        Log.d("inside BroadcastReceiver", newMessage + check);
        Toast.makeText(context, "Broad Cast Reciever " + newMessage + " check : " + check, Toast.LENGTH_LONG).show();



    }
}
