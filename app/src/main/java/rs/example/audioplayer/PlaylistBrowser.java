package rs.example.audioplayer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

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
    private File testPlaylist;
    private FileOutputStream os;
    private FileInputStream is;
    private final String FILENAME = "testPlaylist.txt";
    private boolean testLoad = false; //false: load all tracks //true: only load tracks from file
    private String playlistName; // name to use when loading or saving playlist file
    private File playlistFile;
    PlaylistAddFragment paf;
    Track currentItem;
    private File masterPlaylistFile;
    private String masterPlaylistLocation = "playlists";
    private FileOutputStream mos;

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

        testPlaylist = new File(getContext().getFilesDir(), FILENAME);
        //testPlaylist.delete();
        try {
            getContext().openFileInput(FILENAME); //invokes exception if file does not exist
            testLoad = true;
        } catch(FileNotFoundException e) {
            //testPlaylist = new File(getContext().getFilesDir(), FILENAME);
            testLoad = false;
        }

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

        //adds track to a playlist when long pressed
        trackListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                currentItem = (Track) parent.getItemAtPosition(position);

                //System.out.println(currentItem.getTrackEnd());

                //TODO: display a fragment where the user can input a playlist name to save to
                //todo: change filename to name of this fragment

                paf = new PlaylistAddFragment();
                paf.setPlaylistBrowserReference(PlaylistBrowser.this);
                paf.show(getFragmentManager(), "playlist add dialog");

                return true;
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

    public void writePlaylist() {

        try {
            //playlistFile = new File(getContext().getFilesDir(), playlistName + ".txt");
            //os = new FileOutputStream(FILENAME);
            os = getContext().openFileOutput(playlistName + ".txt", Context.MODE_APPEND);
            mos = getContext().openFileOutput(masterPlaylistLocation + ".txt", Context.MODE_APPEND);
            Toast.makeText(getContext(), currentItem.getTrackName() + " saved to playlist " + playlistName, Toast.LENGTH_SHORT).show();
            //OutputStreamWriter ow = new OutputStreamWriter(os);
            //ow.append(selectedItem.toString());
            //ow.append("\n\r");
            //ow.close();
            //System.out.println(currentItem.toString());
            os.write(currentItem.toString().getBytes());
            os.write("\n".getBytes());
            os.close();
            Scanner reader = new Scanner(masterPlaylistLocation + ".txt");
            boolean alreadyExists = false;
            while (reader.hasNextLine()) {
                if (reader.nextLine().equals(playlistName)) {
                    alreadyExists = true;
                }
            }
            if (!alreadyExists) {
                mos.write(playlistName.getBytes());
                mos.write("\n".getBytes());
            }
            mos.close();
        } catch (Exception e) {
            Log.e("openFileOutput", "onResume: ", e);
        }
    }

    public void setPlaylistName(String playlistName) {
        //System.out.println(playlistName);
        this.playlistName = playlistName;
    }

    private void setTestTracks(){
        for(int i = 0; i < 10; i++){
            Track newTrack = new Track("Hello" + i,"nope", "12112","dummy","dummy", R.drawable.ic_album_black_24dp);
            trackList.add(newTrack);
        }

    }
    private void initialSetup(){


        if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (playlistName == null) {
                getAllTracks();
            } else {
                //loadPlaylist(testPlaylist);
                Track[] playlist = loadPlaylist(playlistFile);
                System.out.println(Arrays.toString(playlist));
                if (playlist != null) {
                    getTracksInPlaylist(playlist);
                }
            }
            //trackArrayAdapter.notifyDataSetChanged();
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

    private void getTracksInPlaylist(Track[] tracks){
        trackList.clear();
        playlistTitle.setText(FILENAME);

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST, MediaStore.Audio.AudioColumns.DURATION};
        Cursor c = getContext().getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%%"}, null);

        //go through every local track, but only add ones that match the array of tracks
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

                System.out.println(audioModel.toString());

                //loop through every track in tracks[]
                for (int currTrack = 0; currTrack < tracks.length; currTrack++) {
                    //System.out.println(tracks[currTrack].toString());
                    //if audioModel == tracks[currTrack]
                    if (audioModel.getPath().equals(tracks[currTrack].getPath()))
                    {
                        Log.e("Name :" + name, " Album :" + album);
                        Log.e("Path :" + path, " Artist :" + artist);

                        trackList.add(tracks[currTrack]);
                    }
                }
            }
            c.close();
            trackList.sort(Comparator.<Track>naturalOrder());
            controlsFragment.setTrackList(trackList);
        }

    }

    private Track[] loadPlaylist(File playlistFile) {
        StringBuilder sb = new StringBuilder();
        int size = 0;
        //first get amount of lines in file
        try {
            BufferedReader br = new BufferedReader(new FileReader(playlistFile));
            while (br.readLine() != null) {
                size++;
            }
            br.close();
        } catch (Exception e) {
            Log.e("loadPlaylist", "loadPlaylist: ", e);
            return null;
        }
        System.out.println(size);

        //create a new array using this size value
        Track[] newPlaylist = new Track[size];
        int i = 0;
        //actually read the file and add all track information to the array
        try {
            is = new FileInputStream(playlistFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String[] split;

            String line = br.readLine();
            System.out.println("writePlaylist " + line);
            while (line != null) {
                sb.append(line);
                sb.append("\n");


                split = line.split("`");
                //System.out.println(split.length);
                for (int j = 0; j < split.length; j++) {
                    System.out.println(split[j]);
                }


                String title = split[0];
                String artist = split[1];
                String album = split[2];
                String path = split[3];
                int img = Integer.parseInt(split[4]);
                int start = Integer.parseInt(split[5]);
                int end = Integer.parseInt(split[6]);

                newPlaylist[i] = new Track(title,artist,start,end,album,path,img);
                //newPlaylist[i].setTrackName();
                //newPlaylist[i].setArtist();
                //newPlaylist[i].setTrackLength(rawtime);
                //newPlaylist[i].setAlbum(split[3]);
                //newPlaylist[i].setPath(split[4]);
                //newPlaylist[i].setImgId(Integer.parseInt(split[5]));
                System.out.println(newPlaylist[i].toString());
                System.out.println(i);
                i++;
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            Log.e("loadPlaylist", "loadPlaylist: ", e);
            return null;
        }

        return newPlaylist;
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
                //load the playlist relating to playlistName, or all tracks if it is null
                if (playlistName == null) {
                    getAllTracks();
                } else {
                    playlistTitle.setText(playlistName);

                    //createPlaylist(testPlaylist);
                    playlistFile = new File(getContext().getFilesDir(), playlistName + ".txt");
                    Track[] playlist = loadPlaylist(playlistFile);
                    System.out.println(Arrays.toString(playlist));
                    if (playlist != null) {
                        getTracksInPlaylist(playlist);
                    }
                }
                trackArrayAdapter.notifyDataSetChanged();
                trackListView.smoothScrollToPosition(0);
            }
        }

    }
    public Track getTrackFromList(int index){
        return trackList.get(index);
    }

}
