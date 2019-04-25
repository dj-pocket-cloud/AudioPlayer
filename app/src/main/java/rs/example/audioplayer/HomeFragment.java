package rs.example.audioplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Button testButton;
    private List<Playlist> playlistList = new ArrayList<>();
    private ListView playlistListView;
    private PlaylistArrayAdapter playlistArrayAdapter;
    private File masterPlaylistFile;
    private String masterPlaylistLocation = "playlists";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set the layout you want to display in First Fragment
        View view = inflater.inflate(R.layout.home_menu_fragment, container, false);

        testButton = (Button) view.findViewById(R.id.testButton);
        testButton.setOnClickListener(testClickListener);
        playlistListView = (ListView) view.findViewById(R.id.playlistsListView);
        playlistArrayAdapter = new PlaylistArrayAdapter(getContext(),playlistList);
        playlistListView.setAdapter(playlistArrayAdapter);

        masterPlaylistFile = new File(getContext().getFilesDir(), masterPlaylistLocation + ".txt");

        if(masterPlaylistFile.exists())
            loadPlaylists();
        else
        {
            try {
                masterPlaylistFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //todo: find all saved playlists and display them in a displayAdapter
        //todo: clicking a playlist will load the playlistbrowser with the filename as the argument
        //todo: also add "all tracks" at the top of this list, which will pass a special argument to load all tracks
        //todo: finally, longclick a playlist to delete it

        playlistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();

                Playlist selectedItem = (Playlist) parent.getItemAtPosition(position);

                PlaylistBrowser pb = new PlaylistBrowser();
                pb.setPlaylistName(selectedItem.getPlaylistName());
                fragmentTransaction.replace(R.id.fragment_container, pb).addToBackStack(null).commit();
            }
        });

        playlistListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                FileInputStream is;
                Playlist selectedItem = (Playlist) parent.getItemAtPosition(position);
                try {
                    is = new FileInputStream(masterPlaylistFile);
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = br.readLine();
                    while(line != null) {
                        if (line.equals(selectedItem.getPlaylistName())) {

                        }
                        line = br.readLine();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }
        });

        return view;

    }

    View.OnClickListener testClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();

            PlaylistBrowser pb = new PlaylistBrowser();
            pb.setPlaylistName(null);
            fragmentTransaction.replace(R.id.fragment_container, pb).addToBackStack(null).commit();

            //fragmentTransaction.addToBackStack(null);
            //fragmentTransaction.commit();
        }
    };

    private void loadPlaylists() {
        playlistList.clear();
        Playlist playlist = new Playlist();
        //playlist.setPlaylistName("heloo");
        //playlist.setImgId(R.drawable.ic_featured_play_list_black_24dp);

        try {
            FileInputStream is = new FileInputStream(masterPlaylistFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            while (line != null) {
                playlist.setPlaylistName(line);
                playlist.setImgId(R.drawable.ic_featured_play_list_black_24dp);
                line = br.readLine();
            }
        } catch (Exception e) {
            Log.e("playlists", "loadPlaylists: ", e);
        }

        playlistList.add(playlist);
    }
}
