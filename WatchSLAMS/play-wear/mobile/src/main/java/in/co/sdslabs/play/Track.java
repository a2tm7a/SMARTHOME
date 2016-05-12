package in.co.sdslabs.play;

import android.graphics.Bitmap;

/**
 * Created by marauder on 3/13/15.
 */
public class Track {

    String title, artist, album_url;
    int track_id;

    public Bitmap getAlbum_art () {
        return album_art;
    }

    public void setAlbum_art (Bitmap album_art) {
        this.album_art = album_art;
    }

    Bitmap album_art;

    public Track(int track_id){
        this.track_id = track_id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum_url() {
        return album_url;
    }

    public void setAlbum_url(String album_url) {
        this.album_url = album_url;
    }

    public int getTrack_id() {
        return track_id;
    }

    public void setTrack_id(int track_id) {
        this.track_id = track_id;
    }
}
