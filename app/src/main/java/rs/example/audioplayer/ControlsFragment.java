package rs.example.audioplayer;

import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.view.View.OnClickListener;

import java.io.IOException;

public class ControlsFragment extends Fragment {

    TrackOperations operations = new TrackOperations();
    String uriString = "android.resource://rs.example.audioplayer/" + R.raw.knux;

    ImageButton playButton;
    ImageButton rewindButton;
    ImageButton loopButton;

    int loop = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //set layout
        View view = inflater.inflate(R.layout.controls_fragment,
                container, false);

        try {
            operations.setSource(this.getContext(), Uri.parse(uriString));
        } catch (IOException e) {
            e.printStackTrace();
        }

        playButton = (ImageButton)view.findViewById(R.id.play);
        rewindButton = (ImageButton)view.findViewById(R.id.rewind);
        loopButton = (ImageButton)view.findViewById(R.id.loopButton);
        playButton.setOnClickListener(playButtonListener);
        rewindButton.setOnClickListener(rewindButtonListener);
        loopButton.setOnClickListener(loopButtonListener);
        return view;

    }

    //play the currently loaded song when this button is pressed
    OnClickListener playButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                operations.toggleMusic();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    };

    //seek to beginning of track
    OnClickListener rewindButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO: replace this with playFromPoint(beginningOfSnippet) when audio snippets are implemented
            operations.seekBack();
        }
    };

    OnClickListener loopButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            operations.setLooping((loop++) % 2 == 1);
        }
    };

}
