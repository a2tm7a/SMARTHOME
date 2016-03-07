package com.example.manchanda.smarthome;

import android.content.Context;

import com.google.android.gcm.GCMRegistrar;

/**
 * Created by amit on 23/3/15.
 */
public class GCM {
    Context context;
    public GCM(Context context){
        this.context=context;
    }
    public String GetGCMId(){
        GCMRegistrar.checkDevice(context);

        GCMRegistrar.checkManifest(context);

        final String regId=GCMRegistrar.getRegistrationId(context);

        return regId;
    }




}
