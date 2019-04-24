package rs.example.audioplayer;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControlsFragment extends Fragment {

    TrackOperations mediaPlayer = new TrackOperations();
    String uriString;// = "android.resource://rs.example.audioplayer/" + R.raw.knux;

    private ImageButton playButton;
    private ImageButton rewindButton;
    private ImageButton loopButton;
    private ImageButton fastForwardButton;
    private ImageButton optionsButton;
    private ImageButton snippetButton;
    private SeekBar seekBar;
    private TextView songInfo;
    private TextView timeAt;
    private TextView timeLeft;
    private int trackIndex;
    private int snipBegin;
    private int snipEnd;
    private boolean musicListFilled = false;
    private boolean snipping;

    private List<Track> trackList = new ArrayList<>();

    private int loop = 0;
    private int beginning;
    private int end;
    private boolean updateTimeMarkers = true;
    private static boolean optionsOpen = false;
    Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //set layout
        View view = inflater.inflate(R.layout.controls_fragment,
                container, false);

        /*try {
            mediaPlayer.setSource(this.getContext(), Uri.parse(uriString));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        playButton = (ImageButton)view.findViewById(R.id.play);
        rewindButton = (ImageButton)view.findViewById(R.id.rewind);
        loopButton = (ImageButton)view.findViewById(R.id.loopButton);
        fastForwardButton = (ImageButton)view.findViewById(R.id.forward);
        optionsButton = (ImageButton)view.findViewById(R.id.menu);
        snippetButton =(ImageButton)view.findViewById(R.id.snippet);
        seekBar = (SeekBar)view.findViewById(R.id.songSeek);
        songInfo = (TextView)view.findViewById(R.id.songInfo);
        timeAt = (TextView)view.findViewById(R.id.timeAt);
        timeLeft = (TextView)view.findViewById(R.id.timeLeft);


        playButton.setOnClickListener(playButtonListener);
        rewindButton.setOnClickListener(rewindButtonListener);
        loopButton.setOnClickListener(loopButtonListener);
        fastForwardButton.setOnClickListener(fastForwardButtonListener);
        optionsButton.setOnClickListener(optionsButtonListener);
        snippetButton.setOnClickListener(snippetButtonListener);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        timeAt.setText("00:00");
        timeLeft.setText("00:00");
        seekBar.setMax(0);//mediaPlayer.getLength());
        songInfo.setText("No currently playing song.");
        mediaPlayer.setLooping(loop == 1);
        beginning = 0;
        end = 0;//mediaPlayer.getLength() - 1;

        //all logic that runs in real-time is here
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mediaPlayer.getPlaying()) playButton.setImageResource(R.drawable.ic_pause_black_24dp);
                else playButton.setImageResource(R.drawable.ic_main_play_arrow_black_24dp);

                if (mediaPlayer.getLooping()) loopButton.setImageResource(R.drawable.ic_loop_black_24dp);
                else loopButton.setImageResource(R.drawable.ic_highlight_off_black_24dp);

                if (updateTimeMarkers && mediaPlayer.getLoaded()) {
                    timeAt.setText(mediaPlayer.getLengthAsString(false, seekBar.getProgress()));
                    timeLeft.setText(mediaPlayer.getLengthAsString(true, seekBar.getProgress()));
                }
                if (!mediaPlayer.getLoaded()) {
                    timeAt.setText("00:00");
                    timeLeft.setText("00:00");
                }

                int mCurrPosition = mediaPlayer.getPosition();
                seekBar.setProgress(mCurrPosition);
                handler.postDelayed(this, 10);

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
            if(musicListFilled&&getSeekBarPercent()<2){
                changeMusic(trackList.get(decrementTrackIndex()));
            }
            mediaPlayer.setPosition(beginning);
        }
    };

    //seek to end of track
    OnClickListener fastForwardButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(musicListFilled)
            changeMusic(trackList.get((trackIndex+1)%(trackList.size())));
        }
    };

    //open options menu fragment
    OnClickListener optionsButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!optionsOpen) {
                setOptionsOpen(true);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();

                OptionsFragment of = new OptionsFragment();
                fragmentTransaction.replace(R.id.fragment_container, of).addToBackStack(null).commit();
            }
        }
    };

    OnClickListener loopButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mediaPlayer.setLooping((++loop) % 2 == 1);
            if (mediaPlayer.getLooping()){
                loopButton.setImageResource(R.drawable.ic_loop_black_24dp);
                //because the image itself changes toast messages probably aren't necessary anymore

                //Toast message = Toast.makeText(getContext(), "Looping On", Toast.LENGTH_SHORT);
                //message.setGravity(Gravity.CENTER, message.getXOffset() / 2, message.getYOffset() / 2);
                //message.show();
            }
            else{
                loopButton.setImageResource(R.drawable.ic_highlight_off_black_24dp);
                //Toast message = Toast.makeText(getContext(), "Looping Off", Toast.LENGTH_SHORT);
                //message.setGravity(Gravity.CENTER, message.getXOffset() / 2, message.getYOffset() / 2);
                //message.show();
            }
        }
    };
    //open options menu fragment
    OnClickListener snippetButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!snipping) {
                if(mediaPlayer.getLoaded()) {
                    snipping = true;
                    snipBegin = seekBar.getProgress();
                }

            } else if(snipBegin<seekBar.getProgress()){
                snipping = false;
                snipEnd = seekBar.getProgress();
                SnippetDialogFragment snippetDialogFragment = new SnippetDialogFragment();
                snippetDialogFragment.show(getFragmentManager(),"snippet dialog");
                try {
                    mediaPlayer.pauseMusic();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    //set track progress when the seekBar is changed
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) { mediaPlayer.setPosition(progress); }
            else if(musicListFilled&&progress==seekBar.getMax()&&!mediaPlayer.getLooping()) {
                changeMusic(trackList.get((trackIndex+1)%(trackList.size())));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            updateTimeMarkers = false;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            updateTimeMarkers = true;
        }
    };

    public void changeMusic(Track track){
        try {
            mediaPlayer.setSource(getContext(),Uri.parse(track.getPath()),track.getTrackStart(),track.getRawTrackLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setPosition(0);
        seekBar.setMax(mediaPlayer.getLength());
        seekBar.setProgress(0);

        trackIndex = trackList.indexOf(track);

        songInfo.setText(track.getTrackName());

        snipping = false;

        Toast message = Toast.makeText(getContext(), "Playing " + track.getTrackName(), Toast.LENGTH_SHORT);
        message.setGravity(Gravity.CENTER, message.getXOffset() / 2,
                message.getYOffset() / 2);
        message.show();
    }

    private int getSeekBarPercent() {
        int percent;
        int max = seekBar.getMax();
        int cur = seekBar.getProgress();
        double decVal = (double)cur/max;
        decVal*=100;
        percent = (int) Math.round(decVal);
        return percent;
    }

    private int decrementTrackIndex(){
        if(trackIndex==0)
            return trackList.size()-1;
        else
            return trackIndex-1;
    }

    public void setTrackList(List<Track> trackList) {
        musicListFilled = true;
        this.trackList = trackList;
    }

    public static void setOptionsOpen(boolean bool) {
        optionsOpen = bool;
    }

    public int getSnipBegin() {
        return snipBegin;
    }

    public int getSnipEnd() {
        return snipEnd;
    }

    public void setSnipBegin(int snipBegin) {
        this.snipBegin = snipBegin;
    }

    public void setSnipEnd(int snipEnd) {
        this.snipEnd = snipEnd;
    }
    public int getTracklength(){
        return mediaPlayer.getLength();
    }

    public void createSnippet(String title){
        if (title.equals(""))
            title = nextSnipName();
        Track original = trackList.get(trackIndex);

        Track snippet = new Track(title,original.getArtist(),snipBegin,snipEnd,original.getAlbum(),original.getPath(),R.drawable.ic_content_cut_black_24dp);
        //add to snippet file
    }

    private String nextSnipName(){
        String name = songInfo.getText() + " snippet";
        int i = 1;
        while (trackList.contains(name)){
            if(i>1)
                name = name.substring(0,name.length()-2);
            name+=i;
            i++;
        }

        return name;
    }

}
