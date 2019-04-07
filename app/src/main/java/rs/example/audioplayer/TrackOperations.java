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
        if (player.isPlaying()) {
            player.pause();
        } else {
            player.start();
        }
    }
}
