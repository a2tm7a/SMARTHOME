package com.example.manchanda.smarthome;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
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

/**
 * Created by manchanda on 28/1/16.
 */
public class SOSRequest extends AsyncTask<Void,Void,Void> {

    Context context;
    ProgressBar spinner;
    ResultListener listener;
    String data;
    String status;
    String Interface="Android App";
    String endpoint;
    double latitude;
    double longitude;

    public SOSRequest(Context context, ProgressBar spinner, ResultListener listener, String endpoint, double latitude, double longitude){
        this.context=context;
        this.endpoint=endpoint;
        this.latitude=latitude;
        this.longitude=longitude;
        this.listener=listener;
        this.spinner=spinner;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }


    @Override
    protected Void doInBackground(Void... params) {
        Log.d("SOSRequest", "Inside doInBackgroud");
        try {


            String data_url = APPLICATION_URL(MainActivity.IP_Address, endpoint);


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data_url);
            Log.d("sosurl=" , data_url) ;



            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("longitude", longitude+""));
            nameValuePairs.add(new BasicNameValuePair("latitude", latitude+""));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            Log.d("SOS Request", "HTTP");
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
        Log.d("Inside ON post Execute of SOS Request","successful");
        //dialog.dismiss();
        Log.d("SOS","OnPostExecute");

        listener.onResult(CommonUtilities.Code_SOS,0, data,endpoint);
    }
}
