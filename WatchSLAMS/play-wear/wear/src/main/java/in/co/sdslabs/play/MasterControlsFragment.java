package in.co.sdslabs.play;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Akshay on 12-03-2015.
 */
public class MasterControlsFragment extends Fragment implements View.OnClickListener{

    ImageButton lights,fan,vol_down , security;

    private static final long CONNECTION_TIME_OUT_MS = 100;

    private GoogleApiClient client;
    private String nodeId;

    RelativeLayout rl;

    public String title , artist ;
    public Bitmap bitmap;

    String str_lights,str_security,str_fan;

    TextView tv_title, tv_artist;

    public MasterControlsFragment(String lights, String security, String fan){
        this.str_fan = fan;
        this.str_security = security;
        this.str_lights = lights;

    }


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.master_controls,container,
                false);

        rl = (RelativeLayout)view.findViewById(R.id.rl_master_fragment);
        security = (ImageButton) view.findViewById(R.id.security);
        lights = (ImageButton)view.findViewById(R.id.lights);
        fan = (ImageButton)view.findViewById(R.id.fan);
        lights.setOnClickListener(this);
        fan.setOnClickListener(this);
        security.setOnClickListener(this);

        if(str_lights.equalsIgnoreCase("0"))
        {
            lights.setTag("off");
            lights.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.bulb_off_wear, null));
        }
        else{
            lights.setTag("on");
            lights.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.bulb_on_wear, null));
        }

        if(str_fan.equalsIgnoreCase("0"))
        {
            fan.setTag("off");
            fan.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.fanoff_wear, null));

        }
        else{
            fan.setTag("on");
            fan.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.fanon_wear, null));
        }

        if(str_security.equalsIgnoreCase("0"))
        {
            security.setTag("off");
            security.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.door_securityoffwear, null));

        }
        else{
            security.setTag("on");
            security.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.door_securityonwear, null));
        }







        initApi();

        Handler mHandler = new Handler();
        mHandler.post(new Runnable() {
            @Override
            public void run () {
                rl.setBackground(new BitmapDrawable(getResources(), String.valueOf(R.drawable.back)));
            }
        });

        return view;
    }

    /**
     * Initializes the GoogleApiClient and gets the Node ID of the connected device.
     */
    private void initApi(){
        client = getGoogleApiClient(getActivity());
        retrieveDeviceNode();
    }

    /**
     * Returns a GoogleApiClient that can access the Wear API.
     * @param context
     * @return A GoogleApiClient that can make calls to the Wear API
     */
    private GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }

    /**
     * Connects to the GoogleApiClient and retrieves the connected device's Node ID. If there are
     * multiple connected devices, the first Node ID is returned.
     */
    private void retrieveDeviceNode() {
        new Thread(new Runnable() {
            @Override
            public void run () {
                client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(client).await();
                List<Node> nodes = result.getNodes();
                if (nodes.size() > 0) {
                    nodeId = nodes.get(0).getId();
                }
                client.disconnect();
            }
        }).start();
    }

    private void sendMessage(final String message) {
        if (nodeId != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    client.blockingConnect(CONNECTION_TIME_OUT_MS,
                            TimeUnit.MILLISECONDS);
                    Wearable.MessageApi.sendMessage(client, nodeId, message, null);
                    client.disconnect();
                }
            }).start();
        }
    }

    @Override
    public void onClick (View v) {
        switch(v.getId()){
            case R.id.fan :
               /*Toast.makeText(getActivity(), "Next", Toast.LENGTH_SHORT).show();*/
                if(fan.getTag() == "off"){
                    fan.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                            R.drawable.fanon_wear, null));
                    fan.setTag("on");
                }

                else if(fan.getTag() == "on"){
                    fan.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                            R.drawable.fanoff_wear, null));
                    fan.setTag("off");
                }
                sendMessage("fan");
                break;
            case R.id.lights :
                if(lights.getTag() == "off"){
                    lights.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                            R.drawable.bulb_on_wear, null));
                    lights.setTag("on");
                }

                else if(lights.getTag() == "on"){
                    lights.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                            R.drawable.bulb_off_wear, null));
                    lights.setTag("off");
                }

               /*Toast.makeText(getActivity(),"Play",Toast.LENGTH_SHORT).show();*/
                sendMessage("lights");
                break;
            case R.id.security:
                if(security.getTag() == "off"){
                    security.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                            R.drawable.door_securityonwear, null));
                    security.setTag("on");
                }

                else if(security.getTag() == "on"){
                    security.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                            R.drawable.door_securityoffwear, null));
                    lights.setTag("off");
                }
                sendMessage("security");
                break;
        }
    }

}