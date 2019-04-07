package rs.example.audioplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.RemoteException;

import java.io.IOException;

public class TrackOperations {

    MediaPlayer player = null;

    public void playMusic(Context context, Uri uri) throws RemoteException {
        try {

            if (player == null)
                player = new MediaPlayer();

            player.reset(); // optional if you want to repeat calling this method
            player.setDataSource(context, uri);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
