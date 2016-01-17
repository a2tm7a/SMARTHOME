package com.example.manchanda.smarthome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static com.example.manchanda.smarthome.CommonUtilities.Code_LoginRequest;
import static com.example.manchanda.smarthome.CommonUtilities.LOGIN_URL;


/**
 * Created by amit on 23/3/15.
 */
public class LoginRequest extends AsyncTask<Void,Void,Void> {
    private Context context;
    private String username,password;

    /**
     * -2 if Error due to UnknownHostException occurs
     * -1 if Other Error occurs
     * 0 if no error
     */
    private int check_login_error=0;

    ProgressDialog dialog;

    ProgressBar spinner;
    ResultListener listener;
    String result;

    public LoginRequest(Context context, String username, String password, ProgressBar spinner, ResultListener listener)
    {
        this.context=context;
        this.password=password;
        this.username=username;
        this.spinner=spinner;
        this.listener=listener;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... params) {


        Log.d("Login Check", "Inside doInBackgroud");
        try {

            String login= LOGIN_URL(MainActivity.IP_Address);

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(login);
            Log.d("login",login);


            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
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

            //Data received
            result = sb.toString().trim();



            Log.d("Login Check", result);


            //Successful login
            if(result.equalsIgnoreCase("1"))
            {
                Log.d("Successful","login");
                /*
                JSONObject data = new JSONObject(result);

                String name=data.getString("name");
                String session_id=data.getString("session_id");

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = preferences.edit() ;
                editor.putString("username", username);
                editor.putString("name", name);
                editor.putString("session_id", session_id);
                editor.commit();


                Log.d("Saved in shared preference", "username,name,session_id") ;


                GCMRegistrar.register(context,PROJECT_ID);

                Log.d("GCM ID", GCMRegistrar.isRegisteredOnServer(context) + "");
                Log.d("GCM ID", GCMRegistrar.getRegistrationId(context));
                */
                check_login_error = 0;
            }
            else
            {
                check_login_error=-1;
                /*
                success_login=0;
                Log.d("Login", success_login + "unsuccessful");

                CommonUtilities.displayMessage(context,"unsuccessful",Broadcast_JustToast);
                */

            }
        }catch(UnknownHostException e)
        {
            e.printStackTrace();
            Log.d("UnknownHostException", e.toString());
            check_login_error=-3;

        }catch (Exception e) {
            e.printStackTrace();
            Log.d("inside error of login request", e.toString());
            check_login_error=-2;

          //  spinner.setVisibility(View.INVISIBLE);
          //  CommonUtilities.displayMessage(context,"Error during Login",Broadcast_JustToast);


        }
        return null;

    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d("postExecute of LoginRequest", "success");
        spinner.setVisibility(View.INVISIBLE);
        if(check_login_error!=0)
        {
            listener.onResult(Code_LoginRequest,check_login_error,"Error");
        }
        else
        {
            listener.onResult(Code_LoginRequest,check_login_error,"Successful Login");
        }

    }
}
