/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.co.sdslabs.play;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataApi.DataItemResult;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageApi.SendMessageResult;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;


/**
 * Receives its own events using a listener API designed for foreground activities. Updates a data
 * item every second while it is open. Also allows user to take a photo and send that as an asset to
 * the paired wearable.
 */
public class MainActivity extends Activity implements DataApi.DataListener,
        MessageApi.MessageListener, NodeApi.NodeListener, ConnectionCallbacks,
        OnConnectionFailedListener {

    private static final String TAG = "MainActivity";

    /**
     * Request code for launching the Intent to resolve Google Play services errors.
     */
    private static final int REQUEST_RESOLVE_ERROR = 1000;

    private static final String START_ACTIVITY_PATH = "/start-activity";

    // keys required for data transfer
    private static final String CURRENT_TRACK = "/current";
    private static final String NEXT_TRACK = "/next";
    private static final String track_title = "title";
    private static final String artist = "artist";

    private static final String baseUrl = "http://192.168.0.162:9000/";
    private static final String current_url = baseUrl + "data";
    private static final String next_url = baseUrl + "next.json";

    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;

    private ListView mDataItemList;
    private Bitmap mImageBitmap;

    private DataItemAdapter mDataItemListAdapter;
    private Handler mHandler;

    // Send DataItems.
    private ScheduledExecutorService mGeneratorExecutor;
    private ScheduledFuture<?> mDataItemGeneratorFuture;

    Track current_track, next_track;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap bitmap;
    public boolean check;

    private VolleySingleton mVolleySingleton;
    private RequestQueue mRequestQueue;
    Handler pollHandler;
    Poll pollRequest;

    @Override
    public void onCreate (Bundle b) {
        super.onCreate(b);
        mHandler = new Handler();
        LOGD(TAG, "onCreate");
        //mCameraSupported = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, ListenerService.class);
        startService(intent);

        setupViews();

        mVolleySingleton = VolleySingleton.getInstance();
        mRequestQueue = mVolleySingleton.getRequestQueue();

        // Stores DataItems received by the local broadcaster or from the paired watch.
        mDataItemListAdapter = new DataItemAdapter(this, android.R.layout.simple_list_item_1);
        mDataItemList.setAdapter(mDataItemListAdapter);

        mGeneratorExecutor = new ScheduledThreadPoolExecutor(1);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        current_track = new Track(-1);
        next_track = new Track(-1);

        //fetchInfo(current_url);
        pollHandler = new Handler();
        pollRequest = new Poll();
        pollRequest.run();

    }

    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            mImageBitmap = (Bitmap) extras.get("data");
            mThumbView.setImageBitmap(mImageBitmap);
        }
    }*/


    public void sendNextTrackInfo (Track track, Bitmap bitmap) {

        // next track

        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(NEXT_TRACK);
        putDataMapRequest.getDataMap().putString(track_title, track.getTitle());
        putDataMapRequest.getDataMap().putString(artist, track.getArtist());

        putDataMapRequest.getDataMap().putAsset("album_art", toAsset(bitmap));  // get Asset
        PutDataRequest request = putDataMapRequest.asPutDataRequest();

        LOGD(TAG, "Generating DataItem: " + request);
        if (!mGoogleApiClient.isConnected()) {
            return;
        }
        Wearable.DataApi.putDataItem(mGoogleApiClient, request)
                .setResultCallback(new ResultCallback<DataItemResult>() {
                    @Override
                    public void onResult (DataItemResult dataItemResult) {
                        if (!dataItemResult.getStatus().isSuccess()) {
                            Log.e(TAG, "ERROR: failed to putDataItem, status code: "
                                    + dataItemResult.getStatus().getStatusCode());
                        }
                    }
                });

    }

    public void sendCurrentTrackInfo (Track track, Bitmap bitmap) {
        // current track

        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(CURRENT_TRACK);
        putDataMapRequest.getDataMap().putString(track_title, track.getTitle());
        putDataMapRequest.getDataMap().putString(artist, track.getArtist());
        putDataMapRequest.getDataMap().putAsset("album_art", toAsset(bitmap));  // get Asset
        PutDataRequest request = putDataMapRequest.asPutDataRequest();

        LOGD(TAG, "Generating DataItem: " + request);
        if (!mGoogleApiClient.isConnected()) {
            return;
        }
        Wearable.DataApi.putDataItem(mGoogleApiClient, request)
                .setResultCallback(new ResultCallback<DataItemResult>() {
                    @Override
                    public void onResult (DataItemResult dataItemResult) {
                        if (!dataItemResult.getStatus().isSuccess()) {
                            Log.e(TAG, "ERROR: failed to putDataItem, status code: "
                                    + dataItemResult.getStatus().getStatusCode());
                        }
                    }
                });

    }

    public void sendTrackInfo (String lights, String security, String fan) {
        // current track

        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(CURRENT_TRACK);
        putDataMapRequest.getDataMap().putString("lights",lights);
        putDataMapRequest.getDataMap().putString("security", security);
        putDataMapRequest.getDataMap().putString("fan",fan);
        // Asset
        PutDataRequest request = putDataMapRequest.asPutDataRequest();

        LOGD(TAG, "Generating DataItem: " + request);
        if (!mGoogleApiClient.isConnected()) {
            return;
        }
        Wearable.DataApi.putDataItem(mGoogleApiClient, request)
                .setResultCallback(new ResultCallback<DataItemResult>() {
                    @Override
                    public void onResult (DataItemResult dataItemResult) {
                        if (!dataItemResult.getStatus().isSuccess()) {
                            Log.e(TAG, "ERROR: failed to putDataItem, status code: "
                                    + dataItemResult.getStatus().getStatusCode());
                        }
                    }
                });

    }

    @Override
    protected void onStart () {
        super.onStart();
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume () {
        super.onResume();
        //mDataItemGeneratorFuture = mGeneratorExecutor.scheduleWithFixedDelay(
        //      new DataItemGenerator(), 1, 5, TimeUnit.SECONDS);
    }

    @Override
    public void onPause () {
        super.onPause();
        //mDataItemGeneratorFuture.cancel(true /* mayInterruptIfRunning */);
    }

    @Override
    protected void onStop () {
        if (!mResolvingError) {
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            Wearable.MessageApi.removeListener(mGoogleApiClient, this);
            Wearable.NodeApi.removeListener(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override //ConnectionCallbacks
    public void onConnected (Bundle connectionHint) {
        LOGD(TAG, "Google API Client was connected");
        mResolvingError = false;
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        Wearable.NodeApi.addListener(mGoogleApiClient, this);
    }

    @Override //ConnectionCallbacks
    public void onConnectionSuspended (int cause) {
        LOGD(TAG, "Connection to Google API client was suspended");
    }

    @Override //OnConnectionFailedListener
    public void onConnectionFailed (ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            Log.e(TAG, "Connection to Google API client has failed");
            mResolvingError = false;
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            Wearable.MessageApi.removeListener(mGoogleApiClient, this);
            Wearable.NodeApi.removeListener(mGoogleApiClient, this);
        }
    }

    @Override //DataListener
    public void onDataChanged (DataEventBuffer dataEvents) {
        LOGD(TAG, "onDataChanged: " + dataEvents);
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();
        runOnUiThread(new Runnable() {
            @Override
            public void run () {
                for (DataEvent event : events) {
                    if (event.getType() == DataEvent.TYPE_CHANGED) {
                        mDataItemListAdapter.add(
                                new Event("DataItem Changed", event.getDataItem().toString()));
                    } else if (event.getType() == DataEvent.TYPE_DELETED) {
                        mDataItemListAdapter.add(
                                new Event("DataItem Deleted", event.getDataItem().toString()));
                    }
                }
            }
        });
    }

    @Override //MessageListener
    public void onMessageReceived (final MessageEvent messageEvent) {
        LOGD(TAG, "onMessageReceived() A message from watch was received:" + messageEvent
                .getRequestId() + " " + messageEvent.getPath());
        mHandler.post(new Runnable() {
            @Override
            public void run () {
                mDataItemListAdapter.add(new Event("Message from watch", messageEvent.toString()));
            }
        });

    }

    @Override //NodeListener
    public void onPeerConnected (final Node peer) {
        LOGD(TAG, "onPeerConnected: " + peer);
        mHandler.post(new Runnable() {
            @Override
            public void run () {
                mDataItemListAdapter.add(new Event("Connected", peer.toString()));
            }
        });

    }

    @Override //NodeListener
    public void onPeerDisconnected (final Node peer) {
        LOGD(TAG, "onPeerDisconnected: " + peer);
        mHandler.post(new Runnable() {
            @Override
            public void run () {
                mDataItemListAdapter.add(new Event("Disconnected", peer.toString()));
            }
        });
    }

    /**
     * A View Adapter for presenting the Event objects in a list
     */
    private static class DataItemAdapter extends ArrayAdapter<Event> {

        private final Context mContext;

        public DataItemAdapter (Context context, int unusedResource) {
            super(context, unusedResource);
            mContext = context;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(android.R.layout.two_line_list_item, null);
                convertView.setTag(holder);
                holder.text1 = (TextView) convertView.findViewById(android.R.id.text1);
                holder.text2 = (TextView) convertView.findViewById(android.R.id.text2);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Event event = getItem(position);
            holder.text1.setText(event.title);
            holder.text2.setText(event.artist);
            return convertView;
        }

        private class ViewHolder {

            TextView text1;
            TextView text2;
        }
    }

    private class Event {

        String title;
        String artist;

        public Event (String title, String artist) {
            this.title = title;
            this.artist = artist;
        }
    }

    private Collection<String> getNodes () {
        HashSet<String> results = new HashSet<String>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();

        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }

        return results;
    }

    private void sendStartActivityMessage (String node) {
        Wearable.MessageApi.sendMessage(
                mGoogleApiClient, node, START_ACTIVITY_PATH, new byte[0]).setResultCallback(
                new ResultCallback<SendMessageResult>() {
                    @Override
                    public void onResult (SendMessageResult sendMessageResult) {
                        if (!sendMessageResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Failed to send message with status code: "
                                    + sendMessageResult.getStatus().getStatusCode());
                        }
                    }
                }
        );
    }

    private class StartWearableActivityTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground (Void... args) {
            Collection<String> nodes = getNodes();
            for (String node : nodes) {
                sendStartActivityMessage(node);
            }
            return null;
        }
    }

    /**
     * Sends an RPC to start a fullscreen Activity on the wearable.
     */
    public void onStartWearableActivityClick (View view) {
        LOGD(TAG, "Generating RPC");

        // Trigger an AsyncTask that will query for a list of connected nodes and send a
        // "start-activity" message to each connected node.
        new StartWearableActivityTask().execute();
    }


    /**
     * Builds an {@link com.google.android.gms.wearable.Asset} from a bitmap. The image that we get
     * back from the camera in "data" is a thumbnail size. Typically, your image should not exceed
     * 320x320 and if you want to have zoom and parallax effect in your app, limit the size of your
     * image to 640x400. Resize your image before transferring to your wearable device.
     */
    private static Asset toAsset (Bitmap bitmap) {
        ByteArrayOutputStream byteStream = null;
        try {
            byteStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            return Asset.createFromBytes(byteStream.toByteArray());
        } finally {
            if (null != byteStream) {
                try {
                    byteStream.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * Sends the asset that was created form the photo we took by adding it to the Data Item store.
     *//*
    private void sendPhoto(Asset asset) {
        PutDataMapRequest dataMap = PutDataMapRequest.create(IMAGE_PATH);
        dataMap.getDataMap().putAsset(IMAGE_KEY, asset);
        dataMap.getDataMap().putLong("time", new Date().getTime());
        PutDataRequest request = dataMap.asPutDataRequest();
        Wearable.DataApi.putDataItem(mGoogleApiClient, request)
                .setResultCallback(new ResultCallback<DataItemResult>() {
                    @Override
                    public void onResult(DataItemResult dataItemResult) {
                        LOGD(TAG, "Sending image was successful: " + dataItemResult.getStatus()
                                .isSuccess());
                    }
                });

    }

    /**
     * Sets up UI components and their callback handlers.
     */
    private void setupViews () {
        mDataItemList = (ListView) findViewById(R.id.data_item_list);
    }

    /**
     * As simple wrapper around Log.d
     */
    private static void LOGD (final String tag, String message) {
        if (Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message);
        }
    }

    class Poll implements Runnable {

        @Override
        public void run () {
            fetchInfo(current_url);
            Log.i("poll", "complete");
            pollHandler.postDelayed(pollRequest, 2000);
        }
    }

    public void fetchInfo (final String url) {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse (String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        Log.d("volley response", response);
                        switch (url) {
                            case current_url:
                                try {
                                    JSONObject json = new JSONObject(response);
                                    String lights1=json.getString("lights1");
                                    String lights2=json.getString("lights2");
                                    String security=json.getString("security");
                                    String fan=json.getString("fan");
                                    if(lights1.equalsIgnoreCase("1")&&lights2.equalsIgnoreCase("1"))
                                    {
                                        sendTrackInfo("1",security,fan);
                                    }
                                    else{
                                        sendTrackInfo("0",security,fan);
                                    }
                                    /*if (json.getBoolean("playing")) {
                                        JSONObject trackJson = json.getJSONObject("track");
                                        int current_id = trackJson.getInt("id");
                                        if (current_track.getTrack_id() != current_id) {
                                            check = false;
                                            LoadBitmap load = new LoadBitmap();
                                            try {
                                                load.execute(trackJson.getString("artist_pic"));
                                                load.get();
                                            } catch (Exception e) {

                                            }
                                            current_track.setTrack_id(trackJson.getInt("id"));
                                            current_track.setTitle(trackJson.getString("title"));
                                            current_track.setArtist(trackJson.getString("artist"));
                                            current_track.setAlbum_art(bitmap);

                                            fetchInfo(next_url);
                                        }
                                    }*/

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            /*case next_url:
                                try {
                                    JSONObject json = new JSONObject(response);
                                    JSONObject trackJson = json.getJSONObject("track");
                                    int current_id = trackJson.getInt("id");
                                    if (current_track.getTrack_id() != current_id) {
                                        check = false;
                                        LoadBitmap load = new LoadBitmap();
                                        try {
                                            load.execute(trackJson.getString("artist_pic"));
                                            load.get();
                                        } catch (Exception e) {

                                        }
                                        next_track.setTrack_id(trackJson.getInt("id"));
                                        next_track.setTitle(trackJson.getString("title"));
                                        next_track.setArtist(trackJson.getString("artist"));
                                        next_track.setAlbum_art(bitmap);

                                        sendTrackInfo(current_track, next_track,
                                                current_track.getAlbum_art(),
                                                next_track.getAlbum_art());

                                        //fetchInfo(next_url);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;*/
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse (VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });

        mRequestQueue.add(stringRequest);
    }

    class LoadBitmap extends
            AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute () {
            super.onPreExecute();
        }

        /**
         * getting All products from url
         */
        protected String doInBackground (String... args) {

            String url = args[0];

            // First decode with inJustDecodeBounds=true to check dimensions
            try {
                Log.i("TileAdapter: decodeSa", url);
                bitmap = BitmapFactory.decodeStream(
                        (InputStream) new URL(url).getContent(), null, null);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher, null);
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute (String file_url) {
            Log.i("Album should print", "true");
        }
    }
}
