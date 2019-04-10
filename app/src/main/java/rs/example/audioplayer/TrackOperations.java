package rs.example.audioplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.RemoteException;

import java.io.IOException;

public class TrackOperations {

    MediaPlayer player;
    private boolean loaded;

    public TrackOperations() {
        player = new MediaPlayer();
        loaded = false;
    }

    public void setSource(Context context, Uri uri) throws IOException {
        boolean loop = getLooping();
        loaded = true;
        player.reset();
        player.setDataSource(context, uri);
        player.prepare();
        setLooping(loop);
        player.start();
    }

    public void toggleMusic() throws RemoteException {
        //play or pause the player, depending on playing status
        if (player.isPlaying()) { player.pause(); }
        else { player.start(); }
    }

    public boolean getLoaded() {
        return this.loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean getPlaying() {
        return player.isPlaying();
    }

    public void setPosition(int msec) {
        //get the current status of the player and store it in a local boolean
        boolean wasPlaying = player.isPlaying();

        //stop the music, and if it was playing then start playing again
        if (wasPlaying) { player.pause(); }
        player.seekTo(msec);
        if (wasPlaying) { player.start(); }
    }

    public int getPosition() {
        return player.getCurrentPosition(); //returns in milliseconds
    }

    //set looping status
    public void setLooping(boolean loop) {
        player.setLooping(loop);
    }

    public boolean getLooping() {
        return player.isLooping();
    }

    public int getLength() {
        return player.getDuration();
    }

    public String getLengthAsString(boolean getRemainingLength, int msProgress) {
        String result = null;
        int ms = msProgress;
        //if first flag is set true, set ms to remaining length instead of current length
        if (getRemainingLength) {
            ms = player.getDuration() - msProgress;
        }
        int min = ms/60000; //get amount of minutes first
        ms = ms - (min*60000); //subtract those minutes from ms
        int sec = ms/1000; //get amount of seconds from result of above operation

        //build the strings
        String minString;
        String secString;
        if(min < 10) {
            minString = "0" + min;  //result: 0N
        } else if (min >= 10 && min < 99) {
            minString = "" + min;   //result: NN
        } else {
            minString = "99";       //result: 99
        }
        if(sec < 10) {
            secString = "0" + sec;  //result: 0N
        } else if (sec >= 10 && sec < 99) {
            secString = "" + sec;   //result: NN
        } else {
            secString = "99";       //result: 99
        }

        result = minString + ":" + secString;

        return result;
    }
}
