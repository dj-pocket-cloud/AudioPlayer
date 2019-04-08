package rs.example.audioplayer;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

public class ControlsFragment extends Fragment {

    TrackOperations mediaPlayer = new TrackOperations();
    String uriString = "android.resource://rs.example.audioplayer/" + R.raw.knux;

    private ImageButton playButton;
    private ImageButton rewindButton;
    private ImageButton loopButton;
    private ImageButton fastForwardButton;
    private SeekBar seekBar;
    private TextView songInfo;

    private int loop = 0;
    private int beginning;
    private int end;
    Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //set layout
        View view = inflater.inflate(R.layout.controls_fragment,
                container, false);

        try {
            mediaPlayer.setSource(this.getContext(), Uri.parse(uriString));
        } catch (IOException e) {
            e.printStackTrace();
        }

        playButton = (ImageButton)view.findViewById(R.id.play);
        rewindButton = (ImageButton)view.findViewById(R.id.rewind);
        loopButton = (ImageButton)view.findViewById(R.id.loopButton);
        fastForwardButton = (ImageButton)view.findViewById(R.id.forward);
        seekBar = (SeekBar)view.findViewById(R.id.songSeek);
        songInfo = (TextView)view.findViewById(R.id.songInfo);
        playButton.setOnClickListener(playButtonListener);
        rewindButton.setOnClickListener(rewindButtonListener);
        loopButton.setOnClickListener(loopButtonListener);
        fastForwardButton.setOnClickListener(fastForwardButtonListener);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBar.setMax(mediaPlayer.getLength());

        mediaPlayer.setLooping(loop == 1);
        beginning = 0;
        end = mediaPlayer.getLength() - 1;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int mCurrPosition = mediaPlayer.getPosition();
                seekBar.setProgress(mCurrPosition);
                handler.postDelayed(this, 1000);

                if (mediaPlayer.getPlaying()) playButton.setImageResource(R.drawable.ic_pause_black_24dp);
                else playButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);

                if (mediaPlayer.getLooping()) loopButton.setImageAlpha(Color.WHITE);
                else loopButton.setImageAlpha(Color.GRAY);

                songInfo.setText(getResources().getResourceName(R.raw.knux));
            }
        });

        return view;

    }

    //play the currently loaded song when this button is pressed
    OnClickListener playButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                mediaPlayer.toggleMusic();
                if (mediaPlayer.getPlaying()) playButton.setImageResource(R.drawable.ic_pause_black_24dp);
                else playButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    };

    //seek to beginning of track
    OnClickListener rewindButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mediaPlayer.setPosition(beginning);
        }
    };

    //seek to end of track
    OnClickListener fastForwardButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mediaPlayer.setPosition(end);
        }
    };

    OnClickListener loopButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mediaPlayer.setLooping((++loop) % 2 == 1);
            if (mediaPlayer.getLooping()) loopButton.setImageAlpha(Color.WHITE);
            else loopButton.setImageAlpha(Color.GRAY);
        }
    };

    //set track progress when the seekBar is changed
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) { mediaPlayer.setPosition(progress); }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


}
