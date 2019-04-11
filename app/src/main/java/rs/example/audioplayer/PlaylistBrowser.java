package rs.example.audioplayer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlaylistBrowser extends Fragment {
    private List<Track> trackList = new ArrayList<>();
    private ListView trackListView;
    private TrackArrayAdapter trackArrayAdapter;
    private ImageButton backButton;
    private TextView playlistTitle;
    private static final int FILE_PERMISSION_REQUEST_CODE = 1;
    private ControlsFragment controlsFragment;
    private Track previousTrack;
    private int previousPos = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set the layout you want to display in First Fragment
        View view = inflater.inflate(R.layout.playlist_fragment, container, false);

        trackListView = (ListView) view.findViewById(R.id.tracksListView);
        trackArrayAdapter = new TrackArrayAdapter(getContext(),trackList);
        trackListView.setAdapter(trackArrayAdapter);

        playlistTitle = (TextView) view.findViewById(R.id.playlistTitle);
        playlistTitle.setText("");

        controlsFragment = (ControlsFragment) getFragmentManager().findFragmentById(R.id.controls);

        backButton = (ImageButton) view.findViewById(R.id.backButton);

        //gets track item when clicked
        trackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item from ListView

                Track selectedItem = (Track) parent.getItemAtPosition(position);
                //if there was a previous track selected, set its image back to the default
                /*if (previousPos != -1) {
                    Log.d("hi", "onItemClick: ");
                    previousTrack.setImgId(R.drawable.ic_album_black_24dp);
                    trackArrayAdapter.setImg(previousPos, view);
                }
                previousTrack = selectedItem; //after that set previous track to currently selected track
                previousPos = position;
                selectedItem.setImgId(R.drawable.ic_play_arrow_black_24dp); //set play icon on current track
                trackArrayAdapter.setImg(position, view);*/

                controlsFragment.changeMusic(selectedItem);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getFragmentManager().popBackStack();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        //initialSetup();
        checkPermissions();
        //getAllTracks();
        //setTestTracks();
        return view;

    }

    private void setTestTracks(){
        for(int i = 0; i < 10; i++){
            Track newTrack = new Track("Hello" + i,"nope", "12112","dummy","dummy", R.drawable.ic_album_black_24dp);
            trackList.add(newTrack);
        }

    }
    private void initialSetup(){


        if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            getAllTracks();
        }
        else {
            checkPermissions();
        }

    }

    private void getAllTracks(){
        trackList.clear();
        playlistTitle.setText("All Audio");

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST, MediaStore.Audio.AudioColumns.DURATION};
        Cursor c = getContext().getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%%"}, null);

        if (c != null) {
            while (c.moveToNext()) {
                Track audioModel = new Track();
                String path = c.getString(0);
                String name = c.getString(1);
                String album = c.getString(2);
                String artist = c.getString(3);
                String length = c.getString(4);

                audioModel.setTrackName(name);
                audioModel.setAlbum(album);
                audioModel.setArtist(artist);
                audioModel.setPath(path);
                audioModel.setTrackLength(length);
                audioModel.setImgId(R.drawable.ic_album_black_24dp);

                Log.e("Name :" + name, " Album :" + album);
                Log.e("Path :" + path, " Artist :" + artist);

                trackList.add(audioModel);
            }
            c.close();
            trackList.sort(Comparator.<Track>naturalOrder());
            controlsFragment.setTrackList(trackList);
        }

    }

    private void checkPermissions(){
        if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            /*/ shows an explanation for why permission is needed
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // set Alert Dialog's message
                builder.setMessage(R.string.read_write_perms_explanation);

                // add an OK button to the dialog
                builder.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // request permission
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, FILE_PERMISSION_REQUEST_CODE);
                            }
                        }
                );

                // display the dialog
                builder.create().show();}*/
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
            String rationale = "To use all of this app's features, permission is needed to read and write to this device's memory";

            /*Permissions.Options options = new Permissions.Options()
                    .setRationaleDialogTitle("Info")
                    .setSettingsDialogTitle("Warning");*/

            Permissions.check(getContext(), permissions, rationale, null, new PermissionHandler() {
                @Override
                public void onGranted() {
                    //Toast.makeText(getContext(), "Storage granted.", Toast.LENGTH_SHORT).show();
                    getAllTracks();
                    trackArrayAdapter.notifyDataSetChanged();
                    trackListView.smoothScrollToPosition(0);
                }
            });
        }
        /*else {
            // request permission
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, FILE_PERMISSION_REQUEST_CODE);
        }*/
        else {
            //check if both permissions are set and automatically get all tracks
            if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getAllTracks();
                trackArrayAdapter.notifyDataSetChanged();
                trackListView.smoothScrollToPosition(0);
            }
        }

    }
    public Track getTrackFromList(int index){
        return trackList.get(index);
    }

}
