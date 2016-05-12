package in.co.sdslabs.play;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Akshay on 13-03-2015.
 */
public class MessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive (Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
