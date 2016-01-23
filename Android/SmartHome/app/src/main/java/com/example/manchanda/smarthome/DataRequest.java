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

import static com.example.manchanda.smarthome.CommonUtilities.DATA_URL;


/**
 * Created by manchanda on 22/1/16.
 */
public class DataRequest extends AsyncTask<Void,Void,Void> {

    Context context;
    ProgressBar spinner;
    ResultListener listener;
    String data;

    public DataRequest(Context context, ProgressBar spinner, ResultListener listener){
        this.context=context;

        this.spinner=spinner;
        this.listener=listener;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }


    @Override
    protected Void doInBackground(Void... params) {
        Log.d("DataRequest", "Inside doInBackgroud");
        try {


            String data_url= DATA_URL(MainActivity.IP_Address);


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data_url);
            Log.d("dataurl=" , data_url) ;


            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            Log.d("First Fetch Request", "HTTP");
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
        Log.d("Inside ON post Execute of First Fetch Request","successful");
        //dialog.dismiss();
        Log.d("FirstFetch","OnPostExecute");

        listener.onResult(CommonUtilities.Code_Data,0, data);
    }
}
