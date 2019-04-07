package rs.example.audioplayer;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.view.View.OnClickListener;

import java.io.IOException;

public class Controls extends Fragment {

    TrackOperations operations = new TrackOperations();
    String uriString = "android.resource://rs.example.audioplayer/" + R.raw.knux;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //set layout
        View view = inflater.inflate(R.layout.controls_layout,
                container, false);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //TODO: this button is broken currently, but if it DID work it'd play an audio file
        ImageButton playButton = (ImageButton)view.findViewById(R.id.play);
        playButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Controls", "why does this not work");

                try {
                    operations.playMusic(getContext(), Uri.parse(uriString));
                } catch (RemoteException e) {

                }

            }
        });

    }

}
