package in.co.sdslabs.play;

import android.app.Application;
import android.content.Context;

/**
 * Created by Akshay on 13-03-2015.
 */
public class PlayApplication extends Application {

    private static PlayApplication sInstance;

    public static PlayApplication getInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance=this;
    }
}
