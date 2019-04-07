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
        player.stop();
        player.setDataSource(context, uri);
        player.prepare();
    }

    public void toggleMusic() throws RemoteException {
        //play or pause the player, depending on playing status
        if (player.isPlaying()) { player.pause(); }
        else { player.start(); }
    }

    public void seekBack() {
        //get the current status of the player and store it in a local boolean
        boolean wasPlaying = player.isPlaying();

        //stop the music, and if it was playing then start playing again
        player.pause();
        player.seekTo(0);
        if (wasPlaying) { player.start(); }
    }

    public void playFromPoint(int msec) {
        player.pause();
        player.seekTo(msec);
        player.start();
    }

    //set looping status
    public void setLooping(boolean loop) {
        player.setLooping(loop);
    }

    public int getPosition() {
        return player.getCurrentPosition(); //returns in milliseconds
    }
}
