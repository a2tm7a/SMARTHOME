package in.co.sdslabs.play;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.util.List;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, DataApi.DataListener, MessageApi.MessageListener,
        NodeApi.NodeListener {

    private static final String TAG = "MainActivity";
    private static final String CURRENT_TRACK = "/current";
    private static final String NEXT_TRACK = "/next";


    private GoogleApiClient mGoogleApiClient;
    private ListView mDataItemList;
    private TextView mIntroText;
    private View mLayout;
    private Handler mHandler;

    GridViewPager pager;
    public CustomGridPagerAdapter customGridPagerAdapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity_main);

//        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
//        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
//            @Override
//            public void onLayoutInflated (WatchViewStub stub) {
//                mTextView = (TextView) stub.findViewById(R.id.text);
//            }
//        });

        final Resources res = getResources();
        CustomGridPagerAdapter customGridPagerAdapter =
                new CustomGridPagerAdapter(this,getFragmentManager());
        pager = (GridViewPager) findViewById(R.id.pager);
        pager.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {

            @Override
            public WindowInsets onApplyWindowInsets (View v, WindowInsets insets) {

                final boolean round = insets.isRound();
                int rowMargin = res.getDimensionPixelOffset(R.dimen.page_row_margin);
                int colMargin = res.getDimensionPixelOffset(round ?
                        R.dimen.page_column_margin_round : R.dimen.page_column_margin);
                pager.setPageMargins(rowMargin, colMargin);

                // GridViewPager relies on insets to properly handle
                // layout for round displays. They must be explicitly
                // applied since this listener has taken them over.
                pager.onApplyWindowInsets(insets);
                return insets;
            }
        });
        pager.setAdapter(customGridPagerAdapter);

        /*CustomGridPagerAdapter customGridPagerAdapter =
                new CustomGridPagerAdapter(this,getFragmentManager());*/

        //pager.setAdapter(customGridPagerAdapter);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onResume () {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause () {
        super.onPause();
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        Wearable.MessageApi.removeListener(mGoogleApiClient, this);
        Wearable.NodeApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected (Bundle connectionHint) {
        LOGD(TAG, "onConnected(): Successfully connected to Google API client");
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        Wearable.NodeApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended (int cause) {
        LOGD(TAG, "onConnectionSuspended(): Connection to Google API client was suspended");
    }

    @Override
    public void onConnectionFailed (ConnectionResult result) {
        Log.e(TAG, "onConnectionFailed(): Failed to connect, with result: " + result);
    }

    @Override
    public void onDataChanged (DataEventBuffer dataEvents) {
        LOGD(TAG, "onDataChanged(): " + dataEvents);

        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();
        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {

                DataMapItem dataMapItem = DataMapItem.fromDataItem(
                        event.getDataItem());


                String lights = dataMapItem.getDataMap().getString("lights");
                String security = dataMapItem.getDataMap().getString("security");
                String fan = dataMapItem.getDataMap().getString("fan");

                /*Asset next_photo = dataMapItem.getDataMap()
                        .getAsset("next_album_art");
                final Bitmap next_bitmap = loadBitmapFromAsset(mGoogleApiClient, next_photo);
                String next_title = dataMapItem.getDataMap().getString("next_title");
                String next_artist = dataMapItem.getDataMap().getString("next_artist");
                Log.v("Logger", next_title);
                */
                try{
                    ((CustomGridPagerAdapter)pager.getAdapter()).updateAdapter(lights,
                            security,
                            fan);
                    /*pager.notify();
                    pager.notifyAll();*/
                    customGridPagerAdapter.notifyDataSetChanged();

                }catch(Exception e){
                    Log.e("wear exception", e.toString());
                }

                /*mHandler.post(new Runnable() {
                    @Override
                    public void run () {
                        Log.d(TAG, "Setting background image..");
                        mLayout.setBackground(new BitmapDrawable(getResources(), bitmap));
                    }
                });*/



            } else if (event.getType() == DataEvent.TYPE_DELETED) {
            } else {
            }
        }
    }

    /**
     * Extracts {@link android.graphics.Bitmap} data from the
     * {@link com.google.android.gms.wearable.Asset}
     */
    private Bitmap loadBitmapFromAsset (GoogleApiClient apiClient, Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }

        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                apiClient, asset).await().getInputStream();

        if (assetInputStream == null) {
            Log.w(TAG, "Requested an unknown Asset.");
            return null;
        }
        return BitmapFactory.decodeStream(assetInputStream);
    }

    @Override
    public void onMessageReceived (MessageEvent event) {
        LOGD(TAG, "onMessageReceived: " + event);
    }

    @Override
    public void onPeerConnected (Node node) {
    }

    @Override
    public void onPeerDisconnected (Node node) {
    }

    public static void LOGD (final String tag, String message) {
        if (Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message);
        }
    }
}