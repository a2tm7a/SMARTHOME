package com.example.manchanda.smarthome;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Created by amit on 26/3/15.
 */
public class ServerUtilities extends CommonUtilities {

    private  static String gcm_id="gcm_regId";
    private  static String uname="username";
    private static String register="register";
    private static String unsuccessful="unsuccessful";
    private static String successful="successful";


    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();


    static void register(final Context context,final String regId) {
        Log.i("Server Utilities", "registering device (regId = " + regId + ")");

        String serverUrl = GCM_REGISTER_URL(MainActivity.IP_Address);

        Map<String, String> params = new HashMap<String, String>();

        //params.put("session_id",session_id);
        params.put(gcm_id, regId);
/*        params.put(uname, username);

        params.put(register,"1");*/


        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);

        /***** Once GCM returns a registration id, we need to register on our server
         As the server might be down, we will retry it a couple
         times.*****/


        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d("Inside Server Utilities", "Attempt #" + i + " to register");
            try {


                boolean registered = post(serverUrl, params);
                if(registered==true)
                {
                    GCMRegistrar.setRegisteredOnServer(context, true);                      //
                    Log.d("post:register", "registered = true");

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = preferences.edit() ;
                    editor.putString("registered", "1");
                    editor.commit();
                    Log.d("Registed the phone","Server Utilites");

                    CommonUtilities.displayMessage(context,"registration successful",100);

                    return;
                }


            } catch (Exception e) {

                Log.e("ServerUtilities", "Failed to register on attempt " + i + ":" + e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }

                try {
                    Log.d("ServerUtilites", "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);

                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d("ServerUtilities", "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();

                    return;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        Log.d("Before display message: ", "in register: ServerUtilities");


        CommonUtilities.displayMessage(context,unsuccessful,0);

    }


    static void unregister(final Context context, final String regId) {
        Log.i("Server Utilities", "unregistering device (regId = " + regId + ")");
        String serverUrl = GCM_REGISTER_URL(MainActivity.IP_Address);
        Map<String, String> params = new HashMap<String, String>();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String username = preferences.getString("username","");

        params.put(gcm_id, regId);
        params.put(username,username);
        params.put(register,"0");

        try {
           boolean result = post(serverUrl, params);
            if(result == true)
            {
                GCMRegistrar.setRegisteredOnServer(context, false);
                Log.d("post:unregister", "unregistered = true");

            }
            else
                Log.d("unregister:post", "Device Cannot be removed");


        } catch (Exception e) {

            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.

            Log.d("unregister:post", "Caught in exception of ser utilities");

        }
    }



    private static boolean post(String endpoint, Map<String, String> params)
    {

        Log.d("Inside post", endpoint);
        try {

            String serverUrl = GCM_REGISTER_URL(MainActivity.IP_Address);;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(serverUrl);

            String gcm_id=params.get("gcm_regId");
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("gcm_regId", gcm_id));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            Log.d("Login Check", "HTTP");
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String line = "0";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            reader.close();

            String result = sb.toString().trim();

            int success= Integer.parseInt(result);

            Log.d("Post Result", success + "");



            if(success==0)
            {

                return true;
            }
            else
            {
                return false;


            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("inside error of server utilities", e.toString());
        }

       return false;

    }


}
