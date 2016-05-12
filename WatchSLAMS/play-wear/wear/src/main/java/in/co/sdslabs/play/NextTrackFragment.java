package in.co.sdslabs.play;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.BoxInsetLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Akshay on 14-03-2015.
 */
public class NextTrackFragment extends Fragment {
    String title, artist;
    Bitmap bitmap;
    BoxInsetLayout boxLayout;
    TextView tv_title, tv_artist;

    public NextTrackFragment(String title, String artist, Bitmap bitmap){
        this.title = title;
        this.artist = artist;
        this.bitmap = bitmap;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.next_track_fragment, container,
                false);
        boxLayout = (BoxInsetLayout) view.findViewById(R.id.box_layout);
        tv_title = (TextView) view.findViewById(R.id.title_card);
        tv_artist = (TextView) view.findViewById(R.id.artist_card);

        tv_title.setText(title);
        tv_artist.setText(artist);

        Handler mHandler = new Handler();
        mHandler.post(new Runnable() {
            @Override
            public void run () {
                boxLayout.setBackground(new BitmapDrawable(getResources(), bitmap));
            }
        });
        return view;
       }
}
