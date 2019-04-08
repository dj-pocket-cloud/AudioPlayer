package rs.example.audioplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.RemoteException;

import java.io.IOException;

public class TrackOperations {

    MediaPlayer player;

    public TrackOperations() {
        player = new MediaPlayer();
    }

    public void setSource(Context context, Uri uri) throws IOException {
        player.setDataSource(context, uri);
        player.prepare();
    }

    public void toggleMusic() throws RemoteException {
        //play or pause the player, depending on playing status
        if (player.isPlaying()) { player.pause(); }
        else { player.start(); }
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
}
