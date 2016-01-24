package com.example.manchanda.smarthome;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static com.example.manchanda.smarthome.CommonUtilities.APPLICATION_URL;
import static com.example.manchanda.smarthome.CommonUtilities.DATA_URL;

/**
 * Created by manchanda on 24/1/16.
 */
public class ApplicationRequest extends AsyncTask<Void,Void,Void> {

    Context context;
    ProgressBar spinner;
    ResultListener listener;
    String data;
    String status;
    String Interface="Android App";
    String endpoint;

    public ApplicationRequest(Context context, ProgressBar spinner, ResultListener listener, String endpoint, String status){
        this.context=context;
        this.status=status;
        this.endpoint=endpoint;
        this.spinner=spinner;
        this.listener=listener;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }


    @Override
    protected Void doInBackground(Void... params) {
        Log.d("ApplicationRequest", "Inside doInBackgroud");
        try {


            String data_url= APPLICATION_URL(MainActivity.IP_Address, endpoint);


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data_url);
            Log.d("applicationurl=" , data_url) ;

            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("status", status));
            nameValuePairs.add(new BasicNameValuePair("Interface", Interface));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            Log.d("Application Request", "HTTP");
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String line = "0";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            reader.close();

            Log.d("Data Request", "Fetching result");

            //Data Received
            data = sb.toString() ;
            Log.d("Data",data) ;

        }catch(UnknownHostException e)
        {
            e.printStackTrace();
            data="Error";
            Log.d("UnknownHostException",e.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            data="Error";
            Log.d("inside error of First Fetch Request",e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d("Inside ON post Execute of Application Request","successful");
        //dialog.dismiss();
        Log.d("Application","OnPostExecute");

        listener.onResult(CommonUtilities.Code_Application,0, data,endpoint);
    }
}
