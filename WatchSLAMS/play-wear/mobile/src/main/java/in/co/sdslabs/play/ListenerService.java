package in.co.sdslabs.play;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Akshay on 12-03-2015.
 */
public class ListenerService extends WearableListenerService {

    private VolleySingleton mVolleySingleton;
    private RequestQueue mRequestQueue;
    private static final String baseUrl = "http://192.168.0.162:9000/";
    private static final String toggleLights = baseUrl + "watchLights";
    private static final String fan = baseUrl + "watchFan";
    private static final String increase = baseUrl + "volume/increase";
    private static final String toggleSecurity = baseUrl + "watchSecurity";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(ListenerService.class.getSimpleName(), "WEAR create");

        mVolleySingleton = VolleySingleton.getInstance();
        mRequestQueue = mVolleySingleton.getRequestQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(ListenerService.class.getSimpleName(), "WEAR destroy");
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        Log.i(ListenerService.class.getSimpleName(), "WEAR Data changed ");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.i(ListenerService.class.getSimpleName(), "Message Received ");
        /*new JSONAsyncTask().execute("");*/
        switch (messageEvent.getPath()) {
            case "lights":
                sendPlayRequest(toggleLights);
                break;
            case "fan":
                sendPlayRequest(fan);
                break;
            case "increase":
                sendPlayRequest(increase);
                break;
            case "security":
                sendPlayRequest(toggleSecurity);
                break;
        }
        showToast(messageEvent.getPath());
    }

    public void sendPlayRequest(final String url) {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        Log.d("volley response", response);
                        switch (url) {
                            case toggleLights:
                                try {
                                    JSONObject json = new JSONObject(response);
                                    //
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case fan:

                                break;
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });

        mRequestQueue.add(stringRequest);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

/*    class JSONAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute () {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground (String... urls) {
            String data = "123";
            try {

                Log.i("in bg", "Async Task Called");
                URL url = new URL("http://192.168.208.116:8000/kill");
                URI website = null;
                try {
                    website = new URI(url.getProtocol(), url.getUserInfo(),
                            url.getHost(), url.getPort(), url.getPath(),
                            url.getQuery(), url.getRef());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                HttpGet httppost = new HttpGet();
                httppost.setURI(website);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                //Toast.makeText(getApplicationContext(), website.toString(), Toast.LENGTH_LONG);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity()
                        .getContent()));
                StringBuffer sb = new StringBuffer("");
                String l = "";
                String nl = System.getProperty("line.separator");
                while ((l = in.readLine()) != null) {
                    sb.append(l + nl);
                }
                in.close();
                data = sb.toString();
                Log.i("RETURNED", data);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        protected void onPostExecute (String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }*/
}
